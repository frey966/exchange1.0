package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-07 21:11:10
 */
@Data
@TableName("member_recharge_order")
@ApiModel(value = "会员充值订单", description = "会员充值订单")
public class MemberRechargeOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private String id;
	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id",required = false )
	private Long memberId;
	/**
	 *
	 */
	@ApiModelProperty(value = "公链",required = false )
	private String address;

	@ApiModelProperty(value = "钱包公链",required = false )
	private Integer addressId;

	@ApiModelProperty(value = "币种名称",required = false )
	private String chain;
	/**
	 * 1:btc链（比特币 2:eth 链（以太坊）3:trx链（波场）
	 */
	@ApiModelProperty(value = " 1:btc链（比特币 2:eth 链（以太坊）3:trx链（波场）",required = false )
	private Integer chainId;

	/**
	 * 转账返回 hash值
	 */

	@ApiModelProperty(value = "转账返回 hash值",required = false )
	private String transactionHash;

	/**
	 *
	 */
	@ApiModelProperty(value = "查询次数",required = false )
	private Integer count;
	/**
	 *
	 */
	@ApiModelProperty(value = "充值金额",required = false )
	private BigDecimal money;
	/**
	 * 充值状态，0还未成功，1成功
	 */
	@ApiModelProperty(value = "充值状态，0还未成功，1成功",required = false )
	private Integer rechargeStatus;
	/**
	 * 转账状态：1成功，-1失败 0未开始 2转账中
	 */
	@ApiModelProperty(value = "转账状态：1成功，-1失败 0未开始 2转账中",required = false )
	private Integer orderStatus;
	/**
	 *
	 */
//	@ApiModelProperty(value = "余额更新状态1成功更新0未更新",required = false )
//	private Integer updateBalance;
	@ApiModelProperty(value = "充值完成时间",required = false )
	private String jobUpdateTime;
	/**
	 *
	 */
	@ApiModelProperty(value = "状态",required = false )
	private Integer status;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 修改时间
	 */
	private String updateTime;

	@ApiModelProperty(value = "会员名称",required = false)
	private String username;

}
