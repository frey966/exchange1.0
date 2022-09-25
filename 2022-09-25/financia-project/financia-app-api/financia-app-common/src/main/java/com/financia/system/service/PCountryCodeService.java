package com.financia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.PCountryCode;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;

import java.util.Map;

/**
 *
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-27 21:13:55
 */
public interface PCountryCodeService extends IService<PCountryCode> {

    PagePlusUtils queryPage(Map<String, Object> params);
}

