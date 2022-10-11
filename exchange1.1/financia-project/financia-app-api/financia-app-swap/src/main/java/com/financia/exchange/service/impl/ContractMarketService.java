package com.financia.exchange.service.impl;

import com.financia.swap.KLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContractMarketService {
    @Autowired
    private MongoTemplate mongoTemplate;

    private Logger logger = LoggerFactory.getLogger(ContractMarketService.class);

    public List<KLine> findAllKLine(String symbol, long fromTime, long toTime, String period){
        Criteria criteria = Criteria.where("time").gte(fromTime).andOperator(Criteria.where("time").lte(toTime));
        Sort sort = Sort.by(Sort.Direction.ASC,"time");
        Query query = new Query(criteria).with(sort);
        List<KLine> kLines = mongoTemplate.find(query,KLine.class,"contract_kline_"+symbol.toUpperCase()+"_"+ period);
        return kLines;
    }

    public List<KLine> findKLine(String symbol, String period, Integer num){
        Sort sort = Sort.by(Sort.Direction.DESC,"time");
        Query query = new Query().with(sort).limit(num);
        List<KLine> kLines = mongoTemplate.find(query,KLine.class,"contract_kline_"+symbol.toUpperCase()+"_"+ period);
        return kLines;
    }

    /**
     * 保存k线
     * @param symbol
     * @param kLine
     * @return
     */
    public KLine saveKLine(String symbol, KLine kLine){
        // 查询k线
        KLine queryKLine = findMaxTimestampKLine(symbol, kLine.getPeriod(), kLine.getTime());
        // 如果存在k线，则更新
        if(queryKLine != null){
            queryKLine.setClosePrice(kLine.getClosePrice());
            queryKLine.setCount(kLine.getCount());
            queryKLine.setHighestPrice(kLine.getHighestPrice());
            queryKLine.setLowestPrice(kLine.getLowestPrice());
            queryKLine.setOpenPrice(kLine.getOpenPrice());
            queryKLine.setTurnover(kLine.getTurnover());
            queryKLine.setVolume(kLine.getVolume());
            // 更新最新的k线
            Query query = new Query(Criteria.where("time").is(queryKLine.getTime()));
            Update update = new Update().set("closePrice", queryKLine.getClosePrice())
                    .set("count", queryKLine.getCount())
                    .set("highestPrice", queryKLine.getHighestPrice())
                    .set("lowestPrice", queryKLine.getLowestPrice())
                    .set("openPrice", queryKLine.getOpenPrice())
                    .set("turnover", queryKLine.getTurnover())
                    .set("volume", queryKLine.getVolume());
            mongoTemplate.updateFirst(query, update,"contract_kline_"+symbol+"_"+kLine.getPeriod());
            return queryKLine;
        }

//        logger.info("保存K线(" + symbol + "): " + kLine.getPeriod() + "/" + kLine.getTime() + "----maxTime: " + timeStamp);
        // 保存K线
        mongoTemplate.insert(kLine,"contract_kline_"+symbol+"_"+kLine.getPeriod());
        return kLine;
    }

    /**
     * 获取K最近一条K线的时间
     * @param symbol
     * @param period
     * @return
     */
    public KLine findMaxTimestampKLine(String symbol, String period, Long time) {
        Sort sort = Sort.by(Sort.Direction.DESC,"time");
        Query query = new Query(Criteria.where("time").is(time)).with(sort).limit(1);

        List<KLine> result = mongoTemplate.find(query,KLine.class,"contract_kline_"+symbol+"_"+period);

        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
