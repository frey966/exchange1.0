package com.financia.system.service.impl;

import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.constant.SecurityConstants;
import com.financia.common.core.constant.UserConstants;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.enums.VerificationCodeType;
import com.financia.common.core.utils.JwtUtils;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.redis.service.RedisService;
import com.financia.common.security.service.TokenService;
import com.financia.exchange.Member;
import com.financia.system.api.domain.SysUser;
import com.financia.system.api.model.LoginUser;
import com.financia.system.service.IMemberService;
import com.financia.system.service.MemberLoginService;
import com.financia.system.service.VerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 会员
 */
@Service
@Slf4j
public class MemberLoginServiceImpl implements MemberLoginService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private IMemberService memberService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private VerificationCodeService emailService;

    @Override
    @Transactional
    public AjaxResult LoginByCode(String code, long memberId){
        Member member = memberService.getById(memberId);
        if (member == null) {
            return AjaxResult.error("Abnormal data");
        }
        Object verifCode = redisService.getCacheObject(VerificationCodeType.LOGIN.getPrefix() + member.getEmail());
        if (verifCode == null || !code.equals(verifCode.toString())){
            return AjaxResult.error("The verification code is incorrect");
        }
        member.setPassword(null);
        LoginUser user = new LoginUser();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(member.getId());
        sysUser.setUserName(member.getUsername());
        user.setSysUser(sysUser);
        Map<String, Object> tokenMap = tokenService.createToken(user);
        Object token = tokenMap.get("access_token");
        member.setToken(token.toString());
        return AjaxResult.success(member);
    }

    @Override
    @Transactional
    public AjaxResult gsturePasswordSave(Long id, String password){
        Member member = memberService.getById(id);
        if (member == null) {
            return AjaxResult.error("The id is incorrect");
        }
        Member update = new Member();
        update.setId(member.getId());
        update.setGesturePassword(MD5Utils.md5Hex(password + member.getUid().toString(), "UTF-8"));
        update.setStatus(1);
        boolean b = memberService.updateById(update);
        if (b) {
            return AjaxResult.success(1);
        }
        return AjaxResult.error("update error");

    }

    @Override
    @Transactional
    public AjaxResult LoginByGesturePassword(String ip, Long id, String password){
        Member member = memberService.getById(id);
        if (member == null) {
            return AjaxResult.error("The account or password is incorrect");
        }
        if (member.getStatus() == DataStatus.DELETED.getCode()){
            return AjaxResult.error("The account or password is incorrect");
        }
        String md5Pwd = MD5Utils.md5Hex(password + member.getUid().toString(), "UTF-8");
        if (!md5Pwd.equals(member.getGesturePassword())){
            return AjaxResult.error("The user name or password is incorrect");
        }
        return loginSuccess(ip, member);
    }

    @Override
    @Transactional
    public AjaxResult closePasswordVerify(Long id, String password, String loginPassword){
        Member member = memberService.getById(id);
        if (member == null) {
            return AjaxResult.success("The password is incorrect");
        }
        if (member.getStatus() == DataStatus.DELETED.getCode()){
            return AjaxResult.success("The password is incorrect");
        }
        if (password != null) {
            String md5Pwd = MD5Utils.md5Hex(password + member.getUid().toString(), "UTF-8");
            if (!md5Pwd.equals(member.getGesturePassword())) {
                return AjaxResult.success(-1);
            }
        } else {
            String md5Pwd = MD5Utils.md5Hex(loginPassword + member.getUid().toString(), "UTF-8");
            if (!md5Pwd.equals(member.getPassword())) {
                return AjaxResult.success(-1);
            }
        }
        Member update = new Member();
        update.setGesture(0);
        update.setId(member.getId());
        update.setGesturePassword("");
        memberService.updateById(update);
        return AjaxResult.success(0);
    }

    @Override
    @Transactional
    public AjaxResult LoginByPassword(String ip, String userName, String password){
        Member member = memberService.getOne(new QueryWrapper<Member>()
                .lambda().eq(Member::getUsername, userName)
                .or().eq(Member::getEmail, userName)
                .or().eq(Member::getPhone, userName));
        if (member == null) {
            return AjaxResult.error("The account or password is incorrect");
        }
        if (member.getStatus() == DataStatus.DELETED.getCode()){
            return AjaxResult.error("The account or password is incorrect");
        }
        String md5Pwd = MD5Utils.md5Hex(password + member.getUid().toString(), "UTF-8");
        if (!md5Pwd.equals(member.getPassword())){
            return AjaxResult.error("The user name or password is incorrect");
        }
        return loginSuccess(ip, member);
    }

    @Override
    @Transactional
    public AjaxResult gestureSwitch(Long id , Integer isOpent){
        Member member = memberService.getById(id);
        if (member == null) {
            return AjaxResult.error("The idis incorrect");
        }
        Member update = new Member();
        update.setId(id);
        update.setGesture(isOpent);
        boolean b = memberService.updateById(update);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
    @NotNull
    private AjaxResult loginSuccess(String ip, Member member) {
        Member memberUpdate = new Member();
        memberUpdate.setId(member.getId());
        memberUpdate.setLastLoginTime(new Date());
        memberUpdate.setLoginIp(ip);
        memberService.updateById(memberUpdate);
        Map<String,Object> user = new HashMap<>();
        user.put(SecurityConstants.DETAILS_USER_ID,member.getId());
        user.put(SecurityConstants.DETAILS_USERNAME,member.getUsername());
        UUID uuid = UUID.randomUUID();
        redisService.setCacheObject(SecurityConstants.LOGIN_USER + uuid.toString(),user, 2l,TimeUnit.HOURS);
//        Map<String, Object> tokenMap = tokenService.createToken(user);
//        Object token = tokenMap.get("access_token");
        member.setToken(uuid.toString());
        member.setPassword(null);
        if (StringUtils.isEmpty(member.getJyPassword())) {
            member.setJyPassword("0");
        }else {
            member.setJyPassword("1");
        }
        return AjaxResult.success(member);
    }


    @Override
    @Transactional
    public AjaxResult login(Member member) {
        Member memberDb = memberService.getOne(new QueryWrapper<Member>().lambda()
                                                                    .eq(Member::getUsername, member.getUsername())
                                                                    .eq(Member::getStatus, DataStatus.VALID.getCode()));
        if (memberDb == null){
            return AjaxResult.error("The user name or password is incorrect");
        }
        String md5Pwd = MD5Utils.md5Hex(member.getPassword() + memberDb.getUid().toString(), "UTF-8");
        if (!md5Pwd.equals(memberDb.getPassword())){
            return AjaxResult.error("The user name or password is incorrect");
        }
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put("memberId", memberDb.getId());
        String token = JwtUtils.createToken(claimsMap);
        if (emailService.emailSendCode(member.getUsername(), VerificationCodeType.LOGIN)) {
            return AjaxResult.success(token);
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    /**
     * 查询 交易密码错误次数
     * @param memberId
     * @return
     */
    public Integer verifyJyPasswordFailureCount(Long memberId){
        Map<String, Object> cacheMap = redisService.getCacheMap(UserConstants.JY_PASSWORD_STATUS + memberId);
        if (cacheMap == null || cacheMap.size() == 0  || !LocalDate.now().toString().equals(cacheMap.get(UserConstants.JY_PASSWORD_STATUS_DATE))) {
            return 0;
        }
        return (Integer) cacheMap.get(UserConstants.JY_PASSWORD_STATUS_COUNT);
    }

    /**
     * 统一验证 交易密码 ，TODO: 增加次数限制
     * @param memberId
     * @param jyPwd
     * @return
     */
    @Override
    public boolean verifyJyPassword (Long memberId,String jyPwd) {
        Member member = memberService.getById(memberId);
        if (member == null) {
            return false;
        }
        String jyPasswordDb = member.getJyPassword();
        if (StringUtils.isEmpty(jyPasswordDb)) {
            return false;
        }
        String s = MD5Utils.md5Hex(jyPwd + member.getUid().toString(), "UTF-8");
        if (jyPasswordDb.equals(s)){
            return true;
        }
        recordJyPasswordFailureCount(memberId);
        return false;
    }

    /**
     * 记录会议输入错误密码次数
     * @param memberId
     */
    private void recordJyPasswordFailureCount(Long memberId) {
        Map<String, Object> cacheMap = redisService.getCacheMap(UserConstants.JY_PASSWORD_STATUS + memberId);

        if (cacheMap == null || cacheMap.size() == 0 || !LocalDate.now().toString().equals(cacheMap.get(UserConstants.JY_PASSWORD_STATUS_DATE).toString())) {
            Map<String,Object> jypasswordStatus = new HashMap<>();
            jypasswordStatus.put(UserConstants.JY_PASSWORD_STATUS_COUNT,1);
            jypasswordStatus.put(UserConstants.JY_PASSWORD_STATUS_DATE, LocalDate.now().toString());
            redisService.setCacheMap(UserConstants.JY_PASSWORD_STATUS+ memberId,jypasswordStatus);
        }else {
            Map<String,Object> jypasswordStatus = new HashMap<>();
            Integer count = (Integer) cacheMap.get(UserConstants.JY_PASSWORD_STATUS_COUNT);
            jypasswordStatus.put(UserConstants.JY_PASSWORD_STATUS_COUNT,count + 1);
            jypasswordStatus.put(UserConstants.JY_PASSWORD_STATUS_DATE, LocalDate.now().toString());
            redisService.setCacheMap(UserConstants.JY_PASSWORD_STATUS + memberId,jypasswordStatus);
        }
    }

    /**
     * 统一验证 登陆密码 ，TODO: 增加次数限制
     * @param memberId
     * @param loginPwd
     * @return
     */
    @Override
    public boolean verifyLoginPassword (Long memberId,String loginPwd) {
        Member member = memberService.getById(memberId);
        if (member == null) {
            return false;
        }
        String passwordDb = member.getPassword();
        if (StringUtils.isEmpty(passwordDb)) {
            return false;
        }
        String s = MD5Utils.md5Hex(loginPwd + member.getUid().toString(), "UTF-8");
        if (passwordDb.equals(s)){
            return true;
        }
        return false;
    }



}
