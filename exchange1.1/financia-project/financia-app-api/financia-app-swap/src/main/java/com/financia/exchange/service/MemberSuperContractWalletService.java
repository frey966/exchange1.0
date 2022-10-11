package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.swap.ContractCoin;

import java.math.BigDecimal;
import java.util.List;

public interface MemberSuperContractWalletService extends IService<MemberSuperContractWallet> {

    /**
     * 根据用户ID和合约ID查询钱包记录
     * @param memberId
     * @param contractId
     * @return
     */
    MemberSuperContractWallet findByMemberIdAndContractCoin(Long memberId, Long contractId);

    /**
     * 冻结钱包
     * @param memberWallet
     * @param amount
     * @return
     */
    int freezeUsdtBalance(MemberSuperContractWallet memberWallet, BigDecimal amount);

    /**
     * 解冻钱包
     * @param memberWallet
     * @param amount
     * @return
     */
    int thawUsdtBalance(MemberSuperContractWallet memberWallet, BigDecimal amount);

    /**
     * 冻结空仓持仓
     * @param walletId
     * @param volume
     */
    void freezeUsdtSellPosition(Long walletId, BigDecimal volume);

    /**
     * 解冻空仓持仓
     * @param walletId
     * @param volume
     */
    void thrawUsdtSellPosition(Long walletId, BigDecimal volume);

    /**
     * 冻结多仓持仓
     * @param walletId
     * @param volume
     */
    void freezeUsdtBuyPosition(Long walletId, BigDecimal volume);

    /**
     * 解冻多仓持仓
     * @param walletId
     * @param volume
     */
    void thrawUsdtBuyPosition(Long walletId, BigDecimal volume);

    /**
     * 减少冻结资产（与余额无关）
     * @param walletId
     * @param amount
     */
    void decreaseUsdtFrozen(Long walletId, BigDecimal amount);

    /**
     * 增加做多保证金（与冻结余额有关）
     * @param walletId
     * @param amount
     */
    void increaseUsdtBuyPrincipalAmountWithFrozen(Long walletId, BigDecimal amount);

    /**
     * 增加做空保证金（与冻结余额有关）
     * @param walletId
     * @param amount
     */
    void increaseUsdtSellPrincipalAmountWithFrozen(Long walletId, BigDecimal amount);

    /**
     * 更新开仓价格和持仓量（多仓）
     * @param walletId
     * @param avaPrice
     * @param volume
     */
    void updateUsdtBuyPriceAndPosition(Long walletId, BigDecimal avaPrice, BigDecimal volume);

    /**
     * 更新开仓价格和持仓量（空仓）
     * @param walletId
     * @param avaPrice
     * @param volume
     */
    void updateUsdtSellPriceAndPosition(Long walletId, BigDecimal avaPrice, BigDecimal volume);

    /**
     * 更新合约面值
     * @param walletId
     * @param shareNumber
     */
    void updateShareNumber(Long walletId, BigDecimal shareNumber);

    /**
     * 减少冻结持仓（空仓）和保证金
     * @param walletId
     * @param volume
     * @param principalAmount
     */
    void decreaseUsdtFrozenSellPositionAndPrincipalAmount(Long walletId, BigDecimal volume, BigDecimal principalAmount);

    /**
     * 增加余额
     * @param walletId
     * @param amount
     */
    void increaseUsdtBalance(Long walletId, BigDecimal amount);

    /**
     * 增加合约账户盈利
     * @param walletId
     * @param pL
     */
    void increaseUsdtProfit(Long walletId, BigDecimal pL);

    /**
     * 更新合约账户亏损
     * @param walletId
     * @param pL
     */
    void increaseUsdtLoss(Long walletId, BigDecimal pL);

    /**
     * 减少用户多仓持仓 和 保证金
     * @param walletId
     * @param volume
     * @param principalAmount
     */
    void decreaseUsdtFrozenBuyPositionAndPrincipalAmount(Long walletId, BigDecimal volume, BigDecimal principalAmount);

    /**
     * 查询所有需要同步的会员合约账户
     * @param contractCoin
     * @return
     */
    List<MemberSuperContractWallet> findAllNeedSync(ContractCoin contractCoin);

    /**
     * 多仓清空（可用仓位 + 冻结仓位），多仓保证金清空
     * @param walletId
     */
    void blastBuy(Long walletId);

    /**
     * 空仓清空（可用仓位 + 冻结仓位），空仓保证金清空
     * @param walletId
     */
    void blastSell(Long walletId);

    /**
     * 减少余额
     * @param walletId
     * @param amount
     */
    void decreaseUsdtBalance(Long walletId, BigDecimal amount);




}
