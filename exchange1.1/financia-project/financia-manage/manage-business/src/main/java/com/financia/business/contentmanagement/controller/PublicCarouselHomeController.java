package com.financia.business.contentmanagement.controller;

import com.financia.business.contentmanagement.service.PublicCarouselHomeService;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.exchange.PublicCarouselHome;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 首页轮播
 *
 * @author dalong
 * @email
 * @date 2022-07-19 22:58:22
 */
@RestController
@Api(tags = "内容管理-首页轮播管理")
@RequestMapping("/newest/publiccarouselhome")
public class PublicCarouselHomeController  extends BaseController {
    @Autowired
    private PublicCarouselHomeService publicCarouselHomeService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "首页轮播列表",notes = "后台首页轮播列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currPage",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
//    @RequiresPermissions("bitrade:publiccarouselhome:list")
    public TableDataInfo list(PublicCarouselHome pCarouselHome){
        startPage();
        List<PublicCarouselHome> list = publicCarouselHomeService.selectPCarouselHomeList(pCarouselHome);
        return getDataTable(list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "首页轮播详情",notes = "首页轮播详情")
//    @RequiresPermissions("bitrade:publiccarouselhome:info")
    public AjaxResult info(@PathVariable("id") Long id){
		PublicCarouselHome publicCarouselHome = publicCarouselHomeService.getById(id);
        return AjaxResult.success(publicCarouselHome);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "首页轮播保存",notes = "后台首页轮播保存")
//    @RequiresPermissions("bitrade:publiccarouselhome:save")
    public AjaxResult save(@RequestBody PublicCarouselHome publicCarouselHome){
        if (StringUtils.isEmpty(publicCarouselHome.getImageName())) {
            return AjaxResult.error("名称不能为空");
        }
        if (StringUtils.isEmpty(publicCarouselHome.getImageUrl())) {
            return AjaxResult.error("图片路径不能为空");
        }
        if (StringUtils.isEmpty(publicCarouselHome.getUrl())) {
            return AjaxResult.error("跳转地址不能为空");
        }
        boolean save = publicCarouselHomeService.save(publicCarouselHome);
        if (save){
            return AjaxResult.success();
        }
        return error("save failure");
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "首页轮播修改",notes = "后台首页轮播修改")
//    @RequiresPermissions("bitrade:publiccarouselhome:update")
    public AjaxResult update(@RequestBody PublicCarouselHome publicCarouselHome){
        return toAjax(publicCarouselHomeService.updatePCarouselHome(publicCarouselHome));
    }

    /**
     * 删除
     */
//    @RequiresPermissions("system:home:remove")
    @ApiOperation(value = "删除",notes = "删除")
    @Log(title = "首页轮播", businessType = BusinessType.DELETE)
    @GetMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(publicCarouselHomeService.deletePCarouselHomeByIds(ids));
    }

}
