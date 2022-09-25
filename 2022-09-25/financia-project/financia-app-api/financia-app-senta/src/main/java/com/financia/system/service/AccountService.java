package com.financia.system.service;

public interface AccountService {


    /**
     * 账户扣减
     * @param userId 用户 ID
     * @param price 扣减金额
     */
    void reduceBalance(Long userId, Double price);

}
