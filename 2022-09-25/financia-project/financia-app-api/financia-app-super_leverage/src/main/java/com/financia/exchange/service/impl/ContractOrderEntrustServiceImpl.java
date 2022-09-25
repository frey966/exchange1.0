package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.utils.StringUtils;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.exchange.mapper.SuperContractOrderEntrustMapper;
import com.financia.exchange.service.MemberBusinessWalletService;
import com.financia.exchange.service.MemberSuperContractWalletService;
import com.financia.exchange.service.SuperContractOrderEntrustService;
import com.financia.superleverage.SuperContractOrderEntrust;
import com.financia.swap.ContractOrderDirection;
import com.financia.swap.ContractOrderEntrust;
import com.financia.swap.ContractOrderEntrustStatus;
import com.financia.swap.ContractOrderEntrustType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ContractOrderEntrustServiceImpl extends ServiceImpl<SuperContractOrderEntrustMapper, SuperContractOrderEntrust> implements SuperContractOrderEntrustService {

    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;

    @Autowired
    private MemberSuperContractWalletService memberSuperContractWalletService;

    @Override
    public List<SuperContractOrderEntrust> loadUnMatchOrders(Long id) {
        LambdaQueryWrapper<SuperContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractOrderEntrust::getStatus,ContractOrderEntrustStatus.ENTRUST_ING);
        queryWrapper.eq(SuperContractOrderEntrust::getContractId, id);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SuperContractOrderEntrust> queryAllEntrustClosingOrdersByContractCoin(Long memberId, Long contractId, ContractOrderDirection direction) {
        LambdaQueryWrapper<SuperContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractOrderEntrust::getMemberId,memberId);
        queryWrapper.eq(SuperContractOrderEntrust::getContractId, contractId);
        queryWrapper.eq(SuperContractOrderEntrust::getDirection, direction);
        queryWrapper.eq(SuperContractOrderEntrust::getEntrustType, ContractOrderEntrustType.CLOSE);
        queryWrapper.eq(SuperContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_ING);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateStatus(Long id, ContractOrderEntrustStatus status) {
        baseMapper.updateStatus(id, status);
    }

    @Override
    public Page<SuperContractOrderEntrust> queryPageEntrustingOrdersBySymbol(Long memberId, Long contractCoinId, Integer type, int pageNo, int pageSize) {

        LambdaQueryWrapper<SuperContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractOrderEntrust::getMemberId, memberId);
        queryWrapper.eq(contractCoinId!=null,SuperContractOrderEntrust::getContractId, contractCoinId);
        queryWrapper.eq(type!=null,SuperContractOrderEntrust::getType, type);
        queryWrapper.eq(SuperContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_ING);
        queryWrapper.orderByDesc(SuperContractOrderEntrust::getCreateTime);
        Page<SuperContractOrderEntrust> page = new Page<>(pageNo, pageSize);
        return baseMapper.selectPage(page, queryWrapper);

    }

    @Override
    public List<SuperContractOrderEntrust> findAllByMemberIdAndContractId(Long memberId) {
        LambdaQueryWrapper<SuperContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractOrderEntrust::getMemberId, memberId);
        queryWrapper.eq(SuperContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_ING);
        queryWrapper.eq(SuperContractOrderEntrust::getEntrustType, ContractOrderEntrustType.OPEN);
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveOpenOrderAndFreezeBalance(SuperContractOrderEntrust order, MemberSuperContractWallet memberSuperContractWallet) {
        Long memberId = order.getMemberId();
        // 冻结主钱包余额
        MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(memberId);
        int ret = memberBusinessWalletService.freezeBalance(memberBusinessWallet.getId(), order.getValue(), memberBusinessWallet.getMoney().subtract(order.getValue()), order.getContractOrderEntrustId(), BusinessSubType.SUPER_OPEN,"");
        if(ret == 0) {
            return ret;
        }
        // 保存开仓订单
        int insert = baseMapper.insert(order);
        // 保存超级杠杆钱包
        if(order.getEntrustType() == ContractOrderEntrustType.OPEN) {
            boolean saveResult = memberSuperContractWalletService.save(memberSuperContractWallet);
        }
        return 1;
    }


}
