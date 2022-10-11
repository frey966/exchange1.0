package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.MemberBusinessWallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 会员-业务钱包金额
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-09 18:11:42
 */
public interface MemberBusinessWalletService extends IService<MemberBusinessWallet> {


    /**
     * 查询钱包信息
     *
     * @param memberId
     * @return
     */
    MemberBusinessWallet getMemberBusinessWalletByMemberId(Long memberId);

    /**
     * 冻结钱包余额
     *
     * @param memberWalletId 钱包id
     * @param amount 操作金额
     * @param endBalance  操作后余额
     * @param busId  订单号
     * @param businessSubType  业务类型
     * @param mark  备注
     * @return
     */
    int freezeBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark);

    /**
     * 解冻钱包余额
     * @param memberWalletId 钱包id
     * @param amount 操作金额
     * @param endBalance  操作后余额
     * @param busId  订单号
     * @param businessSubType  业务类型
     * @param mark  备注
     * @return
     */
    int unfreezeBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark);

    /**
     * 扣减冻结余额
     * @param memberWalletId 钱包id
     * @param amount 操作金额
     * @param endBalance  操作后余额
     * @param busId  订单号
     * @param businessSubType  业务类型
     * @param mark  备注
     * @return
     */
    int decreaseFreezeBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark);

    /**
     * 增加可用余额
     * @param memberWalletId 钱包id
     * @param amount 操作金额
     * @param endBalance  操作后余额
     * @param busId  订单号
     * @param businessSubType  业务类型
     * @param mark  备注
     * @return
     */
    int increaseBalance(Integer memberWalletId, BigDecimal amount, BigDecimal endBalance, String busId, BusinessSubType businessSubType, String mark);

}

