package com.offcn.scw.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//当前工具类的作用是实现日期数据转换为字符串
public class AppDateUtils {

    //转换当前系统时间为指定格式字符串 -- 格式固定
    public static String getFormatTime(){
        //格式化对象
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    //转换当前系统时间为参数指定的格式 -- 传参 ：  格式参数
    public static String getFormatTime(String pattern){
        //格式化对象
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    //转换指定日期为参数指定的格式 -- 日期是参数传入，格式参数传入
    public static String getFormatTime(Date date,String pattern){
        //格式化对象
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
