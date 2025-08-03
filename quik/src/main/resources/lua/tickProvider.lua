-- Модуль для работы с обезличенными сделками, может работать без открытой таблицы обезличенных сделок
tp = {} -- tick tickProvider module
tp.subscriptions = {} -- таблица подписок

-- подписка на обезличенные сделки
function tp.SubscribeTicks(subscription)
    trace("info", "Tick subscription for " .. subscription.instrument .. " received.")

    subscription.classCode = codes:getClassCode(subscription.instrument)

    -- Проверяем, есть ли уже активная подписка на инструмент
    if tp.subscriptions[subscription.instrument] then
        trace("warn", "Instrument " .. subscription.instrument .. " is already subscribed.")
        return
    end

    -- Создаем источник данных, он строится на основе таблицы обезличенных сделок, по тику в OnAllTrade будет прилетать сделка
    local ds, err = CreateDataSource(subscription.classCode, subscription.instrument, INTERVAL_TICK)
    if not ds or err then
        trace("error", "Failed to create DataSource for " .. subscription.instrument .. ": " .. (err or "unknown error"))
        return
    end

    -- Сохраняем источник данных в подписке
    subscription.ds = ds
    tp.subscriptions[subscription.instrument] = subscription

    trace("info", "Subscription for " .. subscription.instrument .. " with classCode " .. subscription.classCode .. " is now active.")
end

-- отписка от обезличенных сделок, почему-то в OnAllTrade все равно прилетают сделки по этому инструменту
function tp.UnsubscribeTicks(subscription)
    local activeSubscription = tp.subscriptions[subscription.instrument]

    -- Проверяем, существует ли подписка для этого инструмента
    if activeSubscription then
        -- Закрываем DataSource
        if activeSubscription.ds then
            activeSubscription.ds:SetEmptyCallback()
            local success = activeSubscription.ds:Close()
            if success then
                trace("info", "Tick DataSource for " .. subscription.instrument .. " closed successfully.")
            else
                trace("error", "Failed to close tick DataSource for " .. subscription.instrument)
            end
        end

        -- Удаляем подписку
        tp.subscriptions[subscription.instrument] = nil
        trace("info", "Unsubscribed from ticks" .. subscription.instrument)
    else
        trace("warn", "No subscription found for ticks" .. subscription.instrument)
    end
end

-- обработка обезличенной сделки
function tp.processTick(tick)
    --trace("tickProvider", tick.sec_code)
    -- Проверяем, есть ли подписчики на данный инструмент
    local subscription = tp.subscriptions[tick.sec_code]
    if subscription then
        local tickMessage = tp.CreateTickMessage(tick)
        tp.SendTickMessage(tickMessage)
    end
end

-- создание сообщения для отправки сделки
function tp.CreateTickMessage(tick)
    local tickMessage = {
        message_type = transport.messageTypes.Tick,
        trade_num = tick.trade_num,
        sec_code = tick.sec_code,
        flags = tick.flags,
        price = tick.price,
        qty = tick.qty,
        datetime = tick.datetime
        -- пока ненужные поля
        --value = tick.value,
        --accruedint = tick.accruedint,
        --yield = tick.yield,
        --settlecode = tick.settlecode,
        --reporate = tick.reporate,
        --repovalue = tick.repowalue,
        --repo2value = tick.repo2value,
        --repoterm = tick.repoterm,
        --class_code = tick.class_code,
        --period = tick.period,
        --open_interest = tick.open_interest,
        --exec_market = tick.exec_market,
        --benchmark = tick.benchmark
    }
    return tickMessage
end

-- функция для отправки сообщения подписчику
function tp.SendTickMessage(tickMessage)
    -- Реализация отправки сообщения
    transport:putMessage(tickMessage)
end

-- функция удаления всех подписчиков
function tp.clearSubscriptions()
    trace("info", "Removing all ticks subscriptions.")

    -- Проходим по всем подпискам
    for instrument, subscription in pairs(tp.subscriptions) do
        -- Закрываем DataSource
        if subscription.ds then
            local success = subscription.ds:Close()
            if success then
                trace("info", "Ticks DataSource for " .. instrument .. " closed successfully.")
            else
                trace("error", "Failed to close ticks DataSource for " .. instrument)
            end
        end
        -- Удаляем подписку
        tp.subscriptions[instrument] = nil
    end

    trace("info", "All ticks subscriptions have been removed.")
end

return tp
