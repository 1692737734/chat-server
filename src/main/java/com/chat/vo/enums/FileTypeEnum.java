package com.chat.vo.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 文件类型枚举
 */
public enum FileTypeEnum {
    IMAGE(1,"图片"),
    OFFICE(2,"office文件"),
    VIDEO(3,"视频")
    ;

    private final Integer key;
    private final String value;

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private FileTypeEnum(int key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static FileTypeEnum getFileTypeEnum(int key){
        if(0 == key){
            return null;
        }
        for(FileTypeEnum temp: FileTypeEnum.values()){
            if(temp.getKey()==key){
                return temp;
            }
        }
        return null;
    }
    public static FileTypeEnum getFileTypeEnum(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(FileTypeEnum temp: FileTypeEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return null;
    }
}
