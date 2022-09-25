package com.financia.conversion.exchange.controller;


import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.conversion.exchange.service.PConversionRateService;
import com.financia.exchange.PConversionRate;
import com.financia.exchange.PNationalCurrency;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 法币汇率
 */
@Api(tags="法币汇率")
@RestController
@RequestMapping("/pconversionrateApi")
@Slf4j
public class PConversionRateController extends BaseController {

    @Autowired
    private PConversionRateService pConversionRateService;

    @PostMapping(value = "/honmRateList")
    @ApiOperation(value = "一键汇兑",notes = "")
    public AjaxResult register()
    {
        List<Map> list=pConversionRateService.selectPconversionRate();
        List zhong=new ArrayList();
        for (int i = 0; i < list.size(); i++)
        {
            List list1=new ArrayList();
            for (int k = 0; k < list.size(); k++)
            {
                list1.add(((PConversionRate)list.get(i)).getConversionRate().divide(((PConversionRate)list.get(k)).getConversionRate(),4, RoundingMode.HALF_UP));
            }
            Map map=new HashMap();
            map.put("currencyPath",((PConversionRate)list.get(i)).getCurrencyPath());
            map.put("currencySymbol",((PConversionRate)list.get(i)).getCurrencySymbol());
            map.put("ratelist",list1);
            zhong.add(map);
        }
        Map resultDate=new HashMap();
        resultDate.put("zhong",zhong);
        resultDate.put("heng",list);
        return AjaxResult.success(resultDate);
    }

    /**
     * 分页  pageNum  pageSize
     * @param pNationalCurrency
     * @return
     */
    @ApiOperation(value = "查询法币列表",notes = "查询法币列表")
    @GetMapping("pnationalcurrency/list")
    public TableDataInfo list(PNationalCurrency pNationalCurrency)
    {
//        startPage();
        List<PNationalCurrency> list = pConversionRateService.selectPNationalCurrencyList(pNationalCurrency);
        return getDataTable(list);
    }

    /**
     * 法币热搜列表
     * @param pNationalCurrency
     * @return
     */
    @ApiOperation(value = "法币热搜列表",notes = "法币热搜列表")
    @GetMapping("pnationalcurrencyHot/list")
    public TableDataInfo pnationalcurrencyHot(PNationalCurrency pNationalCurrency)
    {
        pNationalCurrency.setIshot("2");
        List<PNationalCurrency> list = pConversionRateService.selectPNationalCurrencyList(pNationalCurrency);
        return getDataTable(list);
    }

    /**
     * 汇兑交易对列表
     * @param pNationalCurrency
     * @return
     */
    @ApiOperation(value = "汇兑交易对列表",notes = "汇兑交易对列表")
    @GetMapping("selectPNationalCurrencyPair/list")
    public TableDataInfo selectPNationalCurrencyPair(PNationalCurrency pNationalCurrency)
    {
        List<PNationalCurrency> list = pConversionRateService.selectPNationalCurrencyPair(pNationalCurrency);
        return getDataTable(list);
    }

}
