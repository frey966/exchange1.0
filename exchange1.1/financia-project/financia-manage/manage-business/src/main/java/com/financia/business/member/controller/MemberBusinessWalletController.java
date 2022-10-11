package com.financia.business.member.controller;

import java.util.List;

import com.financia.business.member.service.IMemberBusinessWalletService;
import com.financia.exchange.MemberBusinessWallet;
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
 * 会员-业务钱包金额Controller
 * 
 * @author 花生
 * @date 2022-08-11
 */
@RestController
@RequestMapping("/memberbusinesswallet")
@Api(tags="会员管理-会员信息管理--余额信息")
public class MemberBusinessWalletController extends BaseController
{

    @Autowired
    private  IMemberBusinessWalletService memberBusinessWalletService;

    /**
     * 查询会员-业务钱包金额列表
     */
//    @RequiresPermissions("system:wallet:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(MemberBusinessWallet memberBusinessWallet)
    {
        startPage();
        List<MemberBusinessWallet> list = memberBusinessWalletService.selectMemberBusinessWalletList(memberBusinessWallet);
        return getDataTable(list);
    }

//    /**
//     * 导出会员-业务钱包金额列表
//     */
//    @RequiresPermissions("system:wallet:export")
//    @Log(title = "会员-业务钱包金额", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberBusinessWallet memberBusinessWallet)
//    {
//        List<MemberBusinessWallet> list = memberBusinessWalletService.selectMemberBusinessWalletList(memberBusinessWallet);
//        ExcelUtil<MemberBusinessWallet> util = new ExcelUtil<MemberBusinessWallet>(MemberBusinessWallet.class);
//        util.exportExcel(response, list, "会员-业务钱包金额数据");
//    }

    /**
     * 获取会员-业务钱包金额详细信息
     */
//    @RequiresPermissions("system:wallet:query")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "查询详细信息",notes = "查询详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberBusinessWalletService.selectMemberBusinessWalletById(id));
    }

    /**
     * 新增会员-业务钱包金额
     */
//    @RequiresPermissions("system:wallet:add")
    @Log(title = "会员-业务钱包金额", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody MemberBusinessWallet memberBusinessWallet)
    {
        return toAjax(memberBusinessWalletService.insertMemberBusinessWallet(memberBusinessWallet));
    }

    /**
     * 修改会员-业务钱包金额
     */
//    @RequiresPermissions("system:wallet:edit")
    @Log(title = "会员-业务钱包金额", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody MemberBusinessWallet memberBusinessWallet)
    {
        return toAjax(memberBusinessWalletService.updateMemberBusinessWallet(memberBusinessWallet));
    }

    /**
     * 删除会员-业务钱包金额
     */
    @RequiresPermissions("system:wallet:remove")
    @Log(title = "会员-业务钱包金额", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberBusinessWalletService.deleteMemberBusinessWalletByIds(ids));
    }
}
