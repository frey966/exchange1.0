package com.financia.system;

import com.financia.common.core.annotation.Excel;
import com.financia.common.core.annotation.Excel.ColumnType;
import com.financia.common.core.constant.UserConstants;
import com.financia.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 字典数据表 sys_dict_data
 * 
 * @author ruoyi
 */
@Data
@ApiModel(value = "SysDictData", description = "字典数据")
public class SysDictData implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @Excel(name = "字典编码", cellType = ColumnType.NUMERIC)
    @ApiModelProperty("字典编码")
    private Long dictCode;

    /** 字典排序 */
    @Excel(name = "字典排序", cellType = ColumnType.NUMERIC)
    @ApiModelProperty("字典排序")
    private Long dictSort;

    /** 字典标签 */
    @Excel(name = "字典标签")
    @ApiModelProperty("字典标签")
    private String dictLabel;

    /** 字典键值 */
    @Excel(name = "字典键值")
    @ApiModelProperty("字典键值")
    private String dictValue;

    /** 字典类型 */
    @Excel(name = "字典类型")
    @ApiModelProperty("字典类型")
    private String dictType;

    /** 样式属性（其他样式扩展） */
    @ApiModelProperty("样式属性")
    private String cssClass;

    /** 表格字典样式 */
    @ApiModelProperty("表格字典样式")
    private String listClass;

    /** 是否默认（Y是 N否） */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    @ApiModelProperty("是否默认")
    private String isDefault;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @ApiModelProperty("状态")
    private String status;

    private String imgurl;

}
