package com.financia.business.contentmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.contentmanagement.mapper.MemberShareWalletMapper;
import com.financia.business.contentmanagement.service.MemberShareService;
import com.financia.common.MemberShare;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Yezi
 */
@Service
public class MemberShareServiceImpl extends ServiceImpl<MemberShareWalletMapper, MemberShare> implements MemberShareService {

    @Override
    public boolean insertShareInfo(MemberShare memberShare) {
        return save(memberShare);
    }

    @Override
    public boolean updateShareInfo(MemberShare info) {
        return update(new QueryWrapper<MemberShare>()
                .lambda()
                .eq(MemberShare::getShareId,info.getShareId()));
    }
}
