package com.financia.conversion.exchange.service;

import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.vo.MemberConversionVo;

import java.util.List;

/**
 * 会员-换汇订单Service接口
 * 
 * @author 花生
 * @date 2022-08-31
 */
public interface IMemberExchangeOrderService 
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
    public AjaxResult insertMemberExchangeOrder(MemberConversionVo MemberConversionVo);



}
