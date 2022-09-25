package com.financia.quantitative.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.finance.FinanceInterestMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 量化理财-会员存款利息
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-15 17:16:52
 */
@Mapper
public interface FinanceInterestMemberMapper extends BaseMapper<FinanceInterestMember> {

    @Select("select sum(everyday_interest) from finance_interest_member where member_id = #{memberId} and (TO_DAYS( NOW( )) - TO_DAYS(time_interest)) = 1")
    BigDecimal yesterday (Long memberId);
}
