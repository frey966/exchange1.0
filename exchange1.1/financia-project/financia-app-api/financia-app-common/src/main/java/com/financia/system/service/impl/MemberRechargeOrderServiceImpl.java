package com.financia.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.enums.RechargeStatus;
import com.financia.common.core.enums.TransStatus;
import com.financia.exchange.MemberCompanyWalletAddress;
import com.financia.exchange.MemberRechargeOrder;
import com.financia.exchange.MemberWalletAddress;
import com.financia.system.crypto.Manager;
import com.financia.system.crypto.service.ICoinService;
import com.financia.system.mapper.MemberRechargeOrderMapper;
import com.financia.system.service.MemberBusinessWalletService;
import com.financia.system.service.MemberCompanyWalletAddressService;
import com.financia.system.service.MemberRechargeOrderService;
import com.financia.system.service.MemberWalletAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service("memberUpOrderService")
@Slf4j
public class MemberRechargeOrderServiceImpl extends ServiceImpl<MemberRechargeOrderMapper, MemberRechargeOrder> implements MemberRechargeOrderService {

    @Autowired
    private MemberWalletAddressService memberWalletAddressService;

    @Autowired
    private MemberBusinessWalletService businessWalletService;

    @Autowired
    private MemberCompanyWalletAddressService companyWalletAddressService;



    public void updateAllUp (int chainId) {
        MemberCompanyWalletAddress companyChain = companyWalletAddressService.getOne(new QueryWrapper<MemberCompanyWalletAddress>()
                .lambda()
                .eq(MemberCompanyWalletAddress::getChainId, chainId)
                .eq(MemberCompanyWalletAddress::getStatus,DataStatus.VALID.getCode()));
        if (companyChain == null) {
            log.error(chainId + "此公司公链不存在或被禁用");
            return;
        }
        List<MemberWalletAddress> list = memberWalletAddressService.list(new QueryWrapper<MemberWalletAddress>()
                .lambda()
                .eq(MemberWalletAddress::getStatus, DataStatus.VALID.getCode())
                .eq(MemberWalletAddress::getChainId, chainId));
        for (MemberWalletAddress walletAddress : list) {
            ICoinService coinService= Manager.getInstance().getService(walletAddress.getChainId());
            BigDecimal balance ; //
            // TODO：暂时 不查询链
            // TODO： balance=coinService.getBalance(memberRechargeOrder.getAddress());
            BigDecimal trx = coinService.getBalanceOfChain(walletAddress.getAddress());
            System.out.println("trx="+trx);
            if (trx.compareTo(new BigDecimal(0l)) == 1) {
                MemberWalletAddress updateTrx = new MemberWalletAddress();
                updateTrx.setId(walletAddress.getId());
                updateTrx.setEnergyValue(trx);
                memberWalletAddressService.updateById(updateTrx);
            }
            balance=coinService.getBalance(walletAddress.getAddress());
            if(balance.compareTo(BigDecimal.ZERO) <= 0) {//如果没有充值
                continue;
            }


            String hex= coinService.transfer(walletAddress.getAddress(),walletAddress.getPrivateKey()
                    ,companyChain.getAddress(),balance.multiply(new BigDecimal(100000l)).toBigInteger());
            /**
             * 保存
             */
            MemberRechargeOrder save = new MemberRechargeOrder();
            save.setMemberId(walletAddress.getMemberId());
            save.setChain(walletAddress.getChainName());
            save.setChainId(walletAddress.getChainId());
            save.setAddress(walletAddress.getAddress());
            save.setAddressId(walletAddress.getId());
            save.setStatus(DataStatus.VALID.getCode());
            save.setOrderStatus(TransStatus.underway.getCode());
            save.setRechargeStatus(RechargeStatus.succeed.getCode());
            save.setOrderStatus(TransStatus.underway.getCode());
            save.setMoney(balance);
            save.setCount(1);
            save.setTransactionHash(hex);
            save(save);

        }
    }

//    @Override
//    public void updateAllUp() {
//        log.info("开始 更新充值订单=================");
//        List<MemberRechargeOrder> list = list(new QueryWrapper<MemberRechargeOrder>()
//                .lambda()
//                .eq(MemberRechargeOrder::getStatus, 1) // 状态为正常
//                .eq(MemberRechargeOrder::getRechargeStatus, 0)); // 充值状态为未充值
//
//        log.info("更新充值订单-查询结果数量" + list.size());
//        for (MemberRechargeOrder memberRechargeOrder : list) {
//            ICoinService coinService= Manager.getInstance().getService(memberRechargeOrder.getChainId());
//            BigDecimal balance = new BigDecimal(100); //虚拟充值成功
//            // TODO：暂时 不查询链
//            // TODO： balance=coinService.getBalance(memberRechargeOrder.getAddress());
//            if(balance.compareTo(BigDecimal.ZERO) <= 0) {//如果没有充值
//                MemberRechargeOrder updateCount = new MemberRechargeOrder();
//                updateCount.setCount(memberRechargeOrder.getCount() + 1);
//                updateCount.setId(memberRechargeOrder.getId());
//                updateCount.setUpdateTime(new Date());
//                if (memberRechargeOrder.getCount() >= 19) { //如果 已经查询20次则逻辑删除
//                    updateCount.setStatus(DataStatus.DELETED.getCode());
//                }
//                //更新充值订单
//                updateById(updateCount);
//                continue;
//            }
//            MemberWalletAddress address = memberWalletAddressService.getOne(new QueryWrapper<MemberWalletAddress>()
//                    .lambda()
//                    .eq(MemberWalletAddress::getMemberId, memberRechargeOrder.getMemberId())
//                    .eq(MemberWalletAddress::getId, memberRechargeOrder.getAddressId()));
//            MemberRechargeOrder upOrder = new MemberRechargeOrder();
//            upOrder.setId(memberRechargeOrder.getId());
//            upOrder.setCount(memberRechargeOrder.getCount() + 1);
//            upOrder.setMoney(balance);
//            upOrder.setRechargeStatus(RechargeStatus.succeed.getCode());
//            //TODO: 暂时 不操作链
//            //TODO: String hex= coinService.transfer(memberRechargeOrder.getAddress(),address.getPrivateKey(),"统一地址",coinService.transferUsdt2BigInteger(balance));
//            // upOrder.setTransactionHash(hex);
//            upOrder.setOrderStatus(TransStatus.underway.getCode());
//            //更新充值订单
//            updateById(upOrder);
//        }
//        log.info("结束 更新充值订单=================");
//    }

