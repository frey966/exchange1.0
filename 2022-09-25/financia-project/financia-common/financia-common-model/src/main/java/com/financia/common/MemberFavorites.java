package com.financia.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-25 15:56:42
 */
@Data
@TableName("member_favorites")
@ApiModel(value="合约-收藏功能",description = "会员会员收藏信息表")
public class MemberFavorites implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 币种名称:BTC,ETH
	 */
	private String symbol;
	/**
	 * 类型:币币，合约，股票，
	 */
	private String type;
	/**
	 * 会员id
	 */
	private Long memberId;
	/**
	 * 状态： 1正常，0取消收藏
	 */
	private Integer status;
	/**
	 *
	 */
	private Date createTime;

}
