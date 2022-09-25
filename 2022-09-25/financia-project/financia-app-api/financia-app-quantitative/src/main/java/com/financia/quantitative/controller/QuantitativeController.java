package com.financia.quantitative.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.finance.FinanceDepositMember;
import com.financia.finance.FinanceTradingProject;
import com.financia.quantitative.service.FinanceDepositMemberService;
import com.financia.quantitative.service.FinanceInterestMemberService;
import com.financia.quantitative.service.FinanceTradingProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 登陆
 */
@Api(tags = "APP量化理财模块")
@RestController
@RequestMapping("/quantitative")
@Slf4j
public class QuantitativeController extends BaseController {

    @Autowired
    private FinanceTradingProjectService projectService;
    @Autowired
    private FinanceDepositMemberService financeDepositMemberService;
    @Autowired
    private FinanceInterestMemberService financeInterestMemberService;
    @Autowired
    private RemoteMemberWalletService remoteMemberWalletService;

    @GetMapping(value = "/home/list")
    @ApiOperation(value = "首页量化理财列表", notes = "首页量化理财列表")
    public AjaxResult list() {
        List<FinanceTradingProject> list = projectService.list(new QueryWrapper<FinanceTradingProject>()
                .lambda().eq(FinanceTradingProject::getIshome, 0).eq(FinanceTradingProject::getActive, 1));
        String lang = LocaleContextHolder.getLocale().toString();
        if ("zh".equals(lang)) {
            for (FinanceTradingProject project : list) {
                project.setFinanceEnName(project.getFinanceZhName());
            }
        }
        return AjaxResult.success(list);
    }

