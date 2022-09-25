package com.financia.system.contactus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.ContactUs;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.contactus.service.ContactUsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Yezi
 */
@Api(tags = "个人中心-联系我们")
@RestController
@RequestMapping("/contact")
public class ContactUsController extends BaseController {
    @Resource
    private ContactUsService contactUsService;

    @GetMapping("info")
    public AjaxResult info() {
            ContactUs one = contactUsService.getOne(new QueryWrapper<ContactUs>().lambda());
            if (ObjectUtils.isEmpty(one)) {
                return AjaxResult.error("no such result");
            }
            return AjaxResult.success(one);

        }
}
