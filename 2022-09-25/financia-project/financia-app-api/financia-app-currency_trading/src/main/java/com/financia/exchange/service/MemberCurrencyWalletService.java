package com.financia.exchange.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.currency.ExchangeOrder;
import com.financia.currency.MemberCryptoCurrencyWallet;

import java.math.BigDecimal;
import java.util.List;


/**
 * 币币钱包service
 */
public interface MemberCurrencyWalletService extends IService<MemberCryptoCurrencyWallet> {

    /**
     * 查询会员的币币钱包
     * @param memberId
     * @param coinId
     * @return
     */
    MemberCryptoCurrencyWallet findByCoinIdAndMemberId(Long memberId, Long coinId);

    /**
     * 冻结钱包
     *
     * @param memberWalletId
     * @param amount
     * @return
     */
    int freezeBalance(Long memberWalletId, BigDecimal amount);

    /**
     * 解冻钱包
     *
     * @param memberWalletId
     * @param amount
     * @return
     */
    int unfreezeBalance(Long memberWalletId, BigDecimal amount);

    /**
     * 客户买入，减少主钱包冻结金额，增加币币余额
     * */
    int increaseBalanceByBuy(Long memberCurrencyWalletId, ExchangeOrder order);

    /**
     * 客户卖出，减少币币钱包冻结金额，增加主钱包余额
     * */
    int decreaseFreezeBalanceBySell(Integer memberBusinessWalletId, Long memberCurrencyWalletId, ExchangeOrder order);

    /**
     * 查询不是该交易对的币币钱包信息
     * @param memberId
     * @param coinId
     * @return
     */
    Page<MemberCryptoCurrencyWallet> findByMemberIdAndNotContainCoinId(Long memberId, Long coinId, Integer pageNo, Integer pageSize);

    /**
     * 查询会员的所有币币持仓
     * @param memberId
     * @return
     */
    List<MemberCryptoCurrencyWallet> findByMemberId(Long memberId);

}
