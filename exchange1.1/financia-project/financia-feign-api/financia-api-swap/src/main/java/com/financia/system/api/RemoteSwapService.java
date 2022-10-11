package com.financia.system.api;

import com.financia.common.core.constant.ServiceNameConstants;
import com.financia.system.api.factory.RemoteSwapFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 字典服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteSwapService", value = ServiceNameConstants.SWAP_SERVICE, fallbackFactory = RemoteSwapFallbackFactory.class)
public interface RemoteSwapService
{
    /**
     * 查询持仓总金额
     */
    @GetMapping("/contractWallet/total")
     BigDecimal contractWalletTotal(@RequestParam("memberId") Long memberId);

}
