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
 * 公共-余额交易流水记录
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-02 14:55:32
 */
@Data
@TableName("member_business_wallet_record")
@ApiModel(value="公共-余额交易流水记录",description = "公共-余额交易流水记录")
public class MemberBusinessWalletRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键",required = false)
	private Long id;
	/**
	 * 钱包id
	 */
	@ApiModelProperty(value = "钱包id",required = false)
	private Integer businessWalletId;
	/**
	 * 操作金额
	 */
	@ApiModelProperty(value = "操作金额",required = false)
	private BigDecimal money;
	/**
	 * 大类型:1.量化 2.合约交易3.超级杠杆 4.币币交易 5.股票交易 6.资产(充值，提现)
	 */
	@ApiModelProperty(value = "大类型:1.量化 2.合约交易3.超级杠杆 4.币币交易 5.股票交易 6.资产(充值，提现)",required = false)
	private String businessType;
	/**
	 * 子类型1:收入 2.支出 3:冻结  4:解冻
	 */
	@ApiModelProperty(value = "子类型1:收入 2.支出 3:冻结  4:解冻",required = false)
	private Integer tradeType;
	/**
	 * 操作后余额
	 */
	@ApiModelProperty(value = "操作后余额",required = false)
	private BigDecimal balance;
	/**
	 * 业务关联id
	 */
	@ApiModelProperty(value = "业务关联id",required = false)
	private String businessId;
	/**
	 * 交易类型:合约swap-1:开仓swap-2:平仓 swap-3:撤销  超级杠杆:super-1:开仓 super-2:平仓 super-3:撤销 币币交易:bb-1:开仓 bb-2:平仓 bb-3:撤销
	 */
	@ApiModelProperty(value = "交易类型:合约swap-1:开仓swap-2:平仓 swap-3:撤销  超级杠杆:super-1:开仓 super-2:平仓 super-3:撤销 币币交易:bb-1:开仓 bb-2:平仓 bb-3:撤销",required = false)
	private String businessSubType;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注",required = false)
	private String remark;
	/**
	 *
	 */
	@ApiModelProperty(value = "",required = false)
	private Date createTime;

}
