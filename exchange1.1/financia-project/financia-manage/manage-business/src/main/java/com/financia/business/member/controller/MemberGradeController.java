package com.financia.business.member.controller;

import java.util.List;

import com.financia.business.MemberGrade;
import com.financia.business.member.service.IMemberGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 会员等级Controller
 * 
 * @author 花生
 * @date 2022-08-05
 */
@RestController
@Api(tags="会员管理-会员等级")
@RequestMapping("/membergrade")
public class MemberGradeController extends BaseController
{
    @Autowired
    private IMemberGradeService memberGradeService;

    /**
     * 查询会员等级列表
     */
    @ApiOperation(value = "查询会员等级列表",notes = "查询会员等级列表")
//    @RequiresPermissions("member:membergrade:list")
    @GetMapping("/list")
    public TableDataInfo list(MemberGrade memberGrade)
    {
        startPage();
        List<MemberGrade> list = memberGradeService.selectMemberGradeList(memberGrade);
        return getDataTable(list);
    }

//    /**
//     * 导出会员等级列表
//     */
//    @RequiresPermissions("member:membergrade:export")
//    @Log(title = "会员等级", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberGrade memberGrade)
//    {
//        List<MemberGrade> list = memberGradeService.selectMemberGradeList(memberGrade);
//        ExcelUtil<MemberGrade> util = new ExcelUtil<MemberGrade>(MemberGrade.class);
//        util.exportExcel(response, list, "会员等级数据");
//    }

    /**
     * 获取会员等级详细信息
     */
    @ApiOperation(value = "获取会员等级详细信息",notes = "获取会员等级详细信息")
//    @RequiresPermissions("member:membergrade:query")
    @GetMapping(value = "getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberGradeService.selectMemberGradeById(id));
    }

    /**
     * 新增会员等级
     */
    @ApiOperation(value = "新增会员等级",notes = "新增会员等级")
//    @RequiresPermissions("member:membergrade:add")
    @Log(title = "会员等级", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody MemberGrade memberGrade)
    {
        return toAjax(memberGradeService.insertMemberGrade(memberGrade));
    }

    /**
     * 修改会员等级
     */
    @ApiOperation(value = "修改会员等级",notes = "修改会员等级")
//    @RequiresPermissions("member:membergrade:edit")
    @Log(title = "会员等级", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody MemberGrade memberGrade)
    {
        return toAjax(memberGradeService.updateMemberGrade(memberGrade));
    }

//    /**
//     * 删除会员等级
//     */
//    @ApiOperation(value = "删除会员等级",notes = "删除会员等级")
////    @RequiresPermissions("member:membergrade:remove")
//    @Log(title = "会员等级", businessType = BusinessType.DELETE)
//	@GetMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(memberGradeService.deleteMemberGradeByIds(ids));
//    }
}
