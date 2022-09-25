package com.financia.system.share.controller;

import com.financia.common.MemberShare;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.redis.service.RedisService;
import com.financia.system.share.service.MemberShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "APP端-个人中心-用户分享模块")
@RestController
@RequestMapping("/member")
public class MemberShareController extends BaseController {
    @Resource
    MemberShareService memberShareService;

    @Resource
    RedisService redisService;

    @PostMapping(value = "/share")
    @ApiOperation(value = "用户-分享", notes = "好友分享链接")
    public AjaxResult memberShare() {
        List<MemberShare> list = memberShareService.list();
        if (list == null || list.size() ==0){
            return AjaxResult.success();
        }
        MemberShare memberShare = list.get(0);
        return AjaxResult.success(memberShare);
    }
}
