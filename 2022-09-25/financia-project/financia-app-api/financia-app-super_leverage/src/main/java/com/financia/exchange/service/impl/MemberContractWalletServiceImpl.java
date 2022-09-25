package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.exchange.service.MemberSuperContractWalletService;
import com.financia.superleverage.SuperContractCoin;
import com.financia.swap.MemberContractWallet;
import com.financia.exchange.mapper.MemberSuperContractWalletMapper;
import com.financia.swap.ContractCoin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class MemberContractWalletServiceImpl extends ServiceImpl<MemberSuperContractWalletMapper, MemberSuperContractWallet> implements MemberSuperContractWalletService {

    @Override
    public MemberSuperContractWallet findByMemberIdAndContractCoin(Long memberId, Long contractId,String entrustOrder) {
        LambdaQueryWrapper<MemberSuperContractWallet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberSuperContractWallet::getMemberId, memberId);
        queryWrapper.eq(MemberSuperContractWallet::getContractId, contractId);
        queryWrapper.eq(MemberSuperContractWallet::getEntrustOrder,entrustOrder);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<MemberSuperContractWallet> findAllByMemberId(Long memberId) {
        LambdaQueryWrapper<MemberSuperContractWallet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MemberSuperContractWallet::getMemberId, memberId);
        queryWrapper.eq(MemberSuperContractWallet::getStatus,1);
        queryWrapper.ne(MemberSuperContractWallet::getUsdtBuyPosition, BigDecimal.ZERO);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public int freezeUsdtBalance(MemberSuperContractWallet memberWallet, BigDecimal amount) {
        return baseMapper.freezeUsdtBalance(memberWallet.getId(), amount);
    }

    @Override
    public int thawUsdtBalance(MemberSuperContractWallet memberWallet, BigDecimal amount) {
        return baseMapper.thawUsdtBalance(memberWallet.getId(), amount);
    }

    @Override
    public void freezeUsdtSellPosition(Long walletId, BigDecimal volume) {
        baseMapper.freezeUsdtSellPosition(walletId, volume);
    }

    @Override
    public void thrawUsdtSellPosition(Long walletId, BigDecimal volume) {
        baseMapper.thrawUsdtSellPosition(walletId, volume);
    }

    @Override
    public void freezeUsdtBuyPosition(Long walletId, BigDecimal volume) {
        baseMapper.freezeUsdtBuyPosition(walletId, volume);
    }

    @Override
    public void thrawUsdtBuyPosition(Long walletId, BigDecimal volume) {
        baseMapper.thrawUsdtBuyPosition(walletId, volume);
    }

    @Override
    public void decreaseUsdtFrozen(Long walletId, BigDecimal amount){
        baseMapper.decreaseUsdtFrozen(walletId,amount);
    }

    @Override
    public void increaseUsdtBuyPrincipalAmountWithFrozen(Long walletId, BigDecimal amount) {
        int res=baseMapper.increaseUsdtBuyPrincipalAmountWithFrozen(walletId, amount);
        System.out.println("res is "+res);
    }

    @Override
    public void increaseUsdtSellPrincipalAmountWithFrozen(Long walletId, BigDecimal amount) {
        baseMapper.increaseUsdtSellPrincipalAmountWithFrozen(walletId, amount);
    }

    @Override
    public void updateUsdtBuyPriceAndPosition(Long walletId, BigDecimal avaPrice, BigDecimal volume) {
        baseMapper.updateUsdtBuyPriceAndPosition(walletId, avaPrice, volume);
    }

    @Override
    public void decreasePrincipalAmount(Long walletId, BigDecimal price) {
        baseMapper.decreasePrincipalAmount(walletId,price);
    }

    @Override
    public void decreaseUsdtPosition(Long walletId, BigDecimal amount) {
        baseMapper.decreaseUsdtPosition(walletId,amount);
    }

    @Override
    public void increaseUsdtPosition(Long walletId, BigDecimal amount) {
        baseMapper.increasePrincipalAmount(walletId,amount);
    }

    @Override
    public void updateUsdtBuyPrice(Long walletId, BigDecimal avaPrice) {
        baseMapper.updateUsdtBuyPrice(walletId,avaPrice);
    }




    @Override
    public void updateUsdtSellPriceAndPosition(Long walletId, BigDecimal avaPrice, BigDecimal volume) {
        baseMapper.updateUsdtSellPriceAndPosition(walletId, avaPrice, volume);
    }

    @Override
    public void updateShareNumber(Long walletId, BigDecimal shareNumber) {
        baseMapper.updateShareNumber(walletId, shareNumber);
    }

    @Override
    public void decreaseUsdtFrozenSellPositionAndPrincipalAmount(Long walletId, BigDecimal volume, BigDecimal principalAmount) {
        baseMapper.decreaseUsdtFrozenSellPositionAndPrincipalAmount(walletId, volume, principalAmount);
    }

    @Override
    public void increaseUsdtBalance(Long walletId, BigDecimal amount){
        baseMapper.increaseUsdtBalance(walletId,amount);
    }

    @Override
    public void increaseUsdtProfit(Long walletId, BigDecimal pL) {
        baseMapper.increaseUsdtProfit(walletId, pL);
    }

    @Override
    public void increaseUsdtLoss(Long walletId, BigDecimal pL) {
        baseMapper.increaseUsdtLoss(walletId, pL);
    }

    @Override
    public void decreaseUsdtFrozenBuyPositionAndPrincipalAmount(Long walletId, BigDecimal volume, BigDecimal principalAmount) {
        baseMapper.decreaseUsdtFrozenBuyPositionAndPrincipalAmount(walletId, volume, principalAmount);
    }

    @Override
    public List<MemberSuperContractWallet> findAllNeedSync(SuperContractCoin contractCoin) {
        return baseMapper.findAllNeedSync(contractCoin.getId());
    }

    @Override
    public void blastBuy(Long walletId) {
        baseMapper.blastBuy(walletId);
    }

    @Override
    public void blastSell(Long walletId) {
        baseMapper.blastSell(walletId);
    }

    @Override
    public void decreaseUsdtBalance(Long walletId, BigDecimal amount) {
        baseMapper.decreaseUsdtBalance(walletId, amount);
    }

    @Override
    public void decreaseCloseFee(Long walletId, BigDecimal amount) {
        baseMapper.decreaseCloseFee(walletId,amount);
    }

    @Override
    public void increaseCloseFee(Long walletId, BigDecimal amount) {
        baseMapper.increaseCloseFee(walletId,amount);
    }

    @Override
    public void decreaseOpenFee(Long walletId, BigDecimal amount) {
        baseMapper.decreaseOpenFee(walletId,amount);
    }

    @Override
    public void increaseOpenFee(Long walletId, BigDecimal amount) {
        baseMapper.increaseOpenFee(walletId,amount);
    }

    @Override
    public void setStopProfitPrice(Long walletId, BigDecimal amount) {
        baseMapper.setStopProfitPrice(walletId,amount);
    }

    @Override
    public void setStopLossPrice(Long walletId, BigDecimal amount) {
        baseMapper.setStopLossPrice(walletId,amount);
    }

    public void modifyUsdtBuyLeverage(Long walletId, BigDecimal leverage) {
        baseMapper.modifyUsdtBuyLeverage(walletId, leverage);
    }


    @Override
    public void setForceClose(Long walletId, BigDecimal forceClosePrice) {
        baseMapper.setForceClose(walletId,forceClosePrice);
    }

    @Override
    public void increaseExchangeLoss(Long walletId, BigDecimal loss) {
        baseMapper.increaseExchangeLoss(walletId,loss);
    }

    public void setBuyOrSell(Long walletId,int type){
        baseMapper.setBuyOrSell(walletId,type);
    }

    @Override
    public List<MemberSuperContractWallet> findByPositionIsNotZero() {
        LambdaQueryWrapper<MemberSuperContractWallet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(MemberSuperContractWallet::getUsdtBuyPosition, 0);
        return baseMapper.selectList(queryWrapper);
    }
}
