package com.financia.common;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公链钱包对象 p_company_wallet_address
 * 
 * @author 花生
 * @date 2022-08-24
 */
@Data
@TableName("p_company_wallet_address")
@ApiModel(value="财务管理-财务账号配置",description = "公链钱包")
public class PCompanyWalletAddress implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键",required = false,example = "1" )
    private Long id;

    /** 公司收款地址 */
    @Excel(name = "公司收款地址")
    @ApiModelProperty(value = "公司收款地址",required = false,example = "1" )
    private String address;

    /** 公司秘钥 */
    @Excel(name = "公司秘钥")
    @ApiModelProperty(value = "公司秘钥",required = false,example = "1" )
    private String privateKey;

    /** 总额usdt */
    @Excel(name = "总额usdt")
    @ApiModelProperty(value = "总额usdt",required = false,example = "1" )
    private Long amount;

    /** 能量值 */
    @Excel(name = "能量值")
    @ApiModelProperty(value = "能量值",required = false,example = "1" )
    private Long energyValue;

    /** 转账手续费 */
    @Excel(name = "转账手续费")
    @ApiModelProperty(value = "转账手续费",required = false,example = "1" )
    private BigDecimal handlingFee;

    /** 1:TRC20 2:HECO 3:BSC 4:ERC20 */
    @Excel(name = "1:TRC20 2:HECO 3:BSC 4:ERC20")
    @ApiModelProperty(value = "1:TRC20 2:HECO 3:BSC 4:ERC20",required = false,example = "1" )
    private Long chainId;

    /** 转账资金，大于转账资金需要审核 */
    @Excel(name = "转账资金，大于转账资金需要审核")
    @ApiModelProperty(value = "转账资金，大于转账资金需要审核",required = false,example = "1" )
    private Long withdrowAmount;

    /** 链名称 */
    @Excel(name = "链名称")
    @ApiModelProperty(value = "链名称",required = false,example = "1" )
    private String chainName;

    /** 0:删除 1：未删除 */
    @Excel(name = "0:删除 1：未删除")
    @ApiModelProperty(value = "0:删除 1：未删除",required = false,example = "1" )
    private Long status;
    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String createTime;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String updateTime;
}
