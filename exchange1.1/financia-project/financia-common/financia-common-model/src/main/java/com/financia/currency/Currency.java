package com.financia.currency;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description="币种")
@TableName("ex_currency")
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value ="ID")
    private Long id;

    @ApiModelProperty(value ="交易对 BTC/USDT格式")
    private String pair;

//    @NotBlank(message = "交易代码不能为空")
    @ApiModelProperty(value ="交易代码(例:btcusdt)")
    private String symbol;

    @ApiModelProperty(value ="状态(0启用 1停用)")
    private Integer status;

    @ApiModelProperty(value = "0:未热门 1:热门")
    private Integer popular;


}
