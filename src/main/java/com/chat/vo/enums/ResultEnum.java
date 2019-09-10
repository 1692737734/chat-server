package com.chat.vo.enums;

import org.apache.commons.lang.StringUtils;

public enum ResultEnum {
    /************************************正确返回参数 200*****************************************************/
    OK(200,"OK"),
    /***********************************参数类，系统类错误，错误码10000-19999***********************************/
    PARAM_IGNORE(10001,"参数缺失"),
    Param_Error(10002,"参数错误"),
    System_Error(10003,"系统错误"),
    NO_SAFE_OPERATE_BY_SLIDER(10004,"操作不安全"),
    DEVICE_NO_BIND(10005,"设备第一次登陆，未绑定"),
    NAME_REPEAT(10006,"名称重复"),
    Code_Send_Often(10007,"短信发送过于频繁"),
    UPDATE_ERROR(10008,"设备锁绑定失败"),
    DATA_TOO_LONG(10009,"字段过长"),
    NO_LOGIN(10010,"未登录"),
    NO_SAFE_OPERATE(10011,"操作不安全"),
    USER_INFO_TIME_OUT(10012,"用户信息超时,请重新进入课程"),
    /***********************************站点类错误，权限类错误  20000-20999************************************/

    ACCESS_NULL(20001,"权限为空"),
    No_Website(20002,"站点不存在"),
    /***********************************用户 登录类错误  21000-21999******************************************/

    Code_Send_Error(21001,"发送验证码阶段发生错误"),
    CHECK_SLIDER_ERROR(21002,"滑动验证失败"),
    SEND_EMAIL_CODE_FREQUENT(21003,"验证码发送过于频繁，稍后再试"),
    No_Telephone(21004,"手机号不存在"),
    TELEPHONE_OR_PASSWORD_EMPTY(21005,"用户名或密码不能为空"),
    PARAM_ERROR(21006,"用户名不正确"),
    PARAM_ERROR3(21007,"手机号格式不正确"),
    NOT_EXISTS_TELEPHONE_ERROR(21008,"账号不存在"),
    MANAGER_FROZEN(21009,"账号冻结"),
    NO_SAFE_OPERATE2(21010,"获取验证码失败"),
    CODE_TIME_OUT(21011,"验证码未发送或者验证码已过期"),
    CODE_ERROR6(21012,"验证码错误次数过多,请重新获取验证码"),
    CODE_ERROR7(21013,"验证码错误或已过期"),
    PASSWORD_TYPE_ERROR(21020,"密码格式错误"),
    TOKEN_TIME_OUT(21021,"登录超时"),
    MANAGER_STOP(21022,"账号被禁用"),
    MANAGER_MISS(21023,"密码不存在"),
    PASSWORD_ERROR(21024,"密码错误"),
    USER_MISS(21025,"用户不存在"),
    IDENTITY(21026,"身份错误"),
    EMAIL_ERROR(21027,"邮箱格式错误"),
    EMAIL_EXIST(21028,"邮箱已存在"),
    PASSWORD_OR_EMAIL_ERROR(21083,"密码或者邮箱错误"),


    /***********************************文件上传类  22000-22999******************************************/

    IMG_SIZE_OUT(22000,"图片大小超出限制"),
    NON_EXISTENT_FILE(22001,"文件不存在"),
    FILE_UPLOAD_ERROR(22002,"文件上传错误"),
    PROGRESS_ERROR(22003,"进度异常，上传失败"),
    UPLOAD_STATUS_ERROR(22004,"上传过程中状态异常或者发生错误"),
    IMAGE_NO_SAFE(22005,"图片不安全"),
    IMAGE_NO_SUPPORT(22006,"不支持的图片格式"),
    OFFICE_NO_SUPPORT(22007,"不支持的文件格式"),
    OFFICE_NO_SAFE(22008,"上传的文件不安全"),
    VIDEO_NO_SUPPORT(22009,"不支持的视频格式"),


    /***********************************ws  23000-23999******************************************/

    NO_CHANNEL(23001,"频道不存在"),
    COURSE_NO_BIND_CHANNEL(23002,"课程未绑定频道"),
    LIVE_INFO_QUERY_ERROR(23003,"直播信息查询错误"),
    COURSE_NO_BIND(23004,"课程未绑定"),
    NO_RELATION(23005,"关系不存在"),

    /***********************************即时通讯类  25000-25999******************************************/

    MESSAGE_TYPE_ERROR(25001,"消息类型不支持"),
    MESSAGE_EMPTY(25002,"消息不能为空"),
    EREPEATED_EXIT(25003,"重复退出或者退出异常"),

    NO_QUESTION(25004,"问题不存在"),
    NO_JURISDICTION(25005,"没有权限"),
    DUPLICATE_OPERATE(25006,"重复操作"),
    IS_BANNED_SPEAK(25007,"您已被禁言"),
    IS_BANNED_SPEAK_ALL(25008,"当前课程已经被禁言"),
    USER_INFO_ERROR(25009,"获取用户信息失败"),

    /***********************************操作错误 30000-30100******************************************/
    SAVE_ERROR(30001,"保存失败"),
    EDIT_ERROR(30002,"更新失败"),
    DELETE_ERROR(30003,"删除失败"),
    ENABLE_ERROR(30004,"启用失败"),
    DISABLE_ERROR(30005,"禁用失败"),
    EXAMINE_ERROR(30006,"审核失败"),
    RELEASE_ERROR(30007,"发布失败"),
    NAME_NULL(30008,"名称为空"),
    NAME_OVER_50(30009,"名称不能超过50字"),
    NOT_EXISTS_ERROR(30010,"不存在错误"),
    EXISTS_ERROR(30011,"存在错误"),
    IS_NULL_ERROR(30012,"不能为空错误"),
    /***********************************单位管理 30101 - 30150******************************************/
    CHILD_UNITS_EXISTS_ERROR(30101,"该单位下有子单位,不允许删除"),

    /***********************************讲师管理 30151 - 30200******************************************/

    LECTURER_ID_ERROR(30151,"讲师id错误"),
    /***********************************课程管理 30201 - 30300******************************************/
    COURSE_STATUS_ERROR(30201,"课程状态错误"),
    COURSE_MISS(30201,"课程不存在"),





    ;
    private final Integer key;
    private final String value;

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    ResultEnum(int key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static ResultEnum getAreaTypeEnum(int key){
        if(0 == key){
            return null;
        }
        for(ResultEnum temp: ResultEnum.values()){
            if(temp.getKey()==key){
                return temp;
            }
        }
        return null;
    }
    public static ResultEnum getResultEnumByValue(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(ResultEnum temp: ResultEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return null;
    }
}
