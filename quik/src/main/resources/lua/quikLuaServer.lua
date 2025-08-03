-- флаг проставляется в false в колбэке OnStop - нажатие на кнопку остановки скрипта в квике
running = true

-- определение директории, в которой выполняется скрипт
function workingDirectory()
    -- функция getScriptPath есть только в окружении квика
    if getScriptPath then
        return getScriptPath()
    else
        fileNamePos, temp = string.find(arg[0], "quikLuaServer.lua")
        fileDirectoryPath = string.sub(arg[0], 0, fileNamePos - 1)
        return fileDirectoryPath
    end
end

function quikVersion()
    version = getInfoParam("VERSION")

    if version ~= nil then
		local t={}
		for str in string.gmatch(version, "([^%.]+)") do
			table.insert(t, str)
		end
		version = tonumber(t[1]) * 100 + tonumber(t[2])		
    end
    
    return version
end

function areWeRunningInsideQuik()
    if getScriptPath then
        return true
    else
        return false
    end
end

local owLogsPath = workingDirectory() .. "\\logs\\"
local iniPath = workingDirectory() .. "\\quik_lua.ini"

if areWeRunningInsideQuik() then
    version = quikVersion()
    
    if version >= 8.5 then
        libsPath = "\\libs\\x64\\53"
    elseif version >= 8 then
        libsPath = "\\libs\\x64\\51"
    else
        libsPath = "\\libs\\x86"
    end
end

-- Добавляем пути к локальным модулям в начало package.path и package.cpath,
-- чтобы модули из рабочей директории искались в первую очередь.
package.path = workingDirectory() .. "\\?.lua;" ..
	workingDirectory() .. "\\?.luac;" ..
	".\\?.lua;" ..
	".\\?.luac;" ..
	package.path

package.cpath = workingDirectory() .. "\\?.dll;" ..
	workingDirectory() .. libsPath .. "\\?.dll;" ..
	".\\?.dll;" ..
	package.cpath

require("environment")
require("diagnostic")
require("extensions")

initLog(owLogsPath, "quikLuaServer.txt", "info")
trace('info', 'QUIK version is: ' .. version)
trace('info', 'Working directory is: ' .. workingDirectory())
require("config")        

cjson = require("dkjson")
require("string")

transport = require("transport")
require("clientInit")
hp = require("historyProvider")
gp = require("glassProvider")
ip = require("instrumentParamsProvider")
tp = require("tickProvider")
router = require("orderRouter")

require("quikCallbacks")
context = require("context")

-- sleep
function threadSleep(t)
    -- функция sleep доступна только в квике
    if sleep then
        sleep(t)
    else
        socket.select(nil, nil, t / 1000)
    end
end

-- обработчик сообщения от клиента
function clientMessageHandler(message, messageJson)

    if message == nil then return end

    -- обработка настроек
    if message.message_type == transport.messageTypes.QuikSideSettings then
        ip:Setup(message)
        gp:Setup(message)
    end

    -- обработка запроса исторических данных  
    if message.message_type == transport.messageTypes.CandlesRequest then
        hp.SendCandlesOnce(message)
    end

    -- обработка подписки на исторические данные
    if message.message_type == transport.messageTypes.CandlesSubscription then
        hp.SubscribeCandles(message)
    end

    -- обработка подписки на стакан
    if message.message_type ==
        transport.messageTypes.OrderBookSubscriptionRequest then
        gp:subscribe(message)
    end

    -- обработка отписки от стакана
    if message.message_type ==
        transport.messageTypes.OrderBookUnsubscriptionRequest then
        gp:unsubscribe(message)
    end

    -- подписка на параметры инструмента
    if message.message_type ==
        transport.messageTypes.InstrumentParamsSubscriptionRequest then
        ip:subscribe(message)
    end

    -- отписка от параметров инструмента
    if message.message_type ==
        transport.messageTypes.InstrumentParamsUnsubscriptionRequest then
        ip:unsubscribe(message)
    end

    -- транзакция
    if message.message_type == transport.messageTypes.Transaction then
        router:processTransaction(message, messageJson)
    end

    -- подписка на обезличенные сделки по инструменту
    if message.message_type == 
        transport.messageTypes.TicksSubscriptionRequest then
        tp.SubscribeTicks(message)
    end

    -- отписка от обезличенных сделок по инструменту
    if message.message_type == 
        transport.messageTypes.TicksUnsubscriptionRequest then 
        tp.UnsubscribeTicks(message)
    end

end

-- обработчик события подключения клиента к серверу (мы в сервере)
function clientConnectedHandler() initClient() end

-- обработчик события отодключения клиента от сервера (мы в сервере)
function clientDisconnectedHandler() deinitClient() end

local prevTime = os.time()

local function SendHeartbeat()
    -- отправляем раз в секунду
    if os.time() - prevTime > 1 then
        prevTime = os.time()
        msg = {};
        msg.message_type = transport.messageTypes.Heartbeat

        msg.time = getInfoParam("SERVERTIME")
		
		-- Устанавливаем серверное время в контекст.
		context.setServerTime(msg.time)

        -- msg.isTrading = true

        -- NOTE значения STARTTIME, ENDTIME, EVNSTARTTIME, EVNENDTIME
        --      есть только на 2 ближайших фьючерса, по остальным приходят пустые строки

        par = getParamEx("SPBFUT", "RIZ0", "STARTTIME")
        msg.startTime = par.param_image

        par = getParamEx("SPBFUT", "RIZ0", "ENDTIME")
        msg.endTime = par.param_image

        par = getParamEx("SPBFUT", "RIZ0", "EVNSTARTTIME")
        msg.evnStartTime = par.param_image

        par = getParamEx("SPBFUT", "RIZ0", "EVNENDTIME")
        msg.evnEndTime = par.param_image

        --      trace("info", transport.serialize(msg))
        transport:putMessageIfClientConnected(msg)
    end
end
-- par = getParamEx(class,  sec, "") 
-- 
-- 20 TRADINGSTATUS STRING Состояние сессии 
-- 94 STARTTIME STRING Начало основной сессии 
-- 95 ENDTIME STRING Окончание основной сессии 
-- 96 EVNSTARTTIME STRING Начало вечерней сессии 
-- 97 EVNENDTIME STRING Окончание вечерней сессии 
-- 98 MONSTARTTIME STRING Начало утренней сессии 
-- 99 MONENDTIME STRING Окончание утренней сессии 

-- основной цикл работы адаптера
function workingCycle()
	trace('info', 'starting workingCycle')
    transport:init(clientConnectedHandler, clientDisconnectedHandler,
                   clientMessageHandler)

    while running do
        transport.cycle()
        threadSleep(10)
        SendHeartbeat()
    end

    transport.dispose()
end

function stopWorkingCycle() running = false end

-- при запуске в квике вызовется кобэк main (см. QuikCallbacks.lua)
if not areWeRunningInsideQuik() then
    trace("info", "Running out of QUIK")
    workingCycle()
end


