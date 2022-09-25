package com.financia.system.share.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.MemberShare;
import com.financia.system.share.mapper.MemberShareWalletMapper;
import com.financia.system.share.service.MemberShareService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Yezi
 */
@Service
public class MemberShareServiceImpl extends ServiceImpl<MemberShareWalletMapper, MemberShare> implements MemberShareService {

    @Override
    public MemberShare queryShareInfoByPic(String picZh, String picEn) {
        //默认展示开启状态的分享信息
        MemberShare wallet = getOne(new QueryWrapper<MemberShare>()
                .lambda()
                    .eq(StringUtils.isEmpty(picEn), MemberShare::getShareImageEn, picEn)
                .eq(StringUtils.isEmpty(picZh), MemberShare::getShareContentZh, picZh)
                .eq(MemberShare::getActive, 0)
        );
        return wallet;
    }

}
