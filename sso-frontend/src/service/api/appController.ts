// @ts-ignore
/* eslint-disable */
import { request } from '@/utils/requestForManager.ts'

/** addApp POST /app/add */
export async function addAppUsingPost(body: API.AppAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseRespondLong_>('/app/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
