package com.wisewintech.erp.entity.dto;

import java.io.Serializable;

public class ResultDTO<T> implements Serializable {
    private boolean success = true;
    protected String code ;
    protected String msg ;
    protected T data ;

    public ResultDTO() {
    }

    public ResultDTO(boolean success, String code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
