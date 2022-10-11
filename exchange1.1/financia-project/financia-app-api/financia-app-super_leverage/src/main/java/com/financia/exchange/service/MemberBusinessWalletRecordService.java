package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.enums.TradeType;
import com.financia.exchange.MemberBusinessWalletRecord;

import java.math.BigDecimal;

/**
 * 公共-余额交易流水记录
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-02 14:55:32
 */
public interface MemberBusinessWalletRecordService extends IService<MemberBusinessWalletRecord> {

    /**
     * 添加钱包操作流水
     *
     * @param businessWalletId
     * @param money
     * @param tradeType
     * @param businessId
     * @param type
     * @param remark
     * @return
     */
    boolean saveRecord(Integer businessWalletId, BigDecimal money, BigDecimal balance, TradeType tradeType, String businessId, BusinessSubType type, String remark);
}
