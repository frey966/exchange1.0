package com.financia.exchange.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.engine.ContractCoinMatch;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.ContractCoinService;
import com.financia.exchange.service.impl.ContractMarketService;
import com.financia.exchange.util.UtcTimeUtils;
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
@Api(tags = "合约U本位币种行情相关接口")
public class MarketController {
    private Logger logger = LoggerFactory.getLogger(MarketController.class);

    @Autowired
    private ContractCoinService coinService;

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory;

    @Autowired
    private ContractMarketService marketService;

    /**
     * 获取币种缩略行情
     *
     * @return
     */
    @ApiOperation("合约U本位-获取币种行情")
    @RequestMapping(value = "symbol-thumb", method = {RequestMethod.GET})
    public AjaxResult findSymbolThumb() {
        // 查询交易对
        List<ContractCoin> coins = coinService.findAllVisible();
        List<CoinThumb> thumbs = new ArrayList<>();
        for (ContractCoin coin : coins) {
            // 获取最新行情信息
            ContractCoinMatch processor = contractCoinMatchFactory.getContractCoinMatch(coin.getSymbol());
            CoinThumb thumb = processor.getThumb();
            // 是否热门
            thumb.setPopular(coin.getPopular());
            thumbs.add(thumb);
        }
        return AjaxResult.success(thumbs);
    }

    /**
     * 获取首页推荐币种行情
     *
     * @return
     */
    @ApiOperation("合约U本位-获取首页推荐币种行情")
    @RequestMapping(value = "home-symbol-thumb", method = {RequestMethod.GET})
    public AjaxResult findHomeRecommendSymbolThumb() {
        // 查询交易对
        List<ContractCoin> coins = coinService.findAllVisible().subList(0, 3);
        List<CoinThumb> thumbs = new ArrayList<>();
        for (ContractCoin coin : coins) {
            // 获取最新行情信息
            ContractCoinMatch processor = contractCoinMatchFactory.getContractCoinMatch(coin.getSymbol());
            CoinThumb thumb = processor.getThumb();
            thumbs.add(thumb);
        }
        return AjaxResult.success(thumbs);
    }

    /**
     * 获取某交易对详情
     *
     * @param symbol
     * @return
     */
    @ApiOperation("获取某交易对详情")
    @RequestMapping(value = "symbol-info", method = {RequestMethod.GET})
    public AjaxResult findSymbol(String symbol) {
        // 获取交易对详情
        ContractCoin coin = coinService.findBySymbol(symbol);
        coin.setCurrentTime(Calendar.getInstance().getTimeInMillis());
        // 触发资金费率时间
        Map<String, Integer> utcHourAndMinuteAndSecond = UtcTimeUtils.getUtcHourAndMinuteAndSecond();
        Integer currHour = utcHourAndMinuteAndSecond.get("hour");
        Integer currMinute = utcHourAndMinuteAndSecond.get("minute");
        Integer currSecond = utcHourAndMinuteAndSecond.get("second");
        // 时间在0点到8点之间
        if (currHour >= 0 && currHour < 8) {
            int hour = 7 - currHour;
            int minute = 59 - currMinute;
            int second = 59 - currSecond;
            int secondNum = hour * 60 * 60 + minute * 60 + second;
            coin.setPercentFeeCountdown(secondNum);
        } else if (currHour >= 8 && currHour < 16) { // 时间在8点到16点之间
            int hour = 15 - currHour;
            int minute = 59 - currMinute;
            int second = 59 - currSecond;
            int secondNum = hour * 60 * 60 + minute * 60 + second;
            coin.setPercentFeeCountdown(secondNum);
        } else { // 时间在16点到24点之间
            int hour = 23 - currHour;
            int minute = 59 - currMinute;
            int second = 59 - currSecond;
            int secondNum = hour * 60 * 60 + minute * 60 + second;
            coin.setPercentFeeCountdown(secondNum);
        }
        List<String> percentFeeTriggerTimes = new ArrayList<>();
        percentFeeTriggerTimes.add("00:00 UTC");
        percentFeeTriggerTimes.add("08:00 UTC");
        percentFeeTriggerTimes.add("16:00 UTC");
        coin.setPercentFeeTriggerTime(percentFeeTriggerTimes);
        return AjaxResult.success(coin);
    }

