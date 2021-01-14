package com.offcn.scw.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OSSTemplate {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String bucketDomain;

    //文件上传功能
    public String upload(InputStream is,String filename) throws IOException {

        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String nName = UUID.randomUUID().toString().replaceAll("-","")+filename;
        String objectName = "images/"+nName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.putObject(bucketName, objectName, is);

        is.close();
        // 关闭OSSClient。
        ossClient.shutdown();

        return "https://"+bucketDomain+"/"+objectName;
    }
}
