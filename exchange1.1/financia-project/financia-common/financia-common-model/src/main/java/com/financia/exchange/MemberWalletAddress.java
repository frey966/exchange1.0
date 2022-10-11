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
 * @date 2022-08-05 20:22:36
 */
@Data
@TableName("member_wallet_address")
@ApiModel(value="会员信息对象",description = "会员信息对象对象介绍")
public class MemberWalletAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id主键ID",required = false )
	private Integer id;
	/**
	 * 客户id
	 */
	@ApiModelProperty(value = "客户id",required = false )
	private Long memberId;
	/**
	 * 收款地址地址
	 */
	@ApiModelProperty(value = "收款地址地址",required = false )
	private String address;
	/**
	 * 私钥，建议加密
	 */
	@ApiModelProperty(value = "私钥",required = false )
	private String privateKey;
	/**
	 * 1:btc链（比特币 2:eth 链（以太坊）3:trx链（波场）
	 */
	@ApiModelProperty(value = " 1:btc链（比特币 2:eth 链（以太坊）3:trx链（波场）",required = false )
	private Integer chainId;
	/**
	 * 链名称
	 */
	@ApiModelProperty(value = "链名称",required = false )
	private String chainName;

	@ApiModelProperty(value = "充值次数",required = false )
	private Integer tradeCount;

	@ApiModelProperty(value = "累计充值金额",required = false )
	private BigDecimal sumRechargeMoney;
	/**
	 * 能量值
	 */
	private BigDecimal energyValue;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间",required = false )
	private Date updateTime;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false )
	private Date createTime;
	/**
	 * 0:删除 1：未删除
	 */
	@ApiModelProperty(value = "0:删除 1：未删除",required = false )
	private Integer status;

}
