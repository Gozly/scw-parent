package com.offcn.scw.project.config;

import com.offcn.scw.common.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProjectConfig {
    //IOC容器管理对象
    @ConfigurationProperties(prefix = "oss")//从配置文件中找到值，当创建对象时同名属性赋值
    @Bean
    public OSSTemplate getOssTamplate(){
        return new OSSTemplate();
    }
}
