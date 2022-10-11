package com.financia.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 隐私政策对象 p_privacy_policy
 * 
 * @author 花生
 * @date 2022-08-22
 */
@Data
@TableName("p_privacy_policy")
@ApiModel(value = "内容管理-隐私政策", description = "隐私政策")
public class PPrivacyPolicy implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 隐私政策地址 */
    @Excel(name = "隐私政策地址")
    @ApiModelProperty(value = "隐私政策地址")
    private String privacyUrl;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String createTime;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String updateTime;
}
