package com.financia.business.member.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.MemberBank;
import com.financia.business.member.service.IMemberBankService;
import io.swagger.annotations.Api;
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
 * memberBankController
 * 
 * @author 花生
 * @date 2022-08-17
 */
@Api(tags="会员管理-会员收款账户")
@RestController
@RequestMapping("/memberbank")
public class MemberBankController extends BaseController
{
    @Autowired
    private IMemberBankService memberBankService;

    /**
     * 查询memberBank列表
     */
//    @RequiresPermissions("gen:bank:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(MemberBank memberBank)
    {
        startPage();
        List<MemberBank> list = memberBankService.selectMemberBankList(memberBank);
        return getDataTable(list);
    }

//    /**
//     * 导出memberBank列表
//     */
//    @RequiresPermissions("gen:bank:export")
//    @Log(title = "memberBank", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberBank memberBank)
//    {
//        List<MemberBank> list = memberBankService.selectMemberBankList(memberBank);
//        ExcelUtil<MemberBank> util = new ExcelUtil<MemberBank>(MemberBank.class);
//        util.exportExcel(response, list, "memberBank数据");
//    }

    /**
     * 获取memberBank详细信息
     */
//    @RequiresPermissions("gen:bank:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取收款账户详细信息",notes = "获取收款账户详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberBankService.selectMemberBankById(id));
    }

    /**
     * 新增memberBank
     */
//    @RequiresPermissions("gen:bank:add")
    @Log(title = "memberBank", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody MemberBank memberBank)
    {
        return toAjax(memberBankService.insertMemberBank(memberBank));
    }

    /**
     * 修改memberBank
     */
//    @RequiresPermissions("gen:bank:edit")
    @Log(title = "memberBank", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody MemberBank memberBank)
    {
        return toAjax(memberBankService.updateMemberBank(memberBank));
    }

    /**
     * 删除memberBank
     */
//    @RequiresPermissions("gen:bank:remove")
    @Log(title = "memberBank", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberBankService.deleteMemberBankByIds(ids));
    }
}
