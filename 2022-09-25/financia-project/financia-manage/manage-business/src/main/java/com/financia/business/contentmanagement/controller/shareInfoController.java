package com.financia.business.contentmanagement.controller;

import com.financia.business.contentmanagement.service.MemberShareService;
import com.financia.common.MemberShare;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/gm")
@Api(tags = "内容管理-用户分享配置")
public class shareInfoController extends BaseController {

    @Resource
    private MemberShareService memberShareService;

    @PostMapping(value = "/insertNewInfo")
    @ApiOperation(value = "用户-分享-新增分享消息", notes = "创建新的链接链接")
    public AjaxResult insertNewInfo(@RequestBody MemberShare info) {
        boolean isSuccess = memberShareService.insertShareInfo(info);
        if (!isSuccess) {
            return AjaxResult.error("insert new info failed,please check your info");
        } else {
            return AjaxResult.success();
        }
    }

    @PostMapping(value = "/updateNewInfo")
    @ApiOperation(value = "用户-分享-修改分享消息", notes = "修改已有的新的链接链接")
    public AjaxResult updateInfo(@RequestBody MemberShare info) {
        boolean isSuccess = memberShareService.updateShareInfo(info);
        if (!isSuccess) {
            return AjaxResult.error("update info failed,please check your info");
        } else {
            return AjaxResult.success();
        }
    }

}
