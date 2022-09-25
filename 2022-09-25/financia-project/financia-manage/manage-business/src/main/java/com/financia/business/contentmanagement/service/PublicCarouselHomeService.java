    package com.financia.business.contentmanagement.service;

    import com.baomidou.mybatisplus.extension.service.IService;
    import com.financia.common.core.utils.PageUtils;
    import com.financia.common.core.utils.mybatisplus.PagePlusUtils;
    import com.financia.exchange.PublicCarouselHome;

    import java.util.List;
    import java.util.Map;

    /**
 * 首页轮播
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-19 22:58:22
 */
public interface PublicCarouselHomeService extends IService<PublicCarouselHome> {

    PagePlusUtils queryPage(Map<String, Object> params);

        /**
         * 查询首页轮播
         *
         * @param id 首页轮播主键
         * @return 首页轮播
         */
        public PublicCarouselHome selectPCarouselHomeById(Long id);

        /**
         * 查询首页轮播列表
         *
         * @param pCarouselHome 首页轮播
         * @return 首页轮播集合
         */
        public List<PublicCarouselHome> selectPCarouselHomeList(PublicCarouselHome pCarouselHome);

        /**
         * 新增首页轮播
         *
         * @param pCarouselHome 首页轮播
         * @return 结果
         */
        public int insertPCarouselHome(PublicCarouselHome pCarouselHome);

        /**
         * 修改首页轮播
         *
         * @param pCarouselHome 首页轮播
         * @return 结果
         */
        public int updatePCarouselHome(PublicCarouselHome pCarouselHome);

        /**
         * 批量删除首页轮播
         *
         * @param ids 需要删除的首页轮播主键集合
         * @return 结果
         */
        public int deletePCarouselHomeByIds(Long[] ids);

        /**
         * 删除首页轮播信息
         *
         * @param id 首页轮播主键
         * @return 结果
         */
        public int deletePCarouselHomeById(Long id);
}

