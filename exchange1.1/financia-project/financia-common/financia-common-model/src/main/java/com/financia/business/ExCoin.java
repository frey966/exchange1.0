package com.financia.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 公共-充值币种类型
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-11 21:44:30
 */
@Data
@TableName("ex_coin")
@ApiModel(value="公共-充值币种类型",description = "公共-充值币种类型")
public class ExCoin implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 币种名称
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "币种ID",required = false)
	private Long id;

	@ApiModelProperty(value = "币种名称",required = false)
	private String name;
	/**
	 * 判断能否充值0:否 1:是
	 */
	@ApiModelProperty(value = "判断能否充值0:否 1:是",required = false)
	private Integer canRecharge;
	/**
	 * 判断能否转账0:否 1:是
	 */
	@ApiModelProperty(value = "判断能否转账0:否 1:是",required = false)
	private Integer canTransfer;
	/**
	 * 最大提币数量0:否 1:是
	 */
	@ApiModelProperty(value = "最大提币数量0:否 1:是",required = false)
	private BigDecimal maxWithdrawAmount;
	/**
	 * 最小提币数量0:否 1:是
	 */
	@ApiModelProperty(value = "最小提币数量0:否 1:是",required = false)
	private BigDecimal minWithdrawAmount;
	/**
	 * 最小充值数量0:否 1:是
	 */
	@ApiModelProperty(value = "最小充值数量0:否 1:是",required = false)
	private BigDecimal minRechargeAmount;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序",required = false)
	private Integer sort;
	/**
	 * 0：删除 1：有效
	 */
	@ApiModelProperty(value = "0：删除 1：有效",required = false)
	private Integer status;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;

}
