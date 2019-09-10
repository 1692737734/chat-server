package com.chat.service;

import com.chat.vo.ResultData;

import javax.mail.MessagingException;

public interface CodeService {
    ResultData getRegisterCode(String eMail,String deviceId) throws MessagingException;
}
