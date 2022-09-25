package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CancelOrderDTO", description = "撤销合约委托单DTO")
public class CancelOrderDTO implements Serializable {

    @ApiModelProperty("会员Id")
    private Long memberId;

    @ApiModelProperty("合约委托单Id")
    private Long entrustId;

}
