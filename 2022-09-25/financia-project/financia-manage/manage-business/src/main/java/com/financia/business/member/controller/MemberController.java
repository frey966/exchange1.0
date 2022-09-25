package com.financia.business.member.controller;

import com.alibaba.nacos.common.utils.MD5Utils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.business.member.service.IMemberService;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.exchange.Member;
import com.financia.swap.MemberContractWallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 会员信息Controller
 *
 * @author ruoyi
 * @date 2022-07-13
 */
@Api(tags="会员管理-会员基本信息模块")
@RestController
@RequestMapping("/member-manage")
@Slf4j
public class MemberController extends BaseController
{
    @Autowired
    private IMemberService memberService;


    /**
     * 查询会员信息列表
     */
    @ApiOperation(value = "会员列表信息",notes = "会员列表信息分页信息，根据参数查询会员信息")
    //@RequiresPermissions("manage:member:list")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(String userName, Integer status,String email,String phone)
    {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(userName)){
            queryWrapper
                    .lambda()
                    .and(wrapper -> wrapper
                            .like(Member::getUsername,userName)
                            .or().like(Member::getEmail,userName)
                            .or().like(Member::getPhone,userName));
        }
        if (!StringUtils.isEmpty(email)){
            queryWrapper
                    .lambda()
                    .and(wrapper -> wrapper
                            .like(Member::getEmail,email));
        }
        if (!StringUtils.isEmpty(phone)){
            queryWrapper
                    .lambda()
                    .and(wrapper -> wrapper
                            .like(Member::getPhone,phone));
        }
        if (status != null){
            queryWrapper.and(wrapper -> wrapper.lambda().eq(Member::getStatus,status));
        }
        queryWrapper.lambda().orderByDesc(Member::getCreateTime);
        startPage();
        List<Member> list = memberService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 获取会员信息详细信息
     */
    @ApiOperation(value = "获取会员信息详细信息",notes = "通过会员id查询会员详细信息")
    //@RequiresPermissions("manage:member:query")
    @GetMapping(value = "/query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员id", dataTypeClass = Long.class, paramType = "query", example = "1"),
    })
    public AjaxResult query(@ApiIgnore @RequestParam Map<String, String> params)
    {
        System.out.println(params.get("id"));
        Member member = new Member();
        member.setId(Long.parseLong(params.get("id")));
        return AjaxResult.success(memberService.selectMemberById(member.getId()));
    }

    /**
     * 新增会员信息
     */
