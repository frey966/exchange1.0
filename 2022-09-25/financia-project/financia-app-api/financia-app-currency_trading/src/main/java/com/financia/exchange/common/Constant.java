package com.financia.exchange.common;

public interface Constant {

    /**
     * K线订阅
     */
    String KLINE_SUB = "market.%s.kline.%s";

    /**
     * 交易深度
     * <p>
     * step0  默认150档数据
     * step1  20档数据
     */
    String MARKET_DEPTH_SUB = "market.%s.depth.step0";

    /**
     * 交易行情
     */
    String MARKET_TRADE_SUB = "market.%s.trade.detail";

    /**
     * 详情
     */
    String MARKET_DETAIL_SUB = "market.%s.detail";

    /**
     * K线交易周期
     */
    String[] PERIOD = {"1min", "5min", "15min", "30min", "60min", "1day", "1mon", "1week"};

    /**
     * 买一卖一价
     */
    String MARKET_BBO_SUB = "market.%s.bbo";

    //所有交易对的最新 Tickers
    String TICKER_KEY = "ticker:%s";

    String MARKET_TICKERS = " /market/tickers";


    /**
     * 最新交易记录
     */
    String MARKET_TRADE = "/market/history/trade";

    interface Cmd {
        String DETAIL = "detail";
        String DEPTH = "depth";
        String KLINE = "kline";
        String ALL = "all";
    }
}
