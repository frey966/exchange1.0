package com.financia.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.exchange.POmpanyBank;
import com.financia.system.service.IPOmpanyBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.security.annotation.RequiresPermissions;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.utils.poi.ExcelUtil;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 公司收款银行卡Controller
 * 
 * @author 花生
 * @date 2022-09-18
 */
@RestController
@RequestMapping("/pOmpanyBankAppController")
@Api(tags="公共模块-公司收款银行卡")
public class POmpanyBankController extends BaseController
{
    @Autowired
    private IPOmpanyBankService pOmpanyBankService;

    /**
     * 查询公司收款银行卡列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "公司收款银行卡列表",notes = "公司收款银行卡列表")
    public TableDataInfo list(POmpanyBank pOmpanyBank)
    {
        startPage();
        List<POmpanyBank> list = pOmpanyBankService.selectPOmpanyBankList(pOmpanyBank);
        return getDataTable(list);
    }



    /**
     * 获取公司收款银行卡详细信息
     */
    @ApiOperation(value = "公司收款银行卡信息",notes = "公司收款银行卡信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pOmpanyBankService.selectPOmpanyBankById(id));
    }


}
