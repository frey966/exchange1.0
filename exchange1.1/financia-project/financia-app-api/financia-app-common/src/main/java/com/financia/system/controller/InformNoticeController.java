package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financia.common.PInformNotice;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.service.PInformNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公告
 */
@Api(tags="APP首页消息通知")
@RestController
@RequestMapping("/inform")
@Slf4j
public class InformNoticeController extends BaseController
{

    @Autowired
    private PInformNoticeService noticeService;

    @GetMapping(value = "/list")
    @ApiOperation(value = "列表",notes = "")
    public AjaxResult list()
    {
        List<PInformNotice> list = noticeService.list(new LambdaQueryWrapper<PInformNotice>()
                .eq(PInformNotice::getStatus, DataStatus.VALID.getCode())
                .eq(PInformNotice::getOnLine,1).orderByDesc(PInformNotice::getCreateTime));
        return AjaxResult.success(list);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "详情",notes = "")
    public AjaxResult getById(Long id)
    {
        PInformNotice byId = noticeService.getById(id);
        return AjaxResult.success(byId);
    }


    @GetMapping(value = "/test")
    @ApiOperation(value = "test",notes = "")
    public AjaxResult test()
    {
        return null;
    }

}
