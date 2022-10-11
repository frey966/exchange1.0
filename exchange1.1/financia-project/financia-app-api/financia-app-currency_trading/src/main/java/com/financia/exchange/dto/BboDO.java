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
@ApiModel(description="买一卖一")
@Document(collection = "bbo")
@CompoundIndexes({
        @CompoundIndex(name = "bbo_idx", def = "{'type': 1, 'symbol': -1, 'updateTime': -1}")
})
public class BboDO implements Serializable {

    private static final long serialVersionUID = 7507080145174541575L;
    //主键 unix时间 + symbol
    @Id
    @ApiModelProperty(value ="ID")
    private String id;

    //交易对  BTC/USDT
    @ApiModelProperty(value ="交易对大写(BTC/USDT)")
    @JsonIgnore
    private String pair;

    //交易对 btcusdt
    @ApiModelProperty(value ="交易对(btcusdt)")
    private String symbol;

    //买一价
    @ApiModelProperty(value ="买一价")
    private float bid;

    //卖一价
    @ApiModelProperty(value ="卖一价")
    private float ask;

    @JsonIgnore
    private String type;

    //写入时间
    @ApiModelProperty(value ="写入时间")
    @JsonIgnore
    private Long createTime;

    //更新时间
    @ApiModelProperty(value ="更新时间")
    @JsonIgnore
    private Long updateTime;

}
