package com.financia.common;

import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 国家区号对象 p_country_code
 * 
 * @author 花生
 * @date 2022-08-05
 */
@Data
@ApiModel(value="数据配置管理-国家区域号码",description = "数据配置管理-国家区域号码")
public class PCountryCode implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 中文 */
    @Excel(name = "中文")
    @ApiModelProperty(value = "中文",required = false,example = "1" )
    private String cn;

    /** 英文 */
    @Excel(name = "英文")
    @ApiModelProperty(value = "英文",required = false,example = "1" )
    private String en;

    /** 编码 */
    @Excel(name = "编码")
    @ApiModelProperty(value = "编码",required = false,example = "1" )
    private String phoneCode;

    /** 0:未启用 1:启用 */
    @ApiModelProperty(value = "0:未启用 1:启用",required = false,example = "1" )
    @Excel(name = "0:未启用 1:启用")
    private Long active;

}
