package com.financia.business.member.service.impl;

import java.util.List;

import com.financia.business.MemberCoin;
import com.financia.business.member.mapper.MemberCoinMapper;
import com.financia.business.member.service.IMemberCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员数字货币和法币关系中间Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-04
 */
@Service
public class MemberCoinServiceImpl implements IMemberCoinService
{
    @Autowired
    private MemberCoinMapper memberCoinMapper;

    /**
     * 查询会员数字货币和法币关系中间
     * 
     * @param id 会员数字货币和法币关系中间主键
     * @return 会员数字货币和法币关系中间
     */
    @Override
    public MemberCoin selectMemberCoinById(Long id)
    {
        return memberCoinMapper.selectMemberCoinById(id);
    }

    /**
     * 查询会员数字货币和法币关系中间列表
     * 
     * @param memberCoin 会员数字货币和法币关系中间
     * @return 会员数字货币和法币关系中间
     */
    @Override
    public List<MemberCoin> selectMemberCoinList(MemberCoin memberCoin)
    {
        return memberCoinMapper.selectMemberCoinList(memberCoin);
    }

    /**
     * 新增会员数字货币和法币关系中间
     * 
     * @param memberCoin 会员数字货币和法币关系中间
     * @return 结果
     */
    @Override
    public int insertMemberCoin(MemberCoin memberCoin)
    {
        return memberCoinMapper.insertMemberCoin(memberCoin);
    }

    /**
     * 修改会员数字货币和法币关系中间
     * 
     * @param memberCoin 会员数字货币和法币关系中间
     * @return 结果
     */
    @Override
    public int updateMemberCoin(MemberCoin memberCoin)
    {
        return memberCoinMapper.updateMemberCoin(memberCoin);
    }

    /**
     * 批量删除会员数字货币和法币关系中间
     * 
     * @param ids 需要删除的会员数字货币和法币关系中间主键
     * @return 结果
     */
    @Override
    public int deleteMemberCoinByIds(Long[] ids)
    {
        return memberCoinMapper.deleteMemberCoinByIds(ids);
    }

    /**
     * 删除会员数字货币和法币关系中间信息
     * 
     * @param id 会员数字货币和法币关系中间主键
     * @return 结果
     */
    @Override
    public int deleteMemberCoinById(Long id)
    {
        return memberCoinMapper.deleteMemberCoinById(id);
    }
}
