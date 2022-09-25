package com.financia.conversion.collect.controller;
import com.financia.conversion.collect.service.MemberExchangeCurrencyFavoriteService;
import com.financia.exchange.MemberExchangeCurrencyFavorite;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 会员汇兑收藏Controller
 * 
 * @author 花生
 * @date 2022-08-30
 */
@Api(tags="会员汇兑收藏")
@RestController
@RequestMapping("/memberCurrencyOllect")
@Slf4j
public class MemberExchangeCurrencyFavoriteController extends BaseController
{
    @Autowired
    private MemberExchangeCurrencyFavoriteService memberCurrencyOllectService;

    /**
     * 新增会员汇兑收藏
     */
    @PostMapping("add")
    @ApiOperation(value = "新增会员汇兑收藏",notes = "新增会员汇兑收藏")
    public AjaxResult add(@RequestBody MemberExchangeCurrencyFavorite memberExchangeCurrencyFavorite)
    {
        AjaxResult ajaxResult=new AjaxResult();

        try{
            if(ObjectUtils.isEmpty(memberExchangeCurrencyFavorite.getMemberId()) || ObjectUtils.isEmpty(memberExchangeCurrencyFavorite.getCurrencyId())){
                ajaxResult=new AjaxResult(-2,"请检查请求参数！ ");
            }else{
                ajaxResult =toAjax(memberCurrencyOllectService.insertMemberCurrencyOllect(memberExchangeCurrencyFavorite));
            }
        }catch (Exception e){
            Throwable cause = e.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException) {
                String sqlState = ((SQLIntegrityConstraintViolationException) cause).getSQLState();
                ajaxResult=new AjaxResult(-1,"您已經收藏過啦！ ");
            }
        }finally {
            return ajaxResult;
        }
    }

    /**
     * 删除会员汇兑收藏
     */
	@GetMapping("/{ids}")
    @ApiOperation(value = "删除会员汇兑收藏",notes = "删除会员汇兑收藏")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberCurrencyOllectService.deleteMemberCurrencyOllectByIds(ids));
    }
}
