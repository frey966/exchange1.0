package com.financia.business.member.controller;

import java.util.List;

import com.financia.business.member.service.IMemberWalletAddressService;
import com.financia.exchange.MemberWalletAddress;
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
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 公链钱包Controller
 * 
 * @author 花生
 * @date 2022-08-11
 */
@RestController
@RequestMapping("/memberwalletaddress")
@Api(tags = "会员管理-充值记录")//链
public class MemberWalletAddressController extends BaseController
{
    @Autowired
    private IMemberWalletAddressService memberWalletAddressService;

    /**
     * 查询公链钱包列表
     */
//    @RequiresPermissions("system:address:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(MemberWalletAddress memberWalletAddress)
    {
        startPage();
        List<MemberWalletAddress> list = memberWalletAddressService.selectMemberWalletAddressList(memberWalletAddress);
        return getDataTable(list);
    }

//    /**
//     * 导出公链钱包列表
//     */
////    @RequiresPermissions("system:address:export")
//    @Log(title = "公链钱包", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, MemberWalletAddress memberWalletAddress)
//    {
//        List<MemberWalletAddress> list = memberWalletAddressService.selectMemberWalletAddressList(memberWalletAddress);
//        ExcelUtil<MemberWalletAddress> util = new ExcelUtil<MemberWalletAddress>(MemberWalletAddress.class);
//        util.exportExcel(response, list, "公链钱包数据");
//    }

    /**
     * 获取公链钱包详细信息
     */
//    @RequiresPermissions("system:address:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取公链钱包详细信息",notes = "获取公链钱包详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberWalletAddressService.selectMemberWalletAddressById(id));
    }

    /**
     * 新增公链钱包
     */
//    @RequiresPermissions("system:address:add")
    @Log(title = "公链钱包", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody MemberWalletAddress memberWalletAddress)
    {
        return toAjax(memberWalletAddressService.insertMemberWalletAddress(memberWalletAddress));
    }

    /**
     * 修改公链钱包
     */
//    @RequiresPermissions("system:address:edit")
    @Log(title = "公链钱包", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody MemberWalletAddress memberWalletAddress)
    {
        return toAjax(memberWalletAddressService.updateMemberWalletAddress(memberWalletAddress));
    }

    /**
     * 删除公链钱包
     */
//    @RequiresPermissions("system:address:remove")
    @Log(title = "公链钱包", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberWalletAddressService.deleteMemberWalletAddressByIds(ids));
    }
}
