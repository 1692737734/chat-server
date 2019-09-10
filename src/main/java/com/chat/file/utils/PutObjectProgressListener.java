package com.chat.file.utils;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.chat.dao.FilesDao;
import com.chat.util.redis.RedisUtil;
import com.chat.vo.entity.Files;
import com.chat.vo.enums.UploadStatusEnum;


public class PutObjectProgressListener implements ProgressListener {
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    private long id;

    private RedisUtil redisUtil;
    private FilesDao filesDao;
    public PutObjectProgressListener(long size,long id,RedisUtil redisUtil,FilesDao filesDao){
        this.id = id;
        this.totalBytes = size;
        this.redisUtil = redisUtil;
        this.filesDao = filesDao;
    }
    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        Files files = null;
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                //上传开始
                redisUtil.set(RedisKeyUtil.uploadProgress(id),0);
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                //上传即将开始
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    //上传中
                    int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
//                    System.out.println("进度:"+percent);
                    redisUtil.set(RedisKeyUtil.uploadProgress(id),percent);
                } else {
                    //此处需要进行异常处理，文件的总大小异常
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                //上传成功
                redisUtil.del(RedisKeyUtil.uploadProgress(id));
                files = filesDao.getFilesById(id);
                files.setUploadStatus(UploadStatusEnum.SUCCESS.getKey());
                files.setUpdateTime(System.currentTimeMillis());
                filesDao.update(files);
                break;
            case TRANSFER_FAILED_EVENT:
                //上传失败
                redisUtil.del(RedisKeyUtil.uploadProgress(id));
                files = filesDao.getFilesById(id);
                files.setUploadStatus(UploadStatusEnum.ERROR.getKey());
                files.setUpdateTime(System.currentTimeMillis());
                filesDao.update(files);
                break;
            default:
                break;
        }
    }
}
