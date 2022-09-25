package com.financia.exchange.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.engine.ContractCoinMatch;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.SuperContractCoinService;
import com.financia.exchange.service.impl.ContractMarketService;
import com.financia.exchange.util.UtcTimeUtils;
import com.financia.superleverage.SuperContractCoin;
import com.financia.swap.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RestController
@Api(tags = "超级杠杆行情相关接口")
public class MarketController {
    private Logger logger = LoggerFactory.getLogger(MarketController.class);

    @Autowired
    private SuperContractCoinService coinService;

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory;

    @Autowired
    private ContractMarketService marketService;

    /**
     * 获取币种缩略行情
     * @return
     */
    @ApiOperation("超级杠杆-获取币种行情")
    @RequestMapping(value = "symbol-thumb", method = {RequestMethod.GET})
    public AjaxResult findSymbolThumb(){
        List<SuperContractCoin> coins = coinService.findAllVisible();
        List<CoinThumb> thumbs = new ArrayList<>();
        for(SuperContractCoin coin:coins){
            String[] leverageArr = coin.getLeverage().split(",");
            String maxLeverage = leverageArr[leverageArr.length - 1];
            ContractCoinMatch processor = contractCoinMatchFactory.getContractCoinMatch(coin.getSymbol());
            CoinThumb thumb = processor.getThumb();
            thumb.setMaxLeverage(maxLeverage);
            thumbs.add(thumb);
        }
        return AjaxResult.success(thumbs);
    }

    /**
     * 获取某交易对详情
     * @param symbol
     * @return
     */
    @ApiOperation("超级杠杆-获取某交易对详情")
    @RequestMapping(value = "symbol-info", method = {RequestMethod.GET})
    public AjaxResult findSymbol(String symbol){
        SuperContractCoin coin = coinService.findBySymbol(symbol);
        coin.setCurrentTime(Calendar.getInstance().getTimeInMillis());
        return AjaxResult.success(coin);
    }


    /**
     * 获取币种历史K线
     * @param symbol
     * @param resolution
     * @return
     */
    @RequestMapping(value = "history", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name="resolution",value="间隔(1, 5, 15, 30, 60, 1D, 1M, 1W",dataType="string", paramType = "query",example="1"),
            @ApiImplicitParam(name="symbol",value="BTC/USDT",dataType="string", paramType = "query",example="BTC/USDT"),
            @ApiImplicitParam(name="num",value="20",dataType="int", paramType = "query",example="20")
    })
    @ApiOperation("超级杠杆-获取币种历史K线")
    public AjaxResult findKHistory(String symbol, int num, String resolution){
        String period = "";
        if(resolution.endsWith("H") || resolution.endsWith("h")){
            period = resolution.substring(0,resolution.length()-1) + "hour";
        }
        else if(resolution.endsWith("D") || resolution.endsWith("d")){
            period = resolution.substring(0,resolution.length()-1) + "day";
        }
        else if(resolution.endsWith("W") || resolution.endsWith("w")){
            period = resolution.substring(0,resolution.length()-1) + "week";
        }
        else if(resolution.endsWith("M") || resolution.endsWith("m")){
            period = resolution.substring(0,resolution.length()-1) + "mon";
        }
        else{
            Integer val = Integer.parseInt(resolution);
            if(val <= 60) {
                period = resolution + "min";
            }
            else {
                period = (val/60) + "hour";
            }
        }
//        from = from / 1000;
//        to = to / 1000;
//        List<KLine> list = marketService.findAllKLine(symbol,from,to,period);
        List<KLine> list = marketService.findKLine(symbol,period,num);
        // logger.info("获取历史K线）symbol: {},  period: {}, from: {}, to: {}, size: {}", symbol, resolution, from, to, list.size());
        logger.info("获取历史K线）symbol: {},  period: {}, size: {}", symbol, resolution, list.size());
        JSONArray array = new JSONArray();
        boolean startFlag = false;
        KLine temKline = null;
        for(KLine item:list){
            item.setTime(item.getTime() * 1000);
            // 此段处理是过滤币种开头出现0开盘/收盘的K线
            if(!startFlag && item.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }else {
                startFlag = true;
            }
            // 中间段如果出现为0的现象，需要处理一下
            if(item.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
                item.setOpenPrice(temKline.getClosePrice());
                item.setClosePrice(temKline.getClosePrice());
                item.setHighestPrice(temKline.getClosePrice());
                item.setLowestPrice(temKline.getClosePrice());
            }
            JSONArray group = new JSONArray();
            group.add(0,item.getTime());
            group.add(1,item.getOpenPrice());
            group.add(2,item.getHighestPrice());
            group.add(3,item.getLowestPrice());
            group.add(4,item.getClosePrice());
            group.add(5,item.getVolume());
            array.add(group);

            temKline = item;
        }
        return AjaxResult.success(array);
    }

    /**
     * 获取首页币种历史K线
     * @return
     */
    @RequestMapping(value = "homeKLine", method = {RequestMethod.GET})
    @ApiOperation("超级杠杆-获取币种历史K线")
    @ApiImplicitParam(name="resolution",value="间隔(1, 5, 15, 30, 60, 1D, 1M, 1W",dataType="string", paramType = "query",example="1")
    public AjaxResult getHomeKHistory(String resolution){
        String period = "";
        if(resolution.endsWith("H") || resolution.endsWith("h")){
            period = resolution.substring(0,resolution.length()-1) + "hour";
        }
        else if(resolution.endsWith("D") || resolution.endsWith("d")){
            period = resolution.substring(0,resolution.length()-1) + "day";
        }
        else if(resolution.endsWith("W") || resolution.endsWith("w")){
            period = resolution.substring(0,resolution.length()-1) + "week";
        }
        else if(resolution.endsWith("M") || resolution.endsWith("m")){
            period = resolution.substring(0,resolution.length()-1) + "mon";
        }
        else{
            Integer val = Integer.parseInt(resolution);
            if(val <= 60) {
                period = resolution + "min";
            }
            else {
                period = (val/60) + "hour";
            }
        }
        List<SuperContractCoin> superContractCoins = coinService.findAllVisible();
        List<SuperContractCoin> coins = superContractCoins.subList(0, 3);
        List<CoinThumb> thumbs=new ArrayList<>();
        for (SuperContractCoin coin: coins) {
            List<KLine> list = marketService.findKLine(coin.getSymbol(),period,100);
            JSONArray array = new JSONArray();
            boolean startFlag = false;
            KLine temKline = null;
            String[] leverageArr = coin.getLeverage().split(",");
            String maxLeverage = leverageArr[leverageArr.length - 1];
            ContractCoinMatch processor = contractCoinMatchFactory.getContractCoinMatch(coin.getSymbol());
            CoinThumb thumb = processor.getThumb();
            thumb.setMaxLeverage(maxLeverage);
            for(KLine item:list){
                item.setTime(item.getTime() * 1000);
                // 此段处理是过滤币种开头出现0开盘/收盘的K线
                if(!startFlag && item.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }else {
                    startFlag = true;
                }
                // 中间段如果出现为0的现象，需要处理一下
                if(item.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
                    item.setOpenPrice(temKline.getClosePrice());
                }
                array.add(item.getOpenPrice());
                temKline = item;
            }
            thumb.setKLine(array);
            thumbs.add(thumb);
        }

        return AjaxResult.success(thumbs);
    }
}
