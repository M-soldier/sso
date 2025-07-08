package com.miao.sso.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 序列化和反序列化工具类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/20
 */
@Slf4j
public class JsonUtil {
    @Getter
    public final static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 序列化
     *
     * @param obj 对象
     * @return {@code String }
     * @throws JsonProcessingException json处理异常
     */
    public static String toString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        }catch (JsonProcessingException e) {
            log.error("serializeToString has error:{}, object:{}", e.getMessage(), obj);
        }
        return null;
    }

    /**
     * 反序列化解析对象
     *
     * @param jsonString json字符串
     * @param clazz      类名
     * @return {@code T }
     * @throws JsonProcessingException json处理异常
     */
    public static  <T> T parseObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        }catch (JsonProcessingException e) {
            log.error("deserialize has error:{}, jsonString:{}, class:{}", e.getMessage(), jsonString, clazz);
        }
        return null;
    }

    /**
     * 反序列化解析对象
     *
     * @param jsonString    json字符串
     * @param typeReference 类型
     * @return {@code T }
     * @throws JsonProcessingException json处理异常
     */
    public static  <T> T parseObject(String jsonString, TypeReference<T> typeReference) throws JsonProcessingException {
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readValue(jsonString, typeReference);
        }catch (JsonProcessingException e) {
            log.error("deserialize has error:{}, jsonString:{}, typeReference:{}", e.getMessage(), jsonString, typeReference);
        }
        return null;
    }
}
