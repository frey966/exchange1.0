package com.financia.quantitative.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.finance.FinanceDepositMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 量化理财-会员存款
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-15 17:16:52
 */
@Mapper
public interface FinanceDepositMemberMapper extends BaseMapper<FinanceDepositMember> {

    @Select("select sum(estimate_income_amount) from finance_deposit_member where  settle_accounts_status = 0 and member_id = #{memberId}")
    BigDecimal matures (Long memberId);
    @Select("select sum(income_amount) from finance_deposit_member where member_id = #{memberId}")
    BigDecimal revenue (Long memberId);
    @Select("select sum(deposit_amount) from finance_deposit_member where member_id = #{memberId}")
    BigDecimal totalDeposit (Long memberId);
}
