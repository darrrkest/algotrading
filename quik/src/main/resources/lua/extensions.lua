-- индексатор для строки
local strdefi=getmetatable('').__index
getmetatable('').__index=function(str,i) if type(i) == "number" then
    return string.sub(str,i,i)
    else return strdefi[i] end end

-- проверка наличия элемента в массиве
function contains (tab, val)
    for index, value in ipairs(tab) do
        if value == val then
            return true
        end
    end

    return false
end
