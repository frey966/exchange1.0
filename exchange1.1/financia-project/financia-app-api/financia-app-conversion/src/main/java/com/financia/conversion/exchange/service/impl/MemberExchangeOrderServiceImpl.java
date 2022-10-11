package com.financia.conversion.exchange.service.impl;

import java.util.List;

import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.enums.BusinessType;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.conversion.exchange.mapper.MemberExchangeOrderMapper;
import com.financia.conversion.exchange.service.IMemberExchangeOrderService;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberWalletNationalCurrency;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.exchange.vo.MemberConversionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

/**
 * 会员-换汇订单Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-31
 */
@Service
public class MemberExchangeOrderServiceImpl implements IMemberExchangeOrderService
{
    @Autowired
    private MemberExchangeOrderMapper memberExchangeOrderMapper;
    @Autowired
    private RemoteMemberWalletService memberWalletService;

    /**
     * 查询换汇订单
     * 
     * @param id 会员-换汇订单主键
     * @return 会员-换汇订单
     */
    @Override
    public MemberConversionVo selectMemberExchangeOrderById(Long id)
    {
        return memberExchangeOrderMapper.selectMemberExchangeOrderById(id);
    }

    /**
     * 查询换汇订单列表
     * 
     * @param MemberConversionVo 会员-换汇订单
     * @return 会员-换汇订单
     */
    @Override
    public List<MemberConversionVo> selectMemberExchangeOrderList(MemberConversionVo MemberConversionVo)
    {
        return memberExchangeOrderMapper.selectMemberExchangeOrderList(MemberConversionVo);
    }

    /**
     * 新增换汇订单
     * 
     * @param MemberConversionVo 会员-换汇订单
     * @return 结果
     */
    @Transactional
    @Override
    public AjaxResult insertMemberExchangeOrder(MemberConversionVo MemberConversionVo)
    {
        AjaxResult ajaxResult=new AjaxResult(200,"创建成功！");
        //判断余额是否足够
        MemberBusinessWallet memberBusinessWallet=memberWalletService.getMemberBusinessWalletByMemberId(Long.parseLong(MemberConversionVo.getMemberId()));
        if(memberBusinessWallet.getMoney().compareTo(MemberConversionVo.getExchangeAmount()) == -1){
            return AjaxResult.error(-1,"账户余额不足！");
        }else{
            //查询会员钱包是否存在
            MemberWalletNationalCurrency memberWalletNationalCurrency=new MemberWalletNationalCurrency();
            memberWalletNationalCurrency.setCoinId(Integer.parseInt(MemberConversionVo.getCoinId()));
            memberWalletNationalCurrency.setMemberId(Long.parseLong(MemberConversionVo.getMemberId()));
            MemberWalletNationalCurrency memberWalletNationalCurrencyResult=memberWalletService.getNationalCurrencyByMemberIdAndCoinId(memberWalletNationalCurrency);

            memberWalletNationalCurrency.setMoney(MemberConversionVo.getNationalMoney());
            try{
                if(!ObjectUtils.isEmpty(memberWalletNationalCurrencyResult)){
                    //有则修改法币余额
                    if(!(memberWalletService.addMoney(memberWalletNationalCurrency)>0)){
                        ajaxResult=new AjaxResult(-2,"修改法币余额失败！");
                        //回滚
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ajaxResult;
                    }
                }else{
                    //否则直接添加法币钱包
                    if(!(memberWalletService.addNationalCurrency(memberWalletNationalCurrency)>0)){
                        ajaxResult=new AjaxResult(-3,"创建法币钱包失败！");
                        //回滚
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ajaxResult;
                    }
                }
                //添加汇兑账单
                if(!(memberExchangeOrderMapper.insertMemberExchangeOrder(MemberConversionVo)>0)){
                    ajaxResult=new AjaxResult(-5,"添加汇兑账单失败！");
                    //回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ajaxResult;
                }
                //减少钱包余额
                if(!memberWalletService.updateSubBalance(Long.parseLong(MemberConversionVo.getMemberId()),MemberConversionVo.getExchangeAmount(),MemberConversionVo.getId().toString(),BusinessSubType.EXCHANGE,"汇兑：member_exchange_order")){
                    ajaxResult=new AjaxResult(-4,"减少钱包余额失败！");
                    //回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ajaxResult;
                }
            }catch (Exception e){
                ajaxResult=new AjaxResult(-6,"系统异常！");
                //回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }finally {
                return ajaxResult;
            }
        }
    }


}
