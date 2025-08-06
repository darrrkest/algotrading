local config = {}

require("diagnostic")
require("environment")
cjson = require("dkjson")
LIP = require("lip")

local iniPath = getScriptPath() .. "\\quik_lua.ini"

trace('info', 'Trying to open config ' .. iniPath)
local pcallStatus, ini = pcall(LIP.load, iniPath)

if pcallStatus == false then
	ini = nil
end

if ini == nil then
	config.transport = {}
	config.transport.receive_orders = true
	config.transport.port = 1250

	config.orderRouter = {}
	config.orderRouter.orderTimeThreshold = 600 -- 10 минут, значение по-умолчанию

	local ok, jsnIni = pcall(cjson.encode, config)

	trace('info', 'Ini file not found, default config will be used: ' .. jsnIni)
else
	config = ini
	local ok, jsnIni = pcall(cjson.encode, ini)
	trace('info', 'Ini file loaded: ' .. jsnIni)
end

return config
