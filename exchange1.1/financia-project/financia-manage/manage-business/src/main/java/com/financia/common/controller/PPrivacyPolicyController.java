package com.financia.common.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.common.PPrivacyPolicy;
import com.financia.common.service.IPPrivacyPolicyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.security.annotation.RequiresPermissions;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.utils.poi.ExcelUtil;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 隐私政策Controller
 * 
 * @author 花生
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/policy")
@Api(tags="内容管理-隐私政策")
public class PPrivacyPolicyController extends BaseController
{
    @Autowired
    private IPPrivacyPolicyService pPrivacyPolicyService;

    /**
     * 查询隐私政策列表
     */
//    @RequiresPermissions("gen:policy:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询隐私政策列表",notes = "查询隐私政策列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(PPrivacyPolicy pPrivacyPolicy)
    {
        startPage();
        List<PPrivacyPolicy> list = pPrivacyPolicyService.selectPPrivacyPolicyList(pPrivacyPolicy);
        return getDataTable(list);
    }

//    /**
//     * 导出隐私政策列表
//     */
//    @RequiresPermissions("gen:policy:export")
//    @Log(title = "隐私政策", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, PPrivacyPolicy pPrivacyPolicy)
//    {
//        List<PPrivacyPolicy> list = pPrivacyPolicyService.selectPPrivacyPolicyList(pPrivacyPolicy);
//        ExcelUtil<PPrivacyPolicy> util = new ExcelUtil<PPrivacyPolicy>(PPrivacyPolicy.class);
//        util.exportExcel(response, list, "隐私政策数据");
//    }

    /**
     * 获取隐私政策详细信息
     */
//    @RequiresPermissions("gen:policy:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取隐私政策详细信息",notes = "获取隐私政策详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pPrivacyPolicyService.selectPPrivacyPolicyById(id));
    }

    /**
     * 新增隐私政策
     */
//    @RequiresPermissions("gen:policy:add")
    @Log(title = "隐私政策", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody PPrivacyPolicy pPrivacyPolicy)
    {
        return toAjax(pPrivacyPolicyService.insertPPrivacyPolicy(pPrivacyPolicy));
    }

    /**
     * 修改隐私政策
     */
//    @RequiresPermissions("gen:policy:edit")
    @Log(title = "隐私政策", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody PPrivacyPolicy pPrivacyPolicy)
    {
        return toAjax(pPrivacyPolicyService.updatePPrivacyPolicy(pPrivacyPolicy));
    }

    /**
     * 删除隐私政策
     */
//    @RequiresPermissions("gen:policy:remove")
    @Log(title = "隐私政策", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pPrivacyPolicyService.deletePPrivacyPolicyByIds(ids));
    }
}
