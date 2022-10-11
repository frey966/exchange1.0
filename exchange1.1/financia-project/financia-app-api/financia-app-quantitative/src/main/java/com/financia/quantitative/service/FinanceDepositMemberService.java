package com.financia.quantitative.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.finance.FinanceDepositMember;

import java.math.BigDecimal;

/**
 * 量化理财-会员存款
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-15 17:16:52
 */
public interface FinanceDepositMemberService extends IService<FinanceDepositMember> {

    /**
     * 购买量化理财
     * @param financeId
     * @param memberId
     * @param purchase
     * @return
     */
    AjaxResult buyFinance(Long financeId, Long memberId , BigDecimal purchase);

    /**
     * 查询到期收益
     * @param memberId
     * @return
     */
    BigDecimal matures (Long memberId);

    /**
     * 查询总收益
     * @param memberId
     * @return
     */
    BigDecimal revenue (Long memberId);

    /**
     * 持仓和
     * @param memberId
     * @return
     */
    BigDecimal totalDeposit (Long memberId);

    /**
     * 赎回
     * @param depositId
     * @return
     */
    boolean sale (Long depositId);
}

