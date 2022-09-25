package com.financia.quantitative.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.finance.FinanceDepositMember;
import com.financia.finance.FinanceTradingProject;
import com.financia.quantitative.mapper.FinanceDepositMemberMapper;
import com.financia.quantitative.service.FinanceDepositMemberService;
import com.financia.quantitative.service.FinanceTradingProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


@Service("financeDepositMemberService")
public class FinanceDepositMemberServiceImpl extends ServiceImpl<FinanceDepositMemberMapper, FinanceDepositMember> implements FinanceDepositMemberService {

    @Autowired
    private FinanceTradingProjectService tradingProjectService;

    @Autowired
    private RemoteMemberWalletService remoteMemberWalletService;


    @Override
    public BigDecimal matures (Long memberId) {
        BigDecimal matures = baseMapper.matures(memberId);
        return matures;
    }

    @Override
    public BigDecimal revenue (Long memberId) {
        BigDecimal revenue = baseMapper.revenue(memberId);
        return revenue;
    }

    @Override
    public BigDecimal totalDeposit (Long memberId) {
        return baseMapper.totalDeposit(memberId);
    }

    @Transactional
    public AjaxResult buyFinance(Long financeId, Long memberId , BigDecimal purchase){
        FinanceTradingProject project = tradingProjectService.getOne(new QueryWrapper<FinanceTradingProject>().lambda().eq(FinanceTradingProject::getFinanceId,financeId));
        if (purchase.compareTo(project.getDepositSingleMinAmount()) == -1) {
            return AjaxResult.error("To reach the minimum quota");
        }
        FinanceDepositMember save = new FinanceDepositMember();
        save.setFinanceId(financeId);
        save.setMemberId(memberId);
        save.setActive(1l);
        save.setDepositAmount(purchase);
        save.setDepositBeginTime(new Date());
        LocalDateTime localDateTime = LocalDateTime.now().plusDays(project.getDepositCloseDay());
        //获取系统默认是时区，如：Asia/Shanghai
        ZoneId zoneId=ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        save.setDepositEndTime(date);
        save.setDepositCloseDay(project.getDepositCloseDay());
        BigDecimal year = project.getShowRate().divide(new BigDecimal(100l),8, BigDecimal.ROUND_HALF_UP);
        BigDecimal everyday = year.divide(new BigDecimal(365l),8, BigDecimal.ROUND_HALF_UP);
        save.setEstimateIncomeAmount(calculateEstimateIncomeAmount(everyday,purchase,project.getDepositCloseDay(),project.getProfitCustomer()));

        //保存日化
        save.setEverydayRate(everyday);
        save.setFinanceZhName(project.getFinanceZhName());
        save.setProfitCustomer(project.getProfitCustomer());
        save.setIncomeAmount(new BigDecimal(0l));
        if (save(save)) {
            Boolean subMoneyRs = remoteMemberWalletService.updateSubBalance(memberId, purchase,save.getId() + "",BusinessSubType.QUANTIZE_BUY,"量化理财-购买:业务表finance_deposit_member");
            if (subMoneyRs) {
                return AjaxResult.success();
            }
            //混滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return AjaxResult.error("Deduct money failure");
        }
        return AjaxResult.error();
    }

    /**
     * 预估收益计算
     * @param everydayRate 每日利率
     * @param depositAmount 存款
     * @param depositCloseDay 时间 天
     * @return
     */
    private BigDecimal calculateEstimateIncomeAmount(BigDecimal everydayRate, BigDecimal depositAmount, Long depositCloseDay,BigDecimal profitCustomer)
    {
        BigDecimal dayMoney = everydayRate.multiply(depositAmount);
        BigDecimal calculateEstimateIncome = dayMoney.multiply(new BigDecimal(depositCloseDay));
        BigDecimal multiply = calculateEstimateIncome.multiply(profitCustomer);
        return multiply;
    }

    /**
     *  量化理财赎回
     */
    public boolean sale (Long depositId){
        FinanceDepositMember depositMember = getById(depositId);
        if (depositMember.getSettleAccountsStatus() == 1l) {
            return  false;
        }
        if (depositMember.getActive() == 1l) {
            return  false;
        }
        //总收益
        BigDecimal incomeAmount = depositMember.getIncomeAmount();
        //本金
        BigDecimal depositAmount = depositMember.getDepositAmount();
        FinanceDepositMember depositMember1 = new FinanceDepositMember();
        depositMember1.setId(depositId);
        depositMember1.setSettleAccountsStatus(1);
        depositMember1.setSettleAccountsTime(new Date());
        if (updateById(depositMember1)) {
            Boolean aBoolean = remoteMemberWalletService.updateAddBalance(depositMember.getMemberId(), depositAmount.add(incomeAmount)
                    ,depositId + "", BusinessSubType.QUANTIZE_RANSOM, "量化理财-赎回:业务表finance_deposit_member");
            if (aBoolean) {
                return true;
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }else {
            return false;
        }
    }



}