    @Override
    public void updateAllTransferStatus() {
        log.info("开始 更新充值转账状态=================");
        List<MemberRechargeOrder> list = list(new QueryWrapper<MemberRechargeOrder>()
                .lambda()
                .eq(MemberRechargeOrder::getStatus, DataStatus.VALID.getCode()) // 状态为正常
                .eq(MemberRechargeOrder::getOrderStatus, TransStatus.underway.getCode())); // 正在转账中
        log.info("更新充值转账状态-查询结果数量：================="+list.size());
        for (MemberRechargeOrder memberRechargeOrder : list) {
            //TODO: 暂时不查询链
            //TODO: ICoinService coinService= Manager.getInstance().getService(memberRechargeOrder.getChain());
            //TODO: 查询交易状态
            // 修改转账状态 虚拟成功
            MemberRechargeOrder upOrder = new MemberRechargeOrder();
            upOrder.setId(memberRechargeOrder.getId());
            upOrder.setCount(memberRechargeOrder.getCount() + 1);
            upOrder.setOrderStatus(TransStatus.succeed.getCode());
            upOrder.setJobUpdateTime(new Date().toString());
            //更新充值订单
            updateById(upOrder);
            // 更新公链 数据
            memberWalletAddressService.addRechargeMoney(memberRechargeOrder.getAddressId(),memberRechargeOrder.getMoney());
            //TODO: 插入记录
            //TODO: 更新余额表
            boolean b = businessWalletService.updateAddBalance(memberRechargeOrder.getMemberId(), memberRechargeOrder.getMoney(),upOrder.getId() + "", BusinessSubType.RECHARGE_ADD, "充值增加余额：member_recharge_order");

        }
        log.info("结束 更新充值转账状态=================");
    }

}
