package com.chat.vo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Files {
    private long id;
    private String fileName;
    private String fileKey;
    private String fileUrl;
    private int fileType;
    private String fileSuffix;
    private long fileSize;
    private int uploadStatus;
    private long creatorId;
    private long modifierId;
    private long createTime;
    private long updateTime;
    private int isDelete;
}
