package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理和会员关系
系统用户和代理关系
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-03 23:09:56
 */
@Data
@TableName("agen_relation")
@ApiModel(value="会员管理-系统用户和代理关系",description = "代理的邀请码")
public class AgenRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(value = "id主键ID",required = false,example = "1" )
	private Long id;
	/**
	 * 代理登录账号
	 */
	@ApiModelProperty(value = "代理登录账号",required = false)
	private String userName;
	/**
	 * 代理邀请码(4个字符不能重复)
	 */
	@ApiModelProperty(value = "代理邀请码",required = false)
	private String inviteCode;
	/**
	 * 0:删除 1:未删除
	 */
	@ApiModelProperty(value = "0:删除 1:未删除",required = false)
	private Integer status;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间",required = false)
	private Date createTime;

}
