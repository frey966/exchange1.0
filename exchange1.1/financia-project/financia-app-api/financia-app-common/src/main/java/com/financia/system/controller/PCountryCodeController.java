package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.PCountryCode;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.redis.service.RedisService;
import com.financia.system.SysDictData;
import com.financia.system.api.RemoteDictService;
import com.financia.system.service.PCountryCodeService;
import com.financia.system.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-07-27 21:13:55
 */
@RestController
@RequestMapping("/syscountrycode")
@Api(tags = "公共模块-国家区号代码")
public class PCountryCodeController extends BaseController {
    @Autowired
    private PCountryCodeService pCountryCodeService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private SysDictDataService sysDictDataService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "国家区号列表", notes = "获取所有的国家区号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "en", value = "搜索", dataTypeClass = String.class, paramType = "query")
    })
    public AjaxResult list(@RequestParam Map<String, Object> params) {
        QueryWrapper<PCountryCode> objectQueryWrapper = new QueryWrapper<>();
        if (params.get("en") != null && !StringUtils.isEmpty(params.get("en").toString())) {
            objectQueryWrapper.lambda().like(PCountryCode::getEn, params.get("en").toString());
            objectQueryWrapper.lambda().or().like(PCountryCode::getPhoneCode, params.get("en").toString());
        }
        List<PCountryCode> list = pCountryCodeService.list(objectQueryWrapper);
        return AjaxResult.success(list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "国家区号详情", notes = "获取国家区号详情根据id")
//    @RequiresPermissions("country:syscountrycode:info")
    public AjaxResult info(@PathVariable("id") Long id) {
        PCountryCode sysCountryCode = pCountryCodeService.getById(id);

        return AjaxResult.success(sysCountryCode);
    }

    @GetMapping(value = "/recharge/dictdateList")
    @ApiOperation(value = "字典列表", notes = "字典列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典名称")
    })
    public AjaxResult dictdateList(String dictType) {
        String key = "app" + dictType;
        List<SysDictData> dictDatas = redisService.getCacheObject(key);
        if (dictDatas == null) {
            QueryWrapper<SysDictData> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().like(SysDictData::getDictType, dictType);
            dictDatas = sysDictDataService.list(objectQueryWrapper);
            redisService.setCacheObject(key, dictDatas, 10l, TimeUnit.MINUTES);
        }
        return AjaxResult.success(dictDatas);
    }

}
