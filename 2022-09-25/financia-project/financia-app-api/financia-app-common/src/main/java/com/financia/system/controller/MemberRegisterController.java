package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.enums.VerificationCodeType;
import com.financia.common.core.utils.JwtUtils;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.utils.ValidateUtil;
import com.financia.common.core.utils.i18n.LocaleMessageSourceUtil;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.Member;
import com.financia.system.service.IMemberService;
import com.financia.system.service.MemberRegisterService;
import com.financia.system.service.VerificationCodeService;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**

 */
@Api(tags="APP注册模块")
@RestController
@RequestMapping("/member")
public class MemberRegisterController extends BaseController
{

    @Autowired
    private MemberRegisterService memberRegisterService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private LocaleMessageSourceUtil sourceService;

    @PostMapping(value = "/emailRegister")
    @ApiOperation(value = "邮箱注册-邮箱注册会员",notes = "App邮箱注册会员")
    public AjaxResult emailRegister(@RequestBody Member member)
    {
        if (member == null){
            return AjaxResult.error(sourceService.getMessage("the_parameter_cannot_be_empty"));
        }
        if (StringUtils.isEmpty(member.getEmail())) {
            return AjaxResult.error(sourceService.getMessage("the_mailbox_cannot_be_empty"));
        }
        if (StringUtils.isEmpty(member.getPassword())) {
            return AjaxResult.error("The password cannot be empty");
        }
        member.setUsername(member.getEmail());
        return memberRegisterService.registerByEmail(member);
    }

    @GetMapping(value = "/register/emailExist")
    @ApiOperation(value = "邮箱注册-邮箱是否已经注册",notes = "判断邮箱是否已经被注册")
    public AjaxResult emailExist(String email)
    {
        if (StringUtils.isEmpty(email) || !ValidateUtil.isEmail(email)) {
            return AjaxResult.error("Incorrect email address");
        }
        if (memberRegisterService.userExist(email)) {
            return AjaxResult.success();
        }
        return AjaxResult.error("Email already");
    }

