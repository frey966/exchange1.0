package com.financia.system.controller;

import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.service.MemberLoginService;
import com.financia.system.service.VerificationCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登陆
 */
@Api(tags="APP登陆模块")
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberLoginController extends BaseController
{

    @Autowired
    private MemberLoginService memberLoginService;
    @Autowired
    private VerificationCodeService verificationCodeService;

//    @GetMapping(value = "/login/sendEmailCode")
//    @ApiOperation(value = "发送邮箱验证码",notes = "登陆时多次发送邮箱验证码")
//    public AjaxResult sendEmailCode(String email)
//    {
//        if (StringUtils.isEmpty(email) || !ValidateUtil.isEmail(email)) {
//            AjaxResult.error("Incorrect email address");
//        }
//        boolean rs = verificationCodeService.emailSendCode(email, VerificationCodeType.LOGIN);
//        if(rs) {
//            return AjaxResult.success();
//        }
//        return AjaxResult.error();
//    }
//
//    @PostMapping(value = "/loginEmailCode")
//    @ApiOperation(value = "邮箱验证码登陆",notes = "使用邮箱验证码登陆参数verificationCode: 验证码，key：登陆key")
//    public AjaxResult register(@RequestBody Map<String,String> param)
//    {
//        String code = param.get("verificationCode");
//        String key = param.get("key");
//        Claims claims = JwtUtils.parseToken(key);
//        if (StringUtils.isEmpty(code)){
//            return AjaxResult.error("code is not null");
//        }
//        if (StringUtils.isEmpty(key)){
//            return AjaxResult.error("key is not null");
//        }
//        if (claims.get("memberId") == null){
//            return AjaxResult.error("memberId is not null");
//        }
//        return memberLoginService.LoginByCode(code, Long.parseLong(claims.get("memberId").toString()));
//    }
//
//
//    @PostMapping(value = "/login")
//    @ApiOperation(value = "账号密码登陆",notes = "使用账号密码登陆返回key：使用参数username，password")
//    public AjaxResult register(@RequestBody Member member)
//    {
//        if (member == null){
//            return AjaxResult.error("The parameter cannot be empty");
//        }
//        if (StringUtils.isEmpty(member.getUsername())) {
//            return AjaxResult.error("The account cannot be empty");
//        }
//        if (StringUtils.isEmpty(member.getPassword())) {
//            return AjaxResult.error("The password cannot be empty");
//        }
//        return memberLoginService.login(member);
//    }
    @PostMapping(value = "/login")
    @ApiOperation(value = "账号密码登陆",notes = "userName，password")
    public AjaxResult register(@RequestBody Map<String,String> param, HttpServletRequest request)
    {

        if (StringUtils.isEmpty(param.get("userName"))) {
            return AjaxResult.error("The userName cannot be empty");
        }
        if (StringUtils.isEmpty(param.get("password"))) {
            return AjaxResult.error("The password cannot be empty");
        }
        String remoteIp = getRemoteIp(request);
        return memberLoginService.LoginByPassword(remoteIp, param.get("userName"),param.get("password"));
    }

    @PostMapping(value = "/loginByGesture")
    @ApiOperation(value = "账号手势密码登陆",notes = "{id: '12',password: '密码'}")
    public AjaxResult loginByGesture(@RequestBody Map<String,Object> param, HttpServletRequest request)
    {
        if (StringUtils.isEmpty(param.get("id").toString())) {
            return AjaxResult.error("The userName cannot be empty");
        }
        if (StringUtils.isEmpty(param.get("password").toString())) {
            return AjaxResult.error("The password cannot be empty");
        }
        String remoteIp = getRemoteIp(request);
        return memberLoginService.LoginByGesturePassword(remoteIp, Long.parseLong(param.get("id").toString()),param.get("password").toString());
    }


    @PostMapping(value = "/gesturePasswordSet")
    @ApiOperation(value = "账号手势密码设置",notes = "{id: '12',password: '密码'}")
    public AjaxResult gesturePasswordSet(@RequestBody Map<String,Object> param)
    {
        if (StringUtils.isEmpty(param.get("password").toString())) {
            return AjaxResult.error("The password cannot be empty");
        }
        if (StringUtils.isEmpty(param.get("id").toString())) {
            return AjaxResult.error("The userName cannot be empty");
        }
        AjaxResult password = memberLoginService.gsturePasswordSave(Long.parseLong(param.get("id").toString()), param.get("password").toString());
        return password;
    }

    @PostMapping(value = "/closeGesturePassword")
    @ApiOperation(value = "关闭手势密码",notes = "{id : '12',password: '密码',loginPassword: '123'}")
    public AjaxResult closeGesturePassword(@RequestBody Map<String,Object> param)
    {

        if (StringUtils.isEmpty(param.get("id").toString())) {
            return AjaxResult.error("The userName cannot be empty");
        }
        if (param.get("password") != null && !StringUtils.isEmpty(param.get("password").toString())) {
            return memberLoginService.closePasswordVerify(Long.parseLong(param.get("id").toString()), param.get("password").toString(),null);
        }
        if (param.get("loginPassword") != null && !StringUtils.isEmpty(param.get("loginPassword").toString())) {
            return memberLoginService.closePasswordVerify(Long.parseLong(param.get("id").toString()), null,param.get("loginPassword").toString());
        }
        return AjaxResult.error("The password cannot be empty");
    }

//    @PostMapping(value = "/gestureSwitch")
//    @ApiOperation(value = "手势登陆开关",notes = "{id : '12',isOpen: '1/0'}")
//    public AjaxResult gestureSwitch(@RequestBody Map<String,Object> param)
//    {
//        if (StringUtils.isEmpty(param.get("isOpen").toString())) {
//            return AjaxResult.error("The password cannot be empty");
//        }
//        if (StringUtils.isEmpty(param.get("id").toString())) {
//            return AjaxResult.error("The userName cannot be empty");
//        }
//        AjaxResult password = memberLoginService.gestureSwitch(Long.parseLong(param.get("id").toString()), Integer.parseInt(param.get("isOpen").toString()));
//        return password;
//    }



}
