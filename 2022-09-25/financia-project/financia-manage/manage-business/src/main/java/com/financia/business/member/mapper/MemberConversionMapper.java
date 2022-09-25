package com.financia.business.member.mapper;

import com.financia.exchange.vo.MemberConversionVo;

import java.util.List;

public interface MemberConversionMapper {

    List<MemberConversionVo> getMemberConversionList(MemberConversionVo conversionVo);
}
