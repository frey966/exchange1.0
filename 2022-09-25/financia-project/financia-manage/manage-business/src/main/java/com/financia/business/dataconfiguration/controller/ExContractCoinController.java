package com.financia.business.dataconfiguration.controller;

import com.financia.business.ExContractCoin;
import com.financia.business.dataconfiguration.service.ExContractOrderEntrustService;
import com.financia.business.dataconfiguration.service.IExContractCoinService;
import com.financia.business.member.service.IMemberService;
import com.financia.common.PVerificationCode;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.exchange.Member;
import com.financia.swap.ExContractOrderEntrust;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import icu.mhb.mybatisplus.plugln.core.JoinWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合约币种列Controller
 *
 * @author 花生
 * @date 2022-08-03
 */
@Api(tags = "合约管理-合约订单管理")
@RestController
@RequestMapping("/exContractCoin")
public class ExContractCoinController extends BaseController
{
    @Autowired
    private IExContractCoinService exContractCoinService;

    @Autowired
    private ExContractOrderEntrustService contractOrderEntrustService;

    @Autowired
    private IMemberService memberService;

    /**
     *
     */
    @ApiOperation(value = "合约订单列表",notes = "合约订单列表")
//    @RequiresPermissions("system:coin:list")
    @GetMapping("/orderList")
    public TableDataInfo orderList(ExContractOrderEntrust exContractOrderEntrust)
    {
        JoinLambdaWrapper<ExContractOrderEntrust> exContractOrderEntrustQueryWrapper = new JoinLambdaWrapper<>(ExContractOrderEntrust.class);
        JoinWrapper<Member, ExContractOrderEntrust> memberName = exContractOrderEntrustQueryWrapper.leftJoin(Member.class, Member::getId, ExContractOrderEntrust::getMemberId)
                                                                                                    .selectAs(Member::getUsername, "member_name");
        if (exContractOrderEntrust.getMemberId() != null) {
            exContractOrderEntrustQueryWrapper.eq(ExContractOrderEntrust::getMemberId,exContractOrderEntrust.getMemberId());
        }
        if (!StringUtils.isEmpty(exContractOrderEntrust.getMemberName())) {
            memberName.eq(Member::getUsername,exContractOrderEntrust.getMemberName());
        }
        exContractOrderEntrustQueryWrapper.orderByDesc(ExContractOrderEntrust::getCreateTime);
        memberName.end();
        startPage();
        List<ExContractOrderEntrust> list = contractOrderEntrustService.joinList(exContractOrderEntrustQueryWrapper,ExContractOrderEntrust.class);
        return getDataTable(list);
    }

    @ApiOperation(value = "合约订单详情",notes = "合约订单详情")
//    @RequiresPermissions("system:coin:list")
    @GetMapping("/orderInfo/{id}")
    public AjaxResult orderInfo(@PathVariable("id") Long id)
    {
        ExContractOrderEntrust byId = contractOrderEntrustService.getById(id);
        Member byId1 = memberService.getById(byId.getMemberId());
        byId.setMemberName(byId1 == null? null: byId1.getUsername());
        return AjaxResult.success(byId);
    }
    /**
     * 查询合约币种列列表
     */
    @ApiOperation(value = "查询合约币种列列表",notes = "查询合约币种列列表")
//    @RequiresPermissions("system:coin:list")
    @GetMapping("/list")
    public TableDataInfo list(ExContractCoin exContractCoin)
    {
        startPage();
        List<ExContractCoin> list = exContractCoinService.selectExContractCoinList(exContractCoin);
        return getDataTable(list);
    }

//    /**
//     * 导出合约币种列列表
//     */
//    @ApiOperation(value = "导出合约币种列列表",notes = "导出合约币种列列表")
////    @RequiresPermissions("system:coin:export")
//    @Log(title = "合约币种列", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, ExContractCoin exContractCoin)
//    {
//        List<ExContractCoin> list = exContractCoinService.selectExContractCoinList(exContractCoin);
//        ExcelUtil<ExContractCoin> util = new ExcelUtil<ExContractCoin>(ExContractCoin.class);
//        util.exportExcel(response, list, "合约币种列数据");
//    }

    /**
     * 获取合约币种列详细信息
     */
    @ApiOperation(value = "获取合约币种列详细信息",notes = "获取合约币种列详细信息")
//    @RequiresPermissions("system:coin:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(exContractCoinService.selectExContractCoinById(id));
    }

    /**
     * 新增合约币种列
     */
    @ApiOperation(value = "新增合约币种列",notes = "新增合约币种列")
//    @RequiresPermissions("system:coin:add")
    @Log(title = "合约币种列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ExContractCoin exContractCoin)
    {
        boolean save = exContractCoinService.save(exContractCoin);
        return toAjax(save);
    }

    /**
     * 修改合约币种列
     */
    @ApiOperation(value = "修改合约币种列",notes = "修改合约币种列")
//    @RequiresPermissions("system:coin:edit")
    @Log(title = "合约币种列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody ExContractCoin exContractCoin)
    {
        ExContractCoin exContractCoinUpdate = new ExContractCoin();
        exContractCoinUpdate.setId(exContractCoin.getId());
//        exContractCoinUpdate
        return toAjax(exContractCoinService.updateExContractCoin(exContractCoin));
    }

    /**
     * 删除合约币种列
     */
    @ApiOperation(value = "删除合约币种列",notes = "删除合约币种列")
//    @RequiresPermissions("system:coin:remove")
    @Log(title = "合约币种列", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(exContractCoinService.deleteExContractCoinByIds(ids));
    }
}
