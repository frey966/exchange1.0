package com.financia.exchange.service.impl;

import com.financia.exchange.dto.KlineDO;
import com.financia.exchange.service.KlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class KlineServiceImpl implements KlineService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public int updateKline(KlineDO klineDO) {
        Query query = new Query(Criteria.where("id").is(klineDO.getId()));
        KlineDO result = mongoTemplate.findOne(query, KlineDO.class);
        if (null != result) {
            updateNew(query, klineDO);
        } else {
            klineDO.setUpdateTime(System.currentTimeMillis());
            klineDO.setCreateTime(System.currentTimeMillis());
            mongoTemplate.insert(klineDO);
        }
        return 1;
    }


    @Override
    public List<KlineDO> list(long from, long to, String symbol, String kType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("kTime").gte(from).lte(to).and("symbol").is(symbol).and("kType").is(kType))
                .with(Sort.by("kTime").descending())
                .limit(200);
        List<KlineDO> list = mongoTemplate.find(query, KlineDO.class);
        return list;
    }

//    @Override
//    public List<KlineDO> queryFrom(long from, String symbol, String kType) {
//        Query query = new Query();
//        long tt=from/1000;
//        query.addCriteria(Criteria.where("kType").is(kType).and("symbol").is(symbol).and("kTime").gte(tt))
//                .with(Sort.by("kTime").ascending());
//        List<KlineDO> list = mongoTemplate.find(query, KlineDO.class);
//        return list;
//    }

    @Override
    public List<KlineDO> queryFrom(int num, String symbol, String kType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("kType").is(kType).and("symbol").is(symbol))
                .with(Sort.by("kTime").descending()).limit(num);
        List<KlineDO> list = mongoTemplate.find(query, KlineDO.class);
        return list;
    }

    @Override
    public List<KlineDO> queryKline(String kType, String symbol) {
        Query query = new Query();
        long tt=System.currentTimeMillis()/1000;
        query.addCriteria(Criteria.where("kType").is(kType).and("symbol").is(symbol).and("kTime").lte(tt))
                .with(Sort.by("kTime").ascending());
        List<KlineDO> result = mongoTemplate.find(query, KlineDO.class);
        return result;
    }

    @Override
    public List<KlineDO> insertCollection(List<KlineDO> klineList) throws Exception {
        return (List<KlineDO>) mongoTemplate.insert(klineList,KlineDO.class);
    }

    /**
     * @param klineDO
     */
    private void updateNew(Query query, KlineDO klineDO) {
        Update update = new Update();
        update.set("amount", klineDO.getAmount());
        update.set("count", klineDO.getCount());
        update.set("open", BigDecimal.valueOf(klineDO.getOpen()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        update.set("close", BigDecimal.valueOf(klineDO.getClose()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        update.set("low", BigDecimal.valueOf(klineDO.getLow()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        update.set("high", BigDecimal.valueOf(klineDO.getHigh()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        update.set("vol", klineDO.getVol());
        update.set("updateTime", System.currentTimeMillis());
        mongoTemplate.updateFirst(query, update, KlineDO.class);
    }
}
