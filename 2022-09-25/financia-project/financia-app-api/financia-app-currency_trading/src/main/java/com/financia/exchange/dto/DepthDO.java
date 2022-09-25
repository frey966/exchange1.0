package com.financia.exchange.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@ApiModel(description="买卖盘")
@Document(collection = "depth")
@CompoundIndexes({
        @CompoundIndex(name = "depth_idx", def = "{'kType': 1, 'type': 1, 'symbol': 1}")
})
public class DepthDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 unix时间 + symbol
     */
    @Id
    @ApiModelProperty(value ="ID")
    private String id;

    @ApiModelProperty(value ="交易对 BTC/USDT")
    private String pair;

    @ApiModelProperty(value ="交易代码(例:btcusdt)")
    private String symbol;

    //买盘
    @ApiModelProperty(value ="买盘")
    private float[][] bids;

    //卖盘
    @ApiModelProperty(value ="卖盘")
    private float[][] asks;

    @JsonIgnore
    private long version;

    @ApiModelProperty(value ="新加坡时间的时间戳，单位毫秒")
    @JsonIgnore
    private long ts;

    @JsonIgnore
    private String type;

    @ApiModelProperty(value ="类型")
    private String kType;

    //写入时间
    @ApiModelProperty(value ="写入时间")
    @JsonIgnore
    private Long createTime;

    //更新时间
    @ApiModelProperty(value ="更新时间")
    @JsonIgnore
    private Long updateTime;


}
