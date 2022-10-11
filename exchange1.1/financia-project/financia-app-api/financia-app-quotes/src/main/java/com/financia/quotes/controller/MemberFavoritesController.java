package com.financia.quotes.controller;

import com.financia.common.MemberFavorites;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.quotes.service.MemberFavoritesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@Api(tags = "行情收藏自选")
@RestController
@RequestMapping("/member/favorites")
@Slf4j
public class MemberFavoritesController extends BaseController {

    @Autowired
    private MemberFavoritesService memberFavoritesService;

    @PostMapping(value = "/addFavorites")
    @ApiOperation(value = "添加自选", notes = "添加自选")
    public AjaxResult addFavorites(@RequestBody MemberFavorites memberFavorites) {
        Long userId = getUserId();
        memberFavorites.setMemberId(userId);
        memberFavoritesService.insert(memberFavorites);
        return AjaxResult.success();
    }

    @PostMapping(value = "/cancelFavorites")
    @ApiOperation(value = "取消自选", notes = "取消自选")
    public AjaxResult cancelFavorites(@RequestBody MemberFavorites memberFavorites) {
        memberFavoritesService.updateStatus(memberFavorites.getId(), memberFavorites.getStatus());
        return AjaxResult.success();
    }


    @PostMapping(value = "/memberFavoritesList")
    @ApiOperation(value = "自选列表", notes = "自选列表")
    public AjaxResult getMemberFavoritesList(@RequestBody MemberFavorites memberFavorites) {
        Long userId = getUserId();
        memberFavorites.setMemberId(userId);
        List<MemberFavorites> list = memberFavoritesService.getMemberFavoritesList(memberFavorites);
        return AjaxResult.success(list);
    }


}
