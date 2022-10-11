package com.financia.system.controller;

import com.alibaba.nacos.common.utils.MD5Utils;
import com.financia.business.MemberBank;
import com.financia.common.core.constant.UserConstants;
import com.financia.common.core.enums.VerificationCodeType;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.utils.ValidateUtil;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.redis.service.RedisService;
import com.financia.exchange.Member;
import com.financia.system.service.IMemberBankService;
import com.financia.system.service.IMemberService;
import com.financia.system.service.MemberLoginService;
import com.financia.system.service.VerificationCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登陆
 */
@Api(tags = "APP会员信息")
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberUserController extends BaseController {

    @Autowired
    private IMemberService memberService;

    @Autowired
    private MemberLoginService loginService;

    @Autowired
    private IMemberBankService memberBankService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private RedisService redisService;


    @GetMapping(value = "/userInfoById")
    @ApiOperation(value = "会员信息查询", notes = "会员信息查询")
    public Member userInfoById(Long id) {
        Member byId = memberService.getById(id);
        byId.setPassword(null);
        byId.setGesturePassword(null);
        return byId;
    }

    @GetMapping(value = "/jyPasswordFailureCount")
    @ApiOperation(value = "交易密码错误次数", notes = "交易密码错误次数")
    public AjaxResult jyPasswordFailureCount(Long id) {
        Long userId = getUserId();
        if (userId == null) {
            error("is not login");
        }
        Integer integer = loginService.verifyJyPasswordFailureCount(userId);
        Map<String, Object> rs = new HashMap<>();
        rs.put("count", integer);
        if (UserConstants.JY_PASSWORD_LOGIN_MAX <= integer) {
            rs.put("isLogin", false);
        } else {
            rs.put("isLogin", true);
        }
        return AjaxResult.success(rs);
    }

    @PostMapping(value = "/setJyPassword")
    @ApiOperation(value = "设置支付密码", notes = "设置支付密码")
    public AjaxResult setJyPassword(@RequestBody Map<String, String> map) {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(map.get("password"))) {
            return error("password can not be blank");
        }
        String payPassword = MD5Utils.md5Hex(map.get("password") + member.getUid().toString(), "UTF-8");

        if (!StringUtils.isEmpty(member.getJyPassword())) {
            if (member.getJyPassword().equals(payPassword)) {
                return error("The new password cannot be the same as the old password, please reset.");
            }
        }
        if (StringUtils.isEmpty(member.getPhone()) && StringUtils.isEmpty(member.getEmail())) {
            return error("Please bind the verification method");

        }
        if (!StringUtils.isEmpty(member.getEmail())) {
            if (!verificationCodeService.verify(map.get("emailVerificationCode"), VerificationCodeType.SET_PAYPASSWORD, member.getEmail())) {
                return AjaxResult.error("The verification EmailCode is incorrect");
            }
        }
        if (!StringUtils.isEmpty(member.getPhone())) {
            if (!verificationCodeService.verify(map.get("phoneVerificationCode"), VerificationCodeType.SET_PAYPASSWORD, member.getPhone())) {
                return AjaxResult.error("The verification PhoneCode is incorrect");
            }
        }

        Member update = new Member();
        update.setJyPassword(payPassword);
        update.setId(member.getId());
        if (memberService.updateById(update)) {
            String key = "enableTrade" + member.getId();
            redisService.setCacheObject(key, false, 24l, TimeUnit.HOURS);
            return success();
        }
        return error("Failed to set password");
    }

    @PostMapping(value = "/changeLoginPassword")
    @ApiOperation(value = "修改登陆密码", notes = "修改登陆密码{password: '老密码',newPassword: '新密码'}")
    public AjaxResult changeLoginPassword(@RequestBody Map<String, String> param) {
        Long userId = getUserId();
        if (StringUtils.isEmpty(param.get("password"))) {
            return error("password can not be blank");
        }
        if (StringUtils.isEmpty(param.get("newPassword"))) {
            return error("newPassword can not be blank");
        }
        if (loginService.verifyLoginPassword(userId, param.get("password"))) {
            Member member = memberService.getById(userId);
            String newPassword = MD5Utils.md5Hex(param.get("newPassword") + member.getUid().toString(), "UTF-8");
            if (newPassword.equals(member.getPassword())) {
                return error("The new password cannot be the same as the old password");
            }
            Member update = new Member();
            update.setPassword(newPassword);
            update.setId(member.getId());
            if (memberService.updateById(update)) {
                return AjaxResult.success(1);
            }
            return error("Failed to set password");
        } else {
            return error("The original password is wrong");
        }
    }

    @PostMapping(value = "/updatelanguage")
    @ApiOperation(value = "个人中心-修改当前语言/法币", notes = "修改当前语言/法币")
    public AjaxResult updatelanguage(@RequestBody Member member) {
        int num = memberService.updateMember(member);
        if (num > 0) {
            return AjaxResult.success(member.getDefaultLanguage());
        } else {
            return AjaxResult.error("更改用户语言失败！");
        }

    }

    @PostMapping(value = "/insertUserBank")
    @ApiOperation(value = "个人中心-添加用户银行卡", notes = "添加用户银行卡")
    public AjaxResult insertUserBank(@RequestBody MemberBank memberBank) {
        int num = memberBankService.insertMemberBank(memberBank);
        if (num > 0) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error("添加用户银行卡失败！");
        }

    }

    @PostMapping(value = "/userBankList")
    @ApiOperation(value = "个人中心-用户银行卡列表", notes = "用户银行卡列表")
    public TableDataInfo list(@RequestBody MemberBank memberBank) {
        startPage();
        List<MemberBank> list = memberBankService.selectMemberBankList(memberBank);
        return getDataTable(list);
    }

    @GetMapping("remove/{ids}")
    @ApiOperation(value = "个人中心-用户银行卡删除", notes = "用户银行卡删除")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(memberBankService.deleteMemberBankByIds(ids));
    }

    @PostMapping(value = "/updateMemberBank")
    @ApiOperation(value = "个人中心-用户银行卡编辑/置顶", notes = "用户银行卡编辑/置顶")
    public AjaxResult updateMemberBank(@RequestBody MemberBank memberBank) {
        return AjaxResult.success(memberBankService.updateMemberBank(memberBank));
    }

    @GetMapping(value = "/setPayPassword/sendEmailCode")
    @ApiOperation(value = "设置支付密码-发送邮箱验证码", notes = "设置支付密码发送邮箱验证码")
    public AjaxResult sendEmailCode() {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(member.getEmail()) || !ValidateUtil.isEmail(member.getEmail())) {
            return AjaxResult.error("Incorrect email address");
        }
        if (verificationCodeService.emailSendCode(member.getEmail(), VerificationCodeType.SET_PAYPASSWORD)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @GetMapping(value = "/setPayPassword/sendPhoneCode")
    @ApiOperation(value = "设置支付密码-发送手机证码", notes = "设置支付密码发送手机验证码")
    public AjaxResult sendPhoneCode() {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(member.getPhone())) {
            return AjaxResult.error("Incorrect phone");
        }
        if (verificationCodeService.phoneSendCode(member.getPhone(), VerificationCodeType.SET_PAYPASSWORD)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @GetMapping(value = "/bindPhone/sendPhoneCode")
    @ApiOperation(value = "绑定手机-发送手机证码", notes = "绑定手机发送手机验证码")
    public AjaxResult sendPhoneCode(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("Incorrect phone");
        }
        if (verificationCodeService.phoneSendCode(phone, VerificationCodeType.BIND_PHONE)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @GetMapping(value = "/bindEmail/sendEmailCode")
    @ApiOperation(value = "绑定邮箱-发送邮箱验证码", notes = "绑定邮箱-发送邮箱验证码")
    public AjaxResult sendEmailCode(String email) {
        if (StringUtils.isEmpty(email)) {
            return AjaxResult.error("Incorrect phone");
        }

        if (verificationCodeService.emailSendCode(email, VerificationCodeType.BIND_EMAIL)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @PostMapping(value = "/bindPhone")
    @ApiOperation(value = "绑定手机", notes = "绑定手机")
    public AjaxResult bindPhone(@RequestBody Map<String, String> map) {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(map.get("phone"))) {
            return error("phone can not be blank");
        }
        if (!StringUtils.isEmpty(member.getPhone())) {
            return error("Phone is bound.");

        }
        Member phoneMember = memberService.getMemberByPhone(map.get("phone"));
        if (phoneMember != null) {
            return error("Phone is bound.");
        }
        if (StringUtils.isEmpty(map.get("code"))) {
            return error("code can not be blank");
        }

        if (!verificationCodeService.verify(map.get("code"), VerificationCodeType.BIND_PHONE, map.get("phone"))) {
            return AjaxResult.error("The verification PhoneCode is incorrect");
        }


        Member update = new Member();
        update.setPhone(map.get("phone"));
        update.setId(member.getId());
        if (memberService.updateById(update)) {
            return success();
        }
        return error("Failed to bind Phone");
    }

    @PostMapping(value = "/bindEmail")
    @ApiOperation(value = "绑定邮箱", notes = "绑定邮箱")
    public AjaxResult bindEmail(@RequestBody Map<String, String> map) {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(map.get("email"))) {
            return error("email can not be blank");
        }
        if (!StringUtils.isEmpty(member.getEmail())) {
            return error("email is bound.");
        }
        Member emailMember = memberService.getMemberByEmail(map.get("email"));
        if (emailMember != null) {
            return error("email is bound.");
        }

        if (!verificationCodeService.verify(map.get("code"), VerificationCodeType.BIND_EMAIL, map.get("email"))) {
            return AjaxResult.error("The verification EmailCode is incorrect");
        }
        Member update = new Member();
        update.setEmail(map.get("email"));
        update.setId(member.getId());
        if (memberService.updateById(update)) {
            return success();
        }
        return error("Failed to bind Email");
    }


    @GetMapping(value = "/updatePhone/sendPhoneCode")
    @ApiOperation(value = "解除绑定手机-发送手机证码", notes = "解除绑定手机发送手机验证码")
    public AjaxResult sendUpdatePhoneCode() {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(member.getPhone())) {
            return AjaxResult.error("Incorrect phone");
        }
        if (verificationCodeService.phoneSendCode(member.getPhone(), VerificationCodeType.UPDATE_PHONE)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @GetMapping(value = "/updateEmail/sendEmailCode")
    @ApiOperation(value = "解除绑定邮箱-发送邮箱验证码", notes = "解除绑定邮箱-发送邮箱验证码")
    public AjaxResult sendUpdateEmailCode() {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(member.getEmail()) || !ValidateUtil.isEmail(member.getEmail())) {
            return AjaxResult.error("Incorrect email address");
        }
        if (verificationCodeService.emailSendCode(member.getEmail(), VerificationCodeType.UPDATE_EMAIL)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @GetMapping(value = "/updatePhone/verifyPhoneCode")
    @ApiOperation(value = "解除绑定手机-效验验证码", notes = "解除绑定手机-效验验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", dataTypeClass = String.class, required = true, paramType = "query", example = "12749"),
    })
    public AjaxResult verifyPhoneCode(String code) {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (!verificationCodeService.verify(code, VerificationCodeType.UPDATE_PHONE, member.getPhone())) {
            return AjaxResult.error("The verification PhoneCode is incorrect");
        }
        // 此处为了防止直接跳过旧手机验证，用redis记录验证状态，在修改手机和邮箱时候验证,暂定5分钟
        String key = member.getId() + member.getPhone();
        redisService.setCacheObject(key, true, 300l, TimeUnit.SECONDS);
        return AjaxResult.success();
    }

    @GetMapping(value = "/updateEmail/verifyEmailCode")
    @ApiOperation(value = "解除绑定邮箱-效验验证码", notes = "解除绑定邮箱-效验验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", dataTypeClass = String.class, required = true, paramType = "query", example = "12749"),
    })
    public AjaxResult verifyEmailCode(String code) {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        if (StringUtils.isEmpty(member.getEmail()) || !ValidateUtil.isEmail(member.getEmail())) {
            return AjaxResult.error("Incorrect email address");
        }
        if (!verificationCodeService.verify(code, VerificationCodeType.UPDATE_EMAIL, member.getEmail())) {
            return AjaxResult.error("The verification EmailCode is incorrect");
        }
        // 此处为了防止直接跳过旧邮箱验证，用redis记录验证状态，在修改手机和邮箱时候验证,暂定5分钟
        String key = member.getId() + member.getEmail();
        redisService.setCacheObject(key, true, 300l, TimeUnit.SECONDS);
        return AjaxResult.success();
    }


    @PostMapping(value = "/updatePhone")
    @ApiOperation(value = "修改手机", notes = "修改手机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", dataTypeClass = String.class, required = true, paramType = "query", example = "12749"),
            @ApiImplicitParam(name = "phone", value = "新手机", dataTypeClass = String.class, required = true, paramType = "query", example = "971565483042"),
    })
    public AjaxResult updatePhone(@RequestBody Map<String, String> map) {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        String key = member.getId() + member.getPhone();
        Boolean verifyPhoneCode = redisService.getCacheObject(key);
        if (verifyPhoneCode == null || !verifyPhoneCode) {
            return error("illegal request");
        }

        if (StringUtils.isEmpty(map.get("phone"))) {
            return error("New phone cannot be empty");
        }
        Member phoneMember = memberService.getMemberByPhone(map.get("phone"));
        if (phoneMember != null) {
            return error("Phone is bound.");
        }

        if (!StringUtils.isEmpty(member.getPhone())) {
            if (!verificationCodeService.verify(map.get("code"), VerificationCodeType.BIND_PHONE, map.get("phone"))) {
                return AjaxResult.error("The verification PhoneCode is incorrect");
            }
        }

        Member update = new Member();
        if (member.getUsername().equals(member.getPhone())){
            update.setUsername(map.get("phone"));
        }
        update.setPhone(map.get("phone"));
        update.setId(member.getId());
        if (memberService.updateById(update)) {
            return success();
        }
        return error("Failed to update phone");
    }

    @PostMapping(value = "/updateEmail")
    @ApiOperation(value = "修改邮箱", notes = "修改邮箱")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "验证码", dataTypeClass = String.class, required = true, paramType = "query", example = "12749"),
            @ApiImplicitParam(name = "email", value = "新邮箱", dataTypeClass = String.class, required = true, paramType = "query", example = "xxx@gmail.com"),
    })
    public AjaxResult updateEmail(@RequestBody Map<String, String> map) {
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        String key = member.getId() + member.getEmail();
        Boolean verifyEmailCode = redisService.getCacheObject(key);
        if (verifyEmailCode == null || !verifyEmailCode) {
            return error("illegal request");
        }
        if (StringUtils.isEmpty(map.get("email"))) {
            return error("New email cannot be empty");
        }
        Member emailMember = memberService.getMemberByEmail(map.get("email"));
        if (emailMember != null) {
            return error("email is bound.");
        }
        if (!verificationCodeService.verify(map.get("code"), VerificationCodeType.BIND_EMAIL, map.get("email"))) {
            return AjaxResult.error("The verification EmailCode is incorrect");
        }
        Member update = new Member();
        update.setEmail(map.get("email"));
        if (member.getUsername().equals(member.getEmail())){
            update.setUsername(map.get("email"));
        }
        update.setId(member.getId());
        if (memberService.updateById(update)) {
            return success();
        }
        return error("Failed to update email");
    }

    @PostMapping(value = "/updateInfo")
    @ApiOperation(value = "修改会员信息", notes = "修改会员信息")
    public AjaxResult updateInfo(@RequestBody Member member) {
        Long userId = getUserId();
        Member member1 = new Member();
        member1.setId(userId);
        member1.setAvatar(member.getAvatar());
        memberService.updateById(member1);
        return success();
    }

    @PostMapping(value = "/defaultLegal")
    @ApiOperation(value = "默认法币类型", notes = "默认法币类型")
    public AjaxResult defaultLegal(@RequestBody Member member) {
        Long userId = getUserId();
        Member member1 = new Member();
        member1.setId(userId);
        member1.setDefaultLegal(member.getDefaultLegal());
        memberService.updateById(member1);
        return success();
    }

}
