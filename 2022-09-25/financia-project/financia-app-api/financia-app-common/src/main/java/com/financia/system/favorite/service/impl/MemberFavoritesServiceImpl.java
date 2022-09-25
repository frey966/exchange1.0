package com.financia.system.favorite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.MemberFavorites;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;
import com.financia.common.core.utils.mybatisplus.Query;

import com.financia.system.favorite.mapper.MemberFavoritesMapper;


import com.financia.system.favorite.service.MemberFavoritesService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("memberFavoritesService")
public class MemberFavoritesServiceImpl extends ServiceImpl<MemberFavoritesMapper, MemberFavorites> implements MemberFavoritesService {

    @Override
    public PagePlusUtils queryPage(Map<String, Object> params) {
        IPage<MemberFavorites> page = this.page(
                new Query<MemberFavorites>().getPage(params),
                new QueryWrapper<MemberFavorites>()
        );

        return new PagePlusUtils(page);
    }

}
