package com.financia.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 语言对象 member_language
 * 
 * @author 花生
 * @date 2022-08-18
 */
@Data
@TableName("member_language")
@ApiModel(value="数据配置管理-国际语音管理",description = "国际语音管理")
public class MemberLanguage implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @ApiModelProperty(value = "主键id",required = false )
    private Long id;

    /** 语言类型 */
    @Excel(name = "语言类型")
    @ApiModelProperty(value = "语言类型",required = false )
    private String languageType;

    /** 语言 */
    @Excel(name = "语言")
    @ApiModelProperty(value = "语言",required = false )
    private String language;

    /** 图标 */
    @Excel(name = "图标")
    @ApiModelProperty(value = "图标",required = false )
    private String imgurl;
    @ApiModelProperty(value = "创建时间",required = false )
    private String createTime;

    @ApiModelProperty(value = "修改时间",required = false)
    private String updateTime;

    @ApiModelProperty(value = "语言标签",required = false)
    private String lablestr;

    @ApiModelProperty(value = "0:未启用 1：启用",required = false)
    private String active;

}
