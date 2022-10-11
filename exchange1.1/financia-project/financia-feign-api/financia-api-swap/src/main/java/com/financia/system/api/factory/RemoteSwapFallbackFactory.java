package com.financia.system.api.factory;


import com.financia.system.api.RemoteSwapService;
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
public class RemoteSwapFallbackFactory implements FallbackFactory<RemoteSwapService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteSwapFallbackFactory.class);

    @Override
    public RemoteSwapService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteSwapService() {
            @Override
            public BigDecimal contractWalletTotal(Long id) {
                log.error("获取持仓金额:{}", throwable.getMessage());
                return BigDecimal.ZERO;

            }
        };
    }
}
