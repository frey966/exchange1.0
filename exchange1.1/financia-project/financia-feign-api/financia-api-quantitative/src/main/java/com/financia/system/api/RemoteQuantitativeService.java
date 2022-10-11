package com.financia.system.api;

import com.financia.common.core.constant.ServiceNameConstants;
import com.financia.system.api.factory.RemoteQuantitativeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 字典服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteQuantitativeService", value = ServiceNameConstants.QUANTITATIVE_SERVICE, fallbackFactory = RemoteQuantitativeFallbackFactory.class)
public interface RemoteQuantitativeService
{
    /**
     * 查询持仓总金额
     */
    @PostMapping("/quantitative/positionFeign")
     BigDecimal positionFeign(@RequestParam("id") Long memberId);

}
