package com.example.ib.adapter;

import com.example.ib.api.client.*;

import java.util.*;

public class IBAdapter extends EWrapperBase {
    @Override
    public void error(Exception e) {
        super.error(e);
    }

    @Override
    public void error(String string) {
        super.error(string);
    }

    @Override
    public void error(int id, int errorCode, String errorMessage, String advancedOrderRejectJson) {
        super.error(id, errorCode, errorMessage, advancedOrderRejectJson);
    }

    @Override
    public void currentTime(long time) {
        super.currentTime(time);
    }

    @Override
    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        super.tickPrice(tickerId, field, price, attribs);
    }

    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        super.tickSize(tickerId, field, size);
    }

    @Override
    public void tickString(int tickerId, int field, String value) {
        super.tickString(tickerId, field, value);
    }

    @Override
    public void tickGeneric(int tickerId, int field, double value) {
        super.tickGeneric(tickerId, field, value);
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
        super.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate);
    }

    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        super.orderStatus(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld, mktCapPrice);
    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
        super.deltaNeutralValidation(reqId, deltaNeutralContract);
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, int tickAttrib, double impliedVolatility, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        super.tickOptionComputation(tickerId, field, tickAttrib, impliedVolatility, delta, optPrice, pvDividend, gamma, vega, theta, undPrice);
    }

    @Override
    public void tickSnapshotEnd(int tickerId) {
        super.tickSnapshotEnd(tickerId);
    }

    @Override
    public void nextValidId(int orderId) {
        super.nextValidId(orderId);
    }

    @Override
    public void managedAccounts(String accountsList) {
        super.managedAccounts(accountsList);
    }

    @Override
    public void connectionClosed() {
        super.connectionClosed();
    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        super.accountSummary(reqId, account, tag, value, currency);
    }

    @Override
    public void accountSummaryEnd(int reqId) {
        super.accountSummaryEnd(reqId);
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contract) {
        super.bondContractDetails(reqId, contract);
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        super.updateAccountValue(key, value, currency, accountName);
    }

    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        super.updatePortfolio(contract, position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName);
    }

    @Override
    public void updateAccountTime(String timestamp) {
        super.updateAccountTime(timestamp);
    }

    @Override
    public void accountDownloadEnd(String account) {
        super.accountDownloadEnd(account);
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        super.openOrder(orderId, contract, order, orderState);
    }

    @Override
    public void openOrderEnd() {
        super.openOrderEnd();
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        super.contractDetails(reqId, contractDetails);
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        super.contractDetailsEnd(reqId);
    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        super.execDetails(reqId, contract, execution);
    }

    @Override
    public void execDetailsEnd(int reqId) {
        super.execDetailsEnd(reqId);
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {
        super.updateMktDepth(tickerId, position, operation, side, price, size);
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) {
        super.updateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size, isSmartDepth);
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        super.commissionReport(commissionReport);
    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        super.position(account, contract, pos, avgCost);
    }

    @Override
    public void fundamentalData(int reqId, String data) {
        super.fundamentalData(reqId, data);
    }

    @Override
    public void historicalData(int reqId, Bar bar) {
        super.historicalData(reqId, bar);
    }

    @Override
    public void historicalDataUpdate(int reqId, Bar bar) {
        super.historicalDataUpdate(reqId, bar);
    }

    @Override
    public void historicalDataEnd(int reqId, String start, String end) {
        super.historicalDataEnd(reqId, start, end);
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        super.marketDataType(reqId, marketDataType);
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        super.updateNewsBulletin(msgId, msgType, message, origExchange);
    }

    @Override
    public void positionEnd() {
        super.positionEnd();
    }

    @Override
    public void scannerParameters(String xml) {
        super.scannerParameters(xml);
    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
        super.scannerData(reqId, rank, contractDetails, distance, benchmark, projection, legsStr);
    }

    @Override
    public void scannerDataEnd(int reqId) {
        super.scannerDataEnd(reqId);
    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {
        super.realtimeBar(reqId, time, open, high, low, close, volume, wap, count);
    }

    @Override
    public void receiveFA(int faDataType, String faXmlData) {
        super.receiveFA(faDataType, faXmlData);
    }

    @Override
    public void verifyMessageAPI(String apiData) {
        super.verifyMessageAPI(apiData);
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        super.verifyCompleted(isSuccessful, errorText);
    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {
        super.verifyAndAuthMessageAPI(apiData, xyzChallenge);
    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
        super.verifyAndAuthCompleted(isSuccessful, errorText);
    }

    @Override
    public void displayGroupList(int reqId, String groups) {
        super.displayGroupList(reqId, groups);
    }

    @Override
    public void displayGroupUpdated(int reqId, String contractInfo) {
        super.displayGroupUpdated(reqId, contractInfo);
    }

    @Override
    public void connectAck() {
        super.connectAck();
    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {
        super.positionMulti(reqId, account, modelCode, contract, pos, avgCost);
    }

    @Override
    public void positionMultiEnd(int requestId) {
        super.positionMultiEnd(requestId);
    }

    @Override
    public void accountUpdateMulti(int requestId, String account, String modelCode, String key, String value, String currency) {
        super.accountUpdateMulti(requestId, account, modelCode, key, value, currency);
    }

    @Override
    public void accountUpdateMultiEnd(int requestId) {
        super.accountUpdateMultiEnd(requestId);
    }

    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {
        super.securityDefinitionOptionalParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes);
    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {
        super.securityDefinitionOptionalParameterEnd(reqId);
    }

    @Override
    public void securityDefinitionOptionParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, HashSet<String> expirations, HashSet<Double> strikes) {
        super.securityDefinitionOptionParameter(reqId, exchange, underlyingConId, tradingClass, multiplier, expirations, strikes);
    }

    @Override
    public void securityDefinitionOptionParameterEnd(int reqId) {
        super.securityDefinitionOptionParameterEnd(reqId);
    }

    @Override
    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {
        super.softDollarTiers(reqId, tiers);
    }

    @Override
    public void familyCodes(FamilyCode[] familyCodes) {
        super.familyCodes(familyCodes);
    }

    @Override
    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {
        super.symbolSamples(reqId, contractDescriptions);
    }

    @Override
    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {
        super.mktDepthExchanges(depthMktDataDescriptions);
    }

    @Override
    public void tickNews(int tickerId, long timeStamp, String providerCode, String articleId, String headline, String extraData) {
        super.tickNews(tickerId, timeStamp, providerCode, articleId, headline, extraData);
    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Map.Entry<String, Character>> theMap) {
        super.smartComponents(reqId, theMap);
    }

    @Override
    public void smartComponents(int reqId, Dictionary<Integer, Map.Entry<String, Character>> theMap) {
        super.smartComponents(reqId, theMap);
    }

    @Override
    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {
        super.tickReqParams(tickerId, minTick, bboExchange, snapshotPermissions);
    }

    @Override
    public void newsProviders(NewsProvider[] newsProviders) {
        super.newsProviders(newsProviders);
    }

    @Override
    public void newsArticle(int requestId, int articleType, String articleText) {
        super.newsArticle(requestId, articleType, articleText);
    }

    @Override
    public void historicalNews(int requestId, String time, String providerCode, String articleId, String headline) {
        super.historicalNews(requestId, time, providerCode, articleId, headline);
    }

    @Override
    public void historicalNewsEnd(int requestId, boolean hasMore) {
        super.historicalNewsEnd(requestId, hasMore);
    }

    @Override
    public void headTimestamp(int reqId, String headTimestamp) {
        super.headTimestamp(reqId, headTimestamp);
    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {
        super.histogramData(reqId, items);
    }

    @Override
    public void histogramData(int reqId, HistogramEntry[] data) {
        super.histogramData(reqId, data);
    }

    @Override
    public void rerouteMktDataReq(int reqId, int conId, String exchange) {
        super.rerouteMktDataReq(reqId, conId, exchange);
    }

    @Override
    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {
        super.rerouteMktDepthReq(reqId, conId, exchange);
    }

    @Override
    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {
        super.marketRule(marketRuleId, priceIncrements);
    }

    @Override
    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {
        super.pnl(reqId, dailyPnL, unrealizedPnL, realizedPnL);
    }

    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {
        super.pnlSingle(reqId, pos, dailyPnL, unrealizedPnL, realizedPnL, value);
    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        super.historicalTicks(reqId, ticks, done);
    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        super.historicalTicksBidAsk(reqId, ticks, done);
    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        super.historicalTicksLast(reqId, ticks, done);
    }

    @Override
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, Decimal size, TickAttribLast tickAttribLast, String exchange, String specialConditions) {
        super.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange, specialConditions);
    }

    @Override
    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, Decimal bidSize, Decimal askSize, TickAttribBidAsk tickAttribBidAsk) {
        super.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize, tickAttribBidAsk);
    }

    @Override
    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        super.tickByTickMidPoint(reqId, time, midPoint);
    }

    @Override
    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        super.orderBound(orderId, apiClientId, apiOrderId);
    }

    @Override
    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        super.completedOrder(contract, order, orderState);
    }

    @Override
    public void completedOrdersEnd() {
        super.completedOrdersEnd();
    }

    @Override
    public void replaceFAEnd(int reqId, String text) {
        super.replaceFAEnd(reqId, text);
    }

    @Override
    public void wshMetaData(int reqId, String dataJson) {
        super.wshMetaData(reqId, dataJson);
    }

    @Override
    public void wshEventData(int reqId, String dataJson) {
        super.wshEventData(reqId, dataJson);
    }

    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {
        super.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions);
    }

    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, HistoricalSession[] sessions) {
        super.historicalSchedule(reqId, startDateTime, endDateTime, timeZone, sessions);
    }

    @Override
    public void userInfo(int reqId, String whiteBrandingId) {
        super.userInfo(reqId, whiteBrandingId);
    }
}
