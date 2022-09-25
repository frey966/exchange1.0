package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.PNotice;
import com.financia.system.service.PNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公告
 */
@Api(tags="APP公告模块")
@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController extends BaseController
{

    @Autowired
    private PNoticeService noticeService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "获取公告",notes = "")
    public AjaxResult list(String type)
    {
        String lang = LocaleContextHolder.getLocale().toString();
        List<PNotice> list = noticeService.list(new QueryWrapper<PNotice>()
                .lambda().eq(PNotice::getStatus, 0)
                .eq(PNotice::getType, type).eq(PNotice::getLanguage,lang));
        return AjaxResult.success(list);
    }
    @GetMapping(value = "/test")
    @ApiOperation(value = "test",notes = "")
    public PNotice test()
    {
        return null;
    }

}
