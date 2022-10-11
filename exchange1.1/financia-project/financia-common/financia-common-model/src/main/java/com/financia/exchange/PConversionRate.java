package com.financia.exchange;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 法币汇率对象 p_conversion_rate
 * 
 * @author 花生
 * @date 2022-08-08
 */
@Data
@TableName("p_conversion_rate")
@ApiModel(value="数据配置管理-汇率设置",description = "法币汇率表")
public class PConversionRate implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键Id */
    @ApiModelProperty(value = "主键Id",required = false,example = "1" )
    private Long id;

    /** 币种符号，比如CNY */
    @Excel(name = "币种符号，比如CNY")
    @ApiModelProperty(value = "币种符号，比如CNY",required = false,example = "1" )
    private String currencySymbol;

    /** 币种名称，比如人民币 */
    @Excel(name = "币种名称，比如人民币")
    @ApiModelProperty(value = "币种名称，比如人民币",required = false,example = "1" )
    private String currencyName;

    /** 基币，默认USD */
    @Excel(name = "基币，默认USD")
    @ApiModelProperty(value = "基币，默认USD",required = false,example = "1" )
    private String baseCurrency;

    /** 汇率，比如6.6537 */
    @Excel(name = "汇率，比如6.6537")
    @ApiModelProperty(value = "汇率，比如6.6537",required = false,example = "1" )
    private BigDecimal conversionRate;

    /** 币种图片路径 */
    @Excel(name = "币种图片路径")
    @ApiModelProperty(value = "币种图片路径",required = false,example = "1" )
    private String currencyPath;

    /** 1:启用 0：未启用 */
    @Excel(name = "1:启用 0：未启用")
    @ApiModelProperty(value = "1:启用 0：未启用 ",required = false,example = "1" )
    private String active;
    /** 创建时间 */
    @ApiModelProperty(value = "创建时间 ",required = false,example = "1" )
    private String createTime;
    /** 修改时间 */
    @ApiModelProperty(value = "修改时间 ",required = false,example = "1" )
    private String updateTime;
    /** 货币ID */
    @ApiModelProperty(value = "币种ID",required = false,example = "1" )
    private String currencyId;
    /** 排序 */
    @ApiModelProperty(value = "排序",required = false )
    private String sort;


}
