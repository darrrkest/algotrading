require("extensions")
hashset = require("hashset")

local codes={}
codes.classes = hashset.create()
codes.hasOptw = false

trace ("info", "Available instrument classes are")
for i = 0,getNumberOf("classes") - 1 do
    code = getItem("classes",i).code
    trace("info", "Class " .. code)    

    if code == "OPTW" then hasOptw = true end

end

-- возвращает код класса инструмента, анализируя его код
function codes:getClassCode(instrumentCode)
	len = string.len(instrumentCode)

	-- у фьючерсов коды максимум 4 символа
	if string.len(instrumentCode) < 5 then
		return "SPBFUT"
	end

	-- Исключение для вечных фьючерсов
	if instrumentCode == "USDRUBF" or instrumentCode == "EURRUBF" or instrumentCode == "CNYRUBF" then
		return "SPBFUT"
	end

	-- с опционами могут быть нюансы. У нормальных брокеров все опционы 
	-- имеют код SPBOPT, но некоторые зачем-то выносят недельные опционы
	-- в отдельный класс OPTW

	if self.hasOptw then
		lastChar = instrumentCode[len]

		if codes:isDigit(lastChar) == true then
			return "SPBOPT"
		else
			return "OPTW"
		end
	end

	return "SPBOPT"
end

function codes:isDigit(character)
    d = tonumber(character)
    if d ~= nil then
        return true
    end
    return false
end

return codes
