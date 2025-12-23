# SSO 统一认证中心

基于 OAuth2 协议授权码模式的单点认证授权中心，包含后端认证服务、管理后台 API 以及前端管理页面。

## 📚 项目介绍

本项目是一个基于 Spring Boot 和 Vue 3 的前后端分离 SSO 系统。它实现了 OAuth2 的授权码模式，支持多应用单点登录、单点登出、自动续签等功能。

### 核心特性

- **OAuth2 认证**：基于标准 OAuth2 协议授权码模式。
- **单点登录 (SSO)**：一次登录，多处访问。
- **单点登出 (SLO)**：一处退出，全网注销。认证中心远程通知所有客户端销毁 Token。
- **自动续签**：支持 AccessToken 过期后，通过 RefreshToken 自动刷新，前端无感续签。
- **安全存储**：用户密码采用 MD5 + 盐值加密存储，防止明文密码泄露。
- **模块化设计**：后端采用 Maven 多模块架构，封装 Starter 组件，易于扩展。
- **统一规范**：全局跨域配置、统一响应封装、全局异常处理。
- **接口文档**：集成 Knife4j，自动生成在线接口文档。

## 🛠️ 技术栈

### 后端 (sso-backend)

- **核心框架**: Spring Boot 2.6.13
- **语言**: Java 11
- **数据库**: MySQL
- **缓存**: Redis
- **ORM**: MyBatis Plus
- **工具库**: Hutool, Fastjson, Lombok
- **API 文档**: Knife4j (OpenAPI 2)

### 前端 (sso-frontend)

- **核心框架**: Vue 3
- **构建工具**: Vite
- **UI 组件库**: Ant Design Vue
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP 客户端**: Axios / Umi Request
- **语言**: TypeScript

## 📂 项目结构

```
sso/
├── sso-backend/              # 后端工程
│   ├── sso-auth/             # 认证服务聚合模块
│   │   ├── sso-auth-center/  # 核心认证中心服务 (Port: 8081)
│   │   └── sso-auth-manage/  # 管理后台 API 服务
│   ├── sso-app/              # 客户端应用示例
│   │   ├── sso-app-h5/       # H5 示例应用
│   │   └── sso-app-jsp/      # JSP 示例应用
│   └── sso-stater/           # 自定义 Starter 模块
│       ├── sso-stater-auth/  # 认证服务端 Starter
│       ├── sso-stater-client/# 客户端 Starter
│       └── ...
└── sso-frontend/             # 前端工程 (Vue 3 + Vite)
```

## 🚀 快速开始

### 环境准备

- Java 11+
- Node.js 16+
- MySQL 5.7+
- Redis

### 后端启动

1. **配置数据库与 Redis**
   修改 `sso-backend/sso-auth/sso-auth-center/src/main/resources/application.yml` 中的数据库和 Redis 连接信息。

2. **编译安装依赖**
   在 `sso-backend` 目录下执行：
   ```bash
   mvn clean install
   ```

3. **启动服务**
   - 启动认证中心：运行 `sso-auth-center` 模块的主启动类。
   - (可选) 启动管理后台 API：运行 `sso-auth-manage` 模块的主启动类。

### 前端启动

1. **安装依赖**
   在 `sso-frontend` 目录下执行：
   ```bash
   npm install
   ```

2. **启动开发服务器**
   ```bash
   npm run dev
   ```

3. **访问**
   打开浏览器访问控制台输出的地址 (通常是 `http://localhost:5173`)。

## 📝 接口文档

启动后端服务后，访问以下地址查看 Knife4j 接口文档：
`http://localhost:8081/doc.html` (端口取决于配置)
