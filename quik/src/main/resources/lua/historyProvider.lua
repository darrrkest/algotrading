codes = require("codes")
require("quikCallbacks")
transport = require("transport")
luadate = require("date")

hp={} -- history provider
hp.sourcesByKey={} -- хранение источников по ключу из класса, инструмента и размера свечи

-- отправить клиенту свечи по инструменту
function hp.SendCandlesOnce(request)
  trace("info", "Candles data requested for " .. request.instrument .. " received. Request id = " .. request.id .. ", Span is " .. request.span)  
  
  request.classCode = codes:getClassCode(request.instrument)
  request.span = hp.convertCandleSpan(request.span)
  request.key = request.classCode .. "#" .. request.instrument  .. "#" .. request.span
  
  trace("info", "Candles request unique key is " .. request.key)
  
  -- создаём новый или получаем ранее созданный источник данных
  ds = hp.GetDataSource(request)
  ds:Init("SendCandlesOnce") 
  
  if ds.initialized == true then
    ds:ReplyAllRequests()
  end

end

-- подписать клиента на свечи по инструменту и отправлять обновления
function hp.SubscribeCandles(subscription)
  trace("info", "Candles subscription for " .. subscription.instrument .. " received. Subscription id = " .. subscription.id .. ", Span is " .. subscription.span .. ", Since " .. subscription.since)  

  subscription.classCode = codes:getClassCode(subscription.instrument)
  subscription.span = hp.convertCandleSpan(subscription.span)  
  subscription.key = subscription.classCode .. "#" .. subscription.instrument  .. "#" .. subscription.span
  trace("info", "Subscribing to candles with key: " .. subscription.key)
  subscription.since = luadate.parseDateTime(subscription.since) 
  
  -- создаём новый или получаем ранее созданный источник данных
  ds = hp.GetDataSource(subscription)
  if ds == nil then
    trace("error", "Failed to get data source for key: " .. subscription.key)
    return -- Завершаем, если источник данных не был получен
  end
  ds:Init("SubscribeCandles")
  
  if ds.initialized == true then

    ds:ForceSubscription(subscription)
  end
  
end

-- создаёт новый источник данных или возаращает ранее созданный
function hp.GetDataSource(request)
	local ds = hp.sourcesByKey[request.key]

	if ds == nil then
		-- Если источник данных не существует, создаем новый
		ds, err = CreateDataSource(request.classCode, request.instrument, request.span)
		if err then
			trace("error", "Failed to create data source: " .. err)
			return nil
		end

		-- Инициализация нового источника данных
		ds.instrument = request.instrument
		ds.initialized = false
		ds.key = request.key
		ds.requests = {} -- разовые запросы свечей
		ds.subscriptions = {} -- подписки на обновление свечей    
		ds.candles = {} -- хранилище свечей, для повторного использования    
		ds.candlesCount = 0
		ds.Init = hp.DS_InitDataSource
		ds.ReplyAllRequests = hp.DS_ReplyAllRequests
		ds.ForceSubscription = hp.DS_ForceSubscription
		ds.UpdateSubscriptions = hp.DS_UpdateSubscriptions

		-- Создаем замыкание для колбэка
		ds.callback = function(i)
			hp.DS_CandleUpdateCallback(ds, i)
		end

		ds:SetUpdateCallback(ds.callback)
		hp.sourcesByKey[request.key] = ds
	else
		-- Если источник данных уже существует, можно проверить его инициализацию
		if not ds.initialized then
			trace("info", "Data source exists but is not initialized. Initializing: " .. ds.key)
			ds:Init("GetDataSource")
		else
			trace("info", "Data source already exists and is initialized: " .. ds.key)
		end
	end

	-- если это разовый запрос свечей, сохраняем его в ds.requests
	if request.message_type == transport.messageTypes.CandlesRequest then
		request.msg = {}
		request.msg.message_type = transport.messageTypes.CandlesResponse
		request.msg.id = request.id
		trace("info", "Put request " .. request.id .. " into datasource container")
		ds.requests[request.id] = request
	end

	-- если это подписка на обновления, сохраняем её в ds.subscriptions
	if request.message_type == transport.messageTypes.CandlesSubscription then
		ds.subscriptions[request.id] = request
	end

	return ds
end

-- отправка данных по всем запросам к источнику
function hp.DS_ReplyAllRequests(ds)  
  
  -- Для каждого запроса истории формируем и отправляем сообщение, после чего удаляем запрос
  for key, request in pairs(ds.requests) do
    trace("info", "Reply to candles request" .. key)
    request.msg.candles={}
    for i, candle in pairs(ds.candles) do table.insert(request.msg.candles, candle) end
    ds.requests[key] = nil
    transport:putMessage(request.msg) 
  end
  
  -- Для каждой подписки
