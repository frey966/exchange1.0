package com.financia.common;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 内容管理_合规经营对象 p_compliance
 * 
 * @author ruoyi
 * @date 2022-08-23
 */
@Data
@TableName("p_compliance")
@ApiModel(value = "内容管理_合规经营", description = "内容管理_合规经营")
public class PCompliance implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /** 内容 */
    @Excel(name = "内容")
    @ApiModelProperty(value = "内容")
    private String contentstr;

    /** 类型1股票2数值货币 */
    @Excel(name = "类型1股票2数值货币")
    @ApiModelProperty(value = "类型1股票2数值货币")
    private Long type;

    /** 标题 */
    @Excel(name = "标题")
    @ApiModelProperty(value = "标题")
    private String title;

    /** 名称1 */
    @Excel(name = "名称1")
    @ApiModelProperty(value = "名称1")
    private String name1;

    /** 名称2 */
    @Excel(name = "名称2")
    @ApiModelProperty(value = "名称2")
    private String name2;

    /** 图片1 */
    @Excel(name = "图片1")
    @ApiModelProperty(value = "图片1")
    private String imgurl1;

    /** 图片2 */
    @Excel(name = "图片2")
    @ApiModelProperty(value = "图片2")
    private String imgurl2;

    @ApiModelProperty(value = "创建时间",required = false )
    private String createTime;

    @ApiModelProperty(value = "修改时间",required = false)
    private String updateTime;
}
