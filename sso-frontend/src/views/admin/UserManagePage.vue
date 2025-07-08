<script setup lang="ts">
import { computed, h, onMounted, reactive, ref } from 'vue'
import { deleteUserUsingPost, listTokenUserUsingPost } from '@/service/api/userController.ts'
import { message, Tooltip } from 'ant-design-vue'
import dayjs from 'dayjs'

const doDelete = async (id : number) => {
  if (id === null){
    return
  }

  const param = ref<API.deleteUserUsingPOSTParams>({
    userId: id
  })
  const res = await deleteUserUsingPost(param.value)
  if (res.data.code === 20000){
    message.success("删除成功")
    // 刷新数据
    await fetchData()
  }else {
    message.error("删除失败")
  }
}

const doPageChange = (page, pageSize) => {
  searchParams.currentPage = page
  searchParams.pageSize = pageSize
  fetchData()
}

const doPageSizeChange = (current, size) => {
  searchParams.currentPage = 1
  searchParams.pageSize = size
  fetchData()
}

// 获取数据
const doSearch = () => {
  // 重置页码
  searchParams.currentPage = 1
  fetchData()
}


// 数据
const dataList = ref<API.TokenUser[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.listTokenUserUsingPOSTParams>({
  currentPage: 1,
  pageSize: 10,
  sortField: 'userRole',
  sortOrder: 'ascend',
  userAccount: '',
  userName: '',
})

// 获取数据
const fetchData = async () => {
  const res = await listTokenUserUsingPost({
    ...searchParams
  })
  if (res.data.data && res.data.code === 20000) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})


const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    width: '180px',
    align: 'center',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    width: '120px',
    align: 'center',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    width: '150px',
    align: 'center',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    width: '100px',
    align: 'center',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    ellipsis: true, // ✅ 省略显示
    width: '300px',
    align: 'center',
    customRender: ({ text }) => {
      return h(
        Tooltip,
        { title: text },
        () =>
          h(
            'div',
            {
              style: {
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                display: '-webkit-box',
                WebkitLineClamp: 1,
                WebkitBoxOrient: 'vertical',
              },
            },
            text
          )
      )
    },
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    width: '100px',
    align: 'center',
  },
  {
    title: '会员到期时间',
    dataIndex: 'vipExpireTime',
    width: '180px',
    align: 'center',
  },
  {
    title: '操作',
    key: 'action',
    align: 'center',
  },
]

</script>

<template>
  <div id="user-manage-page">
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <div style="margin-bottom: 16px" />
    <div class="table-container" style="height: calc(100vh - 230px); overflow-y: auto">
      <a-table :columns="columns" :data-source="dataList" :pagination="false" @change="doPageChange" :scroll="{ y: 570 }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'userAvatar'">
            <a-avatar shape="square" :size="32" :src="record.userAvatar" />
          </template>
          <template v-else-if="column.dataIndex === 'userRole'">
            <div v-if="record.userRole === 'admin'">
              <a-tag color="green">管理员</a-tag>
            </div>
            <div v-else-if="record.userRole === 'user'">
              <a-tag color="blue">普通用户</a-tag>
            </div>
            <div v-else-if="record.userRole === 'vip'">
              <a-tag color="orange">会员用户</a-tag>
            </div>
          </template>
          <template v-else-if="column.dataIndex === 'vipExpireTime'">
            <div v-if="record.vipExpireTime">
              {{ dayjs(record.vipExpireTime).format('YYYY-MM-DD HH:mm:ss') }}
            </div>
            <div v-else></div>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button danger @click="doDelete(record.id)">删除</a-button>
          </template>
        </template>
      </a-table>
    </div>
    <!-- 分页栏固定 -->
    <div style="padding-right: 20px; padding-top: 10px; text-align: right">
      <a-pagination
        :current="searchParams.currentPage"
        :page-size="searchParams.pageSize"
        :total="Number(total)"
        show-size-changer
        :show-total="total => `共 ${total} 条`"
        @change="doPageChange"
        @showSizeChange="doPageSizeChange"
      />
    </div>
  </div>
</template>

<style scoped>
#user-manage-page {
  padding: 20px;
}

</style>