end

-- отправка подпискам тех свечей, которые уже были прочитаны на момент получения подписки
function hp.DS_ForceSubscription(ds, subscription)
  cndls = {}
  for i, candle in pairs(ds.candles) do
    
    if candle.t == nil then
      trace("error", "Candle.t is nil, candle.time is " .. candle.time)
    end
    
    if candle.t >= subscription.since then
      --trace("info", string.format("Candle %s forced to subscription %s", candle.time, subscription.id))
      table.insert(cndls, candle)
      subscription.lastCandleTime = candle.t
    end
  end
  
  -- отправляем, если что-то отобралось
  if #cndls > 0 then
    trace("info", string.format("Force %d candles to subscription %s", #cndls, subscription.id))
    msg = hp.CreateSubscriptionUpdateMessage(cndls, "batch")
    msg.id = subscription.id
    transport:putMessage(msg)
  end
end

-- рассылка обновления подписчикам
function hp.DS_UpdateSubscriptions(ds, message)     
  --trace("info", string.format("WE COME %s\t%s", ds.key, message.candles[1].time))--, subscriptions.since)) \t%s\t%s  
  for key, subscription in pairs(ds.subscriptions) do    
    msg = shallowcopy(message)    
    msg.id = subscription.id    
    if message.candles[1].t >= subscription.since then    
      --trace("info", string.format("Send candle update (%s) for %s\t%s\t%s", message.update_type, ds.key, msg.id, message.candles[1].time))
      transport:putMessage(msg)
    else
      trace("info", string.format("Don't send candle update for %s (%s), candle time %s, subscription.since %s",  ds.key, msg.id, msg.candles[1].time, subscription.since))      
    end
  end
end

-- метод первоночальной обработки вновь созданного источника данных
-- есть попытки реинициализации, чтобы пришли сначала все данные в терминал, прежде чем начнет обрабатываться callback по одной свече.
-- при 20 попытках, с задержкой реинициализации в 0.2 с были случаи, что данные в терминал еще не пришли.
function hp.DS_InitDataSource(ds, caller, retry_count)      
  retry_count = retry_count or 40

  if ds.initialized == false then
    trace("info", string.format("Data source %s initialization from, call from %s", ds.key, caller))  
    -- сначала считываем из источника то, что в нём уже есть (если открыт график, то данные будут сразу)
    local size = ds:Size()

    trace("info", string.format("Number of preloaded candles in datasource %s is %d", ds.key, size))
    local preloadedCandlesCount = 0
    for i = 1, size do
      local cndl = hp.ConvertRowToCandle(ds, i)
      if cndl ~= nil then        
        ds.candles[i] = cndl 
        ds.candlesCount = i
        --trace("info", string.format("Candle #%d of %d\t%.4f\t%.4f\t%.4f\t%.4f\t%s, \tds.key=%s", i, size, ds.candles[i].o, ds.candles[i].h, ds.candles[i].l, ds.candles[i].c, ds.candles[i].time, ds.key))	
        preloadedCandlesCount=i
      end
    end     
        
    if size > 0 and preloadedCandlesCount == size then
		ds.initialized = true
		sleep(100)
      trace("info", string.format('Data source %s was initialized. Size = %d, preloadedCandlesCount = %d', ds.key, size, preloadedCandlesCount))
    else
      trace("warn", string.format('Data source %s was NOT initialized. Size = %d, preloadedCandlesCount = %d', ds.key, size, preloadedCandlesCount))
    end

    -- Повторная инициализация, если не удалось с первой попытки
    if ds.initialized == false and retry_count > 1 then
      trace("warn", string.format("Reinitializing data source %s. Attempts left: %d", ds.key, retry_count - 1))
      sleep(200)
      hp.DS_InitDataSource(ds, caller, retry_count - 1)
    elseif ds.initialized == false then
      trace("error", "Data source could not be initialized after multiple attempts: " .. ds.key)
    end
    
  else
    trace("info", "Data source was initialized earlier: " .. ds.key)  
  end
end

