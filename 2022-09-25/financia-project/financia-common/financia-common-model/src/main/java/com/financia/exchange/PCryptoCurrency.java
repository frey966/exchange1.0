package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公共-加密货币对象 p_crypto_currency
 * 
 * @author 花生
 * @date 2022-08-07
 */
@Data
@TableName("p_crypto_currency")
@ApiModel(value="数据配置管理-加密货币",description = "加密货币")
public class PCryptoCurrency implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 币种名字 */
    @Excel(name = "币种名字")
    @ApiModelProperty(value = "币种名字",required = false,example = "1" )
    private String coinName;

    /** 货币图片 */
    @Excel(name = "货币图片")
    @ApiModelProperty(value = "货币图片",required = false,example = "1" )
    private String imageUrl;

    /** 币种ID */
    @Excel(name = "币种ID")
    @ApiModelProperty(value = "币种ID",required = false,example = "1" )
    private String coinId;

    /** 0:删除 1：未删除 */
    @Excel(name = "0:删除 1：未删除")
    @ApiModelProperty(value = "0:删除 1：未删除",required = false,example = "1" )
    private Long status;

    /** 货币排名 */
    @Excel(name = "货币排名")
    @ApiModelProperty(value = "货币排名",required = false,example = "1" )
    private Long ranking;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间",required = false )
    private String createTime;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间",required = false )
    private String updateTime;


}
