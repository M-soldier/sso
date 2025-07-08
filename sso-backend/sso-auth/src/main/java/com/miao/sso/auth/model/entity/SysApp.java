package com.miao.sso.auth.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 应用表
 * @TableName sys_app
 */
@TableName(value ="sys_app")
@Data
public class SysApp {
    /**
     * 应用ID(指定主键策略，防止简单递增被爬虫，ASSIGN_ID采用雪花算法)
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String appName;

    /**
     * 是否启用
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 客户端ID
     */
    private String appId;

    /**
     * 客户端密钥
     */
    private String appSecret;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 编辑时间
     */
    private Date editTime;
}