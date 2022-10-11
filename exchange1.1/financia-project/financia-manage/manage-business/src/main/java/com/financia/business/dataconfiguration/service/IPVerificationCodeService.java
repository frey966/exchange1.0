package com.financia.business.dataconfiguration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.PVerificationCode;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;

import java.util.Map;

/**
 * 用户注册或登录发送邮箱验证码Service接口
 *
 * @author chuangzhang
 * @date 2022-07-13
 */
public interface IPVerificationCodeService extends IService<PVerificationCode>
{
    PagePlusUtils queryPage(Map<String, Object> params) ;
}
