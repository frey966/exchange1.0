package com.financia.business.member.service;

import com.financia.exchange.vo.MemberConversionVo;

import java.util.List;

public interface MemberConversionService {
    List<MemberConversionVo> getMemberConversionList(MemberConversionVo conversionVo);
}