-- колбэк для обновления свечи
function hp.DS_CandleUpdateCallback(ds, i)  
  --local t = ds:T(i)
  --проверка, дождались ли инициализации запрошенных свечей
  if ds.initialized == false then
    --trace("warn", "callback still not initialized for key: " .. ds.key)
    return nil
  end

  local dsSize = ds:Size()
  
  --trace("info", "Updating candle for key: " .. ds.key)

  local newCandleAddedToDS = true
  -- если индекс свечи не увеличился, то это обновление уже существующей свечи
  if i == ds.candlesCount then
    newCandleAddedToDS = false
  end
  
  local cndl = hp.ConvertRowToCandle(ds, i)
  
  if cndl ~= nil then
    -- если свечи уже были обработаны
    if i < ds.candlesCount then
      --trace("info", string.format("Skipping candle #%d for %s. Already processed.", i, ds.key))
      return
    end
    ds.candles[i] = cndl
    if i > ds.candlesCount then ds.candlesCount = i end
    --trace("info", string.format("DS_CandleUpdateCallback: Candle #%d of %d\t%.4f\t%.4f\t%.4f\t%.4f\t%s, \tds.key=%s", i, dsSize, ds.candles[i].o, ds.candles[i].h, ds.candles[i].l, ds.candles[i].c, ds.candles[i].time, ds.key))

    if newCandleAddedToDS == true then
      msg = hp.CreateSubscriptionUpdateMessage({ds.candles[i]}, 'added')
    else
      msg = hp.CreateSubscriptionUpdateMessage({ds.candles[i]}, 'updated')
    end
    
    ds:UpdateSubscriptions(msg)
    
    if i == dsSize then
      -- trace("info", "DS_CandleUpdateCallback: Last candle out of " .. tostring(dsSize) .. " candles for " .. ds.instrument .. " received")            
      ds:ReplyAllRequests()
    end
    
  end
end

-- создать сообщение для подписчиков на обновления
function hp.CreateSubscriptionUpdateMessage(candles, update_type)
  msg = {}
  msg.message_type = transport.messageTypes.CandlesUpdate  
  msg.update_type = update_type
  msg.candles = candles
  return msg
end


-- конвертация размера свечи в квиковый формат
function hp.convertCandleSpan(requestSpan)
  if requestSpan == 0 then return INTERVAL_M1 end
  if requestSpan == 1 then return INTERVAL_M5 end
  if requestSpan == 2 then return INTERVAL_M10 end
  if requestSpan == 3 then return INTERVAL_M15 end
  if requestSpan == 4 then return INTERVAL_M30 end
  if requestSpan == 5 then return INTERVAL_H1 end
  if requestSpan == 6 then return INTERVAL_H4 end
  if requestSpan == 7 then return INTERVAL_D1 end
  if requestSpan == 8 then return INTERVAL_W1 end
  if requestSpan == 9 then return INTERVAL_MN1 end
  return INTERVAL_D1
end

-- копирование таблицы
function shallowcopy(orig)
    local orig_type = type(orig)
    local copy    
    if orig_type == 'table' then
      copy = {}
      for orig_key, orig_value in pairs(orig) do
          copy[orig_key] = orig_value
      end
    else -- number, string, boolean, etc
      copy = orig
    end
    return copy
end

-- конвертация свечи в квиковом представлении в таблицу
function hp.ConvertRowToCandle(ds, i)
  local t = ds:T(i)
  
  -- иногда квик присылает свечу с 1601-м годом и нулями во всех параметрах, 
  -- в этом случае возвращаем nil
  if t.year == 1601 then
    return nil
  end  
  
  cndl =  {
    o=ds:O(i); h=ds:H(i); l=ds:L(i); c=ds:C(i); v=ds:V(i);      
    t = os.time{year = t.year, month = t.month, day = t.day, hour = t.hour, min = t.min};
    time = string.format("%04d.%02d.%02d %02d:%02d", t.year, t.month, t.day, t.hour, t.min);
  }
  return cndl
end

function hp.clearSubscriptions()
  trace("info", "Clearing all candle subscriptions.")

  -- Проходим по всем источникам данных
	for key, ds in pairs(hp.sourcesByKey) do
		if ds.subscriptions then
			trace("info", "Clearing subscriptions for data source: " .. key)
			ds.subscriptions = nil -- Полное удаление подписок
		end
		if ds.requests then
			trace("info", "Clearing requests for data source: " .. key)
			ds.requests = nil -- Полное удаление запросов
		end

		--Важная вещь перед закрытием вызовом Close(), чтобы потом можно было снова активировать колбэк
		ds:SetEmptyCallback()
		-- Принудительное закрытие источника данных
		if ds.Close then
			local success = ds:Close() -- Вызов метода Close для закрытия источника данных
			if success then
				trace("info", "Candles Data source closed successfully: " .. key)
			else
				trace("error", "Failed to close candle data source: " .. key)
			end
		else
			trace("warning", "No close method for candle data source: " .. key)
		end

		-- Полная очистка оставшихся ссылок и данных в источнике данных
		ds.candles = nil
		ds.candlesCount = nil
		hp.sourcesByKey[key] = nil

		collectgarbage() -- Запускаем сборщик мусора для очистки неиспользуемой памяти
	end

	trace("info", "All candle subscriptions cleared.")
end

return hp
