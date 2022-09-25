package com.financia.swap;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financia.common.core.utils.uuid.GeneratorUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 委托订单
 */
@Data
@TableName("ex_contract_order_entrust")
@ApiModel(value="ContractOrderEntrust",description = "委托订单")
public class ContractOrderEntrust implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id主键ID")
    private Long id;

    @ApiModelProperty("合约交易对Id")
    private Long contractId;

    @ApiModelProperty("用户id")
    private Long memberId;

    @ApiModelProperty("合约委托订单ID")
    private String contractOrderEntrustId= GeneratorUtil.getOrderId("CE");

    @ApiModelProperty("0全仓 1逐仓")
    private ContractOrderPattern patterns;

    @ApiModelProperty("委托订单类型")
    private ContractOrderEntrustType entrustType;

    @ApiModelProperty("0买 1卖")
    private ContractOrderDirection direction;

    @ApiModelProperty("委托类型 0市价 1限价 2计划委托")
    private ContractOrderType type;

    @ApiModelProperty("交易对符号")
    private String symbol;
    @ApiModelProperty("币单位")
    private String coinSymbol;
    @ApiModelProperty("结算单位")
    private String baseSymbol;

    @ApiModelProperty("触发价(委托方式是计划/限价必填)")
    private BigDecimal triggerPrice;

    @ApiModelProperty("委托价(委托方式是计划/限价必填)")
    private BigDecimal entrustPrice;

    @ApiModelProperty("成交均价")
    private BigDecimal tradedPrice;

    @ApiModelProperty("本金单位（如：USDT）")
    private String principalUnit;

    @ApiModelProperty("冻结保证金")
    private BigDecimal principalAmount;

    @ApiModelProperty("下单时价")
    private BigDecimal currentPrice;

    @ApiModelProperty("开仓手续费")
    private BigDecimal openFee=BigDecimal.ZERO;

    @ApiModelProperty("平仓手续费")
    private BigDecimal closeFee=BigDecimal.ZERO;

    @ApiModelProperty("合约面值")
    private BigDecimal shareNumber;

    @ApiModelProperty("开仓均价")
    private BigDecimal avaPrice;

    @ApiModelProperty("资产返还")
    private BigDecimal backAsset;

    @ApiModelProperty("强平价")
    private BigDecimal forceClosePrice;


    @ApiModelProperty("委托数量")
    private BigDecimal volume;// 委托数量（张） //新逻辑 合约持仓量。比如 btc/usdt 代表btc的量

    @ApiModelProperty("成交数量（张）")
    private BigDecimal tradedVolume;

    @ApiModelProperty("盈亏金额")
    private BigDecimal profitAndLoss;

    @ApiModelProperty("订单状态 0:委托中/1:已撤销/2:委托失败/3:委托成功")
    private ContractOrderEntrustStatus status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long createTime;

    @ApiModelProperty("触发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Long triggeringTime;

    @ApiModelProperty("是否是计划委托的委托单，0：否，1：是")
    private int isFromSpot;

    @ApiModelProperty("是否是爆仓单，0：否，1：是")
    private int isBlast;

    @ApiModelProperty("下单金额")
    private BigDecimal value=BigDecimal.ZERO;//下单金额 这个金额是去除了手续费的基恩

    @ApiModelProperty(value = "止盈价")
    private BigDecimal stopProfitPrice=BigDecimal.ZERO;

    @ApiModelProperty(value = "止损价")
    private BigDecimal stopLossPrice=BigDecimal.ZERO;

    @ApiModelProperty("平仓类型")
    private ContractOrderEntrustCloseType closeOrderType;

    @ApiModelProperty(value = "杠杆倍数")
    private BigDecimal leverage;

    @ApiModelProperty(value = "钱包id")
    private Long walletId;

   @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
