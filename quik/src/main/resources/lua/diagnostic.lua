require("environment")

local diagnostic = {}
diagnostic.logLevel = 'info'
diagnostic.loggingEnabled = false

function initLog(directoryPath, logFileName, level)

    if not isdir(directoryPath) then        
        message ("Directory " .. directoryPath .. " doesn't exist. Trying to create...")
        os.execute("mkdir " .. directoryPath)
    else
        message ("Directory " .. directoryPath .. " exist")
    end

    diagnostic.logLevel = level

    logFileFullPath = directoryPath .. logFileName

    local logFile = io.open(logFileFullPath, "w")

    if logFile ~= nil then
        message("Lua log file created at " .. logFileFullPath, 1)
        diagnostic.logFileFullPath = logFileFullPath
        diagnostic.loggingEnabled = true
    else
        message("ERROR while creating Lua log file at " .. logFileFullPath ..
                    " Logging is disabled.", 3)
    end
end

-- функция логгирования
function trace(level, msg)

    if not diagnostic.loggingEnabled then return end

    if level == 'debug' and diagnostic.logLevel ~= 'debug' then return end

    local local_t = os.date("*t", os.time())
    local mess = ""
    if msg ~= nil then mess = msg end

    local logFile = io.open(diagnostic.logFileFullPath, "a")

    logFile:write(
        os.date("%H:%M:%S", os.time()) .. " " .. level .. ": " .. mess .. "\n")
    logFile:flush()
    logFile:close()
end

function error_handler(err) trace("error", "error_handler: " .. err) end

return diagnostic
