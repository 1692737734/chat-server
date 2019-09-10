package com.chat.service;

import com.chat.vo.ResultData;

import java.io.IOException;

/**
 * 用户相关
 */
public interface UserService {
    /**
     * 创建账号
     * @param eMail
     * @param password
     * @param code
     * @param token
     * @return
     */
    ResultData createAccount(String eMail,String password,String code,String token,String deviceId);

    /**
     * 完善账号信息
     * @param token
     * @param headPortraits
     * @param name
     * @return
     * @throws IOException
     */
    ResultData completeInfo(String token,String headPortraits,String name) throws IOException;

    /**
     * 账号密码登录
     * @param eMail
     * @param password
     * @param deviceId
     * @return
     */
    ResultData loginByPass(String eMail,String password,String deviceId);

    /**
     * 刷新token
     * @param accessToken
     * @param refreshToken
     * @param deviceId
     * @return
     */
    ResultData refresh(String accessToken,String refreshToken,String deviceId);

    ResultData getBaseUserInfo(long userId);
}
