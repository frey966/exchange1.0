package com.financia.exchange.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value="会员管理-汇兑业务",description = "汇兑业务")
public class MemberConversionVo {

    /** 主键id */
    @ApiModelProperty(value = "主键id",required = false,example = "1" )
    private Long id;

    @ApiModelProperty(value = "法币币种", example = "CNY" )
    private String coinName;

    @ApiModelProperty(value = "会员邮箱")
    private String email;

    @ApiModelProperty(value = "会员账号,搜索时用这个字段")
    private String userName;

    @ApiModelProperty(value = "会员手机号")
    private String phone;

    @ApiModelProperty(value = "申请换汇金额USDT")
    private BigDecimal exchangeAmount;

    @ApiModelProperty(value = "换汇后法币金额")
    private BigDecimal nationalMoney;

    @ApiModelProperty(value = "法币符号")
    private String symbols;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "订单状态1成功，-1:失败")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ApiModelProperty(value = "法币币种id")
    private String coinId;

    @ApiModelProperty(value = "会员id")
    private String memberId;

    @ApiModelProperty(value = "0:删除 1：未删除")
    private String status;
}
