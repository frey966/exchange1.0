package com.financia.conversion.exchange.mapper;

import com.financia.exchange.vo.MemberConversionVo;

import java.util.List;

/**
 * 会员-换汇订单Mapper接口
 * 
 * @author 花生
 * @date 2022-08-31
 */
public interface MemberExchangeOrderMapper 
{
    /**
     * 查询会员-换汇订单
     * 
     * @param id 会员-换汇订单主键
     * @return 会员-换汇订单
     */
    public MemberConversionVo selectMemberExchangeOrderById(Long id);

    /**
     * 查询会员-换汇订单列表
     * 
     * @param MemberConversionVo 会员-换汇订单
     * @return 会员-换汇订单集合
     */
    public List<MemberConversionVo> selectMemberExchangeOrderList(MemberConversionVo MemberConversionVo);

    /**
     * 新增会员-换汇订单
     * 
     * @param MemberConversionVo 会员-换汇订单
     * @return 结果
     */
    public int insertMemberExchangeOrder(MemberConversionVo MemberConversionVo);

    /**
     * 修改会员-换汇订单
     * 
     * @param MemberConversionVo 会员-换汇订单
     * @return 结果
     */
    public int updateMemberExchangeOrder(MemberConversionVo MemberConversionVo);

    /**
     * 删除会员-换汇订单
     * 
     * @param id 会员-换汇订单主键
     * @return 结果
     */
    public int deleteMemberExchangeOrderById(Long id);

    /**
     * 批量删除会员-换汇订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberExchangeOrderByIds(Long[] ids);
}
