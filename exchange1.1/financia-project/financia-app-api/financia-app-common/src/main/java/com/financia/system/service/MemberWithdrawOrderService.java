package com.financia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.MemberWithdrawOrder;

import java.math.BigDecimal;

/**
 * 交易所-会员提现申请
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-09 20:53:37
 */
public interface MemberWithdrawOrderService extends IService<MemberWithdrawOrder> {
    /**
     * 申请提现
     * @param memberId
     * @param remark
     * @param money
     * @param jyPassword
     * @param chainId
     * @param address
     * @return
     */
    AjaxResult apply (Long memberId, String remark, BigDecimal money, String jyPassword, Integer chainId, String address);

    /**
     * 更新提现转账状态
     */
    void updateWithdraw();
}

