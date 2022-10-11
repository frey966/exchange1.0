package com.financia.conversion.exchange.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.financia.conversion.exchange.service.IMemberExchangeOrderService;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.exchange.vo.MemberConversionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.security.annotation.RequiresPermissions;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.utils.poi.ExcelUtil;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 会员-换汇订单Controller
 * 
 * @author 花生
 * @date 2022-08-31
 */
@Api(tags="会员-换汇模块")
@Slf4j
@RestController
@RequestMapping("/memberExchangeOrder")
public class MemberExchangeOrderController extends BaseController
{
    @Autowired
    private IMemberExchangeOrderService memberExchangeOrderService;
    @Autowired
    private RemoteMemberWalletService memberWalletService;
    /**
     * 查询会员-换汇订单列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "换汇订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(MemberConversionVo memberExchangeOrder)
    {
        startPage();
        List<MemberConversionVo> list = memberExchangeOrderService.selectMemberExchangeOrderList(memberExchangeOrder);
        return getDataTable(list);
    }

    /**
     * 获取会员-换汇订单详细信息
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "换汇订单详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberExchangeOrderService.selectMemberExchangeOrderById(id));
    }

    /**
     * 换汇订单
     * {
     *   "coinId": "2", 法币id
     *   "memberId": "77",  会员id
     *   "nationalMoney": 100   汇兑金额
     *   "exchangeAmount":15  申请换汇金额USDT
     * }
     */
//    @Log(title = "创建换汇订单", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "创建换汇订单")
    public AjaxResult add(@RequestBody MemberConversionVo memberConversionVo)
    {
        if(ObjectUtils.isEmpty(memberConversionVo.getCoinId()) || ObjectUtils.isEmpty(memberConversionVo.getMemberId()) ||
            ObjectUtils.isEmpty(memberConversionVo.getNationalMoney()) || ObjectUtils.isEmpty(memberConversionVo.getExchangeAmount())){
            return AjaxResult.error(-1,"请检查请求参数！");
        }
        return memberExchangeOrderService.insertMemberExchangeOrder(memberConversionVo);
    }

    @PostMapping("verifyJyPassword")
    @ApiOperation(value = "验证交易密码")
    public AjaxResult verifyJyPassword(@RequestBody Map map)
    {
        return AjaxResult.success(memberWalletService.verifyJyPassword(Long.parseLong(map.get("memberId").toString()),map.get("jyPassword").toString()));
    }

}
