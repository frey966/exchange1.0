package com.financia.superleverage;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("ex_super_coin_fee")
@ApiModel(value="SuperCoinFee",description = "超级杠杆手续费")
public class SuperCoinFee implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id主键ID")
    private Long id;

    private Integer minLever; //最小杠杆数
    private Integer maxLever; //最大杠杆数
    private BigDecimal rate;//杠杆费率

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
