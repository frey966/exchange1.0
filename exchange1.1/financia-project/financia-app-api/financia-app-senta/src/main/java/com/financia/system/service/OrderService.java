package com.financia.system.service;

import com.financia.system.domain.dto.PlaceOrderRequest;

public interface OrderService {


    /**
     * 下单
     *
     * @param placeOrderRequest 订单请求参数
     */
    void placeOrder(PlaceOrderRequest placeOrderRequest);


}
