package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员提现公链
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-12 23:28:49
 */
@Data
@TableName("member_wallet_withdraw_address")
@ApiModel(value="会员提现公链",description = "会员提现公链")
public class MemberWalletWithdrawAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键",required = false)
	private Integer id;
	/**
	 * 客户id
	 */
	@ApiModelProperty(value = "客户id",required = false)
	private Long memberId;
	/**
	 * 收款地址地址
	 */
	@ApiModelProperty(value = "收款地址地址",required = false)
	private String address;
	/**
	 * 累计提现次数
	 */
	@ApiModelProperty(value = "累计提现次数",required = false)
	private Integer tradeCount;
	/**
	 * 累计提现成功金额
	 */
	@ApiModelProperty(value = "累计提现成功金额",required = false)
	private BigDecimal sumMoney;
	/**
	 * 1:btc链 比特币 2:eth 链（以太坊）3:trx链（波场）
	 */
	@ApiModelProperty(value = "1:btc链 比特币 2:eth 链（以太坊）3:trx链（波场）",required = false)
	private Integer chainId;
	/**
	 * 链名称
	 */
	@ApiModelProperty(value = "链名称",required = false)
	private String chainName;

	@ApiModelProperty(value = "备注",required = false)
	private String remark;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间",required = false)
	private Date updateTime;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false)
	private Date createTime;
	/**
	 * 0:删除 1：未删除
	 */
	@ApiModelProperty(value = "0:删除 1：未删除",required = false)
	private Integer status;

}
