package com.miao.sso.auth.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * UUID工具类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/01
 */
public class UUIDUtil {
   /**
    * uuid
    *
    * @return {@code String }
    */
   public static String getUUID() {
      return UUID.randomUUID().toString().replace("-", "");
   }

   public static String getUUIDFast() {
      long mostSigBits = ThreadLocalRandom.current().nextLong();
      long leastSigBits = ThreadLocalRandom.current().nextLong();
      return new UUID(mostSigBits, leastSigBits).toString().replace("-", "");
   }

}