package com.financia.swap;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 合约-委托订单流水记录
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-04 21:34:13
 */
@Data
@TableName("ex_contract_order_entrust")
@ApiModel(value="合约-委托订单流水记录",description = "合约-委托订单流水记录")
public class ExContractOrderEntrust implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "",required = false)
	private Long id;
	/**
	 * 交易对基本符号
	 */
	@ApiModelProperty(value = "交易对基本符号",required = false)
	private String baseSymbol;
	/**
	 * 货币符号
	 */
	@ApiModelProperty(value = "货币符号",required = false)
	private String coinSymbol;
	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id",required = false)
	private Long memberId;

	/**
	 * 会员名称
	 */
	@ApiModelProperty(value = "会员名称",required = false)
	@TableField(exist = false)
	private String memberName;

	/**
	 * 合约id
	 */
	@ApiModelProperty(value = "合约id",required = false)
	private Long contractId;
	/**
	 * 钱包id
	 */
	@ApiModelProperty(value = "钱包id",required = false)
	private Integer walletId;
	/**
	 * 委托订单编号id
	 */
	@ApiModelProperty(value = "委托订单编号id",required = false)
	private String contractOrderEntrustId;
	/**
	 * 下单时价
	 */
	@ApiModelProperty(value = "下单时价",required = false)
	private BigDecimal currentPrice;
	/**
	 * 交易方向 0:买多 1:买空
	 */
	@ApiModelProperty(value = "交易方向 0:买多 1:买空",required = false)
	private Integer direction;
	/**
	 * 委托价格
	 */
	@ApiModelProperty(value = "委托价格",required = false)
	private BigDecimal entrustPrice;
	/**
	 * 委托订单类型(0-开仓,1-平仓,2-加仓,3-减仓,4-设置止盈价,5-设置止损价)
	 */
	@ApiModelProperty(value = "委托订单类型(0-开仓,1-平仓,2-加仓,3-减仓,4-设置止盈价,5-设置止损价)",required = false)
	private Integer entrustType;
	/**
	 * 是否是爆仓单，0：否，1：是
	 */
	@ApiModelProperty(value = "是否是爆仓单，0：否，1：是",required = false)
	private Integer isBlast;
	/**
	 * 是否是计划委托的委托单，0：否，1：是
	 */
	@ApiModelProperty(value = "是否是计划委托的委托单，0：否，1：是",required = false)
	private Integer isFromSpot;
	/**
	 * 平仓手续费usdt
	 */
	@ApiModelProperty(value = "平仓手续费usdt",required = false)
	private BigDecimal closeFee;
	/**
	 * 开仓手续费usdt
	 */
	@ApiModelProperty(value = "开仓手续费usdt",required = false)
	private BigDecimal openFee;
	/**
	 * 仓位模式都为1
	 */
	@ApiModelProperty(value = "仓位模式都为1",required = false)
	private Integer patterns;
	/**
	 * 保证金数量
	 */
	@ApiModelProperty(value = "保证金数量",required = false)
	private BigDecimal principalAmount;
	/**
	 * 保证金单位
	 */
	@ApiModelProperty(value = "保证金单位",required = false)
	private String principalUnit;
	/**
	 * 盈亏usdt
	 */
	@ApiModelProperty(value = "盈亏usdt",required = false)
	private BigDecimal profitAndLoss;
	/**
	 * 未使用
	 */
	@ApiModelProperty(value = "未使用",required = false)
	private BigDecimal shareNumber;
	/**
	 * 交易对符号
	 */
	@ApiModelProperty(value = "交易对符号",required = false)
	private String symbol;
	/**
	 * 成交价格
	 */
	@ApiModelProperty(value = "成交价格",required = false)
	private BigDecimal tradedPrice;
	/**
	 * 交易数量
	 */
	@ApiModelProperty(value = "交易数量",required = false)
	private BigDecimal tradedVolume;
	/**
	 * 触发价
	 */
	@ApiModelProperty(value = "触发价",required = false)
	private BigDecimal triggerPrice;
	/**
	 * 触发时间
	 */
	@ApiModelProperty(value = "触发时间",required = false)
	private Long triggeringTime;
	/**
	 * 委托类型 0市价 1限价 2计划委托
	 */
	@ApiModelProperty(value = "委托类型 0市价 1限价 2计划委托",required = false)
	private Integer type;
	/**
	 * 委托数量
	 */
	@ApiModelProperty(value = "委托数量",required = false)
	private BigDecimal volume;
	/**
	 * 下单金额
	 */
	@ApiModelProperty(value = "下单金额",required = false)
	private BigDecimal value;
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
	 * 平仓类型(0-用户平仓,1-止盈平仓,2-止损平仓,3-强制平仓)
	 */
	@ApiModelProperty(value = "平仓类型(0-用户平仓,1-止盈平仓,2-止损平仓,3-强制平仓)",required = false)
	private Integer closeOrderType;
	/**
	 * 杠杆倍数
	 */
	@ApiModelProperty(value = "杠杆倍数",required = false)
	private BigDecimal leverage;
	/**
	 * 开仓均价(平仓时)
	 */
	@ApiModelProperty(value = "开仓均价(平仓时)",required = false)
	private BigDecimal avaPrice;
	/**
	 * 平仓保证金返回(平仓时)
	 */
	@ApiModelProperty(value = "平仓保证金返回(平仓时)",required = false)
	private BigDecimal backAsset;
	/**
	 * 强平价(平仓时)
	 */
	@ApiModelProperty(value = "强平价(平仓时)",required = false)
	private BigDecimal forceClosePrice;
	/**
	 * 委托状态(0-委托中,1-已撤销,2-委托失败,3-委托成功)
	 */
	@ApiModelProperty(value = "委托状态(0-委托中,1-已撤销,2-委托失败,3-委托成功)",required = false)
	private Integer status;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false)
	private Long createTime;

}
