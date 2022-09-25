package com.financia.exchange.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.swap.MemberContractWallet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合约交易币Mapper接口
 *
 * @author ruoyi
 * @date 2022-07-13
 */
public interface MemberContractWalletMapper extends BaseMapper<MemberContractWallet>
{

    /**
     * 冻结钱包余额
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_balance = wallet.usdt_balance - #{amount},wallet.usdt_frozen_balance=wallet.usdt_frozen_balance + #{amount} where wallet.id = #{walletId} and wallet.usdt_balance >= #{amount}")
    int freezeUsdtBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 解冻钱包余额
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_balance = wallet.usdt_balance + #{amount},wallet.usdt_frozen_balance=wallet.usdt_frozen_balance - #{amount} where wallet.id = #{walletId} and wallet.usdt_frozen_balance >= #{amount}")
    int thawUsdtBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);


    @Update("update member_contract_wallet wallet set wallet.usdt_frozen_sell_position = wallet.usdt_frozen_sell_position + #{amount},wallet.usdt_sell_position=wallet.usdt_sell_position - #{amount} where wallet.id = #{walletId} and wallet.usdt_sell_position >= #{amount}")
    void freezeUsdtSellPosition(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);


    @Update("update member_contract_wallet wallet set wallet.usdt_frozen_sell_position = wallet.usdt_frozen_sell_position - #{amount},wallet.usdt_sell_position=wallet.usdt_sell_position + #{amount} where wallet.id = #{walletId} and wallet.usdt_frozen_sell_position >= #{amount}")
    void thrawUsdtSellPosition(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);

    @Update("update member_contract_wallet wallet set wallet.usdt_frozen_buy_position = wallet.usdt_frozen_buy_position + #{amount},wallet.usdt_buy_position=wallet.usdt_buy_position - #{amount} where wallet.id = #{walletId} and wallet.usdt_buy_position >= #{amount}")
    void freezeUsdtBuyPosition(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);

    @Update("update member_contract_wallet wallet set wallet.usdt_frozen_buy_position = wallet.usdt_frozen_buy_position - #{amount},wallet.usdt_buy_position=wallet.usdt_buy_position + #{amount} where wallet.id = #{walletId} and wallet.usdt_frozen_buy_position >= #{amount}")
    void thrawUsdtBuyPosition(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);

    /**
     * 减少冻结余额
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_frozen_balance=wallet.usdt_frozen_balance - #{amount} where wallet.id = #{walletId} and wallet.usdt_frozen_balance >= #{amount}")
    int decreaseUsdtFrozen(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 减少保证金
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_principal_amount=wallet.usdt_buy_principal_amount - #{amount} where wallet.id = #{walletId} and wallet.usdt_buy_principal_amount >= #{amount}")
    int decreasePrincipalAmount(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);



    /**
     * 减少持仓数量
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_position=wallet.usdt_buy_position - #{amount} where wallet.id = #{walletId}")
    int decreaseUsdtPosition(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 增加持仓数量
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_position=wallet.usdt_buy_position + #{amount} where wallet.id = #{walletId}")
    int increasePrincipalAmount(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 增加做多保证金（与冻结余额有关）
     * @param walletId
     * @param amount
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_principal_amount=wallet.usdt_buy_principal_amount + #{amount} where wallet.id = #{walletId}")
    int increaseUsdtBuyPrincipalAmountWithFrozen(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);

    /**
     * 增加做空保证金（与冻结余额有关）
     * @param walletId
     * @param amount
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_sell_principal_amount=wallet.usdt_sell_principal_amount + #{amount},wallet.usdt_frozen_balance=wallet.usdt_frozen_balance - #{amount} where wallet.id = #{walletId} and wallet.usdt_frozen_balance >= #{amount}")
    void increaseUsdtSellPrincipalAmountWithFrozen(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);

    /**
     * 更新开仓价格和持仓量（多仓）
     * @param walletId
     * @param avaPrice
     * @param volume
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_price=#{avaPrice},wallet.usdt_buy_position=wallet.usdt_buy_position + #{volume} where wallet.id = #{walletId}")
    void updateUsdtBuyPriceAndPosition(@Param("walletId") Long walletId, @Param("avaPrice") BigDecimal avaPrice, @Param("volume") BigDecimal volume);


    /**
     * 更新开仓价格和持仓量（加仓）
     * @param walletId
     * @param avaPrice
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_price=#{avaPrice} where wallet.id = #{walletId}")
    void updateUsdtBuyPrice(@Param("walletId") Long walletId, @Param("avaPrice") BigDecimal avaPrice);

    /**
     * 更新开仓价格和持仓量（空仓）
     * @param walletId
     * @param avaPrice
     * @param volume
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_sell_price=#{avaPrice},wallet.usdt_sell_position=wallet.usdt_sell_position + #{volume} where wallet.id = #{walletId}")
    void updateUsdtSellPriceAndPosition(@Param("walletId") Long walletId, @Param("avaPrice") BigDecimal avaPrice, @Param("volume") BigDecimal volume);

    /**
     * 更新合约面值
     * @param walletId
     * @param shareNumber
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_share_number=#{shareNumber} where wallet.id = #{walletId}")
    void updateShareNumber(@Param("walletId") Long walletId, @Param("shareNumber") BigDecimal shareNumber);

    /**
     * 减少用户持仓（空仓）和保证金
     * @param walletId
     * @param volume
     * @param pAmount
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_sell_principal_amount=wallet.usdt_sell_principal_amount - #{pAmount},wallet.usdt_frozen_sell_position=wallet.usdt_frozen_sell_position - #{volume} where wallet.id = #{walletId} and wallet.usdt_sell_principal_amount>=#{pAmount} and wallet.usdt_frozen_sell_position>=#{volume}")
    void decreaseUsdtFrozenSellPositionAndPrincipalAmount(@Param("walletId") Long walletId, @Param("volume") BigDecimal volume, @Param("pAmount") BigDecimal pAmount);

    /**
     * 增加钱包余额
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_balance = wallet.usdt_balance + #{amount} where wallet.id = #{walletId}")
    int increaseUsdtBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 增加合约账户盈利
     * @param walletId
     * @param amount
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_profit=wallet.usdt_profit+#{amount} where wallet.id = #{walletId}")
    void increaseUsdtProfit(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);

    /**
     * 更新合约账户亏损
     * @param walletId
     * @param amount
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_loss=wallet.usdt_loss+#{amount} where wallet.id = #{walletId}")
    void increaseUsdtLoss(@Param("walletId") Long walletId, @Param("amount") BigDecimal amount);

    /**
     * 减少用户多仓持仓 和 保证金
     * @param walletId
     * @param volume
     * @param pAmount
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_principal_amount=wallet.usdt_buy_principal_amount - #{pAmount},wallet.usdt_frozen_buy_position=wallet.usdt_frozen_buy_position - #{volume} where wallet.id = #{walletId} and wallet.usdt_buy_principal_amount>=#{pAmount}")
    void decreaseUsdtFrozenBuyPositionAndPrincipalAmount(@Param("walletId") Long walletId, @Param("volume") BigDecimal volume, @Param("pAmount") BigDecimal pAmount);

    @Select(value = "select * from member_contract_wallet as wallet where (wallet.usdt_buy_position) and wallet.contract_id=#{contractId}")
    List<MemberContractWallet> findAllNeedSync(@Param("contractId") Long contractId);


    /**
     * 多仓保证金清零，多仓可用仓位清零，多仓冻结仓位清零
     * @param walletId
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_principal_amount=0,wallet.usdt_frozen_buy_position=0,wallet.usdt_buy_position=0 where wallet.id = #{walletId}")
    void blastBuy(@Param("walletId") Long walletId);


    /**
     * 空仓保证金清零，空仓可用仓位清零，空仓冻结仓位清零
     * @param walletId
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_sell_principal_amount=0,wallet.usdt_frozen_sell_position=0,wallet.usdt_sell_position=0 where wallet.id = #{walletId}")
    void blastSell(@Param("walletId") Long walletId);

    /**
     * 减少钱包余额
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_balance = wallet.usdt_balance - #{amount} where wallet.id = #{walletId} and wallet.usdt_balance >= #{amount}")
    int decreaseUsdtBalance(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 修改杠杆倍数
     * @param walletId
     * @param leverage
     */
    @Update("update member_contract_wallet wallet set wallet.usdt_buy_leverage = #{leverage} where wallet.id = #{walletId}")
    void modifyUsdtBuyLeverage(@Param("walletId") Long walletId, @Param("leverage") BigDecimal leverage);