/*    @ApiOperation(value = "新增模块",notes = "新增会员信息")
    //@RequiresPermissions("manage:member:add")
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody Member member)
    {
        return toAjax(memberService.insertMember(member));
    }*/

    /**
     * 重置登陆密码
     */
    @ApiOperation(value = "重置登陆密码",notes = "重置登陆密码请求结构：" +
            "{\n" +
            "    \"password\": \"新密码\",\n" +
            "    \"id\": \"id\"\n" +
            "}")
    //@RequiresPermissions("manage:member:update")
    @PostMapping(value = "/resetPassword")
    public AjaxResult resetPassword(@RequestBody Map<String,Object> param)
    {
        if (StringUtils.isEmpty(param.get("password").toString())){
            return AjaxResult.error("The password cannot be empty");
        }
        if (param.get("id") == null){
            return AjaxResult.error("The id cannot be empty");
        }
        Long id = Long.parseLong(param.get("id").toString());
        Member dbOne = memberService.getById(id);
        if (dbOne == null) {
            return AjaxResult.error("数据库中不存在");
        }
        Member member = new Member();
        member.setId(id);
        member.setPassword(MD5Utils.md5Hex(member.getPassword() + dbOne.getUid().toString(),"UTF-8"));
        boolean b = memberService.updateById(member);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error("保存失败");
    }

    /**
     * 重置交易密码
     */
    @ApiOperation(value = "重置交易密码",notes = "重置交易密码请求结构：" +
            "{\n" +
            "    \"password\": \"新密码\",\n" +
            "    \"id\": \"id\"\n" +
            "}")
    //@RequiresPermissions("manage:member:update")
    @PostMapping(value = "/resetJyPassword")
    public AjaxResult resetJyPassword(@RequestBody Map<String,Object> param)
    {
        if (StringUtils.isEmpty(param.get("password").toString())){
            return AjaxResult.error("The password cannot be empty");
        }
        if (param.get("id") == null){
            return AjaxResult.error("The id cannot be empty");
        }
        Long id = Long.parseLong(param.get("id").toString());
        Member dbOne = memberService.getById(id);
        if (dbOne == null) {
            return AjaxResult.error("数据库中不存在");
        }
        Member member = new Member();
        member.setId(id);
        member.setJyPassword(MD5Utils.md5Hex(param.get("password").toString() + dbOne.getUid().toString(),"UTF-8"));
        boolean b = memberService.updateById(member);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error("保存失败");
    }

    /**
     * 禁用账户
     */
    @ApiOperation(value = "禁用账户",notes = "禁用账户")
    //@RequiresPermissions("manage:member:update")
    @PostMapping(value = "/disable")
    public AjaxResult unenable(Long id)
    {
        if (id == null){
            return AjaxResult.error("The id cannot be empty");
        }
        Member dbOne = memberService.getById(id);
        if (dbOne == null) {
            return AjaxResult.error("数据库中不存在");
        }
        Member member = new Member();
        member.setId(id);
        member.setStatus(DataStatus.UNENABLE.getCode());
        boolean b = memberService.updateById(member);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error("保存失败");
    }

    /**
     * 解禁账户
     */
    @ApiOperation(value = "解禁账户",notes = "解禁账户")
    //@RequiresPermissions("manage:member:update")
    @PostMapping(value = "/enable")
    public AjaxResult enable(Long id)
    {
        if (id == null){
            return AjaxResult.error("The id cannot be empty");
        }
        Member dbOne = memberService.getById(id);
        if (dbOne == null) {
            return AjaxResult.error("数据库中不存在");
        }
        Member member = new Member();
        member.setId(id);
        member.setStatus(DataStatus.VALID.getCode());
        boolean b = memberService.updateById(member);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error("保存失败");
    }

    /**
     * 禁用交易
     */
    @ApiOperation(value = "禁用交易",notes = "禁用交易")
    //@RequiresPermissions("manage:member:update")
    @PostMapping(value = "/jyDisable")
    public AjaxResult jyUnenable(Long id)
    {
        if (id == null){
            return AjaxResult.error("The id cannot be empty");
        }
        Member dbOne = memberService.getById(id);
        if (dbOne == null) {
            return AjaxResult.error("数据库中不存在");
        }
        Member member = new Member();
        member.setId(id);
        member.setTransactionStatus(DataStatus.DELETED.getCode());
        boolean b = memberService.updateById(member);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error("保存失败");
    }

    /**
     * 解禁交易
     */
    @ApiOperation(value = "解禁交易",notes = "解禁交易")
    //@RequiresPermissions("manage:member:update")
    @PostMapping(value = "/jyEnable")
    public AjaxResult jyEnable(Long id)
    {
        if (id == null){
            return AjaxResult.error("The id cannot be empty");
        }
        Member dbOne = memberService.getById(id);
        if (dbOne == null) {
            return AjaxResult.error("数据库中不存在");
        }
        Member member = new Member();
        member.setId(id);
        member.setTransactionStatus(DataStatus.VALID.getCode());
        boolean b = memberService.updateById(member);
        if (b) {
            return AjaxResult.success();
        }
        return AjaxResult.error("保存失败");
    }


//    /**
//     * 修改会员信息
//     */
//    @ApiOperation(value = "修改模块",notes = "修改会员信息")
//    //@RequiresPermissions("manage:member:update")
//    @PostMapping(value = "/update")
//    public AjaxResult update(@RequestBody Member member)
//    {
//        return toAjax(memberService.updateMember(member));
//    }
    /**
     * test
     */
    @ApiOperation(value = "test",notes = "test，test")
    //@RequiresPermissions("manage:member:list")
    @GetMapping("/test")
    public TableDataInfo test(Member member)
    {

        return getDataTable(null);
    }

}
