-- Модуль для хранения переменных, отображающие общее состояние системы
local context = {}

-- Время сервера. Устанавливается в SendHeartbeat, может отставать на ~1 секунду.
context.serverTime = nil

-- Текущая дата торгов. Обновляется при подключении клиента, в функции инициализации.
context.serverDate = nil

-- Unix-время, обновляется при установке serverTime и serverDate
context.serverUnixTime = nil

-- Функция обновления времени и пересчета Unix timestamp
function context.setServerTime(timeStr)
	if not timeStr or timeStr == "" then
		return
	end

	-- Устанавливаем значение в контекст
	context.serverTime = timeStr

	-- Если уже есть дата, пересчитываем Unix-время
	if context.serverDate then
		context.updateUnixTime()
	end
end

-- Функция обновления даты и пересчета Unix timestamp
function context.setServerDate(dateStr)
	if not dateStr or dateStr == "" then
		return
	end

	-- Устанавливаем значение в контекст
	context.serverDate = dateStr

	-- Устанавливаем время при инициализации
	context.updateUnixTime()

end

-- Функция пересчета Unix-времени
function context.updateUnixTime()
	--Делаем запрос к серверу. Нужно только при инициализации, когда еще не устанавливалось время при отправке SendHeartbeat.
	if not context.serverTime then
		context.serverTime = getInfoParam("SERVERTIME")
	end

	-- Если дата не установлена при инициализации
	if not context.serverDate then
		context.serverDate = getInfoParam("TRADEDATE")
	end

	-- Разбираем время
	local hour, min, sec = context.serverTime:match("(%d+):(%d+):(%d+)")
	local day, month, year = context.serverDate:match("(%d+).(%d+).(%d+)")

	if hour and min and sec and day and month and year then
		context.serverUnixTime = os.time({
			year = tonumber(year),
			month = tonumber(month),
			day = tonumber(day),
			hour = tonumber(hour),
			min = tonumber(min),
			sec = tonumber(sec)
		})
	end
end

-- Функция получения текущего Unix-времени
function context.getCurrentServerTime()
	if not context.serverUnixTime then
		context.updateUnixTime()
	end
	return context.serverUnixTime
end

return context
