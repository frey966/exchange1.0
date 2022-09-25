package com.financia.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 反馈问题
 * 
 * @author 葡萄
 * @date 2022-08-07
 */
@Data
@TableName("p_feedback_questions")
@ApiModel(value="反馈问题",description = "反馈问题")
public class PFeedbackQuestions implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 反馈内容 */
    @Excel(name = "反馈内容")
    @ApiModelProperty(value = "反馈内容",required = false,example = "1" )
    private String content;

    /** 联系方式 */
    @Excel(name = "联系方式")
    @ApiModelProperty(value = "联系方式",required = false,example = "1" )
    private String contact;


    /** 提交账户 */
    @Excel(name = "提交账户")
    @ApiModelProperty(value = "提交账户",required = false,example = "1" )
    private String account;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @ApiModelProperty(value = "创建时间",required = false,example = "1" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
