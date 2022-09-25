package com.financia.business.contentmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.contentmanagement.mapper.PublicCarouselHomeMapper;
import com.financia.business.contentmanagement.service.PublicCarouselHomeService;
import com.financia.common.core.utils.mybatisplus.Query;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;
import com.financia.exchange.PublicCarouselHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("publicCarouselHomeService")
public class PublicCarouselHomeServiceImpl extends ServiceImpl<PublicCarouselHomeMapper, PublicCarouselHome> implements PublicCarouselHomeService {
    @Autowired
    private PublicCarouselHomeMapper pCarouselHomeMapper;
    @Override
    public PagePlusUtils queryPage(Map<String, Object> params) {
        IPage<PublicCarouselHome> page = this.page(
                new Query<PublicCarouselHome>().getPage(params),
                new QueryWrapper<PublicCarouselHome>()
        );

        return new PagePlusUtils(page);
    }


    /**
     * 查询首页轮播
     *
     * @param id 首页轮播主键
     * @return 首页轮播
     */
    @Override
    public PublicCarouselHome selectPCarouselHomeById(Long id)
    {
        return pCarouselHomeMapper.selectPCarouselHomeById(id);
    }

    /**
     * 查询首页轮播列表
     *
     * @param pCarouselHome 首页轮播
     * @return 首页轮播
     */
    @Override
    public List<PublicCarouselHome> selectPCarouselHomeList(PublicCarouselHome pCarouselHome)
    {
        return pCarouselHomeMapper.selectPCarouselHomeList(pCarouselHome);
    }

    /**
     * 新增首页轮播
     *
     * @param pCarouselHome 首页轮播
     * @return 结果
     */
    @Override
    public int insertPCarouselHome(PublicCarouselHome pCarouselHome)
    {
        return pCarouselHomeMapper.insertPCarouselHome(pCarouselHome);
    }

    /**
     * 修改首页轮播
     *
     * @param pCarouselHome 首页轮播
     * @return 结果
     */
    @Override
    public int updatePCarouselHome(PublicCarouselHome pCarouselHome)
    {
        return pCarouselHomeMapper.updatePCarouselHome(pCarouselHome);
    }

    /**
     * 批量删除首页轮播
     *
     * @param ids 需要删除的首页轮播主键
     * @return 结果
     */
    @Override
    public int deletePCarouselHomeByIds(Long[] ids)
    {
        return pCarouselHomeMapper.deletePCarouselHomeByIds(ids);
    }

    /**
     * 删除首页轮播信息
     *
     * @param id 首页轮播主键
     * @return 结果
     */
    @Override
    public int deletePCarouselHomeById(Long id)
    {
        return pCarouselHomeMapper.deletePCarouselHomeById(id);
    }

}
