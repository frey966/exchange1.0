package com.financia.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.exchange.MemberWalletNationalCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 会员-会员法币钱包信息
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-11 21:48:29
 */
@Mapper
@Repository("memberWalletNationalCurrencyMapper")
public interface MemberWalletNationalCurrencyMapper extends BaseMapper<MemberWalletNationalCurrency> {
    @Select("SELECT m.money,m.national_money,m.freeze_money,p.exchange_coin_app_logo,p.id as currency_id," +
            " p.exchange_coin_pc_logo,p.exchange_coin_en_name,p.exchange_coin_name,p.exchange_coin_zh_name" +
            " FROM" +
            " member_wallet_national_currency m," +
            " p_national_currency p" +
            " WHERE" +
            " m.member_id = #{memberId}" +
            " AND m.`status` = #{status}" +
            " AND m.coin_id = p.id")
    List<Map> getFiaCurrencyList(@Param("memberId") Long memberId, @Param("status")  Integer status);

    /**
     * 查询会员-会员法币钱包信息
     *
     * @param id 会员-会员法币钱包信息主键
     * @return 会员-会员法币钱包信息
     */
    public MemberWalletNationalCurrency selectMemberWalletNationalCurrencyById(Long id);

    /**
     * 查询会员法币钱包信息
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

    /**
     * 修改会员-会员法币钱包信息
     *
     * @param memberWalletNationalCurrency 会员-会员法币钱包信息
     * @return 结果
     */
    public int updateMemberWalletNationalCurrency(MemberWalletNationalCurrency memberWalletNationalCurrency);


    public int addMoney(MemberWalletNationalCurrency memberWalletNationalCurrency);

    public int subMoney(MemberWalletNationalCurrency memberWalletNationalCurrency);

}
