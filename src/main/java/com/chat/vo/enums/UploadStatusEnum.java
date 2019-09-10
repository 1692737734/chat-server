package com.chat.vo.enums;

import org.apache.commons.lang.StringUtils;

public enum UploadStatusEnum {
    UPLOADING(1,"上传中"),
    SUCCESS(2,"上传成功"),
    ERROR(3,"上传失败")
    ;
    private final Integer key;
    private final String value;

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private UploadStatusEnum(int key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static UploadStatusEnum getUploadStatusEnum(int key){
        if(0 == key){
            return null;
        }
        for(UploadStatusEnum temp: UploadStatusEnum.values()){
            if(temp.getKey()==key){
                return temp;
            }
        }
        return null;
    }
    public static UploadStatusEnum getUploadStatusEnum(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(UploadStatusEnum temp: UploadStatusEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return null;
    }
}
