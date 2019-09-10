package com.chat.file.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.PutObjectRequest;
import com.chat.config.OssConfig;
import com.chat.dao.FilesDao;
import com.chat.util.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class UploadTask {
    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private FilesDao filesDao;
    @Async
    public void uploadTask(InputStream inputStream, String path, String filename,long size,long id) throws IOException {
        OSS client = ossConfig.createClient();
        try {
            client.putObject(new PutObjectRequest(ossConfig.getOssBucket(), path + "/" + filename, inputStream).
                    <PutObjectRequest>withProgressListener(new PutObjectProgressListener(size,id,redisUtil,filesDao)));
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
