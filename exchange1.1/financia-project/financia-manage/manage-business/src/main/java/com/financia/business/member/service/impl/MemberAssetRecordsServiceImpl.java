package com.financia.business.member.service.impl;

import java.util.List;

import com.financia.business.member.mapper.MemberAssetRecordsMapper;
import com.financia.business.member.service.IMemberAssetRecordsService;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.MemberAssetRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员-合约交易 杠杆交易 币币交易 资金移记录Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-26
 */
@Service
public class MemberAssetRecordsServiceImpl implements IMemberAssetRecordsService
{
    @Autowired
    private MemberAssetRecordsMapper memberAssetRecordsMapper;

    /**
     * 查询会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param id 会员-合约交易 杠杆交易 币币交易 资金移记录主键
     * @return 会员-合约交易 杠杆交易 币币交易 资金移记录
     */
    @Override
    public MemberAssetRecords selectMemberAssetRecordsById(Long id)
    {
        return memberAssetRecordsMapper.selectMemberAssetRecordsById(id);
    }

    /**
     * 查询会员-合约交易 杠杆交易 币币交易 资金移记录列表
     * 
     * @param memberAssetRecords 会员-合约交易 杠杆交易 币币交易 资金移记录
     * @return 会员-合约交易 杠杆交易 币币交易 资金移记录
     */
    @Override
    public List<MemberAssetRecords> selectMemberAssetRecordsList(MemberAssetRecords memberAssetRecords)
    {
        return memberAssetRecordsMapper.selectMemberAssetRecordsList(memberAssetRecords);
    }

    /**
     * 新增会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param memberAssetRecords 会员-合约交易 杠杆交易 币币交易 资金移记录
     * @return 结果
     */
    @Override
    public int insertMemberAssetRecords(MemberAssetRecords memberAssetRecords)
    {
        return memberAssetRecordsMapper.insertMemberAssetRecords(memberAssetRecords);
    }

    /**
     * 修改会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param memberAssetRecords 会员-合约交易 杠杆交易 币币交易 资金移记录
     * @return 结果
     */
    @Override
    public int updateMemberAssetRecords(MemberAssetRecords memberAssetRecords)
    {
        return memberAssetRecordsMapper.updateMemberAssetRecords(memberAssetRecords);
    }

    /**
     * 批量删除会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param ids 需要删除的会员-合约交易 杠杆交易 币币交易 资金移记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberAssetRecordsByIds(Long[] ids)
    {
        return memberAssetRecordsMapper.deleteMemberAssetRecordsByIds(ids);
    }

    /**
     * 删除会员-合约交易 杠杆交易 币币交易 资金移记录信息
     * 
     * @param id 会员-合约交易 杠杆交易 币币交易 资金移记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberAssetRecordsById(Long id)
    {
        return memberAssetRecordsMapper.deleteMemberAssetRecordsById(id);
    }
}
