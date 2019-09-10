package com.chat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    /**
     * 判断是否为邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)){
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断密码是否符合要求
     * 1，不能全部是数字
     * 2，不能全部是字母
     * 3，必须是数字或字母
     * 8-16位数字或这字母组成
     * @param pass
     * @return
     */
    public static boolean checkPass(String pass){
        if (null==pass || "".equals(pass)){
            return false;
        }
        String regEx1 = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(pass);
        if(m.matches()){
            return true;
        }else{
            return false;
        }
    }
}
