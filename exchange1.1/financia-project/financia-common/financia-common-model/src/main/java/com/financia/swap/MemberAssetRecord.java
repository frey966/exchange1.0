package com.financia.swap;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.exchange.AssetRecordReasonEnum;
import com.financia.exchange.AssetRecordTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("member_asset_records")
@ApiModel(value = "MemberAssetRecord", description = "会员交易记录")
public class MemberAssetRecord {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("自增id")
    private Long id;

    @ApiModelProperty("会员Id")
    private Long memberId;

    @ApiModelProperty("交易值")
    private BigDecimal amount;

    @ApiModelProperty("交易类型 1 合约交易，2 超级杠杆交易 3 量化交易 4 股票交易")
    private AssetRecordTypeEnum  assetType;

    @ApiModelProperty("交易对")
    private String symbol;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("委托单号")
    private String entrustOrder;

    @ApiModelProperty("交易说明")
    private AssetRecordReasonEnum comment;
}
