package com.financia.system.favorite.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.MemberFavorites;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.redis.service.RedisService;

import com.financia.system.favorite.service.MemberFavoritesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 *
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-25 15:56:42
 */
@RestController
@RequestMapping("favorites")
@Api(tags = "合约-收藏功能模块")
public class MemberFavoritesController extends BaseController {
    @Autowired
    private MemberFavoritesService memberFavoritesService;
    @Autowired
    private RedisService redisService;

    /**
     * 列表
     */
    @PostMapping("/tag")
    @ApiOperation(value = "收藏/取消收藏",notes = "App收藏与取消收藏：如果当前状态为未收藏则变更为收藏否则反之")
    public AjaxResult list(@RequestBody MemberFavorites favorite){
        Long userId = getUserId();
        if (userId == null) {
            return error("to get the current user");
        }
        String symbol = favorite.getSymbol();
        String type = favorite.getType();
        if (StringUtils.isEmpty(favorite.getSymbol())){
            return error("type is not empty");
        }
        if (StringUtils.isEmpty(favorite.getType())){
            return error("symbol is not empty");
        }
        MemberFavorites favoriteDb = memberFavoritesService.getOne(new QueryWrapper<MemberFavorites>()
                .lambda()
                .eq(MemberFavorites::getMemberId, userId)
                .eq(MemberFavorites::getSymbol, symbol)
                .eq(MemberFavorites::getType, type));
        if (favoriteDb == null) {// 从未收藏
            MemberFavorites save = new MemberFavorites();
            save.setMemberId(userId);
            save.setSymbol(symbol);
            save.setType(type);
            boolean rs = memberFavoritesService.save(save);
            if (rs) {
                return success();
            }else {
                return error("save failed");
            }
        }else if (favoriteDb.getStatus() == 0) { // 收藏后取消
            MemberFavorites update = new MemberFavorites();
            update.setId(favoriteDb.getId());
            update.setStatus(1);
            boolean rs = memberFavoritesService.updateById(update);
            if (rs) {
                return success();
            }else {
                return error("update failed");
            }
        }else { //已经收藏
            MemberFavorites update = new MemberFavorites();
            update.setId(favoriteDb.getId());
            update.setStatus(0);
            boolean rs = memberFavoritesService.updateById(update);
            if (rs) {
                return success();
            }else {
                return error("untag update failed");
            }
        }

    }


    /**
     * 是否收藏
     */
    @GetMapping("/isTag")
    @ApiOperation(value = "是否收藏",notes = "App查看是否收藏，返回1为已收藏，0为未收藏")
    public AjaxResult info(@RequestParam("type") String type, @RequestParam("symbol") String symbol){
        if (StringUtils.isEmpty(type)){
            return error("type is not empty");
        }
        if (StringUtils.isEmpty(symbol)){
            return error("symbol is not empty");
        }
        Long userId = getUserId();
        if (userId == null) {
            return error("to get the current user");
        }
        long count = memberFavoritesService.count(new QueryWrapper<MemberFavorites>()
                .lambda()
                .eq(MemberFavorites::getMemberId,userId)
                .eq(MemberFavorites::getType, type)
                .eq(MemberFavorites::getSymbol, symbol)
                .eq(MemberFavorites::getStatus, 1));
        if (count > 0){
            return AjaxResult.success(count);
        }else{
            return AjaxResult.success(0);
        }
    }

    /**
     *
     */
    @GetMapping("/list")
    @ApiOperation(value = "收藏列表",notes = "App收藏列表，根据type查询收藏列表")
    public AjaxResult save(@RequestParam("type") String type){
        if (StringUtils.isEmpty(type)){
            return error("type is not empty");
        }
        Long userId = getUserId();
        if (userId == null) {
            return error("to get the current user");
        }
        List<MemberFavorites> list = memberFavoritesService.list(new QueryWrapper<MemberFavorites>()
                .lambda()
                .eq(MemberFavorites::getMemberId, userId)
                .eq(MemberFavorites::getType, type)
                .eq(MemberFavorites::getStatus, 1));
        return AjaxResult.success(list);
    }

}
