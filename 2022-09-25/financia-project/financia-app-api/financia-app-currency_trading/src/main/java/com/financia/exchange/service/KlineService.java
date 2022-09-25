package com.financia.exchange.service;


import com.financia.exchange.dto.KlineDO;

import java.util.List;

public interface KlineService {

    /**
     * 更新K线
     * @param klineDO
     * @return
     */
    int updateKline(KlineDO klineDO);

    /**
     * 查询K线列表
     *
     * @param from   开始时间
     * @param to     结束时间
     * @param symbol 交易对
     * @param type   类型
     * @return
     */
    List<KlineDO> list(long from, long to, String symbol, String type);

    /**
     * 查询K线 从 from时间点开始向后推
     * @param num
     * @param symbol
     * @param kType
     * @return
     */
    List<KlineDO> queryFrom(int num, String symbol, String kType);

    /**
     * K线查询
     * @param kType
     * @return
     */
    List<KlineDO> queryKline(String kType, String symbol);

    /**
     * 批量插入k线图数据
     * @param klineList
     * @return
     * @throws Exception
     */
    List<KlineDO> insertCollection(List<KlineDO> klineList) throws Exception;



}
