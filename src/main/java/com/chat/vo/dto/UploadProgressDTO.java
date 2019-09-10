package com.chat.vo.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UploadProgressDTO {
    private long id;
    private int progressStatus;
    private int progress;

}
