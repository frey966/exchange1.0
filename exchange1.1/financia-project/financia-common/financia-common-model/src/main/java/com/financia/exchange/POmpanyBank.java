package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公司收款银行卡对象 p_ompany_bank
 * 
 * @author 花生
 * @date 2022-09-18
 */
@Data
@TableName("p_ompany_bank")
@ApiModel(value="财务管理-公司收款银行卡",description = "公司收款银行卡")
public class POmpanyBank implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @ApiModelProperty(value = "主键",required = false )
    private Long id;

    /** 开户行 */
    @Excel(name = "开户行")
    @ApiModelProperty(value = "开户行",required = false )
    private String bankName;

    /** 账号 */
    @Excel(name = "账号")
    @ApiModelProperty(value = "账号",required = false )
    private String bankNo;

    /** 持卡人姓名 */
    @Excel(name = "持卡人姓名")
    @ApiModelProperty(value = "持卡人姓名",required = false )
    private String name;

    /** 支行信息 */
    @Excel(name = "支行信息")
    @ApiModelProperty(value = "支行信息",required = false )
    private String bankInfo;

    /** 国家名称 */
    @Excel(name = "国家名称")
    @ApiModelProperty(value = "国家名称",required = false )
    private String country;

    /** 法币币种 */
    @Excel(name = "法币币种")
    @ApiModelProperty(value = "法币币种",required = false )
    private Long coinId;

    /** 类型1银行卡 */
    @Excel(name = "类型1银行卡")
    @ApiModelProperty(value = "类型1银行卡",required = false )
    private Long type;

    /** 方向：1收款卡2发放卡 */
    @Excel(name = "方向：1收款卡2发放卡")
    @ApiModelProperty(value = "方向：1收款卡2发放卡",required = false )
    private Long direction;

    /** 状态：1开启2关闭 */
    @Excel(name = "状态：1开启2关闭")
    @ApiModelProperty(value = "状态：1开启2关闭",required = false )
    private Long status;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间",required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @ApiModelProperty(value = "更新时间",required = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "法币图标",required = false )
    private String exchangeAoinAppLogo;
}
