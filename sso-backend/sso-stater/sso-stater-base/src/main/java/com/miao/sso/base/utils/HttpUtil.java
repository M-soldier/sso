package com.miao.sso.base.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP工具类
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/03
 */
@Slf4j
public class HttpUtil {
    /**
     * get方法
     *
     * @param url 请求地址
     * @return {@code String }
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * get方法
     *
     * @param url       请求地址
     * @param paramMap 参数表
     * @return {@code String }
     */
    public static String get(String url, Map<String, String> paramMap) {
        String result = "";
        String realUrl = url;
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            if (paramMap != null && !paramMap.isEmpty()) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                paramMap.forEach((key, value) -> {
                    params.add(new BasicNameValuePair(key, value));
                });

                String paramStr = URLEncodedUtils.format(params, "utf-8");
                realUrl = realUrl + "?" + paramStr;
            }

            log.info("get url:{}", realUrl);
            HttpGet httpGet = new HttpGet(realUrl);
            response = httpclient.execute(httpGet);

            if(response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "utf-8");
                }
                log.info("method : get, url : {}, result : {}", url, result);
            }
        }catch (IOException e){
            log.error("url : {}, paramMap : {}", url, paramMap, e);
        }finally {
            try {
                httpclient.close();
                if (response != null) {
                    response.close();
                }
            }catch (IOException e){
                log.error("error : ", e);
            }
        }
        return result;
    }

    /**
     * post方法
     *
     * @param url      请求地址
     * @param paramMap 参数表
     * @return {@code String }
     */
    public static String post(String url, Map<String, String> paramMap) {
        return post(url, paramMap, null);
    }

    /**
     * post请求头
     *
     * @param url       请求地址
     * @param headerMap 请求头表
     */
    public static void postHeader(String url, Map<String, String> headerMap) {
        post(url, null, headerMap);
    }

    /**
     * post方法
     *
     * @param url       请求地址
     * @param paramMap  参数表
     * @param headerMap 请求头表
     * @return {@code String }
     */
    public static String post(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        log.info("post url:{}, headerMap:{}", url, headerMap);
        String result = "";

        CloseableHttpResponse response = null;
        // disableAutomaticRetries() 禁用自动重试，适合事务性请求（如支付、订单），避免重复提交
        CloseableHttpClient httpclient = HttpClients.custom().disableAutomaticRetries().build();

        try {
            HttpPost httpPost = new HttpPost(url);

            if (paramMap != null && !paramMap.isEmpty()) {
                List<NameValuePair> params = new ArrayList<>();
                paramMap.forEach((key, value) -> {
                    params.add(new BasicNameValuePair(key, value));
                });
                // 设置请求体
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF_8"));
            }

            // 设置请求参数
            // setSocketTimeout(5000)	数据传输超时，即服务器已经建立连接，但读取数据时间过长（单位：毫秒）。
            // setConnectTimeout(5000)	连接超时，即建立 TCP 连接的最长时间（单位：毫秒）。
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            httpPost.setConfig(requestConfig);

            response = httpclient.execute(httpPost);

            // 设置http请求头，Cookie、身份认证等信息都在请求头中
            if(headerMap != null && !headerMap.isEmpty()) {
                headerMap.forEach(httpPost::setHeader);
            }

            response = httpclient.execute(httpPost);
            if(response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, "utf-8");
                }
            }
            log.info("method : post, url : {}, result : {}", url, result);
        }catch (IOException e){
            log.error("url : {}, paramMap : {}", url, paramMap, e);
        }finally {
            try {
                httpclient.close();
                if (response != null) {
                    response.close();
                }
            }catch (IOException e){
                log.error("error : ", e);
            }
        }
        return result;
    }
}
