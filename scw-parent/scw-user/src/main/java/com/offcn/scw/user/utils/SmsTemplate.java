package com.offcn.scw.user.utils;

import com.offcn.scw.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//此工具类的作用是发送短信
@Component
@Slf4j
public class SmsTemplate {

    @Value("${sms.host}")
    private String host;
    @Value("${sms.path}")
    private String path;
    @Value("${sms.appcode}")
    private String appcode;

    public String sendSms(Map<String,String>querys) {
        //String host = "http://dingxin.market.alicloudapi.com";
        //String path = "/dx/sendSms";
        //String appcode = "374fa5d63ed6415ca5441d3faa36a8a4";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);

//        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("mobile", "13661285397");
//        querys.put("param", "code:1234");
//        querys.put("tpl_id", "TP1711063");

        Map<String, String> bodys = new HashMap<String, String>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
            HttpEntity entity = response.getEntity();
            log.info("短信发送成功");//向控制台或者日志文件输出执行的日志信息
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("短信发送失败");
            return "fail";
        }
    }
}
