package com.financia.exchange.dto;

import com.financia.currency.ExchangeOrderDirection;
import com.financia.currency.ExchangeOrderType;
import com.financia.swap.ContractOrderDirection;
import com.financia.swap.ContractOrderEntrustType;
import com.financia.swap.ContractOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "AddOrderDTO", description = "币币添加订单DTO")
public class AddOrderDTO implements Serializable {

    @ApiModelProperty(value = "会员Id", required = true)
    private Long memberId;

    @ApiModelProperty(value = "交易对Id", required = true)
    private Long currencyId;

    @ApiModelProperty(value = "买/卖 BUY:做多 SELL:做空", required = true)
    private ExchangeOrderDirection direction;

    @ApiModelProperty(value = "订单类型 MARKET_PRICE：市价 LIMIT_PRICE：限价 ", required = true)
    private ExchangeOrderType orderType;

    @ApiModelProperty(value = "买入金额(和买入数量必须填其中之一)")
    private BigDecimal tradeAmount;

    @ApiModelProperty(value = "买入数量(和买入数量必须填其中之一)")
    private BigDecimal tradeNum;

    @ApiModelProperty("委托价格")
    private BigDecimal entrustPrice;

}
