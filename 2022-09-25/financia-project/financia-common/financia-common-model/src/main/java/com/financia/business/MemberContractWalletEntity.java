package com.financia.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合约-会员持仓数据
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-04 23:28:08
 */
@Data
@TableName("member_contract_wallet")
@ApiModel(value="合约-会员持仓数据",description = "合约-会员持仓数据")
public class MemberContractWalletEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键",required = false)
	private Long id;
	/**
	 * 客户id
	 */
	@ApiModelProperty(value = "客户id",required = false)
	private Long memberId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员名称",required = false)
	@TableField(exist = false)
	private String memberName;

	/**
	 * 交易方向 0:买多 1:买空
	 */
	@ApiModelProperty(value = "交易方向 0:买多 1:买空",required = false)
	private Integer direction;
	/**
	 * USDT余额
	 */
	@ApiModelProperty(value = "USDT余额",required = false)
	private BigDecimal usdtBalance;
	/**
	 * 杠杆倍数
	 */
	@ApiModelProperty(value = "杠杆倍数",required = false)
	private BigDecimal usdtBuyLeverage;
	/**
	 * 持仓数量(正数是开多，负数是开空，0是没有持仓)
	 */
	@ApiModelProperty(value = "持仓数量(正数是开多，负数是开空，0是没有持仓)",required = false)
	private BigDecimal usdtBuyPosition;
	/**
	 * 开仓均价
	 */
	@ApiModelProperty(value = "开仓均价",required = false)
	private BigDecimal usdtBuyPrice;
	/**
	 * 开仓保证金
	 */
	@ApiModelProperty(value = "开仓保证金",required = false)
	private BigDecimal usdtBuyPrincipalAmount;
	/**
	 * 冻结余额
	 */
	@ApiModelProperty(value = "冻结余额",required = false)
	private BigDecimal usdtFrozenBalance;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal usdtFrozenBuyPosition;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal usdtFrozenSellPosition;
	/**
	 * USDT亏损
	 */
	@ApiModelProperty(value = "USDT亏损",required = false)
	private BigDecimal usdtLoss;
	/**
	 * 未使用，默认为1
	 */
	@ApiModelProperty(value = "未使用，默认为1",required = false)
	private Integer usdtPattern;
	/**
	 * USDT盈利
	 */
	@ApiModelProperty(value = "USDT盈利",required = false)
	private BigDecimal usdtProfit;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal usdtSellLeverage;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal usdtSellPosition;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal usdtSellPrice;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal usdtSellPrincipalAmount;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal usdtShareNumber;
	/**
	 * 合约交易对id
	 */
	@ApiModelProperty(value = "合约交易对id",required = false)
	private Long contractId;
	/**
	 * 强平价
	 */
	@ApiModelProperty(value = "强平价",required = false)
	private BigDecimal forceClosePrice;
	/**
	 * 开仓手续费
	 */
	@ApiModelProperty(value = "开仓手续费",required = false)
	private BigDecimal openFee;
	/**
	 * 平仓手续费
	 */
	@ApiModelProperty(value = "平仓手续费",required = false)
	private BigDecimal closeFee;
	/**
	 * 止盈价
	 */
	@ApiModelProperty(value = "止盈价",required = false)
	private BigDecimal stopProfitPrice;
	/**
	 * 止损价
	 */
	@ApiModelProperty(value = "止损价",required = false)
	private BigDecimal stopLossPrice;
	/**
	 * 交易所损失
	 */
	@ApiModelProperty(value = "交易所损失",required = false)
	private BigDecimal exchangeLoss;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false)
	private Date createTime;

}
