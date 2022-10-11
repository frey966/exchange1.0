package com.financia.exchange.dto;

import com.financia.swap.ContractOrderDirection;
import com.financia.swap.ContractOrderEntrustType;
import com.financia.swap.ContractOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "OpenOrderDTO", description = "合约开仓DTO")
public class OpenOrderDTO implements Serializable {

    @ApiModelProperty(value = "会员Id", required = true)
    private Long memberId;

    @ApiModelProperty(value = "持仓钱包Id", required = true)
    private Long walletId;

    @ApiModelProperty(value = "合约交易对Id", required = true)
    private Long contractCoinId;

    @ApiModelProperty(value = "买/卖 BUY:做多 SELL:做空", required = true)
    private Integer direction;

    @ApiModelProperty(value = "订单类型 MARKET_PRICE：市价 SPOT_LIMIT：计划委托", required = true)
    private Integer orderType;

    @ApiModelProperty(value = "交易金额", required = true)
    private BigDecimal tradeAmount;

    @ApiModelProperty("委托价格")
    private BigDecimal entrustPrice;

    @ApiModelProperty(value = "杠杆倍数", required = true)
    private BigDecimal leverage;


}
