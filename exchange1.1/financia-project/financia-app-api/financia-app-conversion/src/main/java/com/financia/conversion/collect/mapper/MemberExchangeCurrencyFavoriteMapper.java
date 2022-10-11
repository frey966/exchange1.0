package com.financia.conversion.collect.mapper;

import com.financia.exchange.MemberExchangeCurrencyFavorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员汇兑收藏Mapper接口
 * 
 * @author 花生
 * @date 2022-08-30
 */
@Mapper
public interface MemberExchangeCurrencyFavoriteMapper
{
    /**
     * 新增会员汇兑收藏
     * 
     * @param memberExchangeCurrencyFavorite 会员汇兑收藏
     * @return 结果
     */
    public int insertMemberCurrencyOllect(MemberExchangeCurrencyFavorite memberExchangeCurrencyFavorite);

    /**
     * 批量删除会员汇兑收藏
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberCurrencyOllectByIds(Long[] ids);
}
