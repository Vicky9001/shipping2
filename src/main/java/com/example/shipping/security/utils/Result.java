package com.example.shipping.security.utils;


import java.util.HashMap;
import java.util.Map;

//统一返回结果的类
public class Result extends ResultCode {

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    public Result() {
        this.code = ResultCode.SUCCESS;
        this.message = null;
        this.data = new HashMap<String, Object>();
    }

    public Result(Integer code, String message, Map<String, Object> data){
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}
