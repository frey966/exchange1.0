package com.financia.business.member.controller;

import java.util.List;

import com.financia.business.MemberCoin;
import com.financia.business.member.service.IMemberCoinService;
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
 * 会员数字货币和法币关系中间Controller
 * 
 * @author 花生
 * @date 2022-08-04
 */
@Api(tags="会员管理-会员数字货币和法币关系")
@RestController
@RequestMapping("/membercoin")
public class MemberCoinController extends BaseController
{
    @Autowired
    private IMemberCoinService memberCoinService;

    /**
     * 查询会员数字货币和法币关系中间列表
     */
    @ApiOperation(value = "查询列表",notes = "查询列表")
//    @RequiresPermissions("system:coin:list")
    @GetMapping("/list")
    public TableDataInfo list(MemberCoin memberCoin)
    {
        startPage();
        List<MemberCoin> list = memberCoinService.selectMemberCoinList(memberCoin);
        return getDataTable(list);
    }

//    /**
//     * 导出会员数字货币和法币关系中间列表
//     */
//    @ApiOperation(value = "导出会员数字货币和法币关系中间列表",notes = "导出会员数字货币和法币关系中间列表")
//    @RequiresPermissions("system:coin:export")
//    @Log(title = "会员数字货币和法币关系中间", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberCoin memberCoin)
//    {
//        List<MemberCoin> list = memberCoinService.selectMemberCoinList(memberCoin);
//        ExcelUtil<MemberCoin> util = new ExcelUtil<MemberCoin>(MemberCoin.class);
//        util.exportExcel(response, list, "会员数字货币和法币关系中间数据");
//    }

    /**
     * 获取会员数字货币和法币关系中间详细信息
     */
    @ApiOperation(value = "获取详细信息",notes = "获取详细信息")
//    @RequiresPermissions("system:coin:query")
    @GetMapping(value = "getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberCoinService.selectMemberCoinById(id));
    }

    /**
     * 新增会员数字货币和法币关系中间
     */
    @ApiOperation(value = "新增",notes = "新增")
//    @RequiresPermissions("system:coin:add")
    @Log(title = "会员数字货币和法币关系中间", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody MemberCoin memberCoin)
    {
        return toAjax(memberCoinService.insertMemberCoin(memberCoin));
    }

    /**
     * 修改会员数字货币和法币关系中间
     */
    @ApiOperation(value = "修改",notes = "修改")
//    @RequiresPermissions("system:coin:edit")
    @Log(title = "会员数字货币和法币关系中间", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody MemberCoin memberCoin)
    {
        return toAjax(memberCoinService.updateMemberCoin(memberCoin));
    }

//    /**
//     * 删除会员数字货币和法币关系中间
//     */
//    @ApiOperation(value = "删除",notes = "删除")
////    @RequiresPermissions("system:coin:remove")
//    @Log(title = "会员数字货币和法币关系中间", businessType = BusinessType.DELETE)
//	@GetMapping("remove/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(memberCoinService.deleteMemberCoinByIds(ids));
//    }
}
