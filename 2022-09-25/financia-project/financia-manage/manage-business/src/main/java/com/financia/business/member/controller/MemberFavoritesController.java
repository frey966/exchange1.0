package com.financia.business.member.controller;

import com.financia.business.member.service.MemberFavoritesService;
import com.financia.common.MemberFavorites;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-25 15:56:42
 */
@RestController
@Api(tags = "会员管理-会员收藏管理")
@RequestMapping("newest/memberfavorites")
public class MemberFavoritesController extends BaseController {
    @Autowired
    private MemberFavoritesService memberFavoritesService;

    /**
     * 列表
     */
    @PostMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currPage",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    @ApiOperation(value = "收藏列表",notes = "用户收藏列表")
//    @RequiresPermissions("newest:memberfavorites:list")
    public TableDataInfo list(@RequestParam Map<String, Object> params){
        startPage();
        List<MemberFavorites> list = memberFavoritesService.list();

        return getDataTable(list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "收藏详情",notes = "用户收藏详情")
//    @RequiresPermissions("newest:memberfavorites:info")
    public AjaxResult info(@PathVariable("id") Long id){
		MemberFavorites memberFavorites = memberFavoritesService.getById(id);

        return AjaxResult.success(memberFavorites);
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改收藏",notes = "修改用户收藏")
//    @RequiresPermissions("newest:memberfavorites:update")
    public AjaxResult update(@RequestBody MemberFavorites memberFavorites){
        boolean b = memberFavoritesService.updateById(memberFavorites);
        if (b) {
            return success();
        }

        return error("修改失败");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除收藏",notes = "删除用户收藏")
//    @RequiresPermissions("newest:memberfavorites:delete")
    public AjaxResult delete(@RequestBody Long[] ids){
        boolean b = memberFavoritesService.removeByIds(Arrays.asList(ids));
        if (b){
            return success();
        }

        return error("删除失败");
    }

}
