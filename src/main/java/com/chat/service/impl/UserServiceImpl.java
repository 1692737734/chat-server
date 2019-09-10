package com.chat.service.impl;

import com.chat.dao.UserDao;
import com.chat.file.UploadFactory;
import com.chat.file.utils.FileUtils;
import com.chat.service.UserService;
import com.chat.util.CommonUtil;
import com.chat.util.CreateNamePicture;
import com.chat.util.MD5Util;
import com.chat.util.redis.RedisUtil;
import com.chat.util.token.JavaWebToken;
import com.chat.vo.ResultData;
import com.chat.vo.dto.TokenDTO;
import com.chat.vo.entity.User;
import com.chat.vo.enums.RedisEnum;
import com.chat.vo.enums.ResultEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户相关
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UploadFactory uploadFactory;
    @Autowired
    private UserDao userDao;
    @Override
    public ResultData createAccount(String eMail, String password, String code, String token,String deviceId) {
        if(StringUtils.isBlank(eMail)||StringUtils.isBlank(password)||StringUtils.isBlank(code)||StringUtils.isBlank(token)){
            return ResultData.instance(ResultEnum.PARAM_IGNORE.getKey(),ResultEnum.PARAM_IGNORE.getValue(),null);
        }
        //做一些基本的校验
        //邮箱验证
        if(!CommonUtil.isEmail(eMail)){
            return ResultData.instance(ResultEnum.EMAIL_ERROR.getKey(),ResultEnum.EMAIL_ERROR.getValue(),null);
        }
        //验证邮箱是否是获取验证码的邮箱
        Map<String,Object> tokenMap = JavaWebToken.verifyToken(token);
        if(tokenMap == null){
            return ResultData.instance(ResultEnum.Param_Error.getKey(),ResultEnum.Param_Error.getValue(),null);
        }
        if(!tokenMap.get("eMail").equals(eMail)){
            return ResultData.instance(ResultEnum.Param_Error.getKey(),ResultEnum.Param_Error.getValue(),null);
        }
        //验证密码
        if(!CommonUtil.checkPass(password)){
            return ResultData.instance(ResultEnum.PASSWORD_TYPE_ERROR.getKey(),ResultEnum.PASSWORD_TYPE_ERROR.getValue(),null);
        }
        //验证验证码
        String key = RedisEnum.EMAIL_REGISTER_CODE+deviceId+"_"+eMail;
        Object codeObj = redisUtil.get(key);
        if(codeObj == null){
            return ResultData.instance(ResultEnum.CODE_TIME_OUT.getKey(),ResultEnum.CODE_TIME_OUT.getValue(),null);
        }
        String redisCode = codeObj.toString();
        if(!code.equals(redisCode)){
            return ResultData.instance(ResultEnum.CODE_ERROR7.getKey(),ResultEnum.CODE_ERROR7.getValue(),null);
        }

        //组装
        tokenMap.put("code",code);
        tokenMap.put("password",password);
        token = JavaWebToken.createToken(tokenMap);
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),token);
    }

    @Override
    public ResultData completeInfo(String token, String headPortraits, String name) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(StringUtils.isBlank(token)||StringUtils.isBlank(name)){
            return ResultData.instance(ResultEnum.PARAM_IGNORE.getKey(),ResultEnum.PARAM_IGNORE.getValue(),null);
        }
        //解析一下token
        Map<String,Object> tokenMap = JavaWebToken.verifyToken(token);
        if(tokenMap == null){
            return ResultData.instance(ResultEnum.Param_Error.getKey(),ResultEnum.Param_Error.getValue(),null);
        }
        String eMail = tokenMap.get("eMail").toString();
        String password = tokenMap.get("password").toString();
        long time = System.currentTimeMillis();
        if(StringUtils.isBlank(headPortraits)){
            InputStream img = CreateNamePicture.generateImg(name);
            String path = "img/"+sdf.format(new Date());
            String filename = FileUtils.tranName(name,"jpg");
            uploadFactory.instance().uploadFile(img,path,filename);
            headPortraits = uploadFactory.instance().getFileUrl(path+"/"+filename);
        }

        User user = User.builder()
                .eMail(eMail)
                .password(MD5Util.getMD5(password))
                .name(name)
                .headPortraits(headPortraits)
                .createTime(time)
                .updateTime(time)
                .isDelete(0)
                .build();

        userDao.insert(user);
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),null);
    }

    @Override
    public ResultData loginByPass(String eMail, String password, String deviceId) {
        if(StringUtils.isBlank(eMail)||StringUtils.isBlank(password)||StringUtils.isBlank(deviceId)){
            return ResultData.instance(ResultEnum.PARAM_IGNORE.getKey(),ResultEnum.PARAM_IGNORE.getValue(),null);
        }
        //去数据库查询邮箱以及密码的匹配
        List<User> userList = userDao.getUserByEmailAndPassword(eMail,MD5Util.getMD5(password));
        if(userList == null || userList.size() == 0){
            return ResultData.instance(ResultEnum.PASSWORD_OR_EMAIL_ERROR.getKey(),ResultEnum.PASSWORD_OR_EMAIL_ERROR.getValue(),null);
        }

        User user = userList.get(0);
        //accessToken 有效期半个小时
        //refreshToken 有效期为一星期
        Map<String,Object> accessTokenMap = new HashMap<>();
        String accessTokenPix = UUID.randomUUID().toString().replace("-","");
        accessTokenMap.put("id",user.getId());
        accessTokenMap.put("pix", accessTokenPix);
        String accessToken = JavaWebToken.createToken(accessTokenMap);

        Map<String,Object> refreshTokenMap = new HashMap<>();
        String refreshTokenPix = UUID.randomUUID().toString().replace("-","");
        refreshTokenMap.put("id",user.getId());
        refreshTokenMap.put("pix", refreshTokenPix);
        String refreshToken = JavaWebToken.createToken(accessTokenMap);

        redisUtil.set(RedisEnum.ACCESS_TOKEN_LOCK.getKey()+user.getId()+"_"+deviceId,accessTokenPix,60*30);
        redisUtil.set(RedisEnum.REFRESH_TOKEN_LOCK.getKey()+user.getId()+"_"+deviceId,accessTokenPix,60*60*24*7);
        redisUtil.set(RedisEnum.BASE_SOCKET_USER_INFO.getKey()+user.getId(),user);

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(accessToken);
        tokenDTO.setRefreshToken(refreshToken);
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),tokenDTO);
    }

    @Override
    public ResultData refresh(String accessToken, String refreshToken, String deviceId) {
        if(StringUtils.isBlank(accessToken)||StringUtils.isBlank(refreshToken)||StringUtils.isBlank(deviceId)){
            return ResultData.instance(ResultEnum.PARAM_IGNORE.getKey(),ResultEnum.PARAM_IGNORE.getValue(),null);
        }
        //判断refreshToken是否有效
        Map<String,Object> refreshTokenMap = JavaWebToken.verifyToken(refreshToken);
        long userId = Long.valueOf(refreshTokenMap.get("id").toString());
        String pix = refreshTokenMap.get("pix").toString();
        Object pixObj = redisUtil.get(RedisEnum.REFRESH_TOKEN_LOCK.getKey()+userId+"_"+deviceId);
        if(pixObj == null||!pixObj.toString().equals(pix)){
            return ResultData.instance(ResultEnum.NO_LOGIN.getKey(),ResultEnum.NO_LOGIN.getValue(),null);
        }

        Map<String,Object> accessTokenMap = new HashMap<>();
        String accessTokenPix = UUID.randomUUID().toString().replace("-","");
        accessTokenMap.put("id",userId);
        accessTokenMap.put("pix", accessTokenPix);
        accessToken = JavaWebToken.createToken(accessTokenMap);

        refreshTokenMap = new HashMap<>();
        String refreshTokenPix = UUID.randomUUID().toString().replace("-","");
        refreshTokenMap.put("id",userId);
        refreshTokenMap.put("pix", refreshTokenPix);
        refreshToken = JavaWebToken.createToken(accessTokenMap);

        redisUtil.set(RedisEnum.ACCESS_TOKEN_LOCK.getKey()+userId+"_"+deviceId,accessTokenPix,60*30);
        redisUtil.set(RedisEnum.REFRESH_TOKEN_LOCK.getKey()+userId+"_"+deviceId,accessTokenPix,60*60*24*7);

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAccessToken(accessToken);
        tokenDTO.setRefreshToken(refreshToken);
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),tokenDTO);
    }

    @Override
    public ResultData getBaseUserInfo(long userId) {
        return null;
    }


}
