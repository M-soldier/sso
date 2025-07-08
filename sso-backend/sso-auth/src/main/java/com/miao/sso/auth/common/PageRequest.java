package com.miao.sso.auth.common;

import lombok.Data;

/**
 * 分页请求
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/05/06
 */
@Data
public class PageRequest {
    /**
     * 当前页号
     */
    private int currentPage = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}
