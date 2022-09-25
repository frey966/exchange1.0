package com.financia.business.dataconfiguration.controller;

import com.financia.business.PBusinessType;
import com.financia.business.dataconfiguration.service.IPBusinessTypeService;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 业务类型Controller
 * 
 * @author 花生
 * @date 2022-08-02
 */
@RestController
@Api(tags = "数据配置管理-业务类型")
@RequestMapping("/type")
public class PBusinessTypeController extends BaseController
{
    @Autowired
    private IPBusinessTypeService pBusinessTypeService;

    /**
     * 查询业务类型列表
     */
    @ApiOperation(value = "查询交易类型列表",notes = "查询交易类型列表")
//    @RequiresPermissions("system:type:list")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(PBusinessType pBusinessType)
    {
        startPage();
        List<PBusinessType> list = pBusinessTypeService.selectPBusinessTypeList(pBusinessType);
        return getDataTable(list);
    }

//    /**
//     * 导出业务类型列表
//     */
//    @RequiresPermissions("system:type:export")
//    @Log(title = "业务类型", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, PBusinessType pBusinessType)
//    {
//        List<PBusinessType> list = pBusinessTypeService.selectPBusinessTypeList(pBusinessType);
//        ExcelUtil<PBusinessType> util = new ExcelUtil<PBusinessType>(PBusinessType.class);
//        util.exportExcel(response, list, "业务类型数据");
//    }

    /**
     * 获取业务类型详细信息
     */
    @ApiOperation(value = "查询详细信息",notes = "获取业务类型详细信息")
//    @RequiresPermissions("system:type:query")
    @GetMapping(value = "query/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pBusinessTypeService.selectPBusinessTypeById(id));
    }

    /**
     * 新增业务类型
     */
    @ApiOperation(value = "新增业务类型",notes = "新增业务类型")
//    @RequiresPermissions("system:type:add")
    @Log(title = "业务类型", businessType = BusinessType.INSERT)
    @PostMapping(value = "add")
    public AjaxResult add(@RequestBody PBusinessType pBusinessType)
    {
        return toAjax(pBusinessTypeService.insertPBusinessType(pBusinessType));
    }

    /**
     * 修改业务类型
     */
    @ApiOperation(value = "修改业务类型",notes = "修改业务类型")
//    @RequiresPermissions("system:type:edit")
    @Log(title = "业务类型", businessType = BusinessType.UPDATE)
    @PostMapping(value = "edit")
    public AjaxResult edit(@RequestBody PBusinessType pBusinessType)
    {
        return toAjax(pBusinessTypeService.updatePBusinessType(pBusinessType));
    }

    /**
     * 删除业务类型
     *
     */
    @ApiOperation(value = "删除业务类型",notes = "删除业务类型")
//    @RequiresPermissions("system:type:remove")
    @Log(title = "业务类型", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pBusinessTypeService.deletePBusinessTypeByIds(ids));
    }
}
