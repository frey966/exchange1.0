package com.financia.business.finance.controller;

import com.financia.business.finance.service.IFinanceTradingProjectService;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.finance.FinanceTradingProject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 量化理财-理财产品信息Controller
 *
 * @author 花生
 * @date 2022-08-15
 */
@Api(tags = "量化理财-量化产品")
@RestController
@RequestMapping("/financeTradingProject")
public class FinanceTradingProjectController extends BaseController
{
    @Autowired
    private IFinanceTradingProjectService financeTradingProjectService;

    /**
     * 查询量化理财-理财产品信息列表
     */
//    @RequiresPermissions("gen:project:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询量化产品列表",notes = "查询量化产品列表")
    public TableDataInfo list(FinanceTradingProject financeTradingProject)
    {
        startPage();
        List<FinanceTradingProject> list = financeTradingProjectService.selectFinanceTradingProjectList(financeTradingProject);
        return getDataTable(list);
    }

//    /**
//     * 导出量化理财-理财产品信息列表
//     */
//    @RequiresPermissions("gen:project:export")
//    @Log(title = "量化理财-理财产品信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "验证码删除",notes = "验证码删除根据ID")
//    public void export(HttpServletResponse response, FinanceTradingProject financeTradingProject)
//    {
//        List<FinanceTradingProject> list = financeTradingProjectService.selectFinanceTradingProjectList(financeTradingProject);
//        ExcelUtil<FinanceTradingProject> util = new ExcelUtil<FinanceTradingProject>(FinanceTradingProject.class);
//        util.exportExcel(response, list, "量化理财-理财产品信息数据");
//    }

    /**
     * 获取量化理财-理财产品信息详细信息
     */
//    @RequiresPermissions("gen:project:query")
    @GetMapping(value = "getInfo/{financeId}")
    @ApiOperation(value = "获取详细信息",notes = "获取详细信息")
    public AjaxResult getInfo(@PathVariable("financeId") Long financeId)
    {
        return AjaxResult.success(financeTradingProjectService.selectFinanceTradingProjectByFinanceId(financeId));
    }

    /**
     * 新增量化理财-理财产品信息
     */
//    @RequiresPermissions("gen:project:add")
    @Log(title = "量化理财-理财产品信息", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody FinanceTradingProject financeTradingProject)
    {
        return toAjax(financeTradingProjectService.save(financeTradingProject));
    }

    /**
     * 修改量化理财-理财产品信息
     */
//    @RequiresPermissions("gen:project:edit")
    @Log(title = "量化理财-理财产品信息", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody FinanceTradingProject financeTradingProject)
    {
        financeTradingProject.setUpdateTime(DateUtils.getTime());
        return toAjax(financeTradingProjectService.updateById(financeTradingProject));
    }

    /**
     * 删除量化理财-理财产品信息
     */
//    @RequiresPermissions("gen:project:remove")
    @Log(title = "量化理财-理财产品信息", businessType = BusinessType.DELETE)
	@GetMapping("remove/{financeIds}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] financeIds)
    {
        return toAjax(financeTradingProjectService.deleteFinanceTradingProjectByFinanceIds(financeIds));
    }
}
