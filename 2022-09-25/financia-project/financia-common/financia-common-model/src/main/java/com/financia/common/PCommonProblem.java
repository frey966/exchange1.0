package com.financia.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他对象 p_common_problem
 * 
 * @author 花生
 * @date 2022-08-07
 */
@Data
@TableName("p_common_problem")
@ApiModel(value="内容管理-帮助管理",description = "帮助管理")
public class PCommonProblem implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 语言标识（zh：中文） */
    @Excel(name = "语言标识", readConverterExp = "zh：中文")
    @ApiModelProperty(value = "语言标识",required = false,example = "1" )
    private String language;

    /** 文本 */
    @Excel(name = "文本")
    @ApiModelProperty(value = "文本",required = false,example = "1" )
    private String content;

    /** 标题 */
    @Excel(name = "标题")
    @ApiModelProperty(value = "标题",required = false,example = "1" )
    private String title;

    /** 类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他 */
    @Excel(name = "类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他")
    @ApiModelProperty(value = "类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他",required = false,example = "1" )
    private Long type;

    /** 排序 */
    @Excel(name = "排序")
    @ApiModelProperty(value = "排序",required = false,example = "1" )
    private Long sort;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    private String createTime;

    /** 修改时间 */
    @Excel(name = "修改时间")
    @ApiModelProperty(value = "修改时间",required = false,example = "1" )
    private String updateTime;

    /** 图片url,多张图片以,隔开 123.png,234.png */
    @Excel(name = "图片url")
    @ApiModelProperty(value = "图片url",required = false,example = "1" )
    private String imageUrl;

    /** status 状态 1为开启 0关闭*/
    @Excel(name = "状态")
    @ApiModelProperty(value = "状态",required = false,example = "1" )
    private Integer status;

}