    @GetMapping(value = "/listPage")
    @ApiOperation(value = "量化理财列表", notes = "量化理财列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Long.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataTypeClass = Long.class, paramType = "query", example = "10")
    })
    public TableDataInfo listPage() {
        List<FinanceTradingProject> list = projectService.list(new QueryWrapper<FinanceTradingProject>()
                .lambda().eq(FinanceTradingProject::getActive, 1));
        String lang = LocaleContextHolder.getLocale().toString();
        if ("zh".equals(lang)) {
            for (FinanceTradingProject project : list) {
                project.setFinanceEnName(project.getFinanceZhName());
            }
        }
        return getDataTable(list);
    }

    @GetMapping(value = "/info")
    @ApiOperation(value = "量化理财详情", notes = "量化理财列表")
    public AjaxResult info(Long id) {
        FinanceTradingProject one = projectService.getOne(new QueryWrapper<FinanceTradingProject>().lambda().eq(FinanceTradingProject::getFinanceId, id));
        return AjaxResult.success(one);
    }

    @PostMapping(value = "/buy")
    @ApiOperation(value = "购买", notes = "购买量化理财{financeId: '理财id',purchase: '购买金额'}")
    public AjaxResult buy(@RequestBody Map<String, Object> param) {
        Long userId = getUserId();
        if (param.get("financeId") == null) {
            return error("is financeId not null");
        }
        if (param.get("purchase") == null) {
            return error("is purchase ont null");
        }
        long financeId = Long.parseLong(param.get("financeId").toString());
        BigDecimal purchase = new BigDecimal(param.get("purchase").toString());
        AjaxResult ajaxResult = financeDepositMemberService.buyFinance(financeId, userId, purchase);
        return ajaxResult;
    }

    @PostMapping(value = "/redemption")
    @ApiOperation(value = "赎回", notes = "赎回量化理财{id: 'id'}")
    public AjaxResult redemption(@RequestBody Map<String, Object> param) {
        if (param.get("id") == null) {
            return error("is id not null");
        }
        boolean rs = financeDepositMemberService.sale(Long.parseLong(param.get("id").toString()));
        if (rs) {
            return AjaxResult.success();
        }
        return AjaxResult.error("redemption fail");
    }

    @GetMapping(value = "/test")
    @ApiOperation(value = "test", notes = "test")
    public FinanceTradingProject test(Map<String, Object> param) {
        return new FinanceTradingProject();
    }

    @GetMapping(value = "/test2")
    @ApiOperation(value = "test", notes = "test")
    public FinanceDepositMember test2(Map<String, Object> param) {
        return new FinanceDepositMember();
    }

    @GetMapping(value = "/testDs")
    @ApiOperation(value = "testDs", notes = "test")
    public Boolean testDs(Integer d) throws Exception {
        LocalDate localDate = LocalDate.now().minusDays(d);
        financeInterestMemberService.interestCalculated(localDate);
        return true;
    }

    @GetMapping(value = "/position")
    @ApiOperation(value = "持仓中", notes = "持仓中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Long.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataTypeClass = Long.class, paramType = "query", example = "10")
    })
    public TableDataInfo position() {
        Long userId = getUserId();
        startPage();
        List<FinanceDepositMember> list = financeDepositMemberService.list(new QueryWrapper<FinanceDepositMember>()
                .lambda().eq(FinanceDepositMember::getSettleAccountsStatus, 0).eq(FinanceDepositMember::getMemberId, userId));
        for (FinanceDepositMember depositMember : list) {
            depositMember.setFinanceName(depositMember.getFinanceZhName());
        }
        return getDataTable(list);
    }

    @GetMapping(value = "/history")
    @ApiOperation(value = "历史持仓", notes = "历史持仓")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Long.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataTypeClass = Long.class, paramType = "query", example = "10")
    })
    public TableDataInfo history() {
        Long userId = getUserId();
        startPage();
        List<FinanceDepositMember> list = financeDepositMemberService.list(new QueryWrapper<FinanceDepositMember>()
                .lambda().eq(FinanceDepositMember::getSettleAccountsStatus, 1).eq(FinanceDepositMember::getMemberId, userId));
        for (FinanceDepositMember depositMember : list) {
            depositMember.setFinanceName(depositMember.getFinanceZhName());
        }
        return getDataTable(list);
    }

    @GetMapping(value = "/statistical")
    @ApiOperation(value = "统计", notes = "统计 返回： matures：到期收益，revenue总收益，yesterday昨日收益")
    public AjaxResult statistical() {
        Long userId = getUserId();
        Map<String, BigDecimal> rs = new HashMap<>();
        BigDecimal matures = financeDepositMemberService.matures(userId);
        BigDecimal revenue = financeDepositMemberService.revenue(userId);
        BigDecimal yesterday = financeInterestMemberService.yesterday(userId);
        BigDecimal totalDeposit = financeDepositMemberService.totalDeposit(userId);

        rs.put("matures", matures == null ? new BigDecimal(0) : matures.setScale(2, RoundingMode.DOWN));
        rs.put("revenue", revenue == null ? new BigDecimal(0) : revenue.setScale(2, RoundingMode.DOWN));
        rs.put("yesterday", yesterday == null ? new BigDecimal(0) : yesterday.setScale(2, RoundingMode.DOWN));
        rs.put("totalDeposit", totalDeposit == null ? new BigDecimal(0) : totalDeposit.setScale(2, RoundingMode.DOWN));
        rs.put("legalTotalDeposit", remoteMemberWalletService.convertFiatCurrencyFeign(totalDeposit, userId));
        return AjaxResult.success(rs);
    }

    @PostMapping(value = "/calculateInterest")
    @ApiOperation(value = "每日计算利息", notes = "")
    public Boolean calculateInterest() {
        financeInterestMemberService.interestCalculated();
        return true;
    }


    @PostMapping(value = "/positionFeign")
    @ApiOperation(value = "持仓总金额", notes = "持仓总金额")
    public BigDecimal positionFeign(Long id) {
        List<FinanceDepositMember> list = financeDepositMemberService.list(new QueryWrapper<FinanceDepositMember>()
                .lambda().eq(FinanceDepositMember::getSettleAccountsStatus, 0).eq(FinanceDepositMember::getMemberId, id));
        return list.stream().map(FinanceDepositMember::getDepositAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    @PostMapping(value = "/test")
    @ApiOperation(value = "test", notes = "")
    public Boolean test() {
        Boolean c = remoteMemberWalletService.updateAddBalance(79l, new BigDecimal(100), "test0001", BusinessSubType.RECHARGE_ADD, "fegin test");
        return c;
    }

}
