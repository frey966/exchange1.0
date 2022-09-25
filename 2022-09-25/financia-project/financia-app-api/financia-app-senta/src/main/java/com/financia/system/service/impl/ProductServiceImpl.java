package com.financia.system.service.impl;

import javax.annotation.Resource;

import com.financia.system.domain.Product;
import com.financia.system.mapper.ProductMapper;
import com.financia.system.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.dynamic.datasource.annotation.DS;
import io.seata.core.context.RootContext;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Resource
    private ProductMapper productMapper;

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务 重要！！！！一定要使用REQUIRES_NEW
     */
    @DS("product")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Double reduceStock(Long productId, Integer amount)
    {
        log.info("=============PRODUCT START=================");
        log.info("当前 XID: {}", RootContext.getXID());

        // 检查库存
        Product product = productMapper.selectById(productId);
        if(product==null){
            throw new RuntimeException("库存不足");
        }
        Integer stock = product.getStock();
        log.info("商品编号为 {} 的库存为{},订单商品数量为{}", productId, stock, amount);

        if (stock < amount)
        {
            log.warn("商品编号为{} 库存不足，当前库存:{}", productId, stock);
            throw new RuntimeException("库存不足");
        }
        log.info("开始扣减商品编号为 {} 库存,单价商品价格为{}", productId, product.getPrice());
        // 扣减库存
        int currentStock = stock - amount;
        product.setStock(currentStock);
        Integer resule = productMapper.updateById(product);
        if(resule!=1)
        {
            throw new RuntimeException("扣减库存-失败");
        }
        double totalPrice = product.getPrice() * amount;
        log.info("扣减商品编号为 {} 库存成功,扣减后库存为{}, {} 件商品总价为 {} ", productId, currentStock, amount, totalPrice);
        log.info("=============PRODUCT END=================");
        return totalPrice;
    }

}
