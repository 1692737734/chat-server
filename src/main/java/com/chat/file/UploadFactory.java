package com.chat.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UploadFactory {
    @Autowired
    @Qualifier(value = "ossUploadFile")
    private AbstractUploadFile ossUploadFile;
    /**
     * 模式
     */
    @Value("${file.upload.type}")
    private String mode;
    public AbstractUploadFile instance(){
        switch (mode){
            case "go-fast":{
                return null;
            }
            case "oss":{
                return ossUploadFile;
            }
            default:
                return null;
        }
    }

}
