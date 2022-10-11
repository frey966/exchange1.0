package com.financia.system.service.impl;

import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.enums.VerificationCodeType;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.utils.uuid.GeneratorUtil;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.AgenRelation;
import com.financia.exchange.Member;
import com.financia.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

/**
 * 会员
 */
@Service
@Slf4j
public class MemberRegisterServiceImpl implements MemberRegisterService {

    @Autowired
    private IMemberService memberService;
    @Autowired
    private VerificationCodeService verificationCodeService;
    @Autowired
    private AgenRelationService agenRelationService;
    @Autowired
    private MemberWalletAddressService walletAddressService;
    @Autowired
    private MemberBusinessWalletService businessWalletService;
    @Autowired
    private MemberWalletNationalCurrencyService memberWalletNationalCurrencyService;

    @Override
    @Transactional
    public AjaxResult registerByEmail (Member member){
        if (!verificationCodeService.verify(member.getVerificationCode(),VerificationCodeType.REGISTER,member.getEmail())){
            return AjaxResult.error("The verification code is incorrect");
        }
        long memberCount = memberService.count(new QueryWrapper<Member>().lambda().eq(Member::getEmail, member.getEmail()));
        if (memberCount > 0){
            return AjaxResult.error("Email has been registered.");
        }
        return register(member);
    }

    @Override
    @Transactional
    public AjaxResult registerByPhone (Member member){
        if (!verificationCodeService.verify(member.getVerificationCode(),VerificationCodeType.REGISTER,member.getPhone())){
            return AjaxResult.error("The verification code is incorrect");
        }
        long memberCount = memberService.count(new QueryWrapper<Member>().lambda().eq(Member::getPhone, member.getPhone()));
        if (memberCount > 0){
            return AjaxResult.error("phone has been registered.");
        }
        return register(member);
    }


    @Override
    @Transactional
    public AjaxResult registerByUserName (Member member){
        long memberCount = memberService.count(new QueryWrapper<Member>().lambda().eq(Member::getUsername, member.getUsername()));
        if (memberCount > 0){
            return AjaxResult.error("userName has been registered.");
        }
        return register(member);
    }

    @Transactional
    public AjaxResult register(Member member) {
        //TODO: 邀请人查询
        Member save = new Member();
        String inviteCode = member.getInviteCode();
        if (!StringUtils.isEmpty(inviteCode)) { //如果存在邀请码
            AgenRelation agenRelation = agenRelationService.getOne(new QueryWrapper<AgenRelation>().lambda().eq(AgenRelation::getInviteCode, inviteCode));
            if (agenRelation != null) { // 是否存在 代理信息
                save.setInviteCode(agenRelation.getInviteCode());
                save.setAgenRelationId(agenRelation.getId());
            }else {
                // 会员 邀请
                Member one = memberService.getOne(new QueryWrapper<Member>().lambda().eq(Member::getInviteCode, inviteCode));
                if (one == null) {
                    return AjaxResult.error("InviteCode does not exist");
                }
                save.setInviteId(one.getId());
            }
        }
        boolean isExist = true;
        long randomNumber = 0l;
        while (isExist){
            randomNumber = GeneratorUtil.getRandomNumber(1000000, 9999999);
            long count = memberService.count(new QueryWrapper<Member>().lambda().eq(Member::getUid, randomNumber));
            if (count == 0) {
                isExist = false;
            }
        }
        //生成 邀请码
        boolean isExist2 = true;
        long saveInviteCode = 0l;
        while (isExist2){
            saveInviteCode = GeneratorUtil.getRandomNumber(1000000, 9999999);
            long count = memberService.count(new QueryWrapper<Member>().lambda().eq(Member::getInviteCode, saveInviteCode));
            if (count == 0) {
                isExist2 = false;
            }
        }
        save.setUid(randomNumber);
        //密码：（明文+uid）MD5
        save.setPassword(MD5Utils.md5Hex(member.getPassword() + save.getUid().toString(),"UTF-8"));
        save.setEmail(member.getEmail());
        save.setUsername(member.getUsername());
        save.setPhone(member.getPhone());
        save.setInviteCode(saveInviteCode + "");
        save.setCreateTime(new Date());
        save.setUpdateTime(new Date());
        if (memberService.save(save)) {
            //添加 钱包地址
            //TODO: 创建钱包
            boolean isSaveWallet = walletAddressService.createWalletAddress(save);
            if (!isSaveWallet) {
                //回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return AjaxResult.error("Failed to create the wallet address. Procedure");
            }
            boolean wallet = businessWalletService.createWallet(save.getId());
            if (wallet) {
                //回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return AjaxResult.error("Failed to create the wallet . Procedure");
            }
            // 此处暂时不加入数字货币钱包，有币币交易创建时，币币交易会生成数字货币钱包数据
//            if (!memberWalletCryptoCurrencyService.createWalletCryptoCurrency(save.getId())) {
//                //回滚
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                return AjaxResult.error("Failed to create the CryptoCurrency . Procedure");
//            }
            if (!memberWalletNationalCurrencyService.createWalletNationalCurrency(save.getId())) {
                //回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return AjaxResult.error("Failed to create the NationalCurrency . Procedure");
            }
            return AjaxResult.success();
        }
        return AjaxResult.error("Registration failed");
    }


//    @Override
//    public boolean emailExist(String email){
//        int memberCount = memberService.count(new QueryWrapper<Member>().lambda().eq(Member::getEmail, email));
//        if (memberCount > 0){
//            return false;
//        }
//        return true;
//    }

//    @Override
//    public boolean phoneExist(String phone){
//        int memberCount = memberService.count(new QueryWrapper<Member>().lambda().eq(Member::getPhone, phone));
//        if (memberCount > 0){
//            return false;
//        }
//        return true;
//    }

    @Override
    public boolean userExist(String userName) {
        long memberCount = memberService.count(new QueryWrapper<Member>()
                .lambda().eq(Member::getUsername, userName)
                .or().eq(Member::getEmail, userName)
                .or().eq(Member::getPhone, userName));
        if (memberCount > 0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional
    public boolean changePassword (Long memberId,String uid, String password){
        Member member = new Member();
        member.setId(memberId);
        member.setPassword(MD5Utils.md5Hex(password + uid,"UTF-8"));
        boolean b = memberService.updateById(member);
        if (b) {
            return true;
        }
        return false;
    }
}
