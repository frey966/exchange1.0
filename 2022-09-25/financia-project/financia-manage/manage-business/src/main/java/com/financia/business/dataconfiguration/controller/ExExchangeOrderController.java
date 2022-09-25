package com.financia.business.dataconfiguration.controller;

import com.financia.business.dataconfiguration.service.ExExchangeOrderService;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.currency.ExchangeOrder;
import com.financia.exchange.Member;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import icu.mhb.mybatisplus.plugln.core.JoinWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 币币交易委托单
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-08 14:22:46
 */
@Api(tags = "币币管理-币币交易订单")
@RestController
@RequestMapping("/currency/order")
public class ExExchangeOrderController extends BaseController {
    @Autowired
    private ExExchangeOrderService exExchangeOrderService;

    @ApiOperation(value = "币币订单列表",notes = "币币订单列表")
    @GetMapping("/list")
    public TableDataInfo list(ExchangeOrder exchangeOrder)
    {
        JoinLambdaWrapper<ExchangeOrder> wrapper = new JoinLambdaWrapper(ExchangeOrder.class);
        JoinWrapper<Member, ExchangeOrder> joinWrapper = wrapper.leftJoin(Member.class, Member::getId, ExchangeOrder::getMemberId).select(Member::getUsername);
        if (exchangeOrder.getMemberId() != null){
            wrapper.eq(ExchangeOrder::getMemberId,exchangeOrder.getMemberId());
        }
        if (!StringUtils.isEmpty(exchangeOrder.getUsername())) {
            joinWrapper.eq(Member::getUsername,exchangeOrder.getUsername());
        }
        wrapper.orderByDesc(ExchangeOrder::getCreateTime);
        joinWrapper.end();
        startPage();
        List<ExchangeOrder> list = exExchangeOrderService.joinList(wrapper,ExchangeOrder.class);
        return getDataTable(list);
    }

    @ApiOperation(value = "币币订单详情",notes = "币币订单详情")
    @GetMapping("/info/{id}")
    public AjaxResult orderInfo(@PathVariable("id") Long id)
    {
        JoinLambdaWrapper<ExchangeOrder> wrapper = new JoinLambdaWrapper<>(ExchangeOrder.class);
        wrapper.leftJoin(Member.class,Member::getId, ExchangeOrder::getMemberId)
               .select(Member::getUsername).end();
        wrapper.eq(ExchangeOrder::getId,id);
        ExchangeOrder exchangeOrder = exExchangeOrderService.joinGetOne(wrapper, ExchangeOrder.class);
        return AjaxResult.success(exchangeOrder);
    }

}
