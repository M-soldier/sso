package com.miao.sso.auth.authManager.model.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppAddRequest implements Serializable {
    private static final long serialVersionUID = 8766248521399203234L;


    /**
     * 应用程序编码
     */
    private String code;

    /**
     * 应用程序名称
     */
    private String appName;
}
