    package com.financia.exchange.service;

    import com.baomidou.mybatisplus.extension.service.IService;
    import com.financia.common.core.utils.mybatisplus.PagePlusUtils;
    import com.financia.exchange.PublicCarouselHome;

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
}

