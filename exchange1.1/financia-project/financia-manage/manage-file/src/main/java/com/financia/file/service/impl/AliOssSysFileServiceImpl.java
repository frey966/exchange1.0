package com.financia.file.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.financia.common.core.domain.R;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.utils.StringUtils;
import com.financia.file.service.IOssFileService;
import com.financia.system.api.domain.SysFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * aliyunOss 文件存储
 *
 * @author ruoyi
 */
@Service("aliOssSysFileService")
public class AliOssSysFileServiceImpl implements IOssFileService
{
    /**
     *
     */
    @Value("${aliyun.accessKeyId:LTAI5tKdykfhPvDC58FhcZuf}")
    public String accessKeyId;

    @Value("${aliyun.accessKeySecret:IowqsVsAr6bfWVAxJkBAdmdpH1aIGE}")
    public String accessKeySecret;

    @Value("${aliyun.ossEndpoint:oss-ap-southeast-1.aliyuncs.com}")
    public String ossEndpoint;

    @Value("${aliyun.ossBucketName:co-base}")
    public String ossBucketName;



    /**
     * FastDfs文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public R<SysFile> uploadFile(MultipartFile file) throws IOException
    {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = getPath(null,
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);

        try {
            // 创建PutObject请求。
            PutObjectResult putObjectResult = ossClient.putObject(ossBucketName, objectName, file.getInputStream());
            return R.ok(new SysFile(objectName));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return R.fail("Update failed");
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return R.fail("Update failed");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public R<SysFile> deleteFile(String objectName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);

        try {
            boolean found = ossClient.doesObjectExist(ossBucketName, objectName);
            if (!found){
                return R.fail("File does not exist");
            }
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(ossBucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return R.fail("Delete failed");
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return R.fail("Delete failed");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return R.ok();
    }

    public R<Map> policy() {
        String dir = DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()) + "/"; // 用户上传文件时指定的前缀。

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);
        Map<String, String> respMap = new LinkedHashMap<>();
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap.put("accessid", accessKeyId);
            respMap.put("accessKeySecret", accessKeySecret);
            respMap.put("ossBucketName", ossBucketName);//
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", ossEndpoint);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return R.ok(respMap);
    }

    @Override
    public OSSObject downloadFile (String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = ossClient.getObject(ossBucketName, objectName);
            return ossObject;
//            InputStream objectContent = ossObject.getObjectContent();
//            // 读取文件内容。
//            System.out.println("Object content:");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));
//            while (true) {
//                String line = reader.readLine();
//                if (line == null) break;
//
//                System.out.println("\n" + line);
//            }
//            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
//            reader.close();
//            // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
//            ossObject.close();

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            return null;
        } catch (Throwable ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 文件路径
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 返回上传路径
     */
    public String getPath(String prefix, String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path = DateUtils.dateTime() + "/" + uuid;

        if(StringUtils.isNotBlank(prefix)){
            path = prefix + "/" + path;
        }
        return path + suffix;
    }

}
