package com.miao.sso.base.constants;

/**
 * 单点登录基本常量
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/20
 */
public class BaseConstant {
    /**
     * 登录页面登录路径
     */
    public static final String H5_LOGIN_PATH = "/login";
    /**
     * 服务端登录路径
     */
    public static final String LOGIN_PATH = "/authServer/login";

    /**
     * 服务端退出路径
     */
    public static final String LOGOUT_PATH = "/authServer/logout";

    public static final String CLIENT_LOGOUT_PATH = "/logout";

    /**
     * 认证路径
     */
    public static final String AUTH_PATH = "/authServer/oauth2";

    /**
     * 注册路径
     */
    public static final String REGISTER_PATH = "/authServer/register";

    /**
     * 服务端回调客户端地址参数名称
     */
    public static final String REDIRECT_URI = "redirectUri";

    /**
     * 服务端回调客户端服务器地址参数名称（用于前后端分离）
     */
    public static final String APP_SERVER_URI = "appServerUri";

    /**
     * 服务端注销回调客户端注销地址参数名称
     */
    public static final String LOGOUT_URI = "logoutUri";

    /**
     * 服务端单点退出回调客户端退出参数名称
     */
    public static final String LOGOUT_PARAMETER_NAME = "logoutRequest";

    /**
     * 授权方式
     */
    public static final String GRANT_TYPE = "grantType";

    /**
     * 客户端ID
     */
    public static final String APP_ID = "appId";

    /**
     * 客户端密钥
     */
    public static final String APP_SECRET = "appSecret";

    /**
     * 访问token
     */
    public static final String ACCESS_TOKEN = "accessToken";

    /**
     * 刷新token
     */
    public static final String REFRESH_TOKEN = "refreshToken";

    /**
     * 授权码（授权码模式）
     */
    public static final String AUTH_CODE = "code";

    /**
     * 获取accessToken地址
     */
    public static final String ACCESS_TOKEN_PATH = BaseConstant.AUTH_PATH + "/access-token";

    /**
     * 刷新accessToken地址
     */
    public static final String REFRESH_TOKEN_PATH = BaseConstant.AUTH_PATH + "/refresh-token";
}
