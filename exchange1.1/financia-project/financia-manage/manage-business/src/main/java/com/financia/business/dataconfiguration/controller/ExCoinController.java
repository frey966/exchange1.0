package com.financia.business.dataconfiguration.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financia.business.ExCoin;
import com.financia.business.dataconfiguration.service.ExCoinService;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 * 公共-充值币种类型
 *
 * @author dalong
 * @email xxxxxx@qq.com
 * @date 2022-09-11 21:44:30
 */
@RestController
@Api(tags = "公共-充值币种类型")
@RequestMapping("excoin")
public class ExCoinController  extends BaseController {
    @Autowired
    private ExCoinService exCoinService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ExCoin exCoin)
    {
        startPage();
        LambdaQueryWrapper<ExCoin> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(exCoin.getName())) {
            wrapper.eq(ExCoin::getName,exCoin.getName());
        }
        wrapper.eq(ExCoin::getStatus,1);
        List<ExCoin> list = exCoinService.list(wrapper);
        return getDataTable(list);
    }

    /**
     * 信息
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取详情",notes = "获取详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(exCoinService.getById(id));
    }

    /**
     * 保存
     */
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody ExCoin exCoin)
    {
        return toAjax(exCoinService.save(exCoin));
    }

    /**
     * 修改
     */
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody ExCoin exCoin)
    {
        return toAjax(exCoinService.updateById(exCoin));
    }

    /**
     * 删除
     */

    @GetMapping("del/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        for (Long id : ids) {
            ExCoin exCoin1 = new ExCoin();
            exCoin1.setId(id);
            exCoin1.setStatus(0);
            exCoinService.updateById(exCoin1);
        }
        return toAjax(true);
    }


}
