package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financia.swap.ContractOrderPattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员超级杠杆钱包
 */
@Data
@TableName("member_super_contract_wallet")
@ApiModel(value="MemberSuperContractWallet",description = "会员超级杠杆钱包")
public class MemberSuperContractWallet {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id主键ID")
    private Long id;

    @ApiModelProperty(value = "会员Id")
    private Long memberId;

    @ApiModelProperty(value = "合约币Id")
    private Long contractId;

    /**
     * 金本位合约账户
     */
    @ApiModelProperty(value = "USDT余额（金本位用）")
    private BigDecimal usdtBalance;// USDT余额（金本位用）

    @ApiModelProperty(value = "冻结保证金（金本位用）")
    private BigDecimal usdtFrozenBalance; // 冻结保证金（金本位用）

    @ApiModelProperty(value = "1逐仓 2全仓")
    private ContractOrderPattern usdtPattern; // 1逐仓 2全仓

    @ApiModelProperty(value = "做多杠杆倍数（金本位））")
    private BigDecimal usdtBuyLeverage;// 做多杠杆倍数（金本位））


    @ApiModelProperty(value = "开仓仓位(USDT本位)，正数为开多仓，负数为开空仓")
    private BigDecimal usdtBuyPosition;// 开多仓位(USDT本位)

    @ApiModelProperty(value = "冻结仓位(USDT本位)")
    private BigDecimal usdtFrozenBuyPosition;// 冻结仓位(USDT本位)

    @ApiModelProperty(value = "开仓均价(USDT本位)")
    private BigDecimal usdtBuyPrice;// 多仓均价(USDT本位)

    @ApiModelProperty(value = "开仓保证金(USDT本位)")
    private BigDecimal usdtBuyPrincipalAmount;// 多仓保证金(USDT本位)


    @ApiModelProperty(value = "盈利")
    // 用户总盈亏 = usdtProfit + usdtLoss
    private BigDecimal usdtProfit;// 盈利

    @ApiModelProperty(value = "亏损")
    private BigDecimal usdtLoss;// 亏损


    @ApiModelProperty(value = "强平价")
    private BigDecimal forceClosePrice;// 强平价

    @ApiModelProperty(value = "开仓手续费")
    private BigDecimal openFee;

    @ApiModelProperty(value = "平仓手续费")
    private BigDecimal closeFee;

    @ApiModelProperty(value = "止盈价")
    private BigDecimal stopProfitPrice;

    @ApiModelProperty(value = "止损价")
    private BigDecimal stopLossPrice;
    /**
     * 币本位合约账户
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "币种余额（币本位用）")
    private BigDecimal coinBalance;// 币种余额（币本位用）

    @TableField(exist = false)
    @ApiModelProperty(value = "冻结保证金（币本位用）")
    private BigDecimal coinFrozenBalance;

    @TableField(exist = false)
    @ApiModelProperty(value = "1逐仓 2全仓")
    private ContractOrderPattern coinPattern; // 1逐仓 2全仓

    @TableField(exist = false)
    @ApiModelProperty(value = "做多杠杆倍数（币本位））")
    private BigDecimal coinBuyLeverage;// 做多杠杆倍数（币本位））


    @TableField(exist = false)
    @ApiModelProperty(value = "开多仓位(币本位)")
    private BigDecimal coinBuyPosition;// 开多仓位(币本位)

    @TableField(exist = false)
    @ApiModelProperty(value = "冻结仓位(币本位)")
    private BigDecimal coinFrozenBuyPosition;// 冻结仓位(币本位)

    @TableField(exist = false)
    @ApiModelProperty(value = "多仓均价(币本位)")
    private BigDecimal coinBuyPrice;// 多仓均价(币本位)

    @TableField(exist = false)
    @ApiModelProperty(value = "多仓保证金(币本位)")
    private BigDecimal coinBuyPrincipalAmount;// 多仓保证金(币本位)



    @ApiModelProperty("交易对")
    @TableField(exist = false)
    private String symbol;

    @ApiModelProperty("当前持仓收益")
    @TableField(exist = false)
    private BigDecimal usdtTotalProfitAndLoss = BigDecimal.ZERO; // 持仓合约权益（空 + 多）

    @ApiModelProperty("当前持仓收益率")
    @TableField(exist = false)
    private String usdtTotalProfitAndLossRate;

    @TableField(exist = false)
    private BigDecimal coinTotalProfitAndLoss = BigDecimal.ZERO; // 持仓合约权益（空 + 多）

    @ApiModelProperty("当前市价")
    @TableField(exist = false)
    private BigDecimal currentPrice=BigDecimal.ZERO;

    @TableField(exist = false)
    private BigDecimal cnyRate = BigDecimal.valueOf(7L);

    @ApiModelProperty("平仓费率")
    @TableField(exist = false)
    private BigDecimal closeFeeRate = BigDecimal.ZERO;

    @ApiModelProperty("委托订单")
    private String entrustOrder;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("开仓状态 0 买多 1 买空")
    private Integer direction;

    @ApiModelProperty(value = "会员名称")
    @TableField(exist = false)
    private String username;


}
