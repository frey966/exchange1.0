package com.financia.business.dataconfiguration.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.business.dataconfiguration.service.IPVerificationCodeService;
import com.financia.common.PVerificationCode;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.swap.ContractOrderEntrust;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用户注册或登录发送邮箱验证码Controller
 *
 * @author ruoyi
 * @date 2022-07-13
 */
@Api(tags="数据配置管理-验证码管理")
@RestController
@RequestMapping("/email")
public class PVerificationCodeController extends BaseController
{
    @Autowired
    private IPVerificationCodeService pEmailVerificationCodeService;


    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "验证码列表",notes = "获取所有的验证码列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(String contact,Integer sendStatus,Integer type){
        QueryWrapper<PVerificationCode> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(contact)){
            queryWrapper
                    .lambda()
                    .and(wrapper -> wrapper
                            .like(PVerificationCode::getContact,contact));
        }
        if (sendStatus != null){
            queryWrapper.and(wrapper -> wrapper.lambda().eq(PVerificationCode::getSendStatus,sendStatus));
        }
        if (type != null) {
            queryWrapper.and(wrapper -> wrapper.lambda().eq(PVerificationCode::getType,type));
        }
        queryWrapper.lambda().orderByDesc(PVerificationCode::getCreateTime);
        startPage();
        List<PVerificationCode> list = pEmailVerificationCodeService.list(queryWrapper);
        return getDataTable(list);
    }


    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation(value = "验证码删除",notes = "验证码删除根据ID")
//    @PostMapping("country:syscountrycode:delete")
    public AjaxResult delete(@RequestBody Long[] ids){
        boolean b = pEmailVerificationCodeService.removeByIds(Arrays.asList(ids));
        if (b) {
            return AjaxResult.success();
        }
        return error();
    }
}
