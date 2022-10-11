package com.financia.system;

import com.financia.common.core.annotation.Excel;
import com.financia.common.core.annotation.Excel.ColumnType;
import com.financia.common.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 字典类型表 sys_dict_type
 * 
 * @author ruoyi
 */
@Data
@ApiModel(value = "SysDictType", description = "字典类型表")
public class SysDictType implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 字典主键 */
    @Excel(name = "字典主键", cellType = ColumnType.NUMERIC)
    private Long dictId;

    /** 字典名称 */
    @Excel(name = "字典名称")
    private String dictName;

    /** 字典类型 */
    @Excel(name = "字典类型")
    private String dictType;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;


}
