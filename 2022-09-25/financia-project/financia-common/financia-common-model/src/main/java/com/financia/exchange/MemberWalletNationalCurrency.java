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
 * 会员-会员法币钱包信息
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-11 21:48:29
 */
@Data
@TableName("member_wallet_national_currency")
@ApiModel(value="会员-会员法币钱包信息",description = "会员-会员法币钱包信息")
public class MemberWalletNationalCurrency implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键自动增长
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键自动增长",required = false)
	private Integer id;
	/**
	 * 法币币种id
	 */
	@ApiModelProperty(value = "法币币种id",required = false)
	private Integer coinId;
	/**
	 * 币种名称(简称)
	 */
	@ApiModelProperty(value = "币种名称(简称)",required = false)
	private Long memberId;
	/**
	 * 总金额
	 */
	@ApiModelProperty(value = "总金额",required = false)
	private BigDecimal money;
	/**
	 * 法币金额
	 */
	@ApiModelProperty(value = "法币金额",required = false)
	private BigDecimal nationalMoney;
	/**
	 * 冻结金额
	 */
	@ApiModelProperty(value = "冻结金额",required = false)
	private BigDecimal freezeMoney;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false)
	private Date createTime;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间",required = false)
	private Date updateTime;
	/**
	 * 0:删除 1：未删除
	 */
	@ApiModelProperty(value = "0:删除 1：未删除",required = false)
	private Integer status;

}
