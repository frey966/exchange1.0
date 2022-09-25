package com.financia.exchange.service;

import com.financia.exchange.dto.BboDO;
import com.financia.exchange.dto.CoinThumb;
import com.financia.exchange.dto.DepthDO;
import com.financia.exchange.dto.ExchangeTrade;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DepthService {

    /**
     * 更新盘口
     *
     * @return
     */
    int updateDepth(DepthDO depthDO);

    /**
     * 盘口 买卖盘数据查询
     * @param kType
     * @return
     */
    DepthDO queryDepth(String kType, String symbol);

    /**
     * 更新盘口买一卖一价
     * @param bboDO
     * @return
     */
    int updateBbo(BboDO bboDO);

    /**
     * 获取币币行情
     * @return
     */
    List<CoinThumb> getSymbolThumb();

    /**
     * 获取首页推荐币币的行情
     * @return
     */
    List<CoinThumb> getHomeSymbolThumb();

    /**
     * 最近成交 只返回10条数据
     * @param symbol
     * @return
     * @throws IOException
     */
    List<ExchangeTrade> queryTradeDetail(String symbol, int size) throws IOException;


}
