package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.swap.MemberContractWallet;
import com.financia.swap.ContractCoin;

import java.math.BigDecimal;
import java.util.List;

public interface MemberContractWalletService extends IService<MemberContractWallet> {

    /**
     * 根据用户ID和合约ID查询合约钱包
     * @param memberId
     * @param contractId
     * @return
     */
    MemberContractWallet findByMemberIdAndContractCoin(Long memberId, Long contractId);

    /**
     * 查询用户所有的合约钱包信息
     * @param memberId
     * @return
     */
    List<MemberContractWallet> findAllByMemberId(Long memberId);

    /**
     * 冻结钱包
     * @param memberWallet
     * @param amount
     * @return
     */
    int freezeUsdtBalance(MemberContractWallet memberWallet, BigDecimal amount);

    /**
     * 解冻钱包
     * @param memberWallet
     * @param amount
     * @return
     */
    int thawUsdtBalance(MemberContractWallet memberWallet, BigDecimal amount);

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
     * 减少保证金
     *
     * */

    void decreasePrincipalAmount(Long walletId,BigDecimal price);

    /**
     * 减少持仓数量
     * */

    void decreaseUsdtPosition(Long walletId,BigDecimal amount);

    /**
     * 增加持仓数量
     * */

    void increaseUsdtPosition(Long walletId,BigDecimal amount);

    /**
     * 更新开仓价格和持仓量（加仓）
     * @param walletId
     * @param avaPrice
     * @param volume
     */
    void updateUsdtBuyPrice(Long walletId, BigDecimal avaPrice);

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
    int increaseUsdtBalance(Long walletId, BigDecimal amount);

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
    List<MemberContractWallet> findAllNeedSync(ContractCoin contractCoin);

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


    /**
     * 减少平仓手续费
     * */
    void decreaseCloseFee(Long walletId,BigDecimal amount);

    /**
     * 增加平仓手续费
     * */
    void increaseCloseFee(Long walletId,BigDecimal amount);


    /**
     * 减少开仓手续费
     * */
    void decreaseOpenFee(Long walletId,BigDecimal amount);

    /**
     * 增加开仓手续费
     * */
    void increaseOpenFee(Long walletId,BigDecimal amount);

    /**
     * 设置止盈价格
     * */
    void setStopProfitPrice(Long walletId,BigDecimal amount);

    /**
     * 设置止损价格
     * */
    void setStopLossPrice(Long walletId,BigDecimal amount);

    /**
     * 修改杠杆倍数
     * @param walletId
     * @param leverage
     */
    void modifyUsdtBuyLeverage(Long walletId, BigDecimal leverage);


    void setForceClose(Long walletId,BigDecimal forceClosePrice);


    void increaseExchangeLoss(Long walletId,BigDecimal loss);

    void setBuyOrSell(Long walletId,int type);

    /**
     * 查询持仓不为0的数据
     * @return
     */
    List<MemberContractWallet> findByPositionIsNotZero();




}
