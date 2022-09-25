package com.financia.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员数字货币和法币关系中间对象 member_coin
 * 
 * @author 花生
 * @date 2022-08-04
 */
@Data
@TableName("member_coin")
@ApiModel(value="公共-会员数字货币和法币关系中间表(有改动)",description = "会员数字货币和法币关系中间表")
public class MemberCoin implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 币种ID */
    @Excel(name = "币种ID")
    @ApiModelProperty(value = "币种ID",required = false,example = "1" )
    private String coinId;

    /** 币种类型: 1:数字货币 2:法币 */
    @Excel(name = "币种类型: 1:数字货币 2:法币")
    @ApiModelProperty(value = "币种类型: 1:数字货币 2:法币",required = false,example = "1" )
    private Long coinType;

    /** 0:删除 1：未删除 */
    @Excel(name = "0:删除 1：未删除")
    @ApiModelProperty(value = "0:删除 1：未删除",required = false,example = "1" )
    private Long status;

    /** 货币排名 */
    @Excel(name = "货币排名")
    @ApiModelProperty(value = "货币排名",required = false,example = "1" )
    private Long ranking;

    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String createTime;

    @ApiModelProperty(value = "修改时间",required = false,example = "1" )
    private String updateTime;

}
