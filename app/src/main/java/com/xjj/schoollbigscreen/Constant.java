package com.xjj.schoollbigscreen;

import okhttp3.MediaType;

/**
 * FileName: Constant
 * Author: Target
 * Date: 2020/10/21 5:46 PM
 */
public class Constant {

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");


    /**
     * 系统模块名称－用户中心
     */
    public static String MODULE_NAME_UC = "uc";

    /**
     * 系统模块名称－文件中心
     */
    public static String MODULE_NAME_FILE = "file";

    /**
     * 系统模块名称－接送助手
     */
    public static String MODULE_NAME_KIDSCARD = "kidscare";

    /**
     * 系统模块名称－健康
     */
    public static String MODULE_NAME_HEALTH = "health";


    public static String URL_VERSION_CHECK = MODULE_NAME_KIDSCARD + "/face/version/check";


    /**
     * 大屏验证、绑定地址
     */
    public static String CHECKSCREEN = "autoScreen/checkScreen";
    /**
     * 解绑大屏接口
     */
    public static String UNBUNDLYSCREEN = "autoScreen/unbundlyScreen";
}
