package com.financia.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.PCountryCode;
import com.financia.common.core.utils.mybatisplus.PagePlusUtils;
import com.financia.common.core.utils.mybatisplus.Query;
import com.financia.system.mapper.PCountryCodeMapper;
import com.financia.system.service.PCountryCodeService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysCountryCodeService")
public class PCountryCodeServiceImpl extends ServiceImpl<PCountryCodeMapper, PCountryCode> implements PCountryCodeService {

    @Override
    public PagePlusUtils queryPage(Map<String, Object> params) {
        IPage<PCountryCode> page = this.page(
                new Query<PCountryCode>().getPage(params),
                new QueryWrapper<PCountryCode>()
        );

        return new PagePlusUtils(page);
    }

}
