package com.financia.common.core.web.controller;

import cn.hutool.core.io.IoUtil;
import com.financia.common.core.constant.HttpStatus;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.utils.PageUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.github.pagehelper.PageInfo;
import org.lionsoul.ip2region.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author ruoyi
 */
public class BaseController
{

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getAppToken () {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String token = request.getHeader("x-auth-token");
        return token;
    }

    public Long getUserId () {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Long userId = Long.parseLong(request.getHeader("user_id"));
        return userId;
    }
    public String getUserName () {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String username = request.getHeader("username");
        return username;
    }
    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setRows(list);
        rspData.setMsg("查询成功");
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }


    public String getRemoteIp(HttpServletRequest request) {
        String ip = getIpAddress(request);
        String fromIp = null;
        try {
            fromIp = getFromIp(ip);
        }catch (DbMakerConfigException db){
            db.printStackTrace();
        }
        ip = ip + fromIp;
        return ip;
    }

    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip ==null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip ==null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip ==null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }


    private static  final String dbPath = "/ip2region.db";
    private static byte[] dbData = null;
    static {
        dbData = IoUtil.readBytes(BaseController.class.getResourceAsStream(dbPath));
    }

    /**
     * 根据IP获取地区
     * @param ip
     * @return
     * @throws DbMakerConfigException
     */
    public static String getFromIp(String ip) throws DbMakerConfigException{
        DbSearcher dbSearcher = new DbSearcher(new DbConfig(), dbData);
        String region = null;
        DataBlock dataBlock = null;
        if (Util.isIpAddress(ip)){
            try {
                dataBlock = dbSearcher.memorySearch(ip);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            region = dataBlock.getRegion();
            return region;
        }
        return null;
    }
}
