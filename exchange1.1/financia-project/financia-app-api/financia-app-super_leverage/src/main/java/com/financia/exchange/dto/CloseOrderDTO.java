package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "CloseOrderDTO", description = "合约平仓DTO")
public class CloseOrderDTO implements Serializable {

    @ApiModelProperty("持仓Id")
    private Long walletId;

    @ApiModelProperty("平仓数量")
    private BigDecimal volume;

}
