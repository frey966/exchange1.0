package com.financia.exchange;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 会员-合约交易 杠杆交易 币币交易 资金移记录对象 member_asset_records
 * 
 * @author 花生
 * @date 2022-09-26
 */
@Data
@TableName("member_asset_records")
@ApiModel(value="交易订单管理-交易订单",description = "交易订单管理-交易订单")
public class MemberAssetRecords implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键",required = false)
    private Long id;

    /** 客户id */
    @Excel(name = "客户id")
    @ApiModelProperty(value = "客户id",required = false)
    private Long memberId;

    /** 交易类型 1.合约交易 2.超级杠杆 3.币币交易 */
    @Excel(name = "交易类型 1.合约交易 2.超级杠杆 3.币币交易")
    @ApiModelProperty(value = "交易类型 1.合约交易 2.超级杠杆 3.币币交易",required = false)
    private Long assetType;

    /** 数量，收入为正，支出为负 */
    @Excel(name = "数量，收入为正，支出为负")
    @ApiModelProperty(value = "数量，收入为正，支出为负",required = false)
    private BigDecimal amount;

    /** 交易对 */
    @Excel(name = "交易对")
    @ApiModelProperty(value = "交易对",required = false)
    private String symbol;

    /** 委托订单 */
    @Excel(name = "委托订单")
    @ApiModelProperty(value = "委托订单",required = false)
    private String entrustOrder;

    /** 交易说明 */
    @Excel(name = "交易说明")
    @ApiModelProperty(value = "交易说明",required = false)
    private String comment;

    @ApiModelProperty(value = "创建时间",required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "会员名称",required = false)
    private String username;
}
