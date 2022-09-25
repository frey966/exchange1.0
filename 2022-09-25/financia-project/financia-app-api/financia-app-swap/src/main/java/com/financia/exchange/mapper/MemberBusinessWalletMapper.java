package com.financia.exchange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.exchange.MemberBusinessWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 会员-业务钱包金额
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-07 18:17:03
 */
@Mapper
@Repository("memberBusinessWalletMapper")
public interface MemberBusinessWalletMapper extends BaseMapper<MemberBusinessWallet> {

    /**
     * 冻结钱包余额
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_business_wallet wallet set wallet.money = wallet.money - #{amount},wallet.freeze_money=wallet.freeze_money + #{amount} where wallet.id = #{walletId} and wallet.money >= #{amount}")
    int freezeBalance(@Param("walletId") Integer walletId, @Param("amount") BigDecimal amount);

    /**
     * 解冻钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_business_wallet wallet set wallet.money = wallet.money + #{amount},wallet.freeze_money=wallet.freeze_money - #{amount} where wallet.id = #{walletId} and wallet.freeze_money >= #{amount}")
    int unfreezeBalance(@Param("walletId") Integer walletId, @Param("amount") BigDecimal amount);

    /**
     * 扣减冻结余额
     * @param memberWalletId
     * @param amount
     * @return
     */
    @Update("update member_business_wallet wallet set wallet.freeze_money=wallet.freeze_money - #{amount} where wallet.id = #{walletId} and wallet.freeze_money >= #{amount}")
    int decreaseFreezeBalance(@Param("walletId") Integer memberWalletId, @Param("amount") BigDecimal amount);

    /**
     * 增加可用余额
     * @param memberWalletId
     * @param amount
     * @return
     */
    @Update("update member_business_wallet wallet set wallet.money=wallet.money + #{amount} where wallet.id = #{walletId}")
    int increaseBalance(@Param("walletId") Integer memberWalletId, @Param("amount") BigDecimal amount);

    /**
     * 扣减可用余额
     * @param memberWalletId
     * @param amount
     * @return
     */
    @Update("update member_business_wallet wallet set wallet.money=wallet.money - #{amount} where wallet.id = #{walletId} and wallet.money >= #{amount}")
    int decreaseBalance(@Param("walletId") Integer memberWalletId, @Param("amount") BigDecimal amount);
}
