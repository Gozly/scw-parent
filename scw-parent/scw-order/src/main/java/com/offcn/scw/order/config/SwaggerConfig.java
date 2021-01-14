package com.offcn.scw.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//开启swagger
@EnableSwagger2
public class SwaggerConfig {

    //对象创建
    @Bean("订单模块")
    public Docket createDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.scw.order.controller"))//到哪个包扫描swagger注解
                .paths(PathSelectors.any()) //显示哪些请求
                .build();
    }


    //文档参数
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("七易众筹--系统文档")//设置文档标题
                .description("众筹系统相关功能接口的列表 -- 技术支持团队")//文档简介
                .termsOfServiceUrl("http://www.ujiuye.com")//公开的地址
                .version("1.0")
                .build();
    }
}
