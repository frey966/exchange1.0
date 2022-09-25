package com.financia.quotes.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.MemberFavorites;
import com.financia.common.core.utils.StringUtils;
import com.financia.quotes.mapper.MemberFavoritesMapper;
import com.financia.quotes.service.MemberFavoritesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("memberFavoritesService")
@Slf4j
public class MemberFavoritesServiceImpl implements MemberFavoritesService {
    @Autowired
    private MemberFavoritesMapper memberFavoritesMapper;

    @Override
    public List<MemberFavorites> getMemberFavoritesList(MemberFavorites memberFavorites) {

        return memberFavoritesMapper.selectList(
                new QueryWrapper<MemberFavorites>().lambda()
                        .eq(MemberFavorites::getMemberId, memberFavorites.getMemberId())
                        .eq(MemberFavorites::getStatus, 1)
                        .eq(!StringUtils.isEmpty(memberFavorites.getType()), MemberFavorites::getType, memberFavorites.getType())
                        .eq(!StringUtils.isEmpty(memberFavorites.getSymbol()), MemberFavorites::getSymbol, memberFavorites.getSymbol())
        );
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        memberFavoritesMapper.updateStatus(id, status);
    }

    @Override
    public void insert(MemberFavorites memberFavorites) {
        MemberFavorites favorites = memberFavoritesMapper.getByMemberIdAndSymbol(memberFavorites.getMemberId(), memberFavorites.getSymbol(), memberFavorites.getType());
        //为空则是第一次收藏，如果不为空,则改为收藏状态
        if (favorites == null) {
            memberFavorites.setCreateTime(new Date());
            memberFavorites.setStatus(1);
            memberFavoritesMapper.insert(memberFavorites);
        } else {
            memberFavoritesMapper.updateStatus(favorites.getId(), 1);

        }


    }
}
