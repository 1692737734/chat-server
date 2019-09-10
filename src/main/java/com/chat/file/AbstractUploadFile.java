package com.chat.file;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractUploadFile {
    /**
     * 上传文件（普通方式）
     * @param inputStream：文件流
     * @param path：文件路径
     * @param filename ： 文件名称 （需要经过特殊构建，区别于原文件名）
     */
    public abstract void uploadFile(InputStream inputStream, String path, String filename);

    /**
     * 大文件上传
     * @param inputStream  ：输入流
     * @param path  ： 路径
     * @param filename
     * @return
     * @throws IOException
     */
    public abstract void uploadBigFile(InputStream inputStream, String path, String filename,long size,long id) throws IOException;

    public abstract String getFileUrl(String fileKey);


}
