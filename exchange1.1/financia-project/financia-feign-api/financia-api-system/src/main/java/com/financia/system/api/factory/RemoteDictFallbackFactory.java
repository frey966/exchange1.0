package com.financia.system.api.factory;

import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.api.RemoteDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 字典服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteDictFallbackFactory implements FallbackFactory<RemoteDictService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteDictFallbackFactory.class);

    @Override
    public RemoteDictService create(Throwable throwable)
    {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteDictService()
        {
            @Override
            public AjaxResult dictType(String dictType) {
                return AjaxResult.error("获取用户失败:" + throwable.getMessage());

            }
        };
    }
}