    /**
     * 查询最近成交记录
     *
     * @param size   返回记录最大数量
     * @param symbol 交易对符号
     * @return
     */
    @ApiOperation("合约U本位-查询最近交易记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对：BTC/USDT", dataType = "string", paramType = "query", example = "BTC/USDT"),
            @ApiImplicitParam(name = "size", value = "返回记录最大数量", dataType = "int", paramType = "query", example = "20")

    })
    @RequestMapping(value = "latest-trade", method = {RequestMethod.GET})
    public AjaxResult latestTrade(String symbol, int size) {
        ContractCoinMatch match = contractCoinMatchFactory.getContractCoinMatch(symbol);
        if (match == null) {
            return AjaxResult.success();
        }
        // 获取最近交易数据
        List<ContractTrade> lastedTradeList = match.getLastedTradeList();
        if (size > 0) {
            if (lastedTradeList.size() >= size) {
                return AjaxResult.success(lastedTradeList.subList(0, size));
            } else {
                return AjaxResult.success(lastedTradeList);
            }
        }
        return AjaxResult.success(lastedTradeList);
    }


    @ApiOperation("合约U本位-获取买卖盘口数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "symbol", value = "交易代码(BTC/USDT等)", dataType = "string", paramType = "query", example = "BTC/USDT")})
    @RequestMapping(value = "exchange-plate-mini", method = {RequestMethod.GET})
    public AjaxResult findTradePlateMini(String symbol) {
        Map<String, JSONObject> result = new HashMap<>();
        ContractCoinMatch match = contractCoinMatchFactory.getContractCoinMatch(symbol);
        if (match == null) {
            return AjaxResult.success();
        }
        // 买盘
        result.put("bid", match.getTradePlate(ContractOrderDirection.BUY).toJSON(20));
        // 卖盘
        result.put("ask", match.getTradePlate(ContractOrderDirection.SELL).toJSON(20));

        return AjaxResult.success(result);
    }

    /**
     * 获取币种历史K线
     *
     * @param symbol
     * @param resolution
     * @return
     */
    @RequestMapping(value = "history", method = {RequestMethod.GET})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resolution", value = "间隔(1, 5, 15, 30, 60, 1D, 1M, 1W", dataType = "string", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "symbol", value = "BTC/USDT", dataType = "string", paramType = "query", example = "BTC/USDT"),
            @ApiImplicitParam(name = "num", value = "20", dataType = "int", paramType = "query", example = "20")
    })
    @ApiOperation("合约U本位-获取币种历史K线")
    public AjaxResult findKHistory(String symbol, int num, String resolution) {
        String period = "";
        if (resolution.endsWith("H") || resolution.endsWith("h")) {
            period = resolution.substring(0, resolution.length() - 1) + "hour";
        } else if (resolution.endsWith("D") || resolution.endsWith("d")) {
            period = resolution.substring(0, resolution.length() - 1) + "day";
        } else if (resolution.endsWith("W") || resolution.endsWith("w")) {
            period = resolution.substring(0, resolution.length() - 1) + "week";
        } else if (resolution.endsWith("M") || resolution.endsWith("m")) {
            period = resolution.substring(0, resolution.length() - 1) + "mon";
        } else {
            Integer val = Integer.parseInt(resolution);
            if (val <= 60) {
                period = resolution + "min";
            } else {
                period = (val / 60) + "hour";
            }
        }
//        from = from / 1000;
//        to = to / 1000;
//        List<KLine> list = marketService.findAllKLine(symbol,from,to,period);
        // 从mongodb查询k线数据
        List<KLine> list = marketService.findKLine(symbol, period, num);
        // logger.info("获取历史K线）symbol: {},  period: {}, from: {}, to: {}, size: {}", symbol, resolution, from, to, list.size());
        logger.info("获取历史K线）symbol: {},  period: {}, size: {}", symbol, resolution, list.size());
        JSONArray array = new JSONArray();
        boolean startFlag = false;
        KLine temKline = null;
        for (KLine item : list) {
            item.setTime(item.getTime() * 1000);
            // 此段处理是过滤币种开头出现0开盘/收盘的K线
            if (!startFlag && item.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            } else {
                startFlag = true;
            }
            // 中间段如果出现为0的现象，需要处理一下
            if (item.getOpenPrice().compareTo(BigDecimal.ZERO) == 0) {
                item.setOpenPrice(temKline.getClosePrice());
                item.setClosePrice(temKline.getClosePrice());
                item.setHighestPrice(temKline.getClosePrice());
                item.setLowestPrice(temKline.getClosePrice());
            }
            JSONArray group = new JSONArray();
            group.add(0, item.getTime());
            group.add(1, item.getOpenPrice());
            group.add(2, item.getHighestPrice());
            group.add(3, item.getLowestPrice());
            group.add(4, item.getClosePrice());
            group.add(5, item.getVolume());
            array.add(group);

            temKline = item;
        }
        return AjaxResult.success(array);
    }
}
