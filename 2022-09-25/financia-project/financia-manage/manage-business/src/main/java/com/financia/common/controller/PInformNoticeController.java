package com.financia.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financia.common.PInformNotice;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.service.PInformNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 */
@RestController
@RequestMapping("/inform")
@Api(tags="公共-通知管理")
public class PInformNoticeController extends BaseController
{
    @Autowired
    private PInformNoticeService informNoticeService;

    /**
     * 通知列表
     */
//    @RequiresPermissions("gen:us:list")
    @GetMapping("/list")
    @ApiOperation(value = "通知列表",notes = "通知列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(PInformNotice inform)
    {
        startPage();
        List<PInformNotice> list = informNoticeService.list(new LambdaQueryWrapper<PInformNotice>().eq(PInformNotice::getStatus, DataStatus.VALID.getCode()));
        return getDataTable(list);
    }


    /**
     *
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "通知管理详细信息",notes = "通知管理详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(informNoticeService.getById(id));
    }

    /**
     * 新增个人中心-关于我们
     */
//    @RequiresPermissions("gen:us:add")
    @Log(title = "通知管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody PInformNotice inform)
    {
        return toAjax(informNoticeService.save(inform));
    }

    /**
     * 通知管理
     */
//    @RequiresPermissions("gen:us:edit")
    @Log(title = "通知管理", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody PInformNotice inform)
    {
        return toAjax(informNoticeService.updateById(inform));
    }

    /**
     * 通知管理
     */
//    @RequiresPermissions("gen:us:remove")
    @Log(title = "通知管理", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        for (Long id : ids) {
            PInformNotice pInformNotice = new PInformNotice();
            pInformNotice.setId(id);
            pInformNotice.setStatus(DataStatus.DELETED.getCode());
            informNoticeService.updateById(pInformNotice);
        }
        return toAjax(true);
    }
}
