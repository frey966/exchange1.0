package com.financia.business.member.service.impl;

import com.financia.business.member.mapper.MemberConversionMapper;
import com.financia.business.member.service.MemberConversionService;
import com.financia.common.core.utils.StringUtils;
import com.financia.exchange.vo.MemberConversionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberConversionServiceImpl implements MemberConversionService {
    @Autowired
    private MemberConversionMapper memberConversionMapper;

    @Override
    public List<MemberConversionVo> getMemberConversionList(MemberConversionVo conversionVo) {
           if (StringUtils.isNotBlank(conversionVo.getUserName())){
               if (conversionVo.getUserName().contains("@")){
                   conversionVo.setEmail(conversionVo.getUserName());
               }else {
                   conversionVo.setPhone(conversionVo.getUserName());
               }
           }
        return memberConversionMapper.getMemberConversionList(conversionVo);
    }
}
