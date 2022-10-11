package com.financia.exchange.handler;

import com.financia.swap.CoinThumb;
import com.financia.swap.ContractTrade;
import com.financia.swap.KLine;

import java.util.List;

public interface MarketHandler {

    /**
     * 存储交易信息
     */
    void handleTrade(String symbol, CoinThumb thumb);

    /**
     * 推送交易信息(Netty使用)
     */
    void handleTrades(String symbol, List<ContractTrade> contractTrades, CoinThumb thumb);

    /**
     * 存储K线信息
     *
     * @param kLine
     */
    void handleKLine(String symbol, KLine kLine);
}
