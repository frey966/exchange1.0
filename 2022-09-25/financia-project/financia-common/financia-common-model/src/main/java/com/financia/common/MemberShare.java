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
 * @author Yezi
 */
@Data
@TableName("p_share_info")
@ApiModel(value = "用户-分享功能", description = "用户分享链接信息表")
public class MemberShare implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Integer shareId;

    /**
     * 分享图片-英文
     */
    @Excel(name = "语言标识")
    @ApiModelProperty(value = "分享图片-英文", required = false, example = "1")
    private String shareImageEn;

    /**
     * 分享图片-中文
     */
    @Excel(name = "语言标识")
    @ApiModelProperty(value = "分享图片-中文", required = false, example = "1")
    private String shareImageZh;

    /**
     * 英文内容
     */
    @Excel(name = "语言标识")
    @ApiModelProperty(value = "分享图片-英文", required = false, example = "1")
    private String shareContentEn;

    /**
     * 中文内容
     */
    @Excel(name = "语言标识")
    @ApiModelProperty(value = "中文内容", required = false, example = "1")
    private String shareContentZh;

    /**
     * 分享链接
     */
    @Excel(name = "语言标识")
    @ApiModelProperty(value = "中文内容", required = false, example = "1")
    private String link;

    /**
     * 0：失效 1：启动
     */
    @Excel(name = "语言标识")
    @ApiModelProperty(value = "信息状态 0：失效 1：启动", required = false, example = "1")
    private Integer active;

    @Excel(name = "时间信息")
    @ApiModelProperty(value = "创建时间", required = false, example = "1")
    private String createTime;

}
