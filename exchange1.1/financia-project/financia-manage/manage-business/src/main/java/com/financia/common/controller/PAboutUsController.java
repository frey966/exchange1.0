package com.financia.common.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.common.AboutUs;
import com.financia.common.service.IPAboutUsService;
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
 * 个人中心-关于我们Controller
 * 
 * @author 花生
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/paboutus")
@Api(tags="内容管理-关于我们")
public class PAboutUsController extends BaseController
{
    @Autowired
    private IPAboutUsService pAboutUsService;

    /**
     * 查询个人中心-关于我们列表
     */
//    @RequiresPermissions("gen:us:list")
    @GetMapping("/list")
    @ApiOperation(value = "关于我们列表",notes = "关于我们列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(AboutUs pAboutUs)
    {
        startPage();
        List<AboutUs> list = pAboutUsService.selectPAboutUsList(pAboutUs);
        return getDataTable(list);
    }

//    /**
//     * 导出个人中心-关于我们列表
//     */
////    @RequiresPermissions("gen:us:export")
//    @Log(title = "个人中心-关于我们", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, PAboutUs pAboutUs)
//    {
//        List<PAboutUs> list = pAboutUsService.selectPAboutUsList(pAboutUs);
//        ExcelUtil<PAboutUs> util = new ExcelUtil<PAboutUs>(PAboutUs.class);
//        util.exportExcel(response, list, "个人中心-关于我们数据");
//    }

    /**
     * 获取个人中心-关于我们详细信息
     */
//    @RequiresPermissions("gen:us:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "关于我们详细信息",notes = "关于我们详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pAboutUsService.selectPAboutUsById(id));
    }

    /**
     * 新增个人中心-关于我们
     */
//    @RequiresPermissions("gen:us:add")
    @Log(title = "个人中心-关于我们", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody AboutUs pAboutUs)
    {
        return toAjax(pAboutUsService.insertPAboutUs(pAboutUs));
    }

    /**
     * 修改个人中心-关于我们
     */
//    @RequiresPermissions("gen:us:edit")
    @Log(title = "个人中心-关于我们", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody AboutUs pAboutUs)
    {
        return toAjax(pAboutUsService.updatePAboutUs(pAboutUs));
    }

    /**
     * 删除个人中心-关于我们
     */
//    @RequiresPermissions("gen:us:remove")
    @Log(title = "个人中心-关于我们", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pAboutUsService.deletePAboutUsByIds(ids));
    }
}
