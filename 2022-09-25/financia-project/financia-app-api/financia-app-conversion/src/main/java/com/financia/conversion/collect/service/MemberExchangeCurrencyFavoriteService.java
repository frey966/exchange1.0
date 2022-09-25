package com.financia.conversion.collect.service;

import com.financia.exchange.MemberExchangeCurrencyFavorite;

/**
 * 会员汇兑收藏Service接口
 * 
 * @author 花生
 * @date 2022-08-30
 */
public interface MemberExchangeCurrencyFavoriteService
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
     * @param ids 需要删除的会员汇兑收藏主键集合
     * @return 结果
     */
    public int deleteMemberCurrencyOllectByIds(Long[] ids);

}
