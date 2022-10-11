package com.financia.exchange.feign.client;

import com.financia.exchange.Member;
import com.financia.exchange.feign.fallback.MemberClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 会员客户端feign接口
 */
//@FeignClient(contextId = "MemberService", value = "127.0.0.1:9104", fallbackFactory = MemberClientFallBack.class)
@FeignClient(contextId = "MemberService", value = "financia-app-common", fallbackFactory = MemberClientFallBack.class)
public interface MemberService {

    @GetMapping("/member/userInfoById")
    Member getMemberInfo(@RequestParam("id") Long memberId);

}