package com.financia.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.constant.UserConstants;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.Member;
import com.financia.exchange.MemberCompanyWalletAddress;
import com.financia.exchange.MemberWithdrawOrder;
import com.financia.system.crypto.Manager;
import com.financia.system.crypto.service.ICoinService;
import com.financia.system.mapper.MemberWithdrawOrderMapper;
import com.financia.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("memberWithdrawOrderService")
public class MemberWithdrawOrderServiceImpl extends ServiceImpl<MemberWithdrawOrderMapper, MemberWithdrawOrder> implements MemberWithdrawOrderService {
    @Autowired
    private IMemberService memberService;
    @Autowired
    private MemberLoginService memberLoginService;
    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;
    @Autowired
    private MemberCompanyWalletAddressService companyWalletAddressService;

    /**
     * 申请提现
     * @param memberId
     * @param remark
     * @param money
     * @param jyPassword
     * @return
     */
    @Override
    @Transactional
    public AjaxResult apply (Long memberId,String remark, BigDecimal money, String jyPassword,Integer chainId,String address) {
        Member byId = memberService.getById(memberId);
        if (byId == null) {
            return AjaxResult.error("Member ID error");
        }
        //验证用户失败多少次
        Integer vCount = memberLoginService.verifyJyPasswordFailureCount(memberId);
        Map<String,Object> rs = new HashMap<>();
        if (vCount >= UserConstants.JY_PASSWORD_LOGIN_MAX) {
            return AjaxResult.error("Password lock");
        }
        if (!memberLoginService.verifyJyPassword(memberId, jyPassword)) {
            rs.put("busCode",-1);
            rs.put("count", vCount + 1);
            return AjaxResult.success(rs);
        }
        MemberCompanyWalletAddress companyChain = companyWalletAddressService.getOne(new QueryWrapper<MemberCompanyWalletAddress>()
                .lambda()
                .eq(MemberCompanyWalletAddress::getChainId, chainId)
                .eq(MemberCompanyWalletAddress::getStatus, DataStatus.VALID.getCode()));
        if (companyChain == null) {
            return AjaxResult.error("此公司公链不存在或被禁用");
        }
        //插入 提现申请
        MemberWithdrawOrder saveOrder = new MemberWithdrawOrder();
        saveOrder.setChain("");
        saveOrder.setChainId(chainId);
        saveOrder.setFromAddress(companyChain.getAddress());
        saveOrder.setToAddress(address);
        saveOrder.setMemberId(memberId);
        saveOrder.setRemark(remark);
        saveOrder.setMoney(money);
        saveOrder.setPoundage(companyChain.getHandlingFee());
        saveOrder.setOrderStatus(0);
        saveOrder.setCheckStatus(0);
        saveOrder.setStatus(DataStatus.VALID.getCode());
        //保存会员订单流程
        if (!save(saveOrder)) {
            return AjaxResult.error("Save failed");
        }
        AjaxResult ajaxResult = memberBusinessWalletService.updateFreezeBalance(memberId, money.add(saveOrder.getPoundage()),saveOrder.getId() + "", BusinessSubType.WITHDRAW_FREEZE,"");
        if (ajaxResult.isSuccess()) {
            if (money.compareTo(companyChain.getWithdrowAmount()) == -1) { // 如果小于审核阀值 自动审核
                ICoinService coinService = Manager.getInstance().getService(chainId);
                BigDecimal balance = coinService.getBalance(companyChain.getAddress());
                if (money.compareTo(balance) == 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return AjaxResult.error("Insufficient account balance");
                }
                BigDecimal balanceOfChain = coinService.getBalanceOfChain(companyChain.getAddress());
                if (balanceOfChain.compareTo(new BigDecimal(1l)) == -1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return AjaxResult.error("Insufficient account trx");
                }
                String hex = coinService.transfer(companyChain.getAddress(), companyChain.getPrivateKey()
                        , address, money.multiply(new BigDecimal(100000l)).toBigInteger());
                MemberWithdrawOrder updateHex = new MemberWithdrawOrder();
                updateHex.setId(saveOrder.getId());
                updateHex.setHex(hex);
                updateHex.setCheckStatus(3);
                updateHex.setOrderStatus(2);
                //修改 hex
                updateById(updateHex);
            }
            rs.put("busCode",1);
            return AjaxResult.success(rs);
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ajaxResult;

    }

    /**
     * 循环查询已经发起提现的订单
     */
    public void updateWithdraw() {
        //查询已经发起的提现转账单
        List<MemberWithdrawOrder> list = list(new QueryWrapper<MemberWithdrawOrder>().lambda().eq(MemberWithdrawOrder::getOrderStatus, 2));
        for (MemberWithdrawOrder memberWithdrawOrder : list) {
            //TODO: 查询转账情况
            MemberWithdrawOrder updateStatus = new MemberWithdrawOrder();
            updateStatus.setId(memberWithdrawOrder.getId());
            updateStatus.setOrderStatus(1);
            updateStatus.setWithdrawStatus(1);
            updateStatus.setJobUpdateTime(new Date()+"");
            updateById(updateStatus);
            AjaxResult ajaxResult = memberBusinessWalletService.updateSubFreezeBalance(memberWithdrawOrder.getMemberId(),
                    memberWithdrawOrder.getMoney().add(memberWithdrawOrder.getPoundage()),memberWithdrawOrder.getId()+"",
                    BusinessSubType.WITHDRAW_DEDUCT,"提现冻结扣款 : member_withdraw_order");
        }
    }
}
