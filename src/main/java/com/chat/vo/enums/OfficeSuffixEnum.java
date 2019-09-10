package com.chat.vo.enums;

import org.apache.commons.lang.StringUtils;

public enum OfficeSuffixEnum {
    DOC("doc","doc"),
    DOCX("docx","docx"),
    PPT("ppt","ppt"),
    PPTX("pptx","pptx"),
    XLS("xls","xls"),
    XLSX("xlsx","xlsx"),
    PDF("pdf","pdf")

    ;
    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private OfficeSuffixEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static OfficeSuffixEnum getOfficeSuffixEnum(String key){
        if("".equals(key)){
            return null;
        }
        for(OfficeSuffixEnum temp: OfficeSuffixEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
    public static OfficeSuffixEnum getOfficeSuffixEnumByValue(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(OfficeSuffixEnum temp: OfficeSuffixEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return null;
    }
}
