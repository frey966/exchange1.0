package com.financia.business.member.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.member.service.IMemberAssetRecordsService;
import com.financia.exchange.MemberAssetRecords;
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
 * 会员-合约交易 杠杆交易 币币交易 资金移记录Controller
 * 
 * @author 花生
 * @date 2022-09-26
 */
@RestController
@RequestMapping("/memberAssetRecordsAdmin")
@Api(tags="交易订单管理-交易订单")
public class MemberAssetRecordsController extends BaseController
{
    @Autowired
    private IMemberAssetRecordsService memberAssetRecordsService;

    /**
     * 查询会员-合约交易 杠杆交易 币币交易 资金移记录列表
     */
//    @RequiresPermissions("gen:records:list")
    @GetMapping("/list")
    @ApiOperation(value = "订单列表",notes = "订单列表")
    public TableDataInfo list(MemberAssetRecords memberAssetRecords)
    {
        startPage();
        List<MemberAssetRecords> list = memberAssetRecordsService.selectMemberAssetRecordsList(memberAssetRecords);
        return getDataTable(list);
    }

//    /**
//     * 导出会员-合约交易 杠杆交易 币币交易 资金移记录列表
//     */
//    @RequiresPermissions("gen:records:export")
//    @Log(title = "会员-合约交易 杠杆交易 币币交易 资金移记录", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberAssetRecords memberAssetRecords)
//    {
//        List<MemberAssetRecords> list = memberAssetRecordsService.selectMemberAssetRecordsList(memberAssetRecords);
//        ExcelUtil<MemberAssetRecords> util = new ExcelUtil<MemberAssetRecords>(MemberAssetRecords.class);
//        util.exportExcel(response, list, "会员-合约交易 杠杆交易 币币交易 资金移记录数据");
//    }

//    /**
//     * 获取会员-合约交易 杠杆交易 币币交易 资金移记录详细信息
//     */
////    @RequiresPermissions("gen:records:query")
//    @GetMapping(value = "getInfo/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(memberAssetRecordsService.selectMemberAssetRecordsById(id));
//    }
//
//    /**
//     * 新增会员-合约交易 杠杆交易 币币交易 资金移记录
//     */
////    @RequiresPermissions("gen:records:add")
//    @Log(title = "会员-合约交易 杠杆交易 币币交易 资金移记录", businessType = BusinessType.INSERT)
//    @PostMapping("add")
//    public AjaxResult add(@RequestBody MemberAssetRecords memberAssetRecords)
//    {
//        return toAjax(memberAssetRecordsService.insertMemberAssetRecords(memberAssetRecords));
//    }
//
//    /**
//     * 修改会员-合约交易 杠杆交易 币币交易 资金移记录
//     */
////    @RequiresPermissions("gen:records:edit")
//    @Log(title = "会员-合约交易 杠杆交易 币币交易 资金移记录", businessType = BusinessType.UPDATE)
//    @PostMapping("edit")
//    public AjaxResult edit(@RequestBody MemberAssetRecords memberAssetRecords)
//    {
//        return toAjax(memberAssetRecordsService.updateMemberAssetRecords(memberAssetRecords));
//    }
//
//    /**
//     * 删除会员-合约交易 杠杆交易 币币交易 资金移记录
//     */
////    @RequiresPermissions("gen:records:remove")
//    @Log(title = "会员-合约交易 杠杆交易 币币交易 资金移记录", businessType = BusinessType.DELETE)
//	@GetMapping("remove/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(memberAssetRecordsService.deleteMemberAssetRecordsByIds(ids));
//    }
}
