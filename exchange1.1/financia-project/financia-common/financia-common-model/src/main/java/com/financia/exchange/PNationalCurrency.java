package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公共-法币信息对象 p_national_currency
 * 
 * @author 花生
 * @date 2022-08-07
 */
@Data
@TableName("p_national_currency")
@ApiModel(value="数据配置管理-法币信息",description = "法币信息")
public class PNationalCurrency implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键",required = false )
    private Long id;

    /** 币种名称(简称) */
    @Excel(name = "币种名称(简称)")
    @ApiModelProperty(value = "币种名称(简称)",required = false )
    private String exchangeCoinName;

    /** 中文名字 */
    @Excel(name = "中文名字")
    @ApiModelProperty(value = "中文名字",required = false )
    private String exchangeCoinZhName;

    /** 英文名字 */
    @Excel(name = "英文名字")
    @ApiModelProperty(value = "英文名字",required = false )
    private String exchangeCoinEnName;

    /** 币种PC端logo */
    @Excel(name = "币种PC端logo")
    @ApiModelProperty(value = "币种PC端logo",required = false )
    private String exchangeCoinPcLogo;

    /** 币种APP端logo */
    @Excel(name = "币种APP端logo")
    @ApiModelProperty(value = "币种APP端logo",required = false )
    private String exchangeCoinAppLogo;

    /** 货币排名 */
    @Excel(name = "货币排名")
    @ApiModelProperty(value = "货币排名",required = false )
    private Long ranking;

    /** 0:删除 1：未删除 */
    @Excel(name = "0:删除 1：未删除")
    @ApiModelProperty(value = "0:删除 1：未删除",required = false )
    private Long status;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间",required = false )
    private String createTime;

    /** 修改时间 */
    @ApiModelProperty(value = "修改时间",required = false )
    private String updateTime;

    /** 费率 */
    @ApiModelProperty(value = "费率",required = false )
    private String conversionRate;

    /** 是否热搜 1不是 2是 */
    @ApiModelProperty(value = "是否热搜1不是 2是",required = false )
    private String ishot;

    /** 交易对 */
    @ApiModelProperty(value = "交易对",required = false )
    private String pair;

    /** 交易对 */
    @ApiModelProperty(value = "交易对名称",required = false )
    private String pairName;

    /** 汇兑收藏id */
    @ApiModelProperty(value = "汇兑收藏id",required = false )
    private String mcoid;

    /** 会员id */
    @ApiModelProperty(value = "会员id",required = false )
    private String memberId;

    /** 币种符号 */
    @ApiModelProperty(value = "币种符号",required = false )
    private String symbols;

    /** 币种符号 */
    @ApiModelProperty(value = "用户币种余额",required = false )
    private String money;
}
