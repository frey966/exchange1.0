package com.financia.common;

import com.financia.common.core.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PComplienceModel {
    /**
     * 类型
     */
    private Long type;

    /**
     * 子标题及其内容
     */

    private List<PComplienceTitleModel> content;

    /**
     * 名称1
     */
    @Excel(name = "名称1")
    @ApiModelProperty(value = "名称1")
    private String name1;

    /**
     * 名称2
     */
    @Excel(name = "名称2")
    @ApiModelProperty(value = "名称2")
    private String name2;

    /**
     * 图片1
     */
    @Excel(name = "图片1")
    @ApiModelProperty(value = "图片1")
    private String imgurl1;

    /**
     * 图片2
     */
    @Excel(name = "图片2")
    @ApiModelProperty(value = "图片2")
    private String imgurl2;


}
