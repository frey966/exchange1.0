package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.Member;
import com.financia.exchange.MemberRechargeRecordTypeEnum;
import com.financia.exchange.mapper.MemberRechargeRecordMapper;
import com.financia.exchange.service.MemberRechargeRecordService;
import com.financia.swap.MemberRechargeRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class MemberRechargeRecordServiceImpl extends ServiceImpl<MemberRechargeRecordMapper, MemberRechargeRecord> implements MemberRechargeRecordService {

//    @Autowired
//    private MemberService memberService;

    @Override
    public MemberRechargeRecord saveMemberRechargeRecord(Long memberId, BigDecimal rechargeAmount, Integer rechargeType) {
//        MemberRechargeRecord memberRecharge = new MemberRechargeRecord();
//        BigDecimal totalBalance = totalBalance(memberId);//总资产
//        Member member = memberService.findOne(memberId);
//        if (member.getBetScale() == null || member.getBetScale().compareTo(BigDecimal.ONE) == -1) {
//            member.setBetScale(BigDecimal.ONE);
//        }
//        BigDecimal multiply = BigDecimal.ZERO;
//        if (rechargeType.equals(MemberRechargeRecordTypeEnum.OPTIONS.getCode()) ||
//                rechargeType.equals(MemberRechargeRecordTypeEnum.BIBI_BUY.getCode()) ||
//                rechargeType.equals(MemberRechargeRecordTypeEnum.CONTRACT.getCode()) ||
//                rechargeType.equals(MemberRechargeRecordTypeEnum.GAME_BET.getCode())) {
//            rechargeAmount = rechargeAmount.negate();//如果是投注就将金额转为负数
//            multiply = rechargeAmount;
//        }else {
//            multiply = rechargeAmount.multiply(member.getBetScale());//冲减金额 * 会员打码比例
//        }
//        memberRecharge.setMemberId(memberId);
//        memberRecharge.setRechargeAmount(rechargeAmount);
//        memberRecharge.setRechargeType(rechargeType);
//        memberRecharge.setRechargeTime(new Date());
//        memberRecharge.setBetScale(member.getBetScale());
//        memberRecharge.setTotalBalance(totalBalance.add(rechargeAmount).compareTo(BigDecimal.ZERO) == -1?BigDecimal.ZERO:totalBalance.add(rechargeAmount));
//        MemberRechargeRecord memberRechargeRecord = this.queryByRechargeRecord(memberId);
//        BigDecimal remainingRequiredBetAmount = multiply.add(memberRechargeRecord == null ? BigDecimal.ZERO : memberRechargeRecord.getRemainingRequiredBetAmount());//再加上剩余打码量
//        if (remainingRequiredBetAmount.compareTo(BigDecimal.ZERO) == -1) {
//            remainingRequiredBetAmount = BigDecimal.ZERO;
//        }
//        memberRecharge.setRemainingRequiredBetAmount(remainingRequiredBetAmount);
//        BigDecimal subtract = totalBalance.subtract(remainingRequiredBetAmount);//可提现余额 = 总钱包余额 - 剩余打码量
//        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>总钱包减去剩余打码量[{}]-[{}]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<",totalBalance,remainingRequiredBetAmount);
//        if (subtract.compareTo(BigDecimal.ZERO) == -1) {
//            subtract = BigDecimal.ZERO;
//        }
//        memberRecharge.setAmountCanBeWithdrawn(subtract);
//        memberRecharge.setRechargeRatio(multiply);//打码量
//        try {
//            memberRecharge = this.save(memberRecharge);
//            log.info("memberId:[{}],充值类型:[{}],金额:[{}]", memberId, rechargeType,rechargeAmount);
//        } catch (Exception e) {
//            log.error("打码量记录保存失败,memberId:{},充值类型：{},金额{},{}", memberId, rechargeType,rechargeAmount,e);
//            e.printStackTrace();
//        }
//        return memberRecharge;
        return null;
    }


    /**
     * 会员总资产
     * @param memberId
     * @return
     */
//    public BigDecimal totalBalance(Long memberId){
//        String serviceName = "SWAP-API";
//        String url = "http://" + serviceName + "/swap/order/sumWallet?memberId="+memberId;
//        ResponseEntity<BigDecimal> result = restTemplate.getForEntity(url,BigDecimal.class);
//        if (null == result){
//            log.error("远程RPC调用获取合约钱包获取失败");
//        }
//        BigDecimal sumWallet = BigDecimal.ZERO;
//        if (result.getStatusCode().value() == 200) {
//            sumWallet = result.getBody();//远程RPC调用获取合约钱包
//        }
//        List<MemberWallet> memberWallet = memberWalletService.findAllByMemberId(memberId);
//        Map<Object, Object> maps = redisUtil.hmget(RedisConstant.REDIS_COIN_RATE);
//        if (CollectionUtils.isEmpty(maps)){
//            log.error("会员总资产bibi钱包获取汇率失败");
//        }
//        List<BigDecimal> list = new ArrayList<>();
//        for (MemberWallet wallet:memberWallet) {
//            for (Object key : maps.keySet()) {
//                Object val = maps.get(key);
//                if (wallet.getCoin().getUnit().equals(key)){
//                    BigDecimal sum = wallet.getBalance().add(wallet.getFrozenBalance());
//                    BigDecimal multiply = sum.multiply(new BigDecimal(val.toString()));
//                    list.add(multiply);
//                    break;
//                }
//            }
//        }
//        BigDecimal sumBiBiWallet = list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);//币币钱包总余额
//        MemberTradeWallet memberTradeWallet = memberTradeWalletService.findByUnitAndMemberId("usdt", memberId);//游戏钱包
//        BigDecimal sumMemberTradeWallet = memberTradeWallet.getBalance().add(memberTradeWallet.getFrozenBalance());
//        BigDecimal totalBalance = sumMemberTradeWallet.add(sumBiBiWallet).add(sumWallet);
//        log.info(">>>>>>>>>>>>>>>>>币币[{}]游戏[{}]合约[{}]总和[{}]<<<<<<<<<<<<<<<<<<<<",sumBiBiWallet,memberTradeWallet.getBalance(),sumWallet,totalBalance);
//        return totalBalance;
//    }




}
