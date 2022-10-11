package com.financia.business.member.mapper;

import com.financia.exchange.MemberAssetRecords;

import java.util.List;

/**
 * 会员-合约交易 杠杆交易 币币交易 资金移记录Mapper接口
 * 
 * @author 花生
 * @date 2022-09-26
 */
public interface MemberAssetRecordsMapper 
{
    /**
     * 查询会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param id 会员-合约交易 杠杆交易 币币交易 资金移记录主键
     * @return 会员-合约交易 杠杆交易 币币交易 资金移记录
     */
    public MemberAssetRecords selectMemberAssetRecordsById(Long id);

    /**
     * 查询会员-合约交易 杠杆交易 币币交易 资金移记录列表
     * 
     * @param memberAssetRecords 会员-合约交易 杠杆交易 币币交易 资金移记录
     * @return 会员-合约交易 杠杆交易 币币交易 资金移记录集合
     */
    public List<MemberAssetRecords> selectMemberAssetRecordsList(MemberAssetRecords memberAssetRecords);

    /**
     * 新增会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param memberAssetRecords 会员-合约交易 杠杆交易 币币交易 资金移记录
     * @return 结果
     */
    public int insertMemberAssetRecords(MemberAssetRecords memberAssetRecords);

    /**
     * 修改会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param memberAssetRecords 会员-合约交易 杠杆交易 币币交易 资金移记录
     * @return 结果
     */
    public int updateMemberAssetRecords(MemberAssetRecords memberAssetRecords);

    /**
     * 删除会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param id 会员-合约交易 杠杆交易 币币交易 资金移记录主键
     * @return 结果
     */
    public int deleteMemberAssetRecordsById(Long id);

    /**
     * 批量删除会员-合约交易 杠杆交易 币币交易 资金移记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberAssetRecordsByIds(Long[] ids);
}
