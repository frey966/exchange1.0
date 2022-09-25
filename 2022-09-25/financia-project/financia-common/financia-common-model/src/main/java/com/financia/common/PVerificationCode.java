package com.financia.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册或登录发送邮箱验证码对象 p_email_verification_code
 *
 * @author ruoyi
 * @date 2022-07-13
 */
@Data
@TableName("p_verification_code")
@ApiModel(value="公共-邮箱验证码",description = "会员注册登录邮箱验证码对象介绍")
public class PVerificationCode implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键UUID */
    @TableId(type = IdType.AUTO)
    private String id;

    /** 1:邮箱 2：手机 */

    @ApiModelProperty(value = "类型1:邮箱，2：手机",required = false )
    private Integer type;

    @ApiModelProperty(value = "发送类型",required = false )
    private String action;

    /** 邮箱全名/手机号 */

    @ApiModelProperty(value = "邮箱全名/手机号",required = false )
    private String contact;

    @ApiModelProperty(value = "验证码",required = false )
    private String code;

    @ApiModelProperty(value = "发送状态1：成功 0：失败",required = false )
    private Integer sendStatus;
    /** 0：删除 1：有效 */

    private Long status;

    private Long createTime;


}
