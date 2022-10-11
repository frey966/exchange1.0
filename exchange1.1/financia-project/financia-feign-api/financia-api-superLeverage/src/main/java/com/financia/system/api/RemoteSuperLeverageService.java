package com.financia.system.api;

import com.financia.common.core.constant.ServiceNameConstants;
import com.financia.system.api.factory.RemoteSuperLeverageFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 字典服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteSuperLeverageService", value = ServiceNameConstants.SUPER_LEVERAGE_SERVICE, fallbackFactory = RemoteSuperLeverageFallbackFactory.class)
public interface RemoteSuperLeverageService
{
    /**
     * 查询持仓总金额
     */
    @GetMapping("/superWallet/total")
     BigDecimal superWalletTotal(@RequestParam("memberId") Long memberId);

}
