package com.wisewintech.erp.config.constants;

import java.util.HashMap;
import java.util.Map;

public class ResponseMsg {
    public static Map<String,String> msg;
    static {
        msg=new HashMap<>();
        //0开头标识公用
        msg.put("00000","成功");
        msg.put("00001","参数异常");


        //1开头用户相关
        msg.put("01000","密码错误");
        msg.put("01001","账号已存在");


    }

}