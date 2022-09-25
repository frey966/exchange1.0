package com.financia.currency;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Jammy
 * @description 会员钱包
 * @date 2019/1/2 15:28
 */

@Data
@TableName("member_crypto_currency_wallet")
@ApiModel(value="MemberCryptoCurrencyWallet",description = "会员币币钱包")
public class MemberCryptoCurrencyWallet {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "币币钱包Id")
    private Long id;

    @ApiModelProperty(value = "会员Id")
    private Long memberId;

    @ApiModelProperty("会员名称")
    @TableField(exist = false)
    private String username;

    @ApiModelProperty(value = "交易币Id")
    private Long coinId;

    @ApiModelProperty("币符号")
    private String coinSymbol;

    @ApiModelProperty(value = "可用余额")
    private BigDecimal balanceMoney;

    @ApiModelProperty(value = "冻结余额")
    private BigDecimal frezzeMoney;

    @ApiModelProperty("钱包创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("0：删除 1：未删除")
    private Integer status;

    @ApiModelProperty(value = "币种图片")
    @TableField(exist = false)
    private String coinImgUrl;

}
