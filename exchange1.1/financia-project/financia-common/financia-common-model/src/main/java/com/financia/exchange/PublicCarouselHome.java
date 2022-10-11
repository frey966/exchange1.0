package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 首页轮播
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-19 22:58:22
 */
@Data
@TableName("p_carousel_home")
@ApiModel(value="首页-轮播图",description = "首页轮播轮")
public class PublicCarouselHome implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "主键",required = false )
	private Long id;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称",required = false )
	private String imageName;
	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述",required = false )
	private String details;
	/**
	 * 转跳地址
	 */
	@ApiModelProperty(value = "转跳地址",required = false )
	private String url;
	/**
	 * 图片地址
	 */
	@ApiModelProperty(value = "图片地址",required = false )
	private String imageUrl;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序",required = false )
	private Integer sort;
	/**
	 * 0启用1禁用
	 */
	@ApiModelProperty(value = "0启用1禁用",required = false )
	private Integer active;
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
	 * 语言标识（zh：中文）
	 */
	@ApiModelProperty(value = "语言标识（zh：中文）",required = false )
	private String language;
	/**
	 * 广告位置1首页
	 */
	@ApiModelProperty(value = "广告位置1首页",required = false )
	private Integer position;

}
