package com.miao.sso.auth.entity.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 授权码内容
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeContent implements Serializable {
    private static final long serialVersionUID = -1985598459045608781L;

    private String tgt;
    private String appId;
}
