package com.financia.business.dataconfiguration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.dataconfiguration.mapper.PVerificationCodeMapper;
import com.financia.business.dataconfiguration.service.IPVerificationCodeService;
import com.financia.common.PVerificationCode;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;
import com.financia.common.core.utils.mybatisplus.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户注册或登录发送邮箱验证码Service业务层处理
 *
 * @author ruoyi
 * @date 2022-07-13
 */
@Service
public class PVerificationCodeServiceImpl extends ServiceImpl<PVerificationCodeMapper, PVerificationCode> implements IPVerificationCodeService
{

    @Override
    public PagePlusUtils queryPage(Map<String, Object> params) {
        IPage<PVerificationCode> page = this.page(
                new Query<PVerificationCode>().getPage(params),
                new QueryWrapper<PVerificationCode>()
        );

        return new PagePlusUtils(page);
    }
}
