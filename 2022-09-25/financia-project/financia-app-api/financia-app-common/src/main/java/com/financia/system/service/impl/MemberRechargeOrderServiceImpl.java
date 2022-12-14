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
            log.error(chainId + "????????????????????????????????????");
            return;
        }
        List<MemberWalletAddress> list = memberWalletAddressService.list(new QueryWrapper<MemberWalletAddress>()
                .lambda()
                .eq(MemberWalletAddress::getStatus, DataStatus.VALID.getCode())
                .eq(MemberWalletAddress::getChainId, chainId));
        for (MemberWalletAddress walletAddress : list) {
            ICoinService coinService= Manager.getInstance().getService(walletAddress.getChainId());
            BigDecimal balance ; //
            // TODO????????? ????????????
            // TODO??? balance=coinService.getBalance(memberRechargeOrder.getAddress());
            BigDecimal trx = coinService.getBalanceOfChain(walletAddress.getAddress());
            System.out.println("trx="+trx);
            if (trx.compareTo(new BigDecimal(0l)) == 1) {
                MemberWalletAddress updateTrx = new MemberWalletAddress();
                updateTrx.setId(walletAddress.getId());
                updateTrx.setEnergyValue(trx);
                memberWalletAddressService.updateById(updateTrx);
            }
            balance=coinService.getBalance(walletAddress.getAddress());
            if(balance.compareTo(BigDecimal.ZERO) <= 0) {//??????????????????
                continue;
            }


            String hex= coinService.transfer(walletAddress.getAddress(),walletAddress.getPrivateKey()
                    ,companyChain.getAddress(),balance.multiply(new BigDecimal(100000l)).toBigInteger());
            /**
             * ??????
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
//        log.info("?????? ??????????????????=================");
//        List<MemberRechargeOrder> list = list(new QueryWrapper<MemberRechargeOrder>()
//                .lambda()
//                .eq(MemberRechargeOrder::getStatus, 1) // ???????????????
//                .eq(MemberRechargeOrder::getRechargeStatus, 0)); // ????????????????????????
//
//        log.info("??????????????????-??????????????????" + list.size());
//        for (MemberRechargeOrder memberRechargeOrder : list) {
//            ICoinService coinService= Manager.getInstance().getService(memberRechargeOrder.getChainId());
//            BigDecimal balance = new BigDecimal(100); //??????????????????
//            // TODO????????? ????????????
//            // TODO??? balance=coinService.getBalance(memberRechargeOrder.getAddress());
//            if(balance.compareTo(BigDecimal.ZERO) <= 0) {//??????????????????
//                MemberRechargeOrder updateCount = new MemberRechargeOrder();
//                updateCount.setCount(memberRechargeOrder.getCount() + 1);
//                updateCount.setId(memberRechargeOrder.getId());
//                updateCount.setUpdateTime(new Date());
//                if (memberRechargeOrder.getCount() >= 19) { //?????? ????????????20??????????????????
//                    updateCount.setStatus(DataStatus.DELETED.getCode());
//                }
//                //??????????????????
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
//            //TODO: ?????? ????????????
//            //TODO: String hex= coinService.transfer(memberRechargeOrder.getAddress(),address.getPrivateKey(),"????????????",coinService.transferUsdt2BigInteger(balance));
//            // upOrder.setTransactionHash(hex);
//            upOrder.setOrderStatus(TransStatus.underway.getCode());
//            //??????????????????
//            updateById(upOrder);
//        }
//        log.info("?????? ??????????????????=================");
//    }

    @Override
    public void updateAllTransferStatus() {
        log.info("?????? ????????????????????????=================");
        List<MemberRechargeOrder> list = list(new QueryWrapper<MemberRechargeOrder>()
                .lambda()
                .eq(MemberRechargeOrder::getStatus, DataStatus.VALID.getCode()) // ???????????????
                .eq(MemberRechargeOrder::getOrderStatus, TransStatus.underway.getCode())); // ???????????????
        log.info("????????????????????????-?????????????????????================="+list.size());
        for (MemberRechargeOrder memberRechargeOrder : list) {
            //TODO: ??????????????????
            //TODO: ICoinService coinService= Manager.getInstance().getService(memberRechargeOrder.getChain());
            //TODO: ??????????????????
            // ?????????????????? ????????????
            MemberRechargeOrder upOrder = new MemberRechargeOrder();
            upOrder.setId(memberRechargeOrder.getId());
            upOrder.setCount(memberRechargeOrder.getCount() + 1);
            upOrder.setOrderStatus(TransStatus.succeed.getCode());
            upOrder.setJobUpdateTime(new Date().toString());
            //??????????????????
            updateById(upOrder);
            // ???????????? ??????
            memberWalletAddressService.addRechargeMoney(memberRechargeOrder.getAddressId(),memberRechargeOrder.getMoney());
            //TODO: ????????????
            //TODO: ???????????????
            boolean b = businessWalletService.updateAddBalance(memberRechargeOrder.getMemberId(), memberRechargeOrder.getMoney(),upOrder.getId() + "", BusinessSubType.RECHARGE_ADD, "?????????????????????member_recharge_order");

        }
        log.info("?????? ????????????????????????=================");
    }

}
