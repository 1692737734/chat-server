package com.chat.service.impl;

import com.chat.dao.UserDao;
import com.chat.service.CodeService;
import com.chat.util.CommonUtil;
import com.chat.util.redis.RedisUtil;
import com.chat.util.token.JavaWebToken;
import com.chat.vo.ResultData;
import com.chat.vo.entity.User;
import com.chat.vo.enums.RedisEnum;
import com.chat.vo.enums.ResultEnum;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class CodeServiceImpl implements CodeService{
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserDao userDao;
    @Override
    public ResultData getRegisterCode(String eMail, String deviceId) throws MessagingException {
        if(StringUtils.isBlank(deviceId)||StringUtils.isBlank(eMail)){
            return ResultData.instance(ResultEnum.PARAM_IGNORE.getKey(),ResultEnum.PARAM_IGNORE.getValue(),null);
        }
        if(!CommonUtil.isEmail(eMail)){
            return ResultData.instance(ResultEnum.EMAIL_ERROR.getKey(),ResultEnum.EMAIL_ERROR.getValue(),null);
        }
        //判断邮箱是否已经被注册
        List<User> users = userDao.getUserByEmail(eMail);
        if(users!=null&&users.size()>0){
            return ResultData.instance(ResultEnum.EMAIL_EXIST.getKey(),ResultEnum.EMAIL_EXIST.getValue(),null);
        }
        //判断是否被锁定
        String lockKey = RedisEnum.EMAIL_REGISTER_CODE_LOCK+eMail;
        Object lockObj = redisUtil.get(lockKey);
        if(lockObj!=null){
            return ResultData.instance(ResultEnum.SEND_EMAIL_CODE_FREQUENT.getKey(),ResultEnum.SEND_EMAIL_CODE_FREQUENT.getValue(),null);
        }
        String code = getCode();
        String msg = "您的验证码为："+code+"有效时间为5分钟";
        String key = RedisEnum.EMAIL_REGISTER_CODE+deviceId+"_"+eMail;

        redisUtil.set(lockKey,code,60);
        redisUtil.set(key,code,60*5);

        sendEmail(eMail,msg);
        //生成相应的token
        String pix = UUID.randomUUID().toString();
        Map<String,Object> tokenMap = new HashMap<>();
        tokenMap.put("eMail",eMail);
        tokenMap.put("pix",pix);
        String token = JavaWebToken.createToken(tokenMap);
        return ResultData.instance(ResultEnum.OK.getKey(),ResultEnum.OK.getValue(),token);
    }

    /**
     * 发送验证码
     * @param email
     * @param msg
     * @throws Exception
     */
    private void sendEmail(String email, String msg) throws MessagingException {//path是指你要发给哪个邮箱号，title是指你的邮件的标题。msg是指你的邮件的内容。

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "SMTP");
        props.setProperty("mail.smtp.host", "smtp.163.com");
        props.setProperty("mail.smtp.port", "25");
        // 指定验证为true
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.timeout","1000");
        // 验证账号及密码，密码需要是第三方授权码
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("18358587522@163.com", "sjd199313");
            }
        };
        Session session = Session.getInstance(props, auth);

        // 2.创建一个Message，它相当于是邮件内容
        Message message = new MimeMessage(session);
        // 设置发送者
        message.setFrom(new InternetAddress("18358587522@163.com"));
        // 设置发送方式与接收者
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
        // 设置主题
        message.setSubject("验证码");
        // 设置内容
        message.setContent(msg, "text/html;charset=utf-8");

        // 3.创建 Transport用于将邮件发送
        Transport.send(message);

    }



    /**
     * 获取随机验证码
     * @return
     */
    private String getCode(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
