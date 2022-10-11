package com.financia.exchange.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.PublicCarouselHome;
import com.financia.exchange.service.PublicCarouselHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("home")
@Api(tags = "首页-首页相关接口")
public class HomeController extends BaseController {
	@Autowired
    private PublicCarouselHomeService carouselHomeService;

    @GetMapping("carouselList")
    @ApiOperation(value = "首页-轮播图",notes = "App首页的轮播图列表")
    public AjaxResult page(HttpServletRequest request) {
        String lang = LocaleContextHolder.getLocale().toString();
        List<PublicCarouselHome> list = carouselHomeService.list(new QueryWrapper<PublicCarouselHome>()
                .lambda()
                .eq(PublicCarouselHome::getActive,0)
                .eq(PublicCarouselHome::getLanguage,lang).orderByDesc(PublicCarouselHome::getSort));
        return AjaxResult.success(list);
    }

    @GetMapping("queryLang")
    @ApiOperation(value = "",notes = "")
    public AjaxResult queryLang(HttpServletRequest request) {
        String lang = LocaleContextHolder.getLocale().toString();
        return AjaxResult.success(lang);
    }

}
