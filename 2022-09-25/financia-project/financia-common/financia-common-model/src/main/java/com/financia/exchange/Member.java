package com.financia.exchange;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员信息对象 member
 *
 * @author ruoyi
 * @date 2022-07-13
 */
@Data
@TableName("member")
@ApiModel(value="会员信息对象",description = "会员信息对象对象介绍")
public class Member  extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id主键ID",required = true )
    private Long id;

    /** 客户随机id，对外id */
    @ApiModelProperty(value = "客户id",required = true)
    private Long uid;

    /** 区域码 */
    @ApiModelProperty(value = "区域码",required = false)
    private String areaCode;

    /** 手机号 */
    @ApiModelProperty(value = "手机号",required = false )
    private String phone;

    /** 邮箱 */
    @ApiModelProperty(value = "邮箱",required = false )
    private String email;

    /** 用户名 */
    @ApiModelProperty(value = "用户名",required = false )
    private String username;

    /** 密码 */
    @ApiModelProperty(value = "密码",required = false )
    private String password;


    /** 图形密码 */
    @ApiModelProperty(value = "手势密码",required = false )
    private String gesturePassword;

    /** 邀请码 */
    @ApiModelProperty(value = "邀请码",required = false )
    private String inviteCode;

    /** 邀请人id */
    @ApiModelProperty(value = "邀请人id",required = false )
    private Long inviteId;

    /** 头像 */
    @ApiModelProperty(value = "头像",required = false )
    private String avatar;

    /** 签名 */
    @ApiModelProperty(value = "签名",required = false )
    private String sign;

    /** 验证码 */
    @ApiModelProperty(value = "验证码",required = false )
    @TableField(exist = false)
    private String verificationCode;

    /** token */
    @ApiModelProperty(value = "token",required = false )
    @TableField(exist = false)
    private String token;

    /** 0:删除 1:正常 2:黑名单(不能提现) */
    @ApiModelProperty(value = "状态",required = false )
    private Integer status;

    /** 新增属性 **/

    @ApiModelProperty(value = "代理商id",required = false )
    private Long agenRelationId;

    @ApiModelProperty(value = "代理商",required = false )
    private String agenRelationName;

    @ApiModelProperty(value = "等级名称",required = false )
    private String memberGradeName;

    @ApiModelProperty(value = "等级id",required = false )
    private Integer memberGradeId;
    /**
     * 交易密码
     */

    @ApiModelProperty(value = "交易密码",required = false )
    private String jyPassword;

    @ApiModelProperty(value = "开启手势验证1：开启0：关闭",required = false )
    private Integer gesture;


    /**
     * 交易状态，0表示禁止交易
     */
    @ApiModelProperty(value = " 交易状态，0表示禁止交易",required = false )
    private Integer transactionStatus ;

    /**
     * 登陆时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "登陆时间",required = false )
    private Date lastLoginTime;
    /**
     * 登陆ip
     */
    @ApiModelProperty(value = "登陆ip",required = false )
    private String loginIp;

    /**
     * 角色类型
     * 1：普通会员
     * 2：代理
     */
    @ApiModelProperty(value = "角色类型",required = false )
    private String roleType;

    /**
     * 系统账户id
     */
    @ApiModelProperty(value = "系统账户id",required = false )
    private Long sysUserId;

    /**
     * 交易次数
     */
    @ApiModelProperty(value = "交易次数",required = false )
    private Integer transactions;
    /**
     * 申诉次数
     */
    @ApiModelProperty(value = "申诉次数",required = false )
    private Integer appealTimes;
    /**
     * 胜诉次数
     */
    @ApiModelProperty(value = "胜诉次数",required = false )
    private Integer appealSuccessTimes;
    /**
     * 合约佣金
     */
    @ApiModelProperty(value = "合约佣金",required = false )
    private String contractAmountStr;
    /**
     * 游戏佣金
     */
    @ApiModelProperty(value = "游戏佣金",required = false )
    private String gameAmountStr;
    /**
     *
     */
    @ApiModelProperty(value = "",required = false )
    private Integer firstLevel;
    /**
     *
     */
    @ApiModelProperty(value = "",required = false )
    private Integer secondLevel;
    /**
     *
     */
    @ApiModelProperty(value = "",required = false )
    private Integer thirdLevel;
    /**
     *
     */
    @ApiModelProperty(value = "",required = false )
    private Integer loginCount;

    @ApiModelProperty(value = "签到能力",required = false )
    private Integer signInAbility;
    /**
    /**
     * 发布广告  1表示可以发布
     */
    @ApiModelProperty(value = "发布广告",required = false )
    private Integer publishAdvertise;
    /**
     * 下级人数
     */
    @ApiModelProperty(value = "下级人数",required = false )
    private Integer lowerNumber;
    /**
     * 累计充值
     */
    @ApiModelProperty(value = "累计充值",required = false )
    private BigDecimal totalRechargeAmount;
    /**
     * 累计打码量
     */
    @ApiModelProperty(value = "累计打码量",required = false )
    private BigDecimal totalBetAmount;
    /**
     * 累计提现
     */
    @ApiModelProperty(value = "累计提现",required = false )
    private BigDecimal totalWithdrawAmount;
    /**
     * 打码量比例
     */
    @ApiModelProperty(value = "打码量比例",required = false )
    private BigDecimal betScale;
    /**
     * 测试账户
     */
    @ApiModelProperty(value = "测试账户",required = false )
    private Integer test;


    private Date createTime;

    private Date updateTime;
    /**
     * 当前语言
     */
    @ApiModelProperty(value = "当前语言",required = false )
    private String defaultLanguage;

    /**
     * 默认法币
     */
    @ApiModelProperty(value = "默认法币",required = false )
    private String defaultLegal;

}
