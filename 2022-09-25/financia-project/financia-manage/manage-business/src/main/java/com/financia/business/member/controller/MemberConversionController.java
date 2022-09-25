package com.financia.business.member.controller;

import com.financia.business.member.service.MemberConversionService;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.exchange.vo.MemberConversionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Dylan
 */

@Api(tags = "会员管理-汇兑记录")
@RestController
@RequestMapping("/conversion")
public class MemberConversionController extends BaseController {
    @Autowired
    private MemberConversionService memberConversionService;

    /**
     * 获取货币详细信息
     */
    @ApiOperation(value = "获取换汇订单详细信息", notes = "获取换汇订单详细信息")
    @GetMapping(value = "list")
    public TableDataInfo getList(MemberConversionVo conversionVo) {
        startPage();
        List<MemberConversionVo> list = memberConversionService.getMemberConversionList(conversionVo);
        return getDataTable(list);
    }

}
