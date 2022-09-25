package com.financia.cs;

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
 * 客服-自动回复问题
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-22 17:05:31
 */
@Data
@TableName("cs_question")
@ApiModel(value="客服-自动回复问题",description = "客服-自动回复问题")
public class CsQuestion implements Serializable {
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

	@ApiModelProperty(value = "时间",required = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;


	/**
	 * 1:上线，0下线
	 */
	@ApiModelProperty(value = "1:上线，0下线",required = false)
	private Integer onLine;
	/**
	 *
	 */
	@ApiModelProperty(value = "",required = false)
	private Integer status;

}
