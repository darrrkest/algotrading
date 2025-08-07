--require("quikCallbacks")
transport = require("transport")
config = require("config")
context = require("context")

router = {}

-- �������� ����������� �����
function router:processTransaction(transaction, tJson)
  trace("info", "NEW TRANSACTION: " .. tJson)
  
  if transaction["ACTION"] == "NEW_ORDER" then
    -- ������� ���� + 3 ���
    local gtc = context.getCurrentServerTime() + 3 * 24 * 60 * 60
    
    local gtcTransaction = {
        ["TRANS_ID"] = transaction["TRANS_ID"],
        ["CLASSCODE"] = transaction["CLASSCODE"],
        ["ACTION"] = "���� ������",
        ["�������� ����"] = transaction["ACCOUNT"],
        ["�/�"] = (transaction["OPERATION"] == "B") and "�������" or "�������",
        ["���"] = (transaction["TYPE"] == "L") and "��������������" or "��������",
        ["����������"] = transaction["SECCODE"],
        ["����"] = transaction["PRICE"],
        ["����������"] = transaction["QUANTITY"],
        ["������� ����������"] = "��������� � �������",
        ["���������� ������"] = "��",
        ["�����������"] = transaction["COMMENT"],
        ["���� ����������"] = os.date("%Y%m%d", gtc)
        -- �������
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


-- ��������� ���������� �������� ����������
function router:processTransactionReply(transReply)
  trace("info", "TRANSACTION REPLY: " .. cjson.encode(transReply))
  transReply.message_type = transport.messageTypes.TransactionReply
  transport:putMessage(transReply)
end

function router:processOrderStateChange(order)
	-- ����������� ����� ������ � ������ ������� Lua
	local orderTime = os.time({
		year = order.datetime.year,
		month = order.datetime.month,
		day = order.datetime.day,
		hour = order.datetime.hour,
		min = order.datetime.min,
		sec = order.datetime.sec
	})

	-- ������� ��������� �����
	local currentTime = context.getCurrentServerTime()

	-- ���� ������� �������� ���������� ������� � ������� ������ ������, ��� ���������� �������� �� �������, �� ������������
	if (currentTime - orderTime) > config.orderRouter.orderTimeThreshold then
		return
	end

	order.message_type = transport.messageTypes.OrderStateChange
	transport:putMessage(order)
end

-- ��������� ��������� ��������� ���� ������
function router:processStopOrderStateChange(stopOrder)
	trace("info", "STOP ORDER STATE CHANGE: " .. cjson.encode(stopOrder))
	stopOrder.message_type = transport.messageTypes.StopOrderStateChange
	transport:putMessage(stopOrder)
end

-- ��������� ��������� ��������� ���� ������
function router:processStopOrderStateChange(stopOrder)
	trace("info", "STOP ORDER STATE CHANGE: " .. cjson.encode(stopOrder))
	stopOrder.message_type = transport.messageTypes.StopOrderStateChange
	transport:putMessage(stopOrder)
end


-- ��������� ��������� ��������� ���� ������
function router:processStopOrderStateChange(stopOrder)
	trace("info", "STOP ORDER STATE CHANGE: " .. cjson.encode(stopOrder))
	stopOrder.message_type = transport.messageTypes.StopOrderStateChange
	transport:putMessage(stopOrder)
end


-- ��������� ������
function router:processFill(fill)
	-- ����������� ����� ������ � ������ ������� Lua
	local fillTime = os.time({
		year = fill.datetime.year,
		month = fill.datetime.month,
		day = fill.datetime.day,
		hour = fill.datetime.hour,
		min = fill.datetime.min,
		sec = fill.datetime.sec
	})

	local currentTime = context.getCurrentServerTime()

	-- ���� ������� �������� ���������� ������� � ������� ������ ������, ��� ���������� �������� �� �������, �� ������������
	if (currentTime - fillTime) > config.orderRouter.orderTimeThreshold then
		return
	end

	fill.message_type = transport.messageTypes.Fill
	transport:putMessage(fill)
end

-- ��������� �����
function router:futuresLimitChange(moneyPosition)
	-- ����� ������ ��� ��������� ��������
	if moneyPosition.limit_type ~= 0 then
		return
	end
	trace("info", "MONEY CHANGE: " .. cjson.encode(moneyPosition))
	moneyPosition.message_type = transport.messageTypes.MoneyPosition
	transport:putMessage(moneyPosition)
end

-- ��������� ������� �� �����������
function router:futuresClientHolding(position)  
  trace("info", "POSITION CHANGE: " .. cjson.encode(position))
  position.message_type = transport.messageTypes.Position  
  transport:putMessage(position)
end

return router
