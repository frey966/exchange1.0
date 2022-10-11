package com.financia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.exchange.Member;
import com.financia.exchange.MemberWalletAddress;

import java.math.BigDecimal;

/**
 * 公链钱包
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-08-05 20:22:36
 */
public interface MemberWalletAddressService extends IService<MemberWalletAddress> {

    /**
     * 创建公链钱包
     * @param save
     * @return
     */
    boolean createWalletAddress(Member save);

    /**
     * 添加钱包公链累计充值次数和金额
     * @param id
     * @param money
     * @return
     */
    boolean addRechargeMoney(Integer id, BigDecimal money);
}

