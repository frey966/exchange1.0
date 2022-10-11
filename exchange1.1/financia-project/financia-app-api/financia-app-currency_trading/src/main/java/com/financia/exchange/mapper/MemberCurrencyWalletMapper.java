package com.financia.exchange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financia.currency.MemberCryptoCurrencyWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface MemberCurrencyWalletMapper extends BaseMapper<MemberCryptoCurrencyWallet> {

    /**
     * 冻结钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_crypto_currency_wallet wallet set wallet.balance_money = wallet.balance_money - #{amount},wallet.frezze_money=wallet.frezze_money + #{amount} where wallet.id = #{walletId} and wallet.balance_money >= #{amount}")
    int freezeBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 解冻钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_crypto_currency_wallet wallet set wallet.frezze_money = wallet.frezze_money - #{amount},wallet.balance_money=wallet.balance_money + #{amount} where wallet.id = #{walletId} and wallet.frezze_money >= #{amount}")
    int unfreezeBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);


    @Update("update member_crypto_currency_wallet wallet set wallet.balance_money = wallet.balance_money + #{amount} where wallet.id = #{walletId} ")
    int increaseBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    @Update("update member_crypto_currency_wallet wallet set wallet.frezze_money = wallet.frezze_money - #{amount} where wallet.id = #{walletId} and wallet.frezze_money >= #{amount}")
    int decreaseFreezeBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 查询会员的币币钱包
     * @param memberId
     * @param coinId
     * @return
     */
    MemberCryptoCurrencyWallet findByCoinIdAndMemberId(@Param("memberId") Long memberId, @Param("coinId") Long coinId);

    /**
     * 查询不是该交易对的币币钱包信息
     * @param memberId
     * @param coinId
     * @return
     */
    Page<MemberCryptoCurrencyWallet> findByMemberIdAndNotContainCoinId(Page page, @Param("memberId") Long memberId, @Param("coinId") Long coinId);

    /**
     * 查询会员的所有币币持仓
     * @param memberId
     * @return
     */
    List<MemberCryptoCurrencyWallet> findByMemberId(@Param("memberId") Long memberId);



}