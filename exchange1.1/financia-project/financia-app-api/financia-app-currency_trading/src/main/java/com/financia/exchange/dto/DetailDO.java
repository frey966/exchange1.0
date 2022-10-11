package com.financia.exchange.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DetailDO implements Serializable {

    private static final long serialVersionUID = 5539611315871271310L;

    //成交量
    private float amount;

    //收盘价
    private float close;

    //成交笔数
    private float count;

    //涨幅
    private float diff;

    //最高价
    private float high;

    //最低价
    private float low;

    //开盘价
    private float open;

    //交易对btcusdt
    private String pair;

    //涨幅百分比
    private float percent;

    //交易对 BTC/USDT
    private String symbol;

    //成交时间戳
    private long ts;

    //类型  detail
    private String type;

    //成交额
    private float vol;

}
