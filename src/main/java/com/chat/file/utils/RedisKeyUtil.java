package com.chat.file.utils;


import com.chat.vo.enums.RedisEnum;

public class RedisKeyUtil {

    /**
     * 文件上传进度
     * @param id
     * @return
     */
    public static String uploadProgress(long id){
        return RedisEnum.Upload_Progress.getKey()+id;
    }
}
