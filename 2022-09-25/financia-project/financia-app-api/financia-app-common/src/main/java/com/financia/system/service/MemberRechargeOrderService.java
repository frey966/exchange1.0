package com.financia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.exchange.MemberRechargeOrder;

/**
 *
 * 充值单
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-07 21:11:10
 */
public interface MemberRechargeOrderService extends IService<MemberRechargeOrder> {


    /**
     * 更新所有充值订单充值状态
     */
    void updateAllUp(int chainId);

    /**
     * 更新所有充值订单交易状态
     */
    void updateAllTransferStatus();
}

