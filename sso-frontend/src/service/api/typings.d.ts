declare namespace API {
  type AppAddRequest = {
    appName?: string
    code?: string
  }

  type BaseRespondLong_ = {
    code?: number
    data?: number
    message?: string
    path?: string
    status?: number
    timestamp?: string
  }

  type BaseRespondPageTokenUser_ = {
    code?: number
    data?: PageTokenUser_
    message?: string
    path?: string
    status?: number
    timestamp?: string
  }

  type BaseRespondSysUser_ = {
    code?: number
    data?: SysUser
    message?: string
    path?: string
    status?: number
    timestamp?: string
  }

  type BaseRespondTokenUser_ = {
    code?: number
    data?: TokenUser
    message?: string
    path?: string
    status?: number
    timestamp?: string
  }

  type BaseRespondVoid_ = {
    code?: number
    message?: string
    path?: string
    status?: number
    timestamp?: string
  }

  type deleteUserUsingPOSTParams = {
    /** userId */
    userId: number
  }

  type getAuthStatusUsingGETParams = {
    /** redirectUri */
    redirectUri?: string
  }

  type getTokenUserUsingGETParams = {
    /** userId */
    userId: number
  }

  type getUserUsingGETParams = {
    /** userId */
    userId: number
  }

  type listTokenUserUsingPOSTParams = {
    currentPage?: number
    id?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    userAccount?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type PageTokenUser_ = {
    current?: number
    pages?: number
    records?: TokenUser[]
    size?: number
    total?: number
  }

  type SysUser = {
    createTime?: string
    editTime?: string
    id?: number
    inviteUser?: number
    isDelete?: number
    lastLoginTime?: string
    loginCount?: number
    shareCode?: string
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: string
    vipCode?: string
    vipExpireTime?: string
    vipNumber?: number
  }

  type TokenUser = {
    id?: number
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
    vipExpireTime?: string
  }

  type UserAddRequest = {
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserUpdateRequest = {
    id?: number
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }
}
