package com.financia.swap;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "CoinThumb", description = "币种行情")
public class CoinThumb {
    @ApiModelProperty("交易对")
    private String symbol;
    @ApiModelProperty("开盘价")
    private BigDecimal open = BigDecimal.ZERO;
    @ApiModelProperty("最高价")
    private BigDecimal high = BigDecimal.ZERO;
    @ApiModelProperty("最低价")
    private BigDecimal low = BigDecimal.ZERO;
    @ApiModelProperty("收盘价")
    private BigDecimal close = BigDecimal.ZERO;
    @ApiModelProperty("")
    private BigDecimal chg = BigDecimal.ZERO.setScale(2);
    @ApiModelProperty("")
    private BigDecimal change = BigDecimal.ZERO.setScale(2);
    @ApiModelProperty("成交量")
    private BigDecimal volume = BigDecimal.ZERO.setScale(2);
    @ApiModelProperty("成交额")
    private BigDecimal turnover = BigDecimal.ZERO;
    //昨日收盘价
    @ApiModelProperty("昨日收盘价")
    private BigDecimal lastDayClose = BigDecimal.ZERO;
    //交易币对usd汇率
    @ApiModelProperty("交易币对usd汇率")
    private BigDecimal usdRate;
    //基币对usd的汇率
    @ApiModelProperty("基币对usd的汇率")
    private BigDecimal baseUsdRate;
    // 交易區
    @ApiModelProperty("交易区")
    private int zone;

    @TableField(exist = false)
    @ApiModelProperty("0:未热门 1:热门")
    private Integer popular;

    @ApiModelProperty("最大杠杆倍数")
    private String maxLeverage;

    @ApiModelProperty("K线100条最新数据")
    private JSONArray kLine;

}
