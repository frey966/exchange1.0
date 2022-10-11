package com.financia.quantitative.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.finance.FinanceInterestMember;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;

/**
 * 量化理财-会员存款利息
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-15 17:16:52
 */
public interface FinanceInterestMemberService extends IService<FinanceInterestMember> {
    BigDecimal yesterday (Long memberId);
    void interestCalculated (LocalDate localDate) throws ParseException;
    void interestCalculated();
}

