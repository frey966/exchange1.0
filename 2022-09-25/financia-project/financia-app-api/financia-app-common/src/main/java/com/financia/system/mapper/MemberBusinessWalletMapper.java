package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.exchange.MemberBusinessWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 会员-业务钱包金额
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-07 18:17:03
 */
@Mapper
@Repository("memberBusinessWalletMapper")
public interface MemberBusinessWalletMapper extends BaseMapper<MemberBusinessWallet> {
    @Select("SELECT money,freeze_money FROM member_business_wallet WHERE member_id=#{ memberId} AND type= #{type}")
    Map<String, BigDecimal> getMoneyByMemberId(@Param("memberId") Long memberId, @Param("type") Integer type);

    BigDecimal updateSubBalance(@Param("memberId") Long memberId, @Param("money") BigDecimal money);
}
