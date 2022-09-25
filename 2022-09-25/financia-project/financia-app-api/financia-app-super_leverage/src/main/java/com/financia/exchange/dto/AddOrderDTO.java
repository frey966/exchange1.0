package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "OpenOrderDTO", description = "合约开仓DTO")
public class AddOrderDTO implements Serializable {

    @ApiModelProperty(value = "会员Id", required = true)
    private Long memberId;

    @ApiModelProperty(value = "持仓钱包Id", required = true)
    private Long walletId;

    @ApiModelProperty(value = "交易金额", required = true)
    private BigDecimal tradeAmount;

}
