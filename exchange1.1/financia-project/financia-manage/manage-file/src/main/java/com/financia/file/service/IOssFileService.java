package com.financia.file.service;

import com.aliyun.oss.model.OSSObject;
import com.financia.common.core.domain.R;
import com.financia.system.api.domain.SysFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 文件上传接口
 *
 * @author ruoyi
 */
public interface IOssFileService
{
    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    R<SysFile> uploadFile(MultipartFile file)  throws IOException;

    R<SysFile> deleteFile(String path);

    R<Map> policy();

    OSSObject downloadFile (String objectName);
}
