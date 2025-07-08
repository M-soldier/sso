package com.miao.sso.auth.utils;

import com.miao.sso.auth.constant.UserConstants;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5工具类
 *
 * @author MiaoShijie
 * @since 1.0.0
 */
public class MD5Util {

    private static final String salt = UserConstants.FRONT_SALT;

    // MD5加密
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    // 前端明文密码加密规则
    private static String inputPassToFormPass(String inputPass) {
        // 此处可以自定义加密规则
        String str = salt + inputPass + salt;
        return md5(str);
    }

    // 后端加密，后端加密的salt可以和前端的不一样
    public static String formPassToDBPass(String formPass, String salt) {
        // 此处可以自定义加密规则
        String str = salt + formPass + salt;
        return md5(str);
    }

    // 完整加密过程
    public static String inputPassToDBPass(String inputPass, String salt) {
        String formPass = inputPassToFormPass(inputPass);
        return formPassToDBPass(formPass, salt);
    }

    public static void main(String[] args) {
        // String timeStamp = String.valueOf(System.currentTimeMillis());
        // System.out.println("salt : " + timeStamp);
        // System.out.println("fromPassword : " + inputPassToFormPass("18801055131"));
        // salt : 1743347762242
        // fromPassword : 9939ffa3553c4f1e508be9ed1d1a13b3
        System.out.println(formPassToDBPass("9939ffa3553c4f1e508be9ed1d1a13b3", "1743347762242"));
    }
}
