package com.example.ib.adapter;

import com.example.ib.api.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class EWrapperBase implements EWrapper {
    private static final Logger log = LoggerFactory.getLogger(IBAdapter.class);

    public void error(Exception e) {
        log.error("< error {}", e.getMessage());
    }

    public void error(String string) {
        log.error("< error {}", string);
    }

    public void error(int id, int errorCode, String errorMessage, String advancedOrderRejectJson) {
        log.error("< error {} {} {} {}", id, errorCode, errorMessage, advancedOrderRejectJson);
    }

    public void currentTime(long time) {
        log.trace("< currentTime {}", time);
    }

    public void tickPrice(int tickerId, int field, double price, TickAttrib attribs) {
        log.trace("< tickPrice {} {} {} {}", tickerId, field, price, attribs);
    }

    @Override
    public void tickSize(int tickerId, int field, Decimal size) {
        log.trace("< tickSize {} {} {}", tickerId, field, size);
    }

    public void tickString(int tickerId, int field, String value) {
        log.trace("< tickString {} {} {}", tickerId, field, value);
    }

    public void tickGeneric(int tickerId, int field, double value) {
        log.trace("< tickGeneric {} {} {}", tickerId, field, value);
    }

    public void tickEFP(
            int tickerId,
            int tickType,
            double basisPoints,
            String formattedBasisPoints,
            double impliedFuture,
            int holdDays,
            String futureLastTradeDate,
            double dividendImpact,
            double dividendsToLastTradeDate) {
    }

    @Override
    public void orderStatus(int orderId, String status, Decimal filled, Decimal remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        log.info(
                "< orderStatus orderId={} | status={} | filled={} | remaining={} | avgFillPrice={} | permId={} | parentId={} | lastFillPrice={} | clientId={} | whyHeld={}",
                orderId,
                status,
                filled,
                remaining,
                avgFillPrice,
                permId,
                parentId,
                lastFillPrice,
                clientId,
                whyHeld
        );
    }

    public void deltaNeutralValidation(int reqId, DeltaNeutralContract deltaNeutralContract) {
    }

    public void tickOptionComputation(
            int tickerId,
            int field,
            int tickAttrib,
            double impliedVolatility,
            double delta,
            double optPrice,
            double pvDividend,
            double gamma,
            double vega,
            double theta,
            double undPrice) {

    }

    public void tickSnapshotEnd(int tickerId) {
        log.trace("< tickSnapshotEnd {}", tickerId);
    }

    public void nextValidId(int orderId) {
        log.trace("< nextValidId {}", orderId);
    }

    public void managedAccounts(String accountsList) {
        log.trace("< managedAccounts {}", accountsList);
    }

    public void connectionClosed() {
        log.trace("< connectionClosed");
    }

    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        log.trace("< accountSummary {} {} {} {} {}", reqId, account, tag, value, currency);
    }

    public void accountSummaryEnd(int reqId) {
        log.trace("< accountSummaryEnd: {}", reqId);
    }

    public void bondContractDetails(int reqId, ContractDetails contract) {

    }

    public void updateAccountValue(String key, String value, String currency, String accountName) {
        log.trace("< updateAccountValue: {} {} {} {}", key, value, currency, accountName);
    }

    @Override
    public void updatePortfolio(Contract contract, Decimal position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {

    }

    public void updateAccountTime(String timestamp) {
        log.trace("< updateAccountTime: {}", timestamp);
    }

    public void accountDownloadEnd(String account) {
        log.trace("< accountDownloadEnd: {}", account);
    }

    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {

    }

    public void openOrderEnd() {

    }

    public void contractDetails(int reqId, ContractDetails contractDetails) {
        log.trace("< contractDetails: {} {}", reqId, contractDetails.contract().localSymbol());
    }

    public void contractDetailsEnd(int reqId) {
        log.trace("< contractDetailsEnd: {}", reqId);
    }

    public void execDetails(int reqId, Contract contract, Execution execution) {
        log.trace("< execDetails: {} {} {} {} {} {}",
                reqId, execution.execId(), contract.localSymbol(), execution.acctNumber(), execution.cumQty(), execution.price());
    }

    public void execDetailsEnd(int reqId) {
        log.trace("< execDetailsEnd: {}", reqId);
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, Decimal size) {
        log.trace("< updateMktDepth: {} {} {} {} {} {}", tickerId, position, operation, side, price, size);
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, Decimal size, boolean isSmartDepth) {

    }

    public void commissionReport(CommissionReport commissionReport) {

    }

    @Override
    public void position(String account, Contract contract, Decimal pos, double avgCost) {
        log.trace("< position: {} {} {} {}", account, contract.symbol(), pos, avgCost);
    }

    public void fundamentalData(int reqId, String data) {

    }

    public void historicalData(int reqId, Bar bar) {

    }

    public void historicalDataUpdate(int reqId, Bar bar) {

    }

    public void historicalDataEnd(int reqId, String start, String end) {

    }

    public void marketDataType(int reqId, int marketDataType) {
        log.trace("< marketDataType: {} {}", reqId, marketDataType);
    }

    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {

    }


    public void positionEnd() {
        log.trace("< positionEnd");
    }

    public void scannerParameters(String xml) {

    }

    public void scannerData(
            int reqId,
            int rank,
            ContractDetails contractDetails,
            String distance,
            String benchmark,
            String projection,
            String legsStr) {

    }

    public void scannerDataEnd(int reqId) {

    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, Decimal volume, Decimal wap, int count) {

    }

    public void receiveFA(int faDataType, String faXmlData) {

    }

    public void verifyMessageAPI(String apiData) {

    }

    public void verifyCompleted(boolean isSuccessful, String errorText) {

    }

    public void verifyAndAuthMessageAPI(String apiData, String xyzChallenge) {

    }

    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {

    }

    public void displayGroupList(int reqId, String groups) {

    }

    public void displayGroupUpdated(int reqId, String contractInfo) {

    }

    public void connectAck() {
        log.trace("< connectAck");
    }

    @Override
    public void positionMulti(int reqId, String account, String modelCode, Contract contract, Decimal pos, double avgCost) {

    }


    public void positionMultiEnd(int requestId) {

    }

    public void accountUpdateMulti(
            int requestId,
            String account,
            String modelCode,
            String key,
            String value,
            String currency) {

    }

    public void accountUpdateMultiEnd(int requestId) {

    }

    @Override
    public void securityDefinitionOptionalParameter(int reqId, String exchange, int underlyingConId, String tradingClass, String multiplier, Set<String> expirations, Set<Double> strikes) {

    }

    @Override
    public void securityDefinitionOptionalParameterEnd(int reqId) {

    }

    public void securityDefinitionOptionParameter(
            int reqId,
            String exchange,
            int underlyingConId,
            String tradingClass,
            String multiplier,
            HashSet<String> expirations,
            HashSet<Double> strikes) {

    }

    public void securityDefinitionOptionParameterEnd(int reqId) {

    }

    public void softDollarTiers(int reqId, SoftDollarTier[] tiers) {

    }

    public void familyCodes(FamilyCode[] familyCodes) {

    }

    public void symbolSamples(int reqId, ContractDescription[] contractDescriptions) {

    }

    public void mktDepthExchanges(DepthMktDataDescription[] depthMktDataDescriptions) {

    }

    public void tickNews(
            int tickerId,
            long timeStamp,
            String providerCode,
            String articleId,
            String headline,
            String extraData) {

    }

    @Override
    public void smartComponents(int reqId, Map<Integer, Map.Entry<String, Character>> theMap) {

    }

    public void smartComponents(int reqId, Dictionary<Integer, Map.Entry<String, Character>> theMap) {

    }

    public void tickReqParams(int tickerId, double minTick, String bboExchange, int snapshotPermissions) {

    }

    public void newsProviders(NewsProvider[] newsProviders) {

    }

    public void newsArticle(int requestId, int articleType, String articleText) {

    }

    public void historicalNews(
            int requestId,
            String time,
            String providerCode,
            String articleId,
            String headline) {

    }

    public void historicalNewsEnd(int requestId, boolean hasMore) {

    }

    public void headTimestamp(int reqId, String headTimestamp) {

    }

    @Override
    public void histogramData(int reqId, List<HistogramEntry> items) {

    }

    public void histogramData(int reqId, HistogramEntry[] data) {

    }

    public void rerouteMktDataReq(int reqId, int conId, String exchange) {

    }

    public void rerouteMktDepthReq(int reqId, int conId, String exchange) {

    }

    public void marketRule(int marketRuleId, PriceIncrement[] priceIncrements) {

    }

    public void pnl(int reqId, double dailyPnL, double unrealizedPnL, double realizedPnL) {

    }

    @Override
    public void pnlSingle(int reqId, Decimal pos, double dailyPnL, double unrealizedPnL, double realizedPnL, double value) {

    }

    @Override
    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {

    }

    @Override
    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {

    }

    @Override
    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {

    }

    public void tickByTickAllLast(
            int reqId,
            int tickType,
            long time,
            double price,
            Decimal size,
            TickAttribLast tickAttribLast,
            String exchange,
            String specialConditions) {

    }

    public void tickByTickBidAsk(
            int reqId,
            long time,
            double bidPrice,
            double askPrice,
            Decimal bidSize,
            Decimal askSize,
            TickAttribBidAsk tickAttribBidAsk) {

    }

    public void tickByTickMidPoint(int reqId, long time, double midPoint) {

    }

    public void orderBound(long orderId, int apiClientId, int apiOrderId) {

    }

    public void completedOrder(Contract contract, Order order, OrderState orderState) {

    }

    public void completedOrdersEnd() {

    }

    public void replaceFAEnd(int reqId, String text) {

    }

    public void wshMetaData(int reqId, String dataJson) {

    }

    public void wshEventData(int reqId, String dataJson) {

    }

    @Override
    public void historicalSchedule(int reqId, String startDateTime, String endDateTime, String timeZone, List<HistoricalSession> sessions) {

    }

    public void historicalSchedule(
            int reqId,
            String startDateTime,
            String endDateTime,
            String timeZone,
            HistoricalSession[] sessions) {

    }

    public void userInfo(int reqId, String whiteBrandingId) {

    }
}
