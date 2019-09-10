package com.chat.vo.enums;

import org.apache.commons.lang.StringUtils;

public enum ImageSuffixEnum {
    JPEG("jpg","jpg"),
    PNG("png","png")

    ;
    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private ImageSuffixEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static ImageSuffixEnum getImageSuffixEnum(String key){
        if("".equals(key)){
            return null;
        }
        for(ImageSuffixEnum temp: ImageSuffixEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
    public static ImageSuffixEnum getImageSuffixEnumByValue(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(ImageSuffixEnum temp: ImageSuffixEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return null;
    }
}
