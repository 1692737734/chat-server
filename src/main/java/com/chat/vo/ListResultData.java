package com.chat.vo;

import java.io.Serializable;

public class ListResultData extends ResultData implements Serializable {
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static ListResultData instance(int total, Integer code, String msg, Object data){
        ListResultData listResultData = new ListResultData();
        listResultData.setCode(code);
        listResultData.setData(data);
        listResultData.setMsg(msg);
        listResultData.setTotal(total);
        return listResultData;
    }
}
