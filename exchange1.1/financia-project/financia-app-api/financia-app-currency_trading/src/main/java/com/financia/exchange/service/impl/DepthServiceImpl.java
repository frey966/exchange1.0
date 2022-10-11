package com.financia.exchange.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.financia.common.core.exception.ServiceException;
import com.financia.exchange.common.Constant;
import com.financia.exchange.dto.*;
import com.financia.exchange.service.CurrencyService;
import com.financia.exchange.service.DepthService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepthServiceImpl implements DepthService {

    @Value("${hb.ws.apiurl}")
    private String huoBiUrl;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Resource
    private RedisTemplate redisTemplate;


    @Resource
    private CurrencyService currencyService;

    @Override
    public int updateDepth(DepthDO depthDO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("kType").is(depthDO.getKType()).and("type").is("depth").and("symbol").is(depthDO.getSymbol())).limit(1);
        DepthDO result = mongoTemplate.findOne(query, DepthDO.class);
        if (Objects.nonNull(result)) {
            updateNew(query, depthDO);
        } else {
            depthDO.setCreateTime(System.currentTimeMillis());
            depthDO.setUpdateTime(System.currentTimeMillis());
            mongoTemplate.insert(depthDO);
        }

        return 1;
    }

    @Override
    public DepthDO queryDepth(String kType, String symbol) {
        Query query = new Query();
        query.addCriteria(Criteria.where("kType").is(kType).and("type").is("depth").and("symbol").is(symbol)).limit(1);
        return mongoTemplate.findOne(query, DepthDO.class);
    }

    @Override
    public int updateBbo(BboDO bboDO) {
        Query query = new Query(Criteria.where("id").is(bboDO.getId()));
        BboDO result = mongoTemplate.findOne(query, BboDO.class);
        if (null != result) {
            updateBboNew(query, bboDO);
        } else {
            bboDO.setCreateTime(System.currentTimeMillis());
            bboDO.setUpdateTime(System.currentTimeMillis());
            mongoTemplate.insert(bboDO);
        }
        return 1;
    }

    @Override
    public  List<ExchangeTrade> queryTradeDetail(String symbol, int size) throws IOException {
        List<ExchangeTrade> tradeList=new ArrayList<>();
        symbol=symbol.toLowerCase().replace("/","");
        com.financia.currency.Currency currency =currencyService.queryCurrencyInfoBySymbol(symbol);
            if(Objects.isNull(currency)){
                throw new ServiceException("交易对不存在");
            }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(huoBiUrl + Constant.MARKET_TRADE + "?symbol=" + symbol + "&size="+size)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.code() == 200) {
            JSONObject jsonObject=JSON.parseObject(response.body().string());
            List<TradeDTO> list= this.getPlatformList(JSON.toJSONString(jsonObject.get("data")));
            String finalSymbol = symbol;
            list.forEach(n->{
                ExchangeTrade exchangeTrade=new ExchangeTrade();
                exchangeTrade.setSymbol(finalSymbol);
                exchangeTrade.setDirection(n.getDirection().toUpperCase());
                exchangeTrade.setPrice(n.getPrice());
                exchangeTrade.setTime(n.getTs());
                exchangeTrade.setAmount(n.getAmount());
                tradeList.add(exchangeTrade);
            });

        }
        return tradeList;
    }


    /**
     * 解析数据结构
     * @param platforms
     * @return
     */
    private ArrayList<TradeDTO> getPlatformList(String platforms) {
        ArrayList<TradeDTO> platformList = new ArrayList<>();
        JSONArray platformArray = JSON.parseArray(platforms);
        for (Object jsonObject : platformArray) {
            JSONArray jsonObject1= JSONArray.parseArray( JSONObject.parseObject(jsonObject.toString()).get("data").toString());
            for (int i=0;i<jsonObject1.size();i++){
                JSONObject bbb=JSONObject.parseObject(jsonObject1.get(i).toString());
                TradeDTO platformModel = new TradeDTO();
                platformModel.setAmount(new BigDecimal(bbb.get("amount").toString()));
                platformModel.setDirection(bbb.get("direction").toString());
                platformModel.setTs(Long.parseLong(bbb.get("ts").toString()));
                platformModel.setPrice(new BigDecimal(bbb.get("price").toString()));
                platformList.add(platformModel);
            }

        }
        return platformList;
    }

    @Override
    public List<CoinThumb> getSymbolThumb() {
        List<CoinThumb> list=new ArrayList<>();
        List<com.financia.currency.Currency> syStr=currencyService.querySymbolList();
        if (CollectionUtils.isEmpty(syStr)) {
            return list;
        }
        syStr.stream().forEach(n->{
            //每日概要从缓存中拿
            String tickerDetailStr = (String) redisTemplate.opsForValue().get(String.format(Constant.TICKER_KEY, n.getSymbol()));
            CoinThumb coinThumb = JSON.parseObject(tickerDetailStr, CoinThumb.class);
            if(coinThumb != null) {
                coinThumb.setPopular(n.getPopular());
                coinThumb.setCoinId(n.getId());
                list.add(coinThumb);
            }
        });
        return list;
    }

    @Override
    public List<CoinThumb> getHomeSymbolThumb() {
        List<CoinThumb> list=new ArrayList<>();
        List<String> syStr=currencyService.querySymbolList().stream().map(nico->nico.getSymbol()).collect(Collectors.toList()).subList(0,3);
        if (CollectionUtils.isEmpty(syStr)) {
            return list;
        }
        syStr.stream().forEach(n->{
            //每日概要从缓存中拿
            String tickerDetailStr = (String) redisTemplate.opsForValue().get(String.format(Constant.TICKER_KEY, n));
            CoinThumb coinThumb = JSON.parseObject(tickerDetailStr, CoinThumb.class);
            list.add(coinThumb);
        });
        return list;
    }

    private void updateNew(Query query, DepthDO depthDO) {
        Update update = new Update();
        update.set("pair", depthDO.getPair());
        update.set("asks", depthDO.getAsks());
        update.set("bids", depthDO.getBids());
        update.set("type", depthDO.getType());
        update.set("kType", depthDO.getKType());
        update.set("ts", depthDO.getTs());
        update.set("updateTime", System.currentTimeMillis());
        mongoTemplate.updateFirst(query, update, DepthDO.class);
    }

    private void updateBboNew(Query query, BboDO bboDO) {
        Update update = new Update();
        update.set("pair", bboDO.getPair());
        update.set("ask", bboDO.getAsk());
        update.set("bid", bboDO.getBid());
        update.set("type", bboDO.getType());
        update.set("id", bboDO.getId());
        update.set("updateTime", System.currentTimeMillis());
        mongoTemplate.updateFirst(query, update, BboDO.class);
    }
}
