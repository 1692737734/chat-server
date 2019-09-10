package com.chat.vo;

import java.io.Serializable;

public class ResultData implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Integer code;
    protected String msg;
    protected Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultData instance(int code, String msg, Object data){
        ResultData instance = new ResultData();
        instance.setCode(code);
        instance.setMsg(msg);
        instance.setData(data);
        return instance;
    }
}
