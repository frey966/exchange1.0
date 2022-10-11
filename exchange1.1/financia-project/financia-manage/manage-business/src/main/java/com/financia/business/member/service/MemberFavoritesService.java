package com.financia.business.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.MemberFavorites;
import com.financia.common.core.utils.PageUtils;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;

import java.util.Map;

/**
 *
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-25 15:56:42
 */
public interface MemberFavoritesService extends IService<MemberFavorites> {

    PagePlusUtils queryPage(Map<String, Object> params);
}

