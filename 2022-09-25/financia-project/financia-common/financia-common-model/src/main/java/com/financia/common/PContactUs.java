package com.financia.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 个人中心-联系我们对象 p_contact_us
 * 
 * @author 花生
 * @date 2022-08-22
 */
@Data
@TableName("p_contact_us")
@ApiModel(value = "内容管理-联系我们", description = "个人中心-联系我们")
public class PContactUs implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 客服联系方式 */
    @Excel(name = "客服联系方式")
    @ApiModelProperty(value = "隐私政策内容_中文")
    private String kefuLink;

    /** telegram 联系方式 */
    @Excel(name = "telegram 联系方式")
    @ApiModelProperty(value = "隐私政策内容_中文")
    private String telegramLink;

    /** telegram官方频道 */
    @Excel(name = "telegram官方频道")
    @ApiModelProperty(value = "隐私政策内容_中文")
    private String telegramChannel;

    /** 0:未启用 1：启用 */
    @Excel(name = "0:未启用 1：启用")
    @ApiModelProperty(value = "隐私政策内容_中文")
    private String active;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String createTime;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String updateTime;
}
