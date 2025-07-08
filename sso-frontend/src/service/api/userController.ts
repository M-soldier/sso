// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/requestForManager.ts'

/** addUser POST /user/add */
export async function addUserUsingPost(body: API.UserAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseRespondLong_>('/user/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteUser POST /user/delete */
export async function deleteUserUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.deleteUserUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseRespondVoid_>('/user/delete', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** getUser GET /user/get */
export async function getUserUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseRespondSysUser_>('/user/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** getTokenUser GET /user/get/vo */
export async function getTokenUserUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTokenUserUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseRespondTokenUser_>('/user/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listTokenUser POST /user/list/page/vo */
export async function listTokenUserUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listTokenUserUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseRespondPageTokenUser_>('/user/list/page/vo', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** updateUser POST /user/update */
export async function updateUserUsingPost(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseRespondVoid_>('/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
