package com.chat.vo.enums;

import org.apache.commons.lang.StringUtils;

public enum VideoSuffixEnum {
    MP4("mp4","jpg"),
    ;
    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private VideoSuffixEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static VideoSuffixEnum getVideoSuffixEnum(String key){
        if("".equals(key)){
            return null;
        }
        for(VideoSuffixEnum temp: VideoSuffixEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
    public static VideoSuffixEnum getVideoSuffixEnumByValue(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(VideoSuffixEnum temp: VideoSuffixEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return null;
    }
}
