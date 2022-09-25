package com.financia.business.member.service;

import com.financia.business.MemberCoin;

import java.util.List;

/**
 * 会员数字货币和法币关系中间Service接口
 * 
 * @author 花生
 * @date 2022-08-04
 */
public interface IMemberCoinService 
{
    /**
     * 查询会员数字货币和法币关系中间
     * 
     * @param id 会员数字货币和法币关系中间主键
     * @return 会员数字货币和法币关系中间
     */
    public MemberCoin selectMemberCoinById(Long id);

    /**
     * 查询会员数字货币和法币关系中间列表
     * 
     * @param memberCoin 会员数字货币和法币关系中间
     * @return 会员数字货币和法币关系中间集合
     */
    public List<MemberCoin> selectMemberCoinList(MemberCoin memberCoin);

    /**
     * 新增会员数字货币和法币关系中间
     * 
     * @param memberCoin 会员数字货币和法币关系中间
     * @return 结果
     */
    public int insertMemberCoin(MemberCoin memberCoin);

    /**
     * 修改会员数字货币和法币关系中间
     * 
     * @param memberCoin 会员数字货币和法币关系中间
     * @return 结果
     */
    public int updateMemberCoin(MemberCoin memberCoin);

    /**
     * 批量删除会员数字货币和法币关系中间
     * 
     * @param ids 需要删除的会员数字货币和法币关系中间主键集合
     * @return 结果
     */
    public int deleteMemberCoinByIds(Long[] ids);

    /**
     * 删除会员数字货币和法币关系中间信息
     * 
     * @param id 会员数字货币和法币关系中间主键
     * @return 结果
     */
    public int deleteMemberCoinById(Long id);
}
