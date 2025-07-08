package com.miao.sso.base.utils;

import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/10
 */
@NoArgsConstructor
public class CookieUtil {

    /**
     * 获取cookie对象
     *
     * @param name    名称
     * @param request http请求
     * @return {@code Cookie }
     */
    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StrUtil.isEmpty(name)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
    /**
     * 获取cookie值
     *
     * @param name    名称
     * @param request http请求
     * @return {@code String }
     */
    public static String getCookieValue(String name, HttpServletRequest request) {
        Cookie cookie = getCookie(name, request);
        return cookie == null ? null : cookie.getValue();
    }

    /**
     * 设置cookie值
     *
     * @param name     名称
     * @param value    值
     * @param path     路径
     * @param request  http请求
     * @param response http响应
     */
    public static void setCookieValue(String name, String value, String path, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        if (path != null) {
            cookie.setPath(path);
        }
        if("https".equals(request.getScheme())) {
            cookie.setSecure(true);
        }

        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * 更新cookie
     *
     * @param name    名称
     * @param value   值
     * @param request http请求
     */
    public static void updateCookie(String name, String value, HttpServletRequest request) {
        Cookie cookie = getCookie(name, request);
        if (cookie != null) {
            cookie.setValue(value);
        }
    }

    /**
     * 删除cookie值
     *
     * @param name     名称
     * @param path     路径
     * @param response http响应
     */
    public static void removeCookieValue(String name, String path, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, null);
        if (path != null) {
            cookie.setPath(path);
        }

        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
