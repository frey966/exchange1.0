package com.financia.exchange.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.dto.*;
import com.financia.exchange.service.DepthService;
import com.financia.exchange.service.KlineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@Api(tags = "币币行情接口")
@Slf4j
@RequestMapping("market")
public class MarketController {

    @Resource
    private DepthService depthService;

    @Resource
    private KlineService service;

    @GetMapping("history")
    @ApiOperation("币币-获取历史K线数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="resolution",value="间隔(1, 5, 15, 30, 60, 1D, 1M, 1W",dataType="string", paramType = "query",example="1"),
            @ApiImplicitParam(name="symbol",value="BTC/USDT",dataType="string", paramType = "query",example="BTC/USDT"),
            @ApiImplicitParam(name="num",value="20",dataType="int", paramType = "query",example="20")
    })
    public AjaxResult findKHistory(String symbol, int num, String resolution){
        symbol=symbol.toLowerCase().replace("/","");
        String interval = "";
        if(resolution.endsWith("H") || resolution.endsWith("h")){
            interval = resolution.substring(0,resolution.length()-1) + "hour";
        }
        else if(resolution.endsWith("D") || resolution.endsWith("d")){
            interval = resolution.substring(0,resolution.length()-1) + "day";
        }
        else if(resolution.endsWith("W") || resolution.endsWith("w")){
            interval = resolution.substring(0,resolution.length()-1) + "week";
        }
        else if(resolution.endsWith("M") || resolution.endsWith("m")){
            interval = resolution.substring(0,resolution.length()-1) + "mon";
        }
        else{
            Integer val = Integer.parseInt(resolution);
            if(val <= 60) {
                interval = resolution + "min";
            }
            else {
                interval = (val/60) + "hour";
            }
        }
        List<KlineDO> list = service.queryFrom(num,symbol,interval);

        JSONArray array = new JSONArray();
        for(KlineDO item:list){
            JSONArray group = new JSONArray();
            group.add(0,Long.valueOf(item.getkTime())*1000);
            group.add(1,item.getOpen());
            group.add(2,item.getHigh());
            group.add(3,item.getLow());
            group.add(4,item.getClose());
            group.add(5,item.getVol());
            array.add(group);
        }
        return AjaxResult.success(array);
    }


    @GetMapping("exchange-plate-mini")
    @ApiOperation("币币-获取买卖盘口数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "symbol", value = "交易代码(BTC/USDT等)", dataType = "string", paramType = "query", example = "BTC/USDT")})
    public AjaxResult traderPlateMini(@RequestParam("symbol") String symbol) {
        String pair=symbol;
        symbol=symbol.toLowerCase().replace("/","");
        Map<String, JSONObject> result = new HashMap<>(2);
        DepthDO depthDO = depthService.queryDepth("step0", symbol);
        if(Objects.isNull(depthDO)) {
            return AjaxResult.success();
        }
        TradePlate sellTradePlate=new TradePlate();
        TradePlate buyTradePlate=new TradePlate();
        sellTradePlate.setDirection(ContractOptionOrderDirection.SELL);
        float[][] asks = depthDO.getAsks();
        ArrayList<TradePlateItem> asksList = new ArrayList<>(asks.length);
        for (float[] foo : asks) {
            TradePlateItem tradePlateItem=new TradePlateItem();
            tradePlateItem.setPrice(BigDecimal.valueOf(foo[0]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            tradePlateItem.setAmount(BigDecimal.valueOf(foo[1]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            asksList.add(tradePlateItem);
        }
        sellTradePlate.setItems(asksList);
        sellTradePlate.setSymbol(pair);
        sellTradePlate.setItems(asksList);

        float[][] bids = depthDO.getBids();
        ArrayList<TradePlateItem> bidsList = new ArrayList<>(bids.length);
        for (float[] foo : bids) {
            TradePlateItem tradePlateItem=new TradePlateItem();
            tradePlateItem.setPrice(BigDecimal.valueOf(foo[0]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            tradePlateItem.setAmount(BigDecimal.valueOf(foo[1]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            bidsList.add(tradePlateItem);
        }
        buyTradePlate.setDirection(ContractOptionOrderDirection.BUY);
        buyTradePlate.setItems(bidsList);
        buyTradePlate.setSymbol(pair);
        result.put("bid", sellTradePlate.toJSON(20));
        result.put("ask", buyTradePlate.toJSON(20));
        return AjaxResult.success(result);
    }


    @PostMapping("/symbol-thumb")
    @ApiOperation("币币-获取市场行情")
    public AjaxResult queryDepthDetail() {
        List<CoinThumb> detailDO = depthService.getSymbolThumb();
        return AjaxResult.success(detailDO);
    }

    @PostMapping("/home-symbol-thumb")
    @ApiOperation("币币-获取首页推荐市场行情")
    public AjaxResult queryHomeSymbolThumb() {
        List<CoinThumb> detailDO = depthService.getHomeSymbolThumb();
        return AjaxResult.success(detailDO);
    }

    @PostMapping("/latest-trade")
    @ApiOperation("币币-获取最新交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对：BTC/USDT", dataType = "string", paramType = "query", example = "BTC/USDT"),
            @ApiImplicitParam(name = "size", value = "大小", dataType = "int", paramType = "query", example = "20")
    })
    public AjaxResult queryTradeDetail(@RequestParam("symbol") String symbol, @RequestParam("size") int size) throws IOException {
        List<ExchangeTrade> list = depthService.queryTradeDetail(symbol,size);
        if(list.size() <= size) {
            return AjaxResult.success(list);
        } else {
            return AjaxResult.success(list.subList(0,size));
        }

    }

}
