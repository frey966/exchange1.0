package com.financia.business.member.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import com.financia.business.member.service.IAgenRelationService;
import com.financia.exchange.AgenRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 代理和会员关系
系统用户和代理关系Controller
 * 
 * @author 花生
 * @date 2022-08-04
 */
@Api(tags="邀请管理-代理邀请设置")
@RestController
@RequestMapping("/relation")
public class AgenRelationController extends BaseController
{
    @Autowired
    private IAgenRelationService agenRelationService;

    /**
     * 查询代理和会员关系
     */
    @ApiOperation(value = "查询代理和会员关系列表",notes = "查询代理和会员关系列表")
//    @RequiresPermissions("system:relation:list")
    @GetMapping("/list")
    public TableDataInfo list(AgenRelation agenRelation)
    {
        startPage();
        List<AgenRelation> list = agenRelationService.selectAgenRelationList(agenRelation);
        return getDataTable(list);
    }

//    /**
//     * 导出代理和会员关系
//系统用户和代理关系列表
//     */
//    @RequiresPermissions("system:relation:export")
//    @Log(title = "代理和会员关系 系统用户和代理关系", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, AgenRelation agenRelation)
//    {
//        List<AgenRelation> list = agenRelationService.selectAgenRelationList(agenRelation);
//        ExcelUtil<AgenRelation> util = new ExcelUtil<AgenRelation>(AgenRelation.class);
//        util.exportExcel(response, list, "代理和会员关系 系统用户和代理关系数据");
//    }

    /**
     * 获取代理和会员关系
     */
//    @RequiresPermissions("system:relation:query")
    @ApiOperation(value = "获取代理和会员关系",notes = "获取代理和会员关系")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(agenRelationService.selectAgenRelationById(id));
    }

    /**
     * 新增代理和会员关系
     */
//    @RequiresPermissions("system:relation:add")
    @ApiOperation(value = "新增代理和会员关系",notes = "新增代理和会员关系")
//    @Log(title = "代理和会员关系 系统用户和代理关系", businessType = BusinessType.INSERT)
    @PostMapping(value = "add")
    public AjaxResult add(@RequestBody AgenRelation agenRelation)
    {
        AjaxResult ajaxResult=new AjaxResult();
        try{
            return toAjax(agenRelationService.insertAgenRelation(agenRelation));
        }catch (Exception e){
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String sqlState = ((SQLIntegrityConstraintViolationException) cause).getSQLState();
                ajaxResult=new AjaxResult(201,"表中已存"+agenRelation.getUserName()+""+agenRelation.getInviteCode());
            }
        }finally {
            return ajaxResult;
        }
    }

    /**
     * 修改代理和会员关系
     */
//    @RequiresPermissions("system:relation:edit")
    @ApiOperation(value = "修改代理和会员关系",notes = "修改代理和会员关系")
//    @Log(title = "代理和会员关系 系统用户和代理关系", businessType = BusinessType.UPDATE)
    @PostMapping(value = "edit")
    public AjaxResult edit(@RequestBody AgenRelation agenRelation)
    {
        AjaxResult ajaxResult=new AjaxResult();
        try{
            return toAjax(agenRelationService.updateAgenRelation(agenRelation));
        }catch (Exception e){
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String sqlState = ((SQLIntegrityConstraintViolationException) cause).getSQLState();
                ajaxResult=new AjaxResult(201,"表中已存"+agenRelation.getUserName()+""+agenRelation.getInviteCode());
            }
        }finally {
            return ajaxResult;
        }
    }

//    /**
//     * 删除代理和会员关系  逻辑删除（修改删除状态）
//     */
////    @RequiresPermissions("system:relation:remove")
//    @ApiOperation(value = "删除代理和会员关系",notes = "删除代理和会员关系")
//    @Log(title = "代理和会员关系 系统用户和代理关系", businessType = BusinessType.DELETE)
//	@PostMapping("remove")
//    public AjaxResult remove(@RequestBody AgenRelation agenRelation)
//    {
//        agenRelation.setStatus(0);
//        return toAjax(agenRelationService.updateAgenRelation(agenRelation));
//    }
}
