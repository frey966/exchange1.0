package com.financia.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 业务类型对象 p_business_type
 * 
 * @author 花生
 * @date 2022-08-02
 */
@Data
@TableName("p_business_type")
@ApiModel(value="公共-业务类型",description = "业务类型")
public class PBusinessType implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 业务ID */
    @Excel(name = "业务ID")
    @ApiModelProperty(value = "业务ID",required = false,example = "1" )
    private Long businessType;

    /** 业务名称 */
    @Excel(name = "业务名称")
    @ApiModelProperty(value = "业务名称",required = false,example = "1" )
    private String businessName;

    /** 0:删除 1:有效 */
    @Excel(name = "0:删除 1:有效")
    @ApiModelProperty(value = "0:删除 1:有效",required = false,example = "1" )
    private String status;

    /** 0:关闭 1:启用 */
    @Excel(name = "0:关闭 1:启用")
    @ApiModelProperty(value = "0:关闭 1:启用",required = false,example = "1" )
    private String active;

    /** 描述 */
    @Excel(name = "描述")
    @ApiModelProperty(value = "描述",required = false,example = "1" )
    private String content;

}
