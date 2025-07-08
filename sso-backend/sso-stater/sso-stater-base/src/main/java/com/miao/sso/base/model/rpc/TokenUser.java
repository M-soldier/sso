package com.miao.sso.base.model.rpc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 脱敏后的用户登录信息
 *
 * @author 睡不醒的喵桑
 * @version 1.0.0
 * @date 2025/04/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenUser implements Serializable {
    private static final long serialVersionUID = -4434667570997668628L;
    /**
     * id
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/vip
     */
    private String userRole;

    /**
     * 会员过期时间
     */
    private Date vipExpireTime;

    // /**
    //  * 会员编号
    //  */
    // private Long vipNumber;
    //
    // /**
    //  * 分享码
    //  */
    // private String shareCode;
    //
    // /**
    //  * 创建时间
    //  */
    // private Date createTime;
    //
    // /**
    //  * 更新时间
    //  */
    // private Date updateTime;
    //
    // /**
    //  * 最后登录时间
    //  */
    // private Date lastLoginTime;
    //
    // /**
    //  * 登录总次数
    //  */
    // private Integer loginCount;
}
