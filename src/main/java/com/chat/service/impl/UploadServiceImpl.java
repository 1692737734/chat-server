package com.chat.service.impl;


import com.chat.dao.FilesDao;
import com.chat.file.UploadFactory;
import com.chat.file.image.ImageSafeHandler;
import com.chat.file.utils.FileUtils;
import com.chat.file.utils.RedisKeyUtil;
import com.chat.service.UploadService;
import com.chat.util.redis.RedisUtil;
import com.chat.vo.ResultData;
import com.chat.vo.dto.UploadProgressDTO;
import com.chat.vo.entity.Files;
import com.chat.vo.enums.FileTypeEnum;
import com.chat.vo.enums.ResultEnum;
import com.chat.vo.enums.UploadStatusEnum;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UploadServiceImpl implements UploadService {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private UploadFactory uploadFactory;
    @Autowired
    private FilesDao filesDao;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public ResultData uploadImg(MultipartFile file,long userId) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (file.getSize() > 1024 * 1024 * 10) {
            return ResultData.instance(ResultEnum.IMG_SIZE_OUT.getKey(), ResultEnum.IMG_SIZE_OUT.getValue(), null);
        }
        //对图片进行安全验证
        if(!ImageSafeHandler.isImage(file.getInputStream())){
            return ResultData.instance(ResultEnum.IMAGE_NO_SAFE.getKey(),ResultEnum.IMAGE_NO_SAFE.getValue(),null);
        }
        //提取文件名称与后缀信息
        String originalFilename = file.getOriginalFilename();
        String name = originalFilename.substring(0,originalFilename.lastIndexOf(".")).toLowerCase();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase();
        //判断是否是符合格式的图片
        if(!FileUtils.imageSupport(suffix)){
            return ResultData.instance(ResultEnum.IMAGE_NO_SUPPORT.getKey(),ResultEnum.IMAGE_NO_SUPPORT.getValue(),null);
        }
        String filename = FileUtils.tranName(name,suffix);
        String path = "img/"+sdf.format(new Date());
        uploadFactory.instance().uploadFile(file.getInputStream(),path,filename);
        long size = file.getSize();

        long time = System.currentTimeMillis();
        Files files = Files.builder()
                .fileName(filename)
                .fileKey(path+"/"+filename)
                .fileType(FileTypeEnum.IMAGE.getKey())
                .fileSuffix(suffix)
                .fileSize(size)
                .uploadStatus(UploadStatusEnum.SUCCESS.getKey())
                .creatorId(userId)
                .modifierId(userId)
                .creatorId(time)
                .updateTime(time)
                .isDelete(0)
                .build();
        filesDao.insert(files);

        String url = uploadFactory.instance().getFileUrl(path+"/"+filename);
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),url);
    }


    @Override
    public ResultData uploadOffice(MultipartFile file,long userId) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String originalFilename = file.getOriginalFilename();
        String name = originalFilename.substring(0,originalFilename.lastIndexOf(".")).toLowerCase();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase();

        if(!FileUtils.officeSupport(suffix)){
            return ResultData.instance(ResultEnum.OFFICE_NO_SUPPORT.getKey(),ResultEnum.OFFICE_NO_SUPPORT.getValue(),null);
        }
        //文件安全性校验
        if(!FileUtils.officeSafe(file.getInputStream(),suffix)){
            return ResultData.instance(ResultEnum.OFFICE_NO_SAFE.getKey(),ResultEnum.OFFICE_NO_SAFE.getValue(),null);
        }

        String filename = FileUtils.tranName(name,suffix);
        String path = "office/"+sdf.format(new Date());
        String fileKey = path+"/"+filename;
        logger.info("fileKey："+fileKey);
        logger.info("fileKeyLength:"+fileKey.length());
        long size = file.getSize();
        long time = System.currentTimeMillis();
        //在开始之前存入相关的数据，状态为进行中
        Files files = Files.builder()
                .fileName(filename)
                .fileKey(path+"/"+filename)
                .fileType(FileTypeEnum.OFFICE.getKey())
                .fileSuffix(suffix)
                .fileSize(size)
                .uploadStatus(UploadStatusEnum.UPLOADING.getKey())
                .creatorId(userId)
                .modifierId(userId)
                .creatorId(time)
                .updateTime(time)
                .isDelete(0)
                .build();
        filesDao.insert(files);
        uploadFactory.instance().uploadBigFile(file.getInputStream(),path,filename,size,files.getId());
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),files.getId());
    }

    @Override
    public ResultData progressOffice(long id) {
        //先从数据库获取状态
        Files files = filesDao.getFilesById(id);
        if(files == null){
            return ResultData.instance(ResultEnum.NON_EXISTENT_FILE.getKey(),ResultEnum.NON_EXISTENT_FILE.getValue(),null);
        }
        if(files.getUploadStatus() == UploadStatusEnum.SUCCESS.getKey()){
            UploadProgressDTO dto = UploadProgressDTO.builder()
                    .id(files.getId())
                    .progress(100)
                    .progressStatus(UploadStatusEnum.SUCCESS.getKey())
                    .build();
            return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),dto);
        }
        if(files.getUploadStatus() == UploadStatusEnum.ERROR.getKey()){
            return ResultData.instance(ResultEnum.FILE_UPLOAD_ERROR.getKey(),ResultEnum. FILE_UPLOAD_ERROR.getValue(),null);
        }

        if(files.getUploadStatus() == UploadStatusEnum.UPLOADING.getKey()){
            Object progressObj = redisUtil.get(RedisKeyUtil.uploadProgress(id));
            if(progressObj == null){
                files.setUploadStatus(UploadStatusEnum.ERROR.getKey());
                files.setUpdateTime(System.currentTimeMillis());
                filesDao.update(files);
                return ResultData.instance(ResultEnum.PROGRESS_ERROR.getKey(),ResultEnum.PROGRESS_ERROR.getValue(),null);
            }
            int progress = Integer.valueOf(progressObj.toString());
            return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),progress);
        }
        return ResultData.instance(ResultEnum.UPLOAD_STATUS_ERROR.getKey(),ResultEnum.UPLOAD_STATUS_ERROR.getValue(),null);
    }

    @Override
    public ResultData uploadVideo(MultipartFile file,long userId) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String originalFilename = file.getOriginalFilename();
        String name = originalFilename.substring(0,originalFilename.lastIndexOf(".")).toLowerCase();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase();
        String filename = FileUtils.tranName(name,suffix);

        if(!FileUtils.videoSupport(suffix)){
            return ResultData.instance(ResultEnum.VIDEO_NO_SUPPORT.getKey(),ResultEnum.VIDEO_NO_SUPPORT.getValue(),null);
        }

        String path = "video/"+sdf.format(new Date());
        String fileKey = path+"/"+filename;
        logger.info("fileKey："+fileKey);
        logger.info("fileKeyLength:"+fileKey.length());
        long size = file.getSize();
        long time = System.currentTimeMillis();
        //在开始之前存入相关的数据，状态为进行中
        Files files = Files.builder()
                .fileName(filename)
                .fileKey(path+"/"+filename)
                .fileType(FileTypeEnum.VIDEO.getKey())
                .fileSuffix(suffix)
                .fileSize(size)
                .uploadStatus(UploadStatusEnum.UPLOADING.getKey())
                .creatorId(userId)
                .modifierId(userId)
                .creatorId(time)
                .updateTime(time)
                .isDelete(0)
                .build();
        filesDao.insert(files);
        uploadFactory.instance().uploadBigFile(file.getInputStream(),path,filename,size,files.getId());
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),files.getId());
    }

    @Override
    public ResultData progressVideo(long id) {
        Files files = filesDao.getFilesById(id);
        if(files == null){
            return ResultData.instance(ResultEnum.NON_EXISTENT_FILE.getKey(),ResultEnum.NON_EXISTENT_FILE.getValue(),null);
        }
        if(files.getUploadStatus() == UploadStatusEnum.SUCCESS.getKey()){
            UploadProgressDTO dto = UploadProgressDTO.builder()
                    .id(files.getId())
                    .progress(100)
                    .progressStatus(UploadStatusEnum.SUCCESS.getKey())
                    .build();
            return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),dto);
        }
        if(files.getUploadStatus() == UploadStatusEnum.ERROR.getKey()){
            return ResultData.instance(ResultEnum.FILE_UPLOAD_ERROR.getKey(),ResultEnum. FILE_UPLOAD_ERROR.getValue(),null);
        }

        if(files.getUploadStatus() == UploadStatusEnum.UPLOADING.getKey()){
            Object progressObj = redisUtil.get(RedisKeyUtil.uploadProgress(id));
            if(progressObj == null){
                files.setUploadStatus(UploadStatusEnum.ERROR.getKey());
                files.setUpdateTime(System.currentTimeMillis());
                filesDao.update(files);
                return ResultData.instance(ResultEnum.PROGRESS_ERROR.getKey(),ResultEnum.PROGRESS_ERROR.getValue(),null);
            }
            int progress = Integer.valueOf(progressObj.toString());
            return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),progress);
        }
        return ResultData.instance(ResultEnum.UPLOAD_STATUS_ERROR.getKey(),ResultEnum.UPLOAD_STATUS_ERROR.getValue(),null);
    }
}
