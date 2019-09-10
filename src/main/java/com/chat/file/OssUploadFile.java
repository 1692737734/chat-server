package com.chat.file;

import com.aliyun.oss.OSS;
import com.chat.config.OssConfig;
import com.chat.file.utils.UploadTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Component(value = "ossUploadFile")
public class OssUploadFile extends AbstractUploadFile {
    @Autowired
    private OssConfig ossConfig;
    @Autowired
    private UploadTask uploadTask;
    @Override
    public void uploadFile(InputStream inputStream, String path, String filename) {
        OSS client = ossConfig.createClient();
        client.putObject(ossConfig.getOssBucket(),path+"/"+filename,inputStream);
    }

    @Override
    public void uploadBigFile(InputStream inputStream, String path, String filename,long size,long id) throws IOException {
        uploadTask.uploadTask(inputStream,path,filename,size,id);
    }

    @Override
    public String getFileUrl(String fileKey) {
        OSS client = ossConfig.createClient();
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        URL url = client.generatePresignedUrl(ossConfig.getOssBucket(), fileKey, expiration);
        if (url != null) {
           return url.toString();
        }
       return "";
    }
}
