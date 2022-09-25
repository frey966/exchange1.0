package com.financia.conversion.collect.service.impl;

import com.financia.conversion.collect.mapper.MemberExchangeCurrencyFavoriteMapper;
import com.financia.conversion.collect.service.MemberExchangeCurrencyFavoriteService;
import com.financia.exchange.MemberExchangeCurrencyFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员汇兑收藏Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-30
 */
@Service
public class MemberExchangeCurrencyFavoriteServiceImpl implements MemberExchangeCurrencyFavoriteService
{
    @Autowired
    private MemberExchangeCurrencyFavoriteMapper memberExchangeCurrencyFavoriteMapper;


    /**
     * 新增会员汇兑收藏
     * 
     * @param memberExchangeCurrencyFavorite 会员汇兑收藏
     * @return 结果
     */
    @Override
    public int insertMemberCurrencyOllect(MemberExchangeCurrencyFavorite memberExchangeCurrencyFavorite)
    {
        return memberExchangeCurrencyFavoriteMapper.insertMemberCurrencyOllect(memberExchangeCurrencyFavorite);
    }



    /**
     * 批量删除会员汇兑收藏
     * 
     * @param ids 需要删除的会员汇兑收藏主键
     * @return 结果
     */
    @Override
    public int deleteMemberCurrencyOllectByIds(Long[] ids)
    {
        return memberExchangeCurrencyFavoriteMapper.deleteMemberCurrencyOllectByIds(ids);
    }

}
