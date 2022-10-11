package com.financia.quantitative.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.finance.FinanceDepositMember;
import com.financia.finance.FinanceInterestMember;
import com.financia.quantitative.mapper.FinanceInterestMemberMapper;
import com.financia.quantitative.service.FinanceInterestMemberService;
import com.financia.quantitative.service.FinanceTradingProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;


@Service("financeInterestMemberService")
@Slf4j
public class FinanceInterestMemberServiceImpl extends ServiceImpl<FinanceInterestMemberMapper, FinanceInterestMember> implements FinanceInterestMemberService {

    @Autowired
    private FinanceTradingProjectService tradingProjectService;

    @Autowired
    private FinanceDepositMemberServiceImpl depositMemberService;

    public void interestCalculated() {
        LocalDate localDate = LocalDate.now().plusDays(-1);
        try {
            interestCalculated(localDate);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void interestCalculated (LocalDate localDate) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMdd");
        Date parse = sf.parse(localDate.toString() + " 00:00:00");
        String yyyyMMdd = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<FinanceDepositMember> list = depositMemberService.list(new QueryWrapper<FinanceDepositMember>()
                .lambda()
                .eq(FinanceDepositMember::getActive, 1).lt(FinanceDepositMember::getDepositBeginTime, parse));
        for (FinanceDepositMember financeDepositMember : list) {
            if (getOne(new QueryWrapper<FinanceInterestMember>().lambda().eq(FinanceInterestMember::getDateInterestNo,Long.parseLong(financeDepositMember.getId()+yyyyMMdd))) != null) {
                log.warn("已存在当日利息信息");
                continue;
            }
            FinanceInterestMember save = new FinanceInterestMember();
            save.setDateInterestNo(Long.parseLong(financeDepositMember.getId()+yyyyMMdd));
            save.setMemberId(financeDepositMember.getMemberId());
            save.setDepositId(financeDepositMember.getId());
            save.setEverydayRate(financeDepositMember.getEverydayRate());
            save.setFinanceId(financeDepositMember.getFinanceId());
//            save.setFinanceZhName(financeDepositMember.getFinanceZhName());
            save.setPrincipal(financeDepositMember.getDepositAmount());
            save.setEverydayRate(financeDepositMember.getEverydayRate());
            save.setTimeInterest(localDate.toString());
            save.setCreateTime(new Date());
            save.setProfitCustomer(financeDepositMember.getProfitCustomer());
            // 计算利息
            save.setEverydayInterest(calculateInterest(financeDepositMember.getEverydayRate(),
                    financeDepositMember.getDepositAmount(),financeDepositMember.getProfitCustomer()));
            if (!save(save)) {
                log.error("保存每日利息失败");
                continue;
            }
            // 累计总收益
            BigDecimal incomeAmount = financeDepositMember.getIncomeAmount();
            FinanceDepositMember depositUpdate = new FinanceDepositMember();
            depositUpdate.setId(financeDepositMember.getId());
            depositUpdate.setIncomeAmount(incomeAmount.add(save.getEverydayInterest()));
            // 如果结束时间等于此次时间 ，关闭该理财
            if (sf2.format(financeDepositMember.getDepositEndTime()).equals(yyyyMMdd)) {
                depositUpdate.setActive(0l);
            }
            depositMemberService.updateById(depositUpdate);
//            if (sf2.format(financeDepositMember.getDepositEndTime()).equals(yyyyMMdd)) {
//                //赎回
//                depositMemberService.sale(financeDepositMember.getId());
//            }
        }
    }

    /**
     * 计算每日利息
     * @param rate
     * @param principal
     * @return
     */
    private BigDecimal calculateInterest(BigDecimal rate, BigDecimal principal, BigDecimal profitCustomer) {
        // 每日利息
        BigDecimal interest = principal.multiply(rate);
        // 客户分配比例
        BigDecimal multiply = interest.multiply(profitCustomer);
        return multiply;
    }

    public BigDecimal yesterday (Long memberId){
        return baseMapper.yesterday(memberId);
    }

}
