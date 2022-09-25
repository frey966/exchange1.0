package com.financia.business.member.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.member.mapper.MemberWalletNationalCurrencyMapper;
import com.financia.business.member.service.MemberWalletNationalCurrencyService;
import com.financia.exchange.MemberWalletNationalCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class MemberWalletNationalCurrencyServiceImpl implements MemberWalletNationalCurrencyService {
    @Autowired
    private MemberWalletNationalCurrencyMapper memberWalletNationalCurrencyMapper;

    @Override
    public boolean createWalletNationalCurrency(Long memberId) {
        MemberWalletNationalCurrency save = new MemberWalletNationalCurrency();
        save.setMemberId(memberId);
        save.setCoinId(1);
        save.setNationalMoney(new BigDecimal(0l));
        save.setFreezeMoney(new BigDecimal(0));
        save.setMoney(new BigDecimal(0));
        if (memberWalletNationalCurrencyMapper.insertMemberWalletNationalCurrency(save)>0){
            return true;
        }
        return false;
    }

    /**
     * 查询会员-会员法币钱包信息
     *
     * @param id 会员-会员法币钱包信息主键
     * @return 会员-会员法币钱包信息
     */
    @Override
    public MemberWalletNationalCurrency selectMemberWalletNationalCurrencyById(Long id)
    {
        return memberWalletNationalCurrencyMapper.selectMemberWalletNationalCurrencyById(id);
    }

    /**
     * 查询会员-会员法币钱包信息
     *
     * @param memberWalletNationalCurrency 会员id  法币id
     * @return 会员-会员法币钱包信息
     */
    public MemberWalletNationalCurrency selectNationalCurrencyByMemberIdAndCoinId(MemberWalletNationalCurrency memberWalletNationalCurrency){
        return memberWalletNationalCurrencyMapper.selectNationalCurrencyByMemberIdAndCoinId(memberWalletNationalCurrency);
    }

    /**
     * 查询会员-会员法币钱包信息列表
     *
     * @param memberWalletNationalCurrency 会员-会员法币钱包信息
     * @return 会员-会员法币钱包信息
     */
    @Override
    public List<MemberWalletNationalCurrency> selectMemberWalletNationalCurrencyList(MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyMapper.selectMemberWalletNationalCurrencyList(memberWalletNationalCurrency);
    }

    /**
     * 新增会员-会员法币钱包信息
     *
     * @param memberWalletNationalCurrency 会员-会员法币钱包信息
     * @return 结果
     */
    @Override
    public int insertMemberWalletNationalCurrency(MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyMapper.insertMemberWalletNationalCurrency(memberWalletNationalCurrency);
    }

    /**
     * 修改会员-会员法币钱包信息
     *
     * @param memberWalletNationalCurrency 会员-会员法币钱包信息
     * @return 结果
     */
    @Override
    public int addMoney(MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyMapper.addMoney(memberWalletNationalCurrency);
    }

    /**
     * 修改会员-会员法币钱包信息
     *
     * @param memberWalletNationalCurrency 会员-会员法币钱包信息
     * @return 结果
     */
    @Override
    public int subMoney(MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyMapper.subMoney(memberWalletNationalCurrency);
    }

}
