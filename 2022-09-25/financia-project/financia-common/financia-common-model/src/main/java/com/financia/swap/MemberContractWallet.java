package com.financia.swap;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 会员合约钱包
 */
@Data
@TableName("member_contract_wallet")
@ApiModel(value="MemberContractWallet",description = "会员合约钱包")
public class MemberContractWallet {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "合约钱包Id")
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

    @ApiModelProperty(value = "1逐仓 0全仓")
    @TableField(exist = false)
    private ContractOrderPattern usdtPattern; // 1逐仓 0全仓

    @ApiModelProperty(value = "做多杠杆倍数（金本位））")
    private BigDecimal usdtBuyLeverage;// 做多杠杆倍数（金本位））

    @ApiModelProperty(value = "做空杠杆倍数（金本位）")
    private BigDecimal usdtSellLeverage;// 做空杠杆倍数（金本位）

    @ApiModelProperty(value = "开仓仓位(USDT本位)，正数为开多仓，负数为开空仓")
    private BigDecimal usdtBuyPosition;// 开多仓位(USDT本位)

    @ApiModelProperty(value = "冻结仓位(USDT本位)")
    private BigDecimal usdtFrozenBuyPosition;// 冻结仓位(USDT本位)

    @ApiModelProperty(value = "开仓均价(USDT本位)")
    private BigDecimal usdtBuyPrice;// 多仓均价(USDT本位)

    @ApiModelProperty(value = "开仓保证金(USDT本位)")
    private BigDecimal usdtBuyPrincipalAmount;// 多仓保证金(USDT本位)

    @ApiModelProperty(value = "开空仓位(USDT本位,多少张)")
    private BigDecimal usdtSellPosition;// 开空仓位(USDT本位,多少张)

    @ApiModelProperty(value = "冻结仓位(USDT本位,多少张)")
    private BigDecimal usdtFrozenSellPosition;// 冻结仓位(USDT本位,多少张)

    @ApiModelProperty(value = "合约面值(USDT本位,1张=多少USDT)")
    private BigDecimal usdtShareNumber;// 合约面值(USDT本位,1张=多少USDT)

    @ApiModelProperty(value = "空仓开仓均价(USDT本位)")
    private BigDecimal usdtSellPrice;// 空仓开仓均价(USDT本位)

    @ApiModelProperty(value = "空仓保证金(USDT本位)")
    private BigDecimal usdtSellPrincipalAmount;// 空仓保证金(USDT本位)

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
    @ApiModelProperty(value = "做空杠杆倍数（币本位）")
    private BigDecimal coinSellLeverage;// 做空杠杆倍数（币本位）

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

    @TableField(exist = false)
    @ApiModelProperty(value = "开空仓位(币本位)")
    private BigDecimal coinSellPosition;// 开空仓位(币本位)

    @TableField(exist = false)
    @ApiModelProperty(value = "冻结仓位(币本位)")
    private BigDecimal coinFrozenSellPosition;// 冻结仓位(币本位)

    @TableField(exist = false)
    @ApiModelProperty(value = "合约面值(币本位，一张=多少Coin)")
    private BigDecimal coinShareNumber;// 合约面值(币本位，一张=多少Coin)

    @TableField(exist = false)
    @ApiModelProperty(value = "空仓均价(币本位)")
    private BigDecimal coinSellPrice;// 空仓均价(币本位)

    @TableField(exist = false)
    @ApiModelProperty(value = "空仓保证金(币本位)")
    private BigDecimal coinSellPrincipalAmount;// 空仓保证金(币本位)

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

    @ApiModelProperty("开仓状态 0 买多 1 买空")
    private Integer direction;



}