    /**
     * 减少平仓手续费
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.close_fee = wallet.close_fee - #{amount} where wallet.id = #{walletId} and wallet.close_fee >= #{amount}")
    int decreaseCloseFee(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);


    /**
     * 增加平仓手续费
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.close_fee = wallet.close_fee + #{amount} where wallet.id = #{walletId} ")
    int increaseCloseFee(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 减少开仓手续费
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.open_fee = wallet.open_fee - #{amount} where wallet.id = #{walletId} and wallet.open_fee >= #{amount}")
    int decreaseOpenFee(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);


    /**
     * 增加开仓手续费
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.open_fee = wallet.open_fee + #{amount} where wallet.id = #{walletId} ")
    int increaseOpenFee(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 设置止盈价
     *
     * @param walletId
     * @param amount
     * @return
     */
    @Update("update member_contract_wallet wallet set wallet.stop_profit_price = #{amount} where wallet.id = #{walletId} ")
    int setStopProfitPrice(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    /**
     * 设置止损价
     *
     * @param walletId
     * @param amount
     * @return
     */

    @Update("update member_contract_wallet wallet set wallet.stop_loss_price = #{amount} where wallet.id = #{walletId} ")
    int setStopLossPrice(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    @Update("update member_contract_wallet wallet set wallet.force_close_price = #{amount} where wallet.id = #{walletId} ")
    int setForceClose(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);

    @Update("update member_contract_wallet wallet set wallet.exchange_loss =wallet.exchange_loss+#{amount} where wallet.id = #{walletId} ")
    int increaseExchangeLoss(@Param("walletId") long walletId, @Param("amount") BigDecimal amount);


    @Update("update member_contract_wallet wallet set wallet.direction =#{type} where wallet.id = #{walletId} ")
    int setBuyOrSell(@Param("walletId") long walletId, @Param("type") int type);


}
