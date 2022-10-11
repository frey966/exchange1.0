package com.financia.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.financia.system.domain.Order;
import com.financia.system.domain.dto.PlaceOrderRequest;
import com.financia.system.mapper.OrderMapper;
import com.financia.system.service.AccountService;
import com.financia.system.service.OrderService;
import com.financia.system.service.ProductService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
@Service
public class OrderServiceImpl  implements OrderService {


    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProductService productService;

    @DS("order") // 每一层都需要使用多数据源注解切换所选择的数据库
    @Override
    @Transactional
    @GlobalTransactional // 重点 第一个开启事务的需要添加seata全局事务注解
    public void placeOrder(PlaceOrderRequest request)
    {
        log.info("=============ORDER START=================");
        Long userId = request.getUserId();
        Long productId = request.getProductId();
        Integer amount = request.getAmount();
        log.info("收到下单请求,用户:{}, 商品:{},数量:{}", userId, productId, amount);

        log.info("当前 XID: {}", RootContext.getXID());

        Order order = new Order(userId, productId, 0, amount);
        //新增数据
        Integer result = orderMapper.insert(order);
        log.info("订单一阶段生成，等待扣库存付款中");
        // 扣减库存并计算总价
        Double totalPrice = productService.reduceStock(productId, amount);
        // 扣减余额
        accountService.reduceBalance(userId, totalPrice);

        order.setStatus(1);
        order.setTotalPrice(totalPrice);
        orderMapper.updateById(order);
        log.info("订单已成功下单");
        log.info("=============ORDER END=================");
    }

}
