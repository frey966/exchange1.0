package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;
import com.financia.common.core.utils.mybatisplus.Query;
import com.financia.exchange.PublicCarouselHome;
import com.financia.exchange.mapper.PublicCarouselHomeMapper;
import com.financia.exchange.service.PublicCarouselHomeService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("publicCarouselHomeService")
public class PublicCarouselHomeServiceImpl extends ServiceImpl<PublicCarouselHomeMapper, PublicCarouselHome> implements PublicCarouselHomeService {

    @Override
    public PagePlusUtils queryPage(Map<String, Object> params) {
        IPage<PublicCarouselHome> page = this.page(
                new Query<PublicCarouselHome>().getPage(params),
                new QueryWrapper<PublicCarouselHome>()
        );

        return new PagePlusUtils(page);
    }

}