    @GetMapping(value = "/register/sendEmailCode")
    @ApiOperation(value = "邮箱注册-发送邮箱验证码",notes = "注册时发送邮箱验证码")
    public AjaxResult sendEmailCode(String email)
    {
        if (StringUtils.isEmpty(email) || !ValidateUtil.isEmail(email)) {
            return AjaxResult.error("Incorrect email address");
        }
        if (verificationCodeService.emailSendCode(email, VerificationCodeType.REGISTER)){
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }


            /**        手机注册          **/

    @PostMapping(value = "/phoneRegister")
    @ApiOperation(value = "手机注册-手机注册会员",notes = "App手机注册会员")
    public AjaxResult phoneRegister(@RequestBody Member member)
    {
        if (member == null){
            return AjaxResult.error("The parameter cannot be empty");
        }
        if (StringUtils.isEmpty(member.getPhone())) {
            return AjaxResult.error("The phone cannot be empty");
        }
        if (StringUtils.isEmpty(member.getPassword())) {
            return AjaxResult.error("The password cannot be empty");
        }
        member.setUsername(member.getPhone());
        return memberRegisterService.registerByPhone(member);
    }

    @GetMapping(value = "/register/sendPhoneCode")
    @ApiOperation(value = "手机注册-发送手机证码",notes = "注册时发送手机验证码")
    public AjaxResult sendPhoneCode(String phone)
    {
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("Incorrect phone");
        }
        if (verificationCodeService.phoneSendCode(phone, VerificationCodeType.REGISTER)){
            return AjaxResult.success();
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @GetMapping(value = "/register/phoneExist")
    @ApiOperation(value = "手机注册-手机号是否已经注册",notes = "判断手机号是否已经被注册")
    public AjaxResult phoneExist(String phone)
    {
        if (StringUtils.isEmpty(phone)) {
            return AjaxResult.error("Incorrect phone");
        }
        if (memberRegisterService.userExist(phone)) {
            return AjaxResult.success(true);
        }
        return AjaxResult.success(false);
    }

    /**   用户名注册     **/
    @PostMapping(value = "/userNameRegister")
    @ApiOperation(value = "用户名注册-用户名注册会员",notes = "App用户名注册会员")
    public AjaxResult userNameRegister(@RequestBody Member member)
    {
        if (member == null){
            return AjaxResult.error("The parameter cannot be empty");
        }
        if (StringUtils.isEmpty(member.getUsername())) {
            return AjaxResult.error("The userName cannot be empty");
        }
        if (StringUtils.isEmpty(member.getPassword())) {
            return AjaxResult.error("The password cannot be empty");
        }
        return memberRegisterService.registerByUserName(member);
    }

    @GetMapping(value = "/register/userNameExist")
    @ApiOperation(value = "用户名注册-用户名是否已经注册",notes = "判断用户名是否已经被注册")
    public AjaxResult userNameExist(String userName)
    {
        if (StringUtils.isEmpty(userName)) {
            return AjaxResult.error("Incorrect userName");
        }
        if (memberRegisterService.userExist(userName)) {
            return AjaxResult.success(true);
        }
        return AjaxResult.success(false);
    }

    /**  找回密码  **/

    @PostMapping(value = "/back/resendCode")
    @ApiOperation(value = "邮箱找回密码-找回密重新码发送邮箱/或手机验证码",notes = "找回密码时发送重新邮箱/手机验证码: {key: key,type: 'email/phone'}")
    public AjaxResult backresendEmailCode(@RequestBody Map<String ,String> param)
    {
        if (StringUtils.isEmpty(param.get("key"))) {
            return AjaxResult.error("Incorrect key");
        }
        Claims claims = JwtUtils.parseToken(param.get("key"));
        if (claims.get("userId") == null) {
            return AjaxResult.error(" Key abnormal ");
        }
        Long userId = Long.parseLong(claims.get("userId").toString());
        Member dbMember = memberService.getById(userId);
        if (dbMember == null) {
            return AjaxResult.error(" Member does not exist ");
        }
        if (param.get("type").equals("phone")){
            if (verificationCodeService.phoneSendCode(dbMember.getPhone(), VerificationCodeType.BACK_PASSWORD)){
                return AjaxResult.success();
            }
        } else {
            if (verificationCodeService.emailSendCode(dbMember.getEmail(), VerificationCodeType.BACK_PASSWORD)){
                return AjaxResult.success();
            }
        }
        return AjaxResult.error("Description Failed to send the verification code");
    }

    @GetMapping(value = "/back/sendCode")
    @ApiOperation(value = "邮箱找回密码-找回密码发送邮箱/或手机验证码",notes = "找回密码时发送邮箱验证码:返回会员邮箱")
    public AjaxResult backSendEmailCode(String userName)
    {
        if (StringUtils.isEmpty(userName)) {
            return AjaxResult.error("Incorrect userName");
        }
        Member one = memberService.getOne(new QueryWrapper<Member>().lambda().eq(Member::getUsername, userName).or().eq(Member::getEmail, userName).or().eq(Member::getPhone, userName));
        if (one == null) {
            Map<String,Object> rsMap = new HashMap<>();
            rsMap.put("type",0);
            return AjaxResult.success(rsMap);
        }
        if (!StringUtils.isEmpty(one.getEmail()) && !StringUtils.isEmpty(one.getPhone())){
            if (verificationCodeService.emailSendCode(one.getEmail(), VerificationCodeType.BACK_PASSWORD)){
                if (verificationCodeService.phoneSendCode(one.getPhone(), VerificationCodeType.BACK_PASSWORD)) {
                    Map<String, Object> userId = new HashMap<>();
                    userId.put("userId", one.getId());
                    String key = JwtUtils.createToken(userId);
                    Map<String, Object> rsMap = new HashMap<>();
                    rsMap.put("email", one.getEmail());
                    rsMap.put("key", key);
                    rsMap.put("type", 3);
                    return AjaxResult.success(rsMap);
                }
            }
        }else if (!StringUtils.isEmpty(one.getEmail())) {
            if (verificationCodeService.emailSendCode(one.getEmail(), VerificationCodeType.BACK_PASSWORD)){
                Map<String,Object> userId = new HashMap<>();
                userId.put("userId",one.getId());
                String key = JwtUtils.createToken(userId);
                Map<String,Object> rsMap = new HashMap<>();
                rsMap.put("email",one.getEmail());
                rsMap.put("key",key);
                rsMap.put("type",1);
                return AjaxResult.success(rsMap);
            }
        }else if(!StringUtils.isEmpty(one.getPhone())) {
            if (verificationCodeService.phoneSendCode(one.getPhone(), VerificationCodeType.BACK_PASSWORD)){
                Map<String,Object> userId = new HashMap<>();
                userId.put("userId",one.getId());
                String key = JwtUtils.createToken(userId);
                Map<String,Object> rsMap = new HashMap<>();
                rsMap.put("phone",one.getPhone());
                rsMap.put("key",key);
                rsMap.put("type",2);
                return AjaxResult.success(rsMap);
            }
        }else {
            Map<String,Object> rsMap = new HashMap<>();
            rsMap.put("type",4);
            return AjaxResult.success(rsMap);
        }

        return AjaxResult.error("Description Failed to send the verification code");
    }

    @PostMapping(value = "/back/emailChangePassword")
    @ApiOperation(value = "邮箱找回密码-修改密码",notes = "邮箱验证码修改密码请求结构：{\n" +
            "    \"token\": \"验证，验证码时返回的token\",\n" +
            "    \"password\": \"重制密码\"\n" +
            "}")
    public AjaxResult emailChangePassword(@RequestBody Map<String ,String> param){
        if (StringUtils.isEmpty(param.get("token"))) {
            return AjaxResult.error("The token cannot be empty");
        }
        if (StringUtils.isEmpty(param.get("password"))) {
            return AjaxResult.error("The password cannot be empty");
        }
        Claims claims = JwtUtils.parseToken(param.get("token"));
        if (claims.get("id") == null) {
            return AjaxResult.error(" Token abnormal ");
        }
        Long userId = Long.parseLong(claims.get("id").toString());
        Member dbMember = memberService.getById(userId);
        if (dbMember == null) {
            return AjaxResult.error(" Member does not exist ");
        }
        boolean rs = memberRegisterService.changePassword(dbMember.getId(), dbMember.getUid().toString(), param.get("password"));
        if (rs) {
            return success();
        }
        return error();
    }

    @PostMapping(value = "/back/verify")
    @ApiOperation(value = "邮箱找回密码-验证验证码",notes = "邮箱验证验证验证码请求结构：" +
            "{\n" +
            "    \"key\": \"发送验证码后返回key\",\n" +
            "    \"verificationCode\": \"邮箱验证码\",\n" +
            "    \"verificationCode2\": \"手机验证码\"\n" +
            "}")
    public AjaxResult verify(@RequestBody Map<String ,String> param)
    {
        if (StringUtils.isEmpty(param.get("key"))) {
            return AjaxResult.error("Incorrect key");
        }
        Claims claims = JwtUtils.parseToken(param.get("key"));
        if (claims.get("userId") == null) {
            return AjaxResult.error(" Key abnormal ");
        }
        Long userId = Long.parseLong(claims.get("userId").toString());
        Member dbMember = memberService.getById(userId);
        if (dbMember == null) {
            return AjaxResult.error(" Member does not exist ");
        }
        if (!StringUtils.isEmpty(dbMember.getPhone())) {
            if (StringUtils.isEmpty(param.get("verificationCode2"))) {
                return AjaxResult.error("The verificationCode cannot be empty");
            }
            if (!verificationCodeService.verify(param.get("verificationCode2"),VerificationCodeType.BACK_PASSWORD,dbMember.getPhone())){
                return AjaxResult.error("The verification code is incorrect");
            }
        }
        if (!StringUtils.isEmpty(dbMember.getEmail())) {
            if (StringUtils.isEmpty(param.get("verificationCode"))) {
                return AjaxResult.error("The verificationCode cannot be empty");
            }
            if (!verificationCodeService.verify(param.get("verificationCode"),VerificationCodeType.BACK_PASSWORD,dbMember.getEmail())){
                return AjaxResult.error("The verification code is incorrect");
            }
        }
        Map<String,Object> token = new HashMap<>();
        token.put("id",dbMember.getId());
        String key = JwtUtils.createToken(token);
        Map<String,String> rsMap = new HashMap<>();
        rsMap.put("token",key);
        return AjaxResult.success(rsMap);
    }

}
