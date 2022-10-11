package com.financia.exchange.feign.fallback;

import com.financia.exchange.Member;
import com.financia.exchange.feign.client.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 喜欢看客户端feign接口快速失败
 * @author: zealon
 * @since: 2019/7/4
 */
@Component
public class MemberClientFallBack implements FallbackFactory<MemberService> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberClientFallBack.class);

    @Override
    public MemberService create(Throwable cause) {
        return new MemberService() {
            @Override
            public Member getMemberInfo(Long memberId) {
                LOGGER.info("获取用户信息失败。。。。。。。。。。。。。。。。");
                LOGGER.info(cause.getMessage());
                return null;
            }
        };
    }
}
