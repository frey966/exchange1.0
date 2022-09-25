package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员汇兑收藏对象 member_currency_ollect
 * 
 * @author 花生
 * @date 2022-08-30
 */
@Data
@TableName("member_exchange_currency_favorite")
@ApiModel(value = "会员管理-法币收藏", description = "会员管理-法币收藏")
public class MemberExchangeCurrencyFavorite implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @ApiModelProperty(value = "主键id",required = false)
    private Long id;

    /** 货币id */
    @Excel(name = "货币id")
    @ApiModelProperty(value = "货币id",required = false)
    private Long currencyId;

    /** 会员id */
    @Excel(name = "会员id")
    @ApiModelProperty(value = "会员id",required = false)
    private Long memberId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "修改时间",required = false)
    private String createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",required = false)
    private String updateTime;

}
