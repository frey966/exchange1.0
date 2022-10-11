package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公告管理对象 p_notice
 * 
 * @author 花生
 * @date 2022-08-16
 */
@Data
@TableName("p_notice")
@ApiModel(value="内容管理-公告管理",description = "公告管理")
public class PNotice implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @ApiModelProperty(value = "主键",required = false )
    private Long id;

    /** 标题 */
    @Excel(name = "标题")
    @ApiModelProperty(value = "标题",required = false )
    private String title;

    /** 内容 */
    @Excel(name = "内容")
    @ApiModelProperty(value = "内容",required = false )
    private String details;

    /** 类型1合约2量化 */
    @Excel(name = "类型1合约2量化")
    @ApiModelProperty(value = "类型1合约2量化",required = false )
    private String type;

    /** 语言 */
    @Excel(name = "语言")
    @ApiModelProperty(value = "语言",required = false )
    private String language;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",required = false )
    private String createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间",required = false )
    private String updateTime;

    /**
     * 状态0开启1关闭
     */
    @ApiModelProperty(value = "状态0开启1关闭",required = false )
    private String status;
}
