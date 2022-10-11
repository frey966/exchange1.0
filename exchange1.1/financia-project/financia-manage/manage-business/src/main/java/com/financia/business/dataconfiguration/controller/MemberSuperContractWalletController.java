package com.financia.business.dataconfiguration.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.dataconfiguration.service.IMemberSuperContractWalletService;
import com.financia.exchange.MemberSuperContractWallet;
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
 * 超级杠杆-持仓Controller
 * 
 * @author 花生
 * @date 2022-09-07
 */
@RestController
@Api(tags = "超级杠杆-交易钱包")
@RequestMapping("/MemberSuperContractWallet")
public class MemberSuperContractWalletController extends BaseController
{
    @Autowired
    private IMemberSuperContractWalletService memberSuperContractWalletService;

    /**
     * 查询超级杠杆-持仓列表
     */
//    @RequiresPermissions("gen:wallet:list")
    @GetMapping("/list")
    @ApiOperation(value = "交易钱包列表",notes = "交易钱包列表")
    public TableDataInfo list(MemberSuperContractWallet memberSuperContractWallet)
    {
        startPage();
        List<MemberSuperContractWallet> list = memberSuperContractWalletService.selectMemberSuperContractWalletList(memberSuperContractWallet);
        return getDataTable(list);
    }

//    /**
//     * 导出超级杠杆-持仓列表
//     */
//    @RequiresPermissions("gen:wallet:export")
//    @Log(title = "超级杠杆-持仓", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberSuperContractWallet memberSuperContractWallet)
//    {
//        List<MemberSuperContractWallet> list = memberSuperContractWalletService.selectMemberSuperContractWalletList(memberSuperContractWallet);
//        ExcelUtil<MemberSuperContractWallet> util = new ExcelUtil<MemberSuperContractWallet>(MemberSuperContractWallet.class);
//        util.exportExcel(response, list, "超级杠杆-持仓数据");
//    }

    /**
     * 获取超级杠杆-持仓详细信息
     */
//    @RequiresPermissions("gen:wallet:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "交易钱包详情",notes = "交易钱包详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberSuperContractWalletService.selectMemberSuperContractWalletById(id));
    }

    /**
     * 新增超级杠杆-持仓
     */
//    @RequiresPermissions("gen:wallet:add")
    @Log(title = "超级杠杆-持仓", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "交易钱包新增",notes = "交易钱包新增")
    public AjaxResult add(@RequestBody MemberSuperContractWallet memberSuperContractWallet)
    {
        return toAjax(memberSuperContractWalletService.insertMemberSuperContractWallet(memberSuperContractWallet));
    }

    /**
     * 修改超级杠杆-持仓
     */
//    @RequiresPermissions("gen:wallet:edit")
    @Log(title = "超级杠杆-持仓", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "交易钱包修改",notes = "交易钱包修改")
    public AjaxResult edit(@RequestBody MemberSuperContractWallet memberSuperContractWallet)
    {
        return toAjax(memberSuperContractWalletService.updateMemberSuperContractWallet(memberSuperContractWallet));
    }

    /**
     * 删除超级杠杆-持仓
     */
//    @RequiresPermissions("gen:wallet:remove")
    @Log(title = "超级杠杆-持仓", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "交易钱包删除",notes = "交易钱包删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberSuperContractWalletService.deleteMemberSuperContractWalletByIds(ids));
    }
}
