package com.financia.exchange.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.currency.ExchangeOrder;
import com.financia.currency.MemberCryptoCurrencyWallet;
import com.financia.exchange.MemberBusinessWallet;

import java.util.List;


/**
 * 币种service
 */
public interface ExchangeOrderService extends IService<ExchangeOrder> {

    /**
     * 添加委托订单单
     * @param memberId
     * @param order
     * @return
     */
    AjaxResult addOrder(MemberBusinessWallet memberBusinessWallet, MemberCryptoCurrencyWallet memberCryptoCurrencyWallet, ExchangeOrder order);

    /**
     * 查询当前交易中的委托
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<ExchangeOrder> findCurrent(Long memberId, Long coinId, int pageNo, int pageSize);

    /**
     * 查询历史委托
     * @param memberId
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<ExchangeOrder> findHistory(Long memberId, Long coinId, Integer direction, Integer days, int pageNo, int pageSize);

    /**
     * 查询持仓账单
     * @param currencyWalletId
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<ExchangeOrder> findCurrencyWalletBill(Long currencyWalletId, Integer status, Integer days, int pageNo, int pageSize);


    /**
     * 加载未匹配订单
     * */
    List<ExchangeOrder> loadUnmatchedOrders(Long contractId);

    /**
     * 取消订单
     *
     * */
    void cancelOrder(Long orderId);

}
