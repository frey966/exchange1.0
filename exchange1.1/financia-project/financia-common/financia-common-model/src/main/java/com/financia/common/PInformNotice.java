package com.financia.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共-通知表
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-18 19:35:54
 */
@Data
@TableName("p_inform_notice")
@ApiModel(value="公共-通知表",description = "公共-通知表")
public class PInformNotice implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键",required = false)
	private Long id;
	/**
	 * 标题
	 */
	@ApiModelProperty(value = "标题",required = false)
	private String title;
	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容",required = false)
	private String content;
	/**
	 * 时间
	 */
	@ApiModelProperty(value = "时间",required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/**
	 * 发布人
	 */
	@ApiModelProperty(value = "发布人",required = false)
	private String createBy;

    @ApiModelProperty(value = "上线状态，1在线 0下线",required = false)
    private Integer onLine;

    @ApiModelProperty(value = "0删除，1 有效",required = false)
    private Integer status;


}
