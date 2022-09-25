package com.financia.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("p_about_us")
public class AboutUs implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "隐私政策内容_中文")
    private String contentZh;

    @ApiModelProperty(value = "隐私政策内容_英文")
    private String contentEn;

    @ApiModelProperty(value = "APP版本")
    private String appVersion;

    @ApiModelProperty(value = "创建时间 ")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;
    @ApiModelProperty(value = "0:未启用 1：启用")
    private String active;
    @ApiModelProperty(value = "平台:ios")
    private String platform;

    @ApiModelProperty(value = "下载地址")
    private String linkurl;
    @ApiModelProperty(value = "备注")
    private String remark;
}
