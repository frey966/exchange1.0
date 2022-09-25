package com.financia.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.io.Serializable;

@Data
@TableName("p_contact_us")
public class ContactUs implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "客服联系方式")
    private String kefuLink;

    @ApiModelProperty(value = "telegram 联系方式")
    private String telegramLink;

    @ApiModelProperty(value = "telegram官方频道")
    private String telegramChannel;

    @ApiModelProperty(value = "创建时间 ")
    private String createTime;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;


}
