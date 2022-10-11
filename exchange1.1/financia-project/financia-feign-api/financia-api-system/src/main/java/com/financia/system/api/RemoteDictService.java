package com.financia.system.api;

import com.financia.common.core.constant.ServiceNameConstants;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.api.factory.RemoteDictFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 字典服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteDictService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteDictFallbackFactory.class)
public interface RemoteDictService
{
    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/dict/data/type/{dictType}")
    public AjaxResult dictType(@PathVariable("dictType") String dictType);

}
