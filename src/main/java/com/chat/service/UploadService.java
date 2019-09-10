package com.chat.service;

import com.chat.vo.ResultData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    /**
     * 基本图片上传
     * @param file
     * @return
     * @throws IOException
     */
    ResultData uploadImg(MultipartFile file,long userId) throws IOException;


    /**
     * 上次文档
     * @param file
     * @return
     * @throws Exception
     */
    ResultData uploadOffice(MultipartFile file,long userId) throws Exception;

    /**
     * 获取上传到阿里云进度
     * @param id
     * @return
     */
    ResultData progressOffice(long id);

    /**
     * 上传视频
     * @param file
     * @return
     * @throws IOException
     */
    ResultData uploadVideo(MultipartFile file,long userId) throws IOException;

    /**
     * 上传视频获取进度
     * @param id
     * @return
     */
    ResultData progressVideo(long id);
}
