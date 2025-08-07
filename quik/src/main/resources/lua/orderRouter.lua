--require("quikCallbacks")
transport = require("transport")
config = require("config")
context = require("context")

router = {}

-- отправка траназакции квику
function router:processTransaction(transaction, tJson)
  trace("info", "NEW TRANSACTION: " .. tJson)
  
  if transaction["ACTION"] == "NEW_ORDER" then
    -- текущая дата + 3 дня
    local gtc = context.getCurrentServerTime() + 3 * 24 * 60 * 60
    
    local gtcTransaction = {
        ["TRANS_ID"] = transaction["TRANS_ID"],
        ["CLASSCODE"] = transaction["CLASSCODE"],
        ["ACTION"] = "Ввод заявки",
        ["Торговый счет"] = transaction["ACCOUNT"],
        ["К/П"] = (transaction["OPERATION"] == "B") and "Покупка" or "Продажа",
        ["Тип"] = (transaction["TYPE"] == "L") and "Лимитированная" or "Рыночная",
        ["Инструмент"] = transaction["SECCODE"],
        ["Цена"] = transaction["PRICE"],
        ["Количество"] = transaction["QUANTITY"],
        ["Условие исполнения"] = "Поставить в очередь",
        ["Переносить заявку"] = "Да",
        ["Комментарий"] = transaction["COMMENT"],
        ["Дата экспирации"] = os.date("%Y%m%d", gtc)
        -- коммент
    }

    transaction = gtcTransaction
  end

  local ok = sendTransaction(transaction)
  if ok ~= "" then
    trace("error", "Error while sending transaction: " .. ok)
    err={}
    err.message_type = transport.messageTypes.TransactionReply
    err.trans_id = transaction.TRANS_ID
    err.status = 4
    err.result_msg = ok
    transport:putMessage(err)
  end  
end


-- обработка результата отправки транзакции
function router:processTransactionReply(transReply)
  trace("info", "TRANSACTION REPLY: " .. cjson.encode(transReply))
  transReply.message_type = transport.messageTypes.TransactionReply
  transport:putMessage(transReply)
end

function router:processOrderStateChange(order)
	-- Преобразуем время заявки в формат времени Lua
	local orderTime = os.time({
		year = order.datetime.year,
		month = order.datetime.month,
		day = order.datetime.day,
		hour = order.datetime.hour,
		min = order.datetime.min,
		sec = order.datetime.sec
	})

	-- Текущее серверное время
	local currentTime = context.getCurrentServerTime()

	-- Если разница текущего серверного времени и времени заявки больше, чем предельный интервал из конфига, не обрабатываем
	if (currentTime - orderTime) > config.orderRouter.orderTimeThreshold then
		return
	end

	order.message_type = transport.messageTypes.OrderStateChange
	transport:putMessage(order)
end

-- обработка изменения состояния стоп заявки
function router:processStopOrderStateChange(stopOrder)
	trace("info", "STOP ORDER STATE CHANGE: " .. cjson.encode(stopOrder))
	stopOrder.message_type = transport.messageTypes.StopOrderStateChange
	transport:putMessage(stopOrder)
end

-- обработка изменения состояния стоп заявки
function router:processStopOrderStateChange(stopOrder)
	trace("info", "STOP ORDER STATE CHANGE: " .. cjson.encode(stopOrder))
	stopOrder.message_type = transport.messageTypes.StopOrderStateChange
	transport:putMessage(stopOrder)
end


-- обработка изменения состояния стоп заявки
function router:processStopOrderStateChange(stopOrder)
	trace("info", "STOP ORDER STATE CHANGE: " .. cjson.encode(stopOrder))
	stopOrder.message_type = transport.messageTypes.StopOrderStateChange
	transport:putMessage(stopOrder)
end


-- обработка сделки
function router:processFill(fill)
	-- Преобразуем время сделки в формат времени Lua
	local fillTime = os.time({
		year = fill.datetime.year,
		month = fill.datetime.month,
		day = fill.datetime.day,
		hour = fill.datetime.hour,
		min = fill.datetime.min,
		sec = fill.datetime.sec
	})

	local currentTime = context.getCurrentServerTime()

	-- Если разница текущего серверного времени и времени сделки больше, чем предельный интервал из конфига, не обрабатываем
	if (currentTime - fillTime) > config.orderRouter.orderTimeThreshold then
		return
	end

	fill.message_type = transport.messageTypes.Fill
	transport:putMessage(fill)
end

-- изменение денег
function router:futuresLimitChange(moneyPosition)
	-- Берем только тип «Денежные средства»
	if moneyPosition.limit_type ~= 0 then
		return
	end
	trace("info", "MONEY CHANGE: " .. cjson.encode(moneyPosition))
	moneyPosition.message_type = transport.messageTypes.MoneyPosition
	transport:putMessage(moneyPosition)
end

-- изменение позиции по инструменту
function router:futuresClientHolding(position)  
  trace("info", "POSITION CHANGE: " .. cjson.encode(position))
  position.message_type = transport.messageTypes.Position  
  transport:putMessage(position)
end

return router
