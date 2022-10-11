package com.financia.system.api.factory;


import com.financia.system.api.RemoteQuantitativeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 量化理财降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteQuantitativeFallbackFactory implements FallbackFactory<RemoteQuantitativeService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteQuantitativeFallbackFactory.class);

    @Override
    public RemoteQuantitativeService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteQuantitativeService() {
            @Override
            public BigDecimal positionFeign(Long id) {
                log.error("获取持仓金额:{}", throwable.getMessage());
                return BigDecimal.ZERO;

            }
        };
    }
}
