package com.financia.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.PVerificationCode;
import com.financia.system.mapper.PVerificationCodeMapper;
import com.financia.system.service.PVerificationCodeService;
import org.springframework.stereotype.Service;


@Service("pVerificationCodeService")
public class PVerificationCodeServiceImpl extends ServiceImpl<PVerificationCodeMapper, PVerificationCode> implements PVerificationCodeService {



}
