package com.financia.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公共-公司钱包TRX转账记录对象 p_company_trx_transfer_order
 * 
 * @author 花生
 * @date 2022-08-28
 */
@Data
@TableName("p_company_trx_transfer_order")
@ApiModel(value="财务管理-trx转账记录",description = "财务管理-trx转账记录")
public class PCompanyTrxTransferOrder implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键",required = false,example = "1" )
    private Long id;

    /** 会员id */
    @Excel(name = "会员id")
    @ApiModelProperty(value = "会员id",required = false,example = "1" )
    private String memberId;

    /** 转账哈希 */
    @Excel(name = "转账哈希")
    @ApiModelProperty(value = "转账哈希",required = false,example = "1" )
    private String hex;

    /** 公司收款地址 */
    @Excel(name = "公司收款地址")
    @ApiModelProperty(value = "公司收款地址",required = false,example = "1" )
    private String fromAddress;

    /** 会员收款地址 */
    @Excel(name = "会员收款地址")
    @ApiModelProperty(value = "会员收款地址",required = false,example = "1" )
    private String toAddress;

    /** 1:TRC20 2:HECO 3:BSC 4:ERC20 */
    @Excel(name = "1:TRC20 2:HECO 3:BSC 4:ERC20")
    @ApiModelProperty(value = "1:TRC20 2:HECO 3:BSC 4:ERC20",required = false,example = "1" )
    private Long chainId;

    /** trx 数量 */
    @Excel(name = "trx 数量")
    @ApiModelProperty(value = "trx 数量",required = false,example = "1" )
    private Long transferTrx;

    /** 转账资金 */
    @Excel(name = "转账资金")
    @ApiModelProperty(value = "转账资金",required = false,example = "1" )
    private Long withdrowAmount;

    /** 转账状态：1成功，-1:失败 0:未开始 2:转账中 */
    @Excel(name = "转账状态：1成功，-1:失败 0:未开始 2:转账中")
    @ApiModelProperty(value = "转账状态：1成功，-1:失败 0:未开始 2:转账中",required = false,example = "1" )
    private Long orderStatus;

    /** 链名称 */
    @Excel(name = "链名称")
    @ApiModelProperty(value = "链名称",required = false,example = "1" )
    private String chainName;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String createTime;

    /** 修改时间 */
    @Excel(name = "修改时间")
    @ApiModelProperty(value = "修改时间",required = false,example = "1" )
    private String updateTime;

    /** 任务更新时间 */
    @Excel(name = "任务更新时间")
    @ApiModelProperty(value = "任务更新时间",required = false,example = "1" )
    private String objUpdateTime;

    /** 金需要审核资金标准 */
    @Excel(name = "金需要审核资金标准")
    @ApiModelProperty(value = "金需要审核资金标准",required = false,example = "1" )
    private String pcwawithdrowAmount;

    @ApiModelProperty(value = "会员名称",required = false,example = "1" )
    private String username;

}
