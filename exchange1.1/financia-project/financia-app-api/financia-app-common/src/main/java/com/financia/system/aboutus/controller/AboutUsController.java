package com.financia.system.aboutus.controller;


import com.financia.common.AboutUs;
import com.financia.common.AboutUsModel;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.aboutus.service.AboutUsService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Yezu
 */
@RestController
@RequestMapping("aboutus")
@Api(tags = "个人中心-关于我们")
public class AboutUsController extends BaseController {
    @Resource
    private AboutUsService aboutUsService;

    @GetMapping("info")
    public AjaxResult info() {
        AboutUs one = aboutUsService.list().get(0);
        //默认返回中文内容
        if (ObjectUtils.isEmpty(one)) {
            return AjaxResult.error("no such version");
        }
        AboutUsModel aboutUsModel = new AboutUsModel();
        aboutUsModel.setContentZh(one.getContentZh());
        aboutUsModel.setAppVersion(one.getAppVersion());
        aboutUsModel.setContentEn(one.getContentEn());
        return AjaxResult.success(aboutUsModel);
    }
}
