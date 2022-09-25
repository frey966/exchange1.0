package com.financia.business;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 合约-杠杆手续费率对象 ex_coin_fee
 *
 * @author 花生
 * @date 2022-08-11
 */
@Data
@TableName("ex_coin_fee")
@ApiModel(value="合约管理-合约手续",description = "合约-杠杆手续费率对象")
public class ExCoinFee implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键",required = false,example = "1" )
    private Long id;

    /** 最小杠杆倍数 */
    @Excel(name = "最小杠杆倍数")
    @ApiModelProperty(value = "最小杠杆倍数",required = false,example = "1" )
    private Long minLever;

    /** 最大杠杆倍数 */
    @Excel(name = "最大杠杆倍数")
    @ApiModelProperty(value = "最大杠杆倍数",required = false,example = "1" )
    private Long maxLever;

    /** 费率 */
    @Excel(name = "费率")
    @ApiModelProperty(value = "费率",required = false,example = "1" )
    private BigDecimal rate;

    /** 0:删除1:有效 */
    @Excel(name = "0:删除1:有效")
    @ApiModelProperty(value = "0:删除1:有效",required = false,example = "1" )
    private Long status;

    @ApiModelProperty(value = "创建时间",required = false)
    private String createTime;

    @ApiModelProperty(value = "名称",required = false)
    private String name;
}
