package com.financia.currency;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("ex_exchange_order")
public class ExchangeOrder implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("订单主键Id")
    private Long id;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("会员Id")
    private Long memberId; //如果memberId 为0 ，则代表机器人

    @ApiModelProperty("会员名称")
    @TableField(exist = false)
    private String username;

    @ApiModelProperty("挂单类型")
    private ExchangeOrderType type;

    @ApiModelProperty("买入或卖出金额")
    private BigDecimal amount = BigDecimal.ZERO;

    @ApiModelProperty("买入或卖出数量")
    private BigDecimal num = BigDecimal.ZERO;

    @ApiModelProperty("交易对Id")
    private Long coinId;

    @ApiModelProperty("交易对符号")
    private String symbol;

    @ApiModelProperty("成交额")
    private BigDecimal tradedAmount = BigDecimal.ZERO;

    @ApiModelProperty("成交量")
    private BigDecimal turnover = BigDecimal.ZERO;

    @ApiModelProperty("成交价")
    private BigDecimal tradedPrice = BigDecimal.ZERO;

    @ApiModelProperty("币单位")
    private String coinSymbol;

    @ApiModelProperty("结算单位")
    private String baseSymbol;

    @ApiModelProperty("订单状态")
    private ExchangeOrderStatus status;

    @ApiModelProperty("订单方向")
    private ExchangeOrderDirection direction;

    @ApiModelProperty("挂单价格")
    private BigDecimal price = BigDecimal.ZERO;

    @ApiModelProperty("挂单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long createTime;

    @ApiModelProperty("交易完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long completedTime;

    @ApiModelProperty("取消时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long canceledTime;

    @ApiModelProperty("订单来源")
    private ExchangeOrderResource orderResource = ExchangeOrderResource.CUSTOMER;

    @ApiModelProperty("币币钱包Id")
    private Long currencyWalletId;


}
