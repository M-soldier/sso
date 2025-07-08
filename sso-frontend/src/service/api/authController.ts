// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/requestForManager.ts'

/** getAuthStatus GET /get/loginStatus */
export async function getAuthStatusUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getAuthStatusUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseRespondTokenUser_>('/get/loginStatus', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}
