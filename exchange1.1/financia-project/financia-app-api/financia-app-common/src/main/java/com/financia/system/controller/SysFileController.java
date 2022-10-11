package com.financia.system.controller;

import com.financia.common.core.domain.R;
import com.financia.system.api.domain.SysFile;
import com.financia.system.service.IOssFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件请求处理
 *
 * @author ruoyi
 */
@Api(tags="公共模块-文件处理模块")
@RestController
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private IOssFileService aliOssSysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    @ApiOperation(value = "文件上传",notes = "上传文件到阿里OSS")
    public R<SysFile> upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            return  aliOssSysFileService.uploadFile(file);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 文件删除
     */
    @PostMapping("delete")
    @ApiOperation(value = "文件删除",notes = "根据路径删除OSS上文件")
    public R<SysFile> delete(String path)
    {
        try
        {
            return  aliOssSysFileService.deleteFile(path);
        }
        catch (Exception e)
        {
            log.error("文件删除失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 获取上传授权
     */
    @PostMapping("policy")
    @ApiOperation(value = "获取上传授权",notes = "前端上传文件前获取授权")
    public R<Map> policy()
    {
        try
        {
            return aliOssSysFileService.policy();
        }
        catch (Exception e)
        {
            log.error("文件删除失败", e);
            return R.fail(e.getMessage());
        }
    }

}
