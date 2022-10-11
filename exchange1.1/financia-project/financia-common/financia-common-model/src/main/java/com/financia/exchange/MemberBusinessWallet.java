package com.financia.exchange;

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
 * 会员-业务钱包金额
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-07 18:17:03
 */
@Data
@TableName("member_business_wallet")
@ApiModel(value="会员管理-会员信息管理--余额信息",description = "会员管理-会员信息管理--余额信息")
public class MemberBusinessWallet implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 客户id
	 */
	private Long memberId;
	/**
	 * 金额-usdt
	 */
	private BigDecimal money;
	/**
	 * 冻结金额
	 */
	private BigDecimal freezeMoney;

	/**
	 * 1:余额
	 */
	private Integer type;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 0：删除 1：未删除 （逻辑删除）
	 */
	private Integer status;

	@ApiModelProperty("币符号")
	@TableField(exist = false)
	private String coinSymbol;

}
