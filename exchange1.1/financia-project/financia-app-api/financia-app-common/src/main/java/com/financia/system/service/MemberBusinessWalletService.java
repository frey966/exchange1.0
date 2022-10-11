package com.financia.system.service;

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
     * 增加余额
     *
     * @param memberId
     * @param money
     * @return
     */
    boolean updateAddBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark);

    /**
     * 扣减余额
     *
     * @param memberId
     * @param money
     * @return
     */
    boolean updateSubBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark);

    /**
     * 冻结余额
     *
     * @param memberId
     * @param money
     * @return
     */
    AjaxResult updateFreezeBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark);

    /**
     * 创建钱包
     *
     * @param memberId
     * @return
     */
    boolean createWallet(Long memberId);


    /**
     * 查看个人总资产
     *
     * @param memberId
     * @return
     */
    Map<String, BigDecimal> getBalance(Long memberId);

    /**
     * 查看法币详情
     *
     * @param memberId
     * @return
     */
    List<Map> getFiaCurrencyList(Long memberId);


    /**
     * 查看充值钱包
     *
     * @param memberId
     * @return
     */
    MemberBusinessWallet getMemberBusinessWalletByMemberId(Long memberId);

    /**
     * 把U单位转换成默认法币金额
     *
     * @param memberId
     * @return
     */
    BigDecimal convertFiatCurrency(Long memberId, BigDecimal amount);

    /**
     * 扣减 冻结金额
     * @param memberId
     * @param money
     * @return
     */
    AjaxResult updateSubFreezeBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark);



    /**
     * 转账和充值币种
     * @param memberId
     * @param memberId
     * @return
     */
    List<Map>  getTransferCurrencyDetail(Long memberId);


    /**
     * 查看总资产
     *
     * @param memberId
     * @return
     */
    Map<String, Map<String,BigDecimal>> getTotalAssets(Long memberId);


}

