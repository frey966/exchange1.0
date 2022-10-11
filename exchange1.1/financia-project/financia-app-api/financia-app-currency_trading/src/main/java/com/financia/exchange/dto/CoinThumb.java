package com.financia.exchange.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 币币交易 二十四小时涨跌
 */
@Data
public class CoinThumb {
    private Long coinId;
    private String symbol;
    private BigDecimal open = BigDecimal.ZERO;
    private BigDecimal high= BigDecimal.ZERO;
    private BigDecimal low= BigDecimal.ZERO;
    private BigDecimal close=BigDecimal.ZERO;
    private BigDecimal chg = BigDecimal.ZERO.setScale(2);
    private BigDecimal change = BigDecimal.ZERO.setScale(2);
    private BigDecimal volume = BigDecimal.ZERO.setScale(2);
    private BigDecimal turnover= BigDecimal.ZERO;
    //昨日收盘价
    private BigDecimal lastDayClose = BigDecimal.ZERO;
    //交易币对usd汇率
    private BigDecimal usdRate;
    //基币对usd的汇率
    private BigDecimal baseUsdRate;
    // 交易區
    private int zone;

    private Integer popular;
}
