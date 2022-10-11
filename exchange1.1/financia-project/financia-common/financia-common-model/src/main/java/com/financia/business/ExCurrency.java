package com.financia.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 货币对象 ex_currency
 *
 * @author 花生
 * @date 2022-08-02
 */
@Data
@TableName("ex_currency")
@ApiModel(value="交易所-币币采集设置",description = "币币设置实体类")
public class ExCurrency implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 币种对,例如BTC/USDT */
    @Excel(name = "币种对,例如BTC/USDT")
    @ApiModelProperty(value = "币种对,例如BTC/USDT",required = false,example = "1" )
    private String pair;

    /** 币种符合，例如btcusdt */
    @Excel(name = "币种符合，例如btcusdt")
    @ApiModelProperty(value = "币种符合，例如btcusdt",required = false,example = "1" )
    private String symbol;

    /** 状态 */
    @Excel(name = "状态")
    @ApiModelProperty(value = "状态",required = false,example = "1" )
    private Long status;


    /** 币种符合，例如btcusdt */
    @Excel(name = "币种名称")
    @ApiModelProperty(value = "币种名称",required = false,example = "1" )
    private String pairName;

    /** 排序字段，值越小越靠前 */
    @Excel(name = "排序字段，值越小越靠前")
    @ApiModelProperty(value = "排序字段，值越小越靠前",required = false,example = "1" )
    private Long sort;

  /*  @ApiModelProperty(value = "币种id",required = false)
    private String coinId;
*/
    @ApiModelProperty(value = "图片地址",required = false)
    private String imgurl;

    @ApiModelProperty(value = "更新时间",required = false)
    private String updateTime;
    @ApiModelProperty(value = "0:未热门 1:热门",required = false)
    private String popular;
    @ApiModelProperty(value = "0:未开启1:开启采集",required = false)
    private String active;
    @ApiModelProperty(value = "0:前端不可见 1：前端可见",required = false)
    private String visible;
}
