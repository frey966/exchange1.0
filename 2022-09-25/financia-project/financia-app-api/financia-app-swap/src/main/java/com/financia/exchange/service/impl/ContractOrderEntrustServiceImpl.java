package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.utils.StringUtils;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.mapper.ContractOrderEntrustMapper;
import com.financia.exchange.service.ContractOrderEntrustService;
import com.financia.exchange.service.MemberBusinessWalletService;
import com.financia.exchange.service.MemberContractWalletService;
import com.financia.swap.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ContractOrderEntrustServiceImpl extends ServiceImpl<ContractOrderEntrustMapper, ContractOrderEntrust> implements ContractOrderEntrustService {

    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;

    @Autowired
    private MemberContractWalletService memberContractWalletService;

    @Override
    public List<ContractOrderEntrust> loadUnMatchOrders(Long id) {
        LambdaQueryWrapper<ContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractOrderEntrust::getStatus,ContractOrderEntrustStatus.ENTRUST_ING);
        queryWrapper.eq(ContractOrderEntrust::getContractId, id);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ContractOrderEntrust> queryAllEntrustClosingOrdersByContractCoin(Long memberId, Long contractId, ContractOrderDirection direction) {
        LambdaQueryWrapper<ContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractOrderEntrust::getMemberId,memberId);
        queryWrapper.eq(ContractOrderEntrust::getContractId, contractId);
        queryWrapper.eq(ContractOrderEntrust::getDirection, direction);
        queryWrapper.eq(ContractOrderEntrust::getEntrustType, ContractOrderEntrustType.CLOSE);
        queryWrapper.eq(ContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_ING);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateStatus(Long id, ContractOrderEntrustStatus status, Long time) {
        baseMapper.updateStatus(id, status, time);
    }

    @Override
    public Page<ContractOrderEntrust> queryPageEntrustingOrdersBySymbol(Long memberId, Long contractCoinId, Integer type, int pageNo, int pageSize) {
        LambdaQueryWrapper<ContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractOrderEntrust::getMemberId, memberId);
        if(contractCoinId != null) {
            queryWrapper.eq(ContractOrderEntrust::getContractId, contractCoinId);
        }
        if(type != null) {
            queryWrapper.eq(ContractOrderEntrust::getType, type);
        }
        queryWrapper.eq(ContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_ING);
        queryWrapper.orderByDesc(ContractOrderEntrust::getCreateTime);
        Page<ContractOrderEntrust> page = new Page<>(pageNo, pageSize);
        return baseMapper.selectPage(page, queryWrapper);

    }


    @Override
    public List<ContractOrderEntrust> findEntrustingOrderByMemberIdAndContractId(Long memberId, Long contractCoinId) {
        LambdaQueryWrapper<ContractOrderEntrust> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractOrderEntrust::getMemberId, memberId);
        queryWrapper.eq(contractCoinId!=null,ContractOrderEntrust::getContractId, contractCoinId);
        queryWrapper.eq(ContractOrderEntrust::getStatus, ContractOrderEntrustStatus.ENTRUST_ING);
        queryWrapper.in(ContractOrderEntrust::getEntrustType, ContractOrderEntrustType.OPEN, ContractOrderEntrustType.ADD);
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveOpenOrderAndFreezeBalance(ContractOrderEntrust order) {
        Long memberId = order.getMemberId();
        // 冻结主钱包余额
        MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(memberId);
        int ret = memberBusinessWalletService.freezeBalance(memberBusinessWallet.getId(), order.getValue(), memberBusinessWallet.getMoney().subtract(order.getValue()), order.getContractOrderEntrustId(), BusinessSubType.SWAP_OPEN,"");
        if(ret == 0) {
            return ret;
        }
        // 保存开仓订单
        int insert = baseMapper.insert(order);
        return 1;
    }


}
