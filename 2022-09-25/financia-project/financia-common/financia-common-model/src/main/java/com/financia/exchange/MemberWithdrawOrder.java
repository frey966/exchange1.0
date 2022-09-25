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
 * 交易所-会员提现申请
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-09 20:53:37
 */
@Data
@TableName("member_withdraw_order")
@ApiModel(value="会员管理-会员提现记录",description = "会员管理-会员提现记录")
public class MemberWithdrawOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键ID",required = false)
	private Long id;
	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id",required = false)
	private Long memberId;
	/**
	 * 收款地址
	 */
	@ApiModelProperty(value = "收款地址",required = false)
	private String toAddress;
	/**
	 * 转账地址
	 */
	@ApiModelProperty(value = "转账地址",required = false)
	private String fromAddress;
	/**
	 * 链类型：TRX，BSC，ETH，HECO
	 */
	@ApiModelProperty(value = "链类型：TRX，BSC，ETH，HECO",required = false)
	private String chain;
	/**
	 * 链类型：TRX，BSC，ETH，HECO
	 */
	@ApiModelProperty(value = "链类型Id",required = false)
	private Integer chainId;
	/**
	 * 充值金额 usdt
	 */
	@ApiModelProperty(value = "充值金额 usdt",required = false)
	private BigDecimal money;

	@ApiModelProperty(value = "手续费率",required = false)
	private BigDecimal poundageLu;

	@ApiModelProperty(value = "手续费",required = false)
	private BigDecimal poundage;
	/**
	 * 提现状态，0未成功,1:成功
	 */
	@ApiModelProperty(value = "提现状态，0未成功,1:成功",required = false)
	private Integer withdrawStatus;
	/**
	 * 转账状态：1成功，-1:失败 0:未开始 2:转账中
	 */
	@ApiModelProperty(value = "转账状态：1成功，-1:失败 0:未开始 2:转账中",required = false)
	private Integer orderStatus;

	/**
	 * 转账哈希
	 */
	@ApiModelProperty(value = "",required = false)
	private String hex;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注",required = false)
	private String remark;
	/**
	 * 1:正常 0:删除
	 */
	@ApiModelProperty(value = "1:正常 0:删除",required = false)
	private Integer status;
	/**
	 * 审核状态 0:待审核,1:通过 2:拒绝
	 */
	@ApiModelProperty(value = "审核状态 0:待审核,1:通过 2:拒绝 3:自动通过",required = false)
	private Integer checkStatus;
	/**
	 * 创建者
	 */
	@ApiModelProperty(value = "创建者",required = false)
	private String createBy;
	/**
	 * 更新者
	 */
	@ApiModelProperty(value = "更新者",required = false)
	private String updateBy;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false)
	private String createTime;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间",required = false)
	private String updateTime;

	@ApiModelProperty(value = "定时任务刷新时间",required = false)
	private String jobUpdateTime;

	@ApiModelProperty(value = "会员名称",required = false)
	private String username;

	@ApiModelProperty(value = "金需要审核资金标准",required = false)
	private String pcwawithdrowAmount;

	@ApiModelProperty(value = "查询次数",required = false)
	private String count;

}
