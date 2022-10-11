package com.financia.system.api.factory;


import com.financia.system.api.RemoteSuperLeverageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 超级杠杆降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteSuperLeverageFallbackFactory implements FallbackFactory<RemoteSuperLeverageService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSuperLeverageFallbackFactory.class);

    @Override
    public RemoteSuperLeverageService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteSuperLeverageService() {
            @Override
            public BigDecimal superWalletTotal(Long id) {
                log.error("获取持仓金额:{}", throwable.getMessage());
                return BigDecimal.ZERO;

            }
        };
    }
}
