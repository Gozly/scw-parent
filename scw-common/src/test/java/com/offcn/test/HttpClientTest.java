package com.offcn.test;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientTest {

    //测试使用HttpClient实现URL设置，发出请求，接收响应，并处理响应内容
    public static void main(String[] args) throws IOException {
        //创建httpclient对象
        HttpClient client = HttpClientBuilder.create().build();

        //设置请求
        HttpGet get = new HttpGet("http://www.baidu.com");

        //设置URL、执行请求
        HttpResponse response = client.execute(get);

        //处理响应 -- 获取响应内容
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        System.out.println(s);

    }
}
