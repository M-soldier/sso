package com.miao.sso.client;

import com.miao.sso.client.common.ClientContextHolder;
import com.miao.sso.client.constant.ClientConstant;
import com.miao.sso.client.filter.AbstractClientFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
public class ClientFilterContainer implements Filter {
    protected String[] excludeUrl;
    protected AbstractClientFilter[] filters;

    /**
     * 初始化
     *
     * @param filterConfig 过滤器配置
     * @throws ServletException servlet异常
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if(filters == null || filters.length == 0){
            throw new IllegalArgumentException("filters不能为空");
        }
    }

    /**
     * 过滤器执行内容
     *
     * @param request  http请求
     * @param response http响应
     * @param chain    链路
     * @throws IOException      IOException
     * @throws ServletException servlet异常
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ClientContextHolder.init(httpRequest, httpResponse);

        try {
            // 放行 OPTIONS 请求
            if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            // 判断是不是排除的URL，如果是的话，那么不需要拦截，直接返回就行
            if(isExcludedUrl(httpRequest.getServletPath())){
                chain.doFilter(request, response);
                return;
            }

            for(AbstractClientFilter filter : filters){
                // 判断是否允许访问，如果不允许访问，直接返回
                if (!filter.isAccessAllowed()) {
                    return;
                }
            }
            chain.doFilter(request, response);
        }finally {
            ClientContextHolder.reset();
        }
    }


    /**
     * 判断是否为被排除在外的url
     *
     * @param url 网址
     * @return boolean
     */
    private boolean isExcludedUrl(String url) {
        if(excludeUrl==null || excludeUrl.length==0){
            return false;
        }
        // true表示模糊匹配，false表示精确匹配
        Map<Boolean, List<String>> map = Arrays.stream(excludeUrl)
                .collect(Collectors.partitioningBy(s -> s.endsWith(ClientConstant.URL_FUZZY_MATCH)));

        // 优先精确匹配
        List<String> urlList = map.get(false);
        if(urlList.contains(url)){
            return true;
        }

        // 接着处理近似匹配
        urlList = map.get(true);
        for (String matchUrl : urlList) {
            if(url.startsWith(matchUrl.replace(ClientConstant.URL_FUZZY_MATCH, ""))){
                return true;
            }
        }

        return false;
    }
}
