require("diagnostic")
local transport = require("transport")
local gp = require("glassProvider")
local router = require("orderRouter")
local config = require("config")
local tp = require("tickProvider")

--local cjson = require "cjson"

function OnInit(script_path)
end

-- точка входа в скрипт в квике
function main()
  trace('info', 'quik main callback start')
  xpcall(workingCycle, error_handler)
end

-- остановка скрипта
function OnStop(signal)
  stopWorkingCycle()
end

-- ответ на отправку транзакции
function OnTransReply(trans_reply)
  if config.transport.receive_orders == false then     
    return
  end
  if trans_reply.status > 16 then
    return
  end  
  router:processTransactionReply(trans_reply)
end

-- изменение статуса заявки
function OnOrder(orderStateChange)
	if config.transport.receive_orders == false then
		return
	end
	router:processOrderStateChange(orderStateChange)
end

-- изменение статуса заявки
function OnStopOrder(stopOrderStateChange)
  if config.transport.receive_orders == false then     
    return
  end
  router:processStopOrderStateChange(stopOrderStateChange)
end

-- новая сделка
function OnTrade(fill)
  router:processFill(fill)
end

-- изменение денег
function OnFuturesLimitChange(futLimit)
  router:futuresLimitChange(futLimit)
end

-- изменение позиции
function OnFuturesClientHolding(futOptPosition)
  router:futuresClientHolding(futOptPosition)
end

-- изменение стакана
function OnQuote(class, sec)
  gp:processGlass(class, sec)
end

-- изменение параметров инструментов
function OnParam( class, sec )
  ip:processParams( class, sec )
end

-- новая обезличенная сделка; если открыта таблица обезличенных сделок, все сделки из нее будут приходить сюда,
-- поэтому рекомендуется фильтровать таблицу только по нужным инструментам
function OnAllTrade(alltrade)
  tp.processTick(alltrade)
end
