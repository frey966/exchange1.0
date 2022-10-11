package com.financia.business.member.controller;

import com.financia.business.MemberContractWalletEntity;
import com.financia.business.member.service.IMemberService;
import com.financia.business.member.service.MemberContractWalletService;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.exchange.Member;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会员-合约钱包Controller
 *
 * @author
 * @date
 */
@RestController
@RequestMapping("/membercontractwallet")
@Api(tags="会员管理-会员信息管理--合约钱包信息")
public class MemberContractWalletController extends BaseController
{

    @Autowired
    private MemberContractWalletService memberContractWalletService;
    @Autowired
    private IMemberService memberService;

    /**
     * 合约钱包列表
     */
//    @RequiresPermissions("system:wallet:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(MemberContractWalletEntity memberContractWallet)
    {
        startPage();
        JoinLambdaWrapper<MemberContractWalletEntity> wrapper = new JoinLambdaWrapper<>(MemberContractWalletEntity.class);
        wrapper.leftJoin(Member.class,Member::getId, MemberContractWalletEntity::getMemberId)
               .selectAs(Member::getUsername,"member_name").end();
        List<MemberContractWalletEntity> list = memberContractWalletService.joinList(wrapper,MemberContractWalletEntity.class);
        return getDataTable(list);
    }

    @ApiOperation(value = "合约钱包详情",notes = "合约钱包详情")
//    @RequiresPermissions("system:coin:list")
    @GetMapping("/info/{id}")
    public AjaxResult orderInfo(@PathVariable("id") Long id)
    {
        MemberContractWalletEntity byId = memberContractWalletService.getById(id);
        Member byId1 = memberService.getById(byId.getMemberId());
        byId.setMemberName(byId1 == null? null: byId1.getUsername());
        return AjaxResult.success(byId);
    }
}
