package com.chat.vo.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private long id;
    private String eMail;
    private String telephone;
    private String password;
    private String name;
    private String nickname;
    private String headPortraits;
    private long createTime;
    private long updateTime;
    private int isDelete;
}
