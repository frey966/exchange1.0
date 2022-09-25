package com.financia.business.member.controller;

import java.util.List;

import com.financia.business.member.service.IMemberWithdrawInfoService;
import com.financia.exchange.MemberWithdrawInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.security.annotation.RequiresPermissions;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 公共-会员提现汇总Controller
 * 
 * @author 花生
 * @date 2022-08-11
 */
@RestController
@RequestMapping("/memberwithdrawinfocontroller")
@Api(tags="会员-会员提现汇总")
public class MemberWithdrawInfoController extends BaseController
{
    @Autowired
    private IMemberWithdrawInfoService memberWithdrawInfoService;

    /**
     * 查询公共-会员提现汇总列表
     */
//    @RequiresPermissions("system:info:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(MemberWithdrawInfo memberWithdrawInfo)
    {
        startPage();
        List<MemberWithdrawInfo> list = memberWithdrawInfoService.selectMemberWithdrawInfoList(memberWithdrawInfo);
        return getDataTable(list);
    }

//    /**
//     * 导出公共-会员提现汇总列表
//     */
//    @RequiresPermissions("system:info:export")
//    @Log(title = "公共-会员提现汇总", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, MemberWithdrawInfo memberWithdrawInfo)
//    {
//        List<MemberWithdrawInfo> list = memberWithdrawInfoService.selectMemberWithdrawInfoList(memberWithdrawInfo);
//        ExcelUtil<MemberWithdrawInfo> util = new ExcelUtil<MemberWithdrawInfo>(MemberWithdrawInfo.class);
//        util.exportExcel(response, list, "公共-会员提现汇总数据");
//    }

    /**
     * 获取公共-会员提现汇总详细信息
     */
//    @RequiresPermissions("system:info:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取详细信息",notes = "获取详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberWithdrawInfoService.selectMemberWithdrawInfoById(id));
    }

    /**
     * 新增公共-会员提现汇总
     */
    @RequiresPermissions("system:info:add")
    @Log(title = "公共-会员提现汇总", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody MemberWithdrawInfo memberWithdrawInfo)
    {
        return toAjax(memberWithdrawInfoService.insertMemberWithdrawInfo(memberWithdrawInfo));
    }

    /**
     * 修改公共-会员提现汇总
     */
//    @RequiresPermissions("system:info:edit")
    @Log(title = "公共-会员提现汇总", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody MemberWithdrawInfo memberWithdrawInfo)
    {
        return toAjax(memberWithdrawInfoService.updateMemberWithdrawInfo(memberWithdrawInfo));
    }

    /**
     * 删除公共-会员提现汇总
     */
//    @RequiresPermissions("system:info:remove")
    @Log(title = "公共-会员提现汇总", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberWithdrawInfoService.deleteMemberWithdrawInfoByIds(ids));
    }
}
