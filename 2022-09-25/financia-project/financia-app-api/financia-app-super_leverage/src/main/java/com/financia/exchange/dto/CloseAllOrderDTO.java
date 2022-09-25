package com.financia.exchange.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CloseAllOrderDTO", description = "合约全部平仓DTO")
public class CloseAllOrderDTO implements Serializable {

    @ApiModelProperty("会员Id")
    private Long memberId;

}
