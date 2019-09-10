package com.chat.vo.enums;

import org.apache.commons.lang.StringUtils;

public enum RedisEnum {
    /**--------------------登录----------------------**/
    EMAIL_REGISTER_CODE("email_register_code_","邮箱注册验证码"),
    EMAIL_REGISTER_CODE_LOCK("email_register_code_lock_","邮箱注册邮箱锁"),

    ACCESS_TOKEN_LOCK("access_token_lock_","accessToken锁"),
    REFRESH_TOKEN_LOCK("refresh_token_lock_","accessToken锁"),

    Upload_Progress("manager_progress_","文件上传进度"),
    /**--------------------单位----------------------**/
    ORGANIZATIONAL_INFO("ORGANIZATIONAL_INFO_","缓存单位信息"),

    /**--------------------讲师----------------------**/
    LECTURER_INFO("LECTURER_INFO_","缓存讲师信息"),


    /**--------------------ws----------------------**/
    WS_API_TOKEN("ws_api_token","网数api token"),
    WS_JS_ANCHOR_TOKEN("ws_js_anchor_token_","网数讲师端token"),
    WS_JS_VIEWER_TOKEN("ws_js_viewer_token_","网数观看端token"),
    COURSE_INFO("COURSE_INFO_","缓存课程信息"),

    /**--------------------消息----------------------**/
    COUNT_OF_COURSE("count_of_course_","课程的用户数"),
    BASE_SOCKET_USER_INFO("base_socket_user_info_","存储基本用户信息"),

    /**---------------------- pc移动端 缓存  -----------------------**/
    NOTICE_COURSE_LIST("notice_course_list_","预告课程列表"),
    BEFORE_LIVED_COURSE_LIST("before_lived_course_list_","轮播课程列表"),
    LIVE_COURSE_LIST_IN_CALENDAR("live_course_list_in_calendar_","日期课程列表"),
    PAYBACK_COURSE_LIST("payback_course_list_","回放课程列表"),
    LIVING_COURSE_LIST("living_course_list_","直播中课程列表"),
    LIVE_COURSE_LIST_IN_LECTURER("live_course_list_in_lecturer_","讲师课程记录列表"),
    LIVE_COURSE_LIST("live_course_list_","直播课程列表"),
    COURSE_PASSWORD("course_password_","讲师登录课程密码"),
    /**---------------------- ws  -----------------------**/
    TASK_LIST("task_list","任务列表"),
    PULL_DOCUMENT_TASK_LIST("pull_document_task_list","拉取文档任务列表"),
    ;
    private final String key;
    private final String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private RedisEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static RedisEnum getAreaTypeEnum(String key){
        if("".equals(key)){
            return null;
        }
        for(RedisEnum temp: RedisEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
    public static RedisEnum getResultEnumByValue(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }
        for(RedisEnum temp: RedisEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return null;
    }
}
