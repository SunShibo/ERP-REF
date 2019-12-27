package com.wisewintech.erp.entity.dto;


import com.wisewintech.erp.config.constants.ResponseMsg;

public  class  ResultDTOBuilder {

    public ResultDTOBuilder() {}

    public static  ResultDTO success() {
        return new ResultDTO(true,"00000" ,ResponseMsg.msg.get("00000"),new Object());
    }


    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<T>(true,"00000" ,ResponseMsg.msg.get("00000"),data);
    }

    public static  ResultDTO failure(String code) {
        return new ResultDTO(false,code ,ResponseMsg.msg.get(code),new Object());
    }

}