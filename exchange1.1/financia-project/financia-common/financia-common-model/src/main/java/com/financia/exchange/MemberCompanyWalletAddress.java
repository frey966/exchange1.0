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
 * 公链钱包
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-23 21:01:00
 */
@Data
@TableName("p_company_wallet_address")
@ApiModel(value="公链钱包",description = "公链钱包")
public class MemberCompanyWalletAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键",required = false)
	private Integer id;
	/**
	 * 公司收款地址
	 */
	@ApiModelProperty(value = "公司收款地址",required = false)
	private String address;
	/**
	 * 公司秘钥
	 */
	@ApiModelProperty(value = "公司秘钥",required = false)
	private String privateKey;
	/**
	 * 总额usdt
	 */
	@ApiModelProperty(value = "总额usdt",required = false)
	private Integer amount;
	/**
	 * 能量值
	 */
	@ApiModelProperty(value = "能量值",required = false)
	private String energyValue;
	/**
	 * 1:TRC20 2:HECO 3:BSC 4:ERC20
	 */
	@ApiModelProperty(value = "1:TRC20 2:HECO 3:BSC 4:ERC20",required = false)
	private Integer chainId;
	/**
	 * 链名称
	 */
	@ApiModelProperty(value = "链名称",required = false)
	private String chainName;

	private BigDecimal withdrowAmount;

	/**
	 * 提现手续费
	 */
	private BigDecimal handlingFee;

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

	private Date objUpdateTime;
	/**
	 * 0:删除 1：未删除
	 */
	@ApiModelProperty(value = "0:删除 1：未删除",required = false)
	private Integer status;

}
