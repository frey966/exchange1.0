package com.financia.business.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.exchange.MemberWalletNationalCurrency;

import java.util.List;

/**
 * 会员-会员法币钱包信息
 *
 * @author 花生
 */
public interface MemberWalletNationalCurrencyService{
    /**
     * 创建法币钱包
     * @param memberId
     * @return
     */
    boolean createWalletNationalCurrency(Long memberId);

    /**
     * 查询会员-会员法币钱包信息
     *
     * @param id 会员-会员法币钱包信息主键
     * @return 会员-会员法币钱包信息
     */
    public MemberWalletNationalCurrency selectMemberWalletNationalCurrencyById(Long id);

    /**
     * 查询会员-会员法币钱包信息
     *
     * @param memberWalletNationalCurrency 会员id  法币id
     * @return 会员-会员法币钱包信息
     */
    public MemberWalletNationalCurrency selectNationalCurrencyByMemberIdAndCoinId(MemberWalletNationalCurrency memberWalletNationalCurrency);


    /**
     * 查询会员-会员法币钱包信息列表
     *
     * @param memberWalletNationalCurrency 会员-会员法币钱包信息
     * @return 会员-会员法币钱包信息集合
     */
    public List<MemberWalletNationalCurrency> selectMemberWalletNationalCurrencyList(MemberWalletNationalCurrency memberWalletNationalCurrency);

    /**
     * 新增会员-会员法币钱包信息
     *
     * @param memberWalletNationalCurrency 会员-会员法币钱包信息
     * @return 结果
     */
    public int insertMemberWalletNationalCurrency(MemberWalletNationalCurrency memberWalletNationalCurrency);

    public int addMoney(MemberWalletNationalCurrency memberWalletNationalCurrency);

    public int subMoney(MemberWalletNationalCurrency memberWalletNationalCurrency);

}

