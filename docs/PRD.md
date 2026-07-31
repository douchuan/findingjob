# 求职网站 — 产品需求文档 (PRD)

> 版本：v1.0-MVP  
> 日期：2026-07-31  
> 状态：架构确认，待开发

---

## 一、产品概述

构建一个**以技能为核心的求职平台**，区别于传统求职网站。核心价值：

1. **突出求职者技能**：关联 GitHub/Gitee，展示项目与星标
2. **双向评价体系**：求职者评价 HR/公司，HR 评价求职者，帮助双方判断可靠性
3. **简历保护性开放**：简历默认不公开，需授权后可查看

---

## 二、用户角色

| 角色 | 说明 |
|------|------|
| **求职者** | 提交个人信息、工作经历、证书，关联代码托管平台，获取 HR 评价 |
| **HR/猎头** | 注册企业信息，搜索求职者，发起简历查看请求，对求职者评分 |
| **管理员** | 平台管理、用户管理、内容审核（MVP 阶段基础功能） |

---

## 三、功能需求

### 3.1 用户认证

| 功能 | 说明 | MVP 状态 |
|------|------|---------|
| 手机号 + 验证码登录 | 预留短信接口，MVP 阶段用模拟验证码 | ✅ 接口预留 |
| 微信 OAuth 登录 | JustAuth 接入微信开放平台 | ✅ |
| 支付宝 OAuth 登录 | JustAuth 接入支付宝 | ✅ |
| GitHub OAuth 登录 | JustAuth 接入，同时用于拉取项目数据 | ✅ |
| Gitee OAuth 登录 | JustAuth 接入，同时用于拉取项目数据 | ✅ |
| JWT Token 认证 | 登录后颁发 JWT，所有 API 通过 JWT 鉴权 | ✅ |

### 3.2 求职者功能

| 功能 | 说明 |
|------|------|
| 个人信息管理 | 姓名、头像、联系方式、个人简介、期望职位 |
| 工作经历管理 | 公司、职位、时间段、工作描述（支持多条） |
| 证书管理 | 证书名称、颁发机构、获得时间、证书图片上传 |
| GitHub/Gitee 关联 | OAuth 授权后拉取用户主要项目及星标数 |
| 项目星标展示 | 在个人主页展示高星项目（Top N） |
| 简历上传 | 上传 PDF/Word 格式简历，默认不公开 |
| 简历授权管理 | 查看/审批 HR 的简历查看请求，授权后 7 天有效 |
| 评价 HR/公司 | upvote / downvote + 可选文字评论 |
| 查看 HR/公司评价 | 浏览其他求职者对 HR/公司的评价和评分 |
| 通知中心 | 收到简历查看请求、新评价等站内通知 |

### 3.3 HR/猎头功能

| 功能 | 说明 |
|------|------|
| 企业信息管理 | 公司名称、行业、规模、公司描述、Logo |
| HR 个人信息 | 姓名、职位、联系方式 |
| 搜索求职者 | 按技能、经验、项目等条件搜索 |
| 查看求职者公开信息 | 个人信息、工作经历、证书、GitHub 项目 |
| 简历查看申请 | 向求职者发起简历查看请求 |
| 评价求职者 | 1-5 星评分 + 可选文字评价（其他 HR 可见） |
| 查看公司评价 | 浏览求职者对本公司的 upvote/downvote 及评论 |

### 3.4 评分系统

**求职者 → HR/公司：**
- upvote（👍）/ downvote（👎），每个用户对同一对象只能投一次
- 可选文字评论
- 注册满 7 天方可投票（防刷）

**HR → 求职者：**
- 1-5 星评分
- 可选文字评价
- 评价对其他 HR 可见

**防滥用机制：**
- 一人对同一对象只能投票一次
- 注册满 7 天才能投票
- 后端 API 频率限制

### 3.5 简历授权流程

```
HR 发起查看请求 → 求职者收到站内通知
→ 求职者选择 授权 / 拒绝
→ 授权后生成 7 天有效期的下载链接
→ 过期后链接失效，需重新申请
```

---

## 四、技术架构

### 4.1 整体架构

```
┌─────────────────────────────────────────────────┐
│     React 前端 (Vite + Ant Design + PWA)         │
│     响应式 Web + 移动端适配                       │
└──────────────────────┬──────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────┐
│         K8s Ingress (Nginx Ingress)              │
│         路由 / TLS / 限流                         │
└──┬────┬────┬────┬────┬────┬──┬──────────────────┘
   │    │    │    │    │    │  │
   ▼    ▼    ▼    ▼    ▼    ▼  ▼
 ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐
 │AU│ │PR│ │CO│ │RA│ │RE│ │ST│ │NO│
 │TH│ │OF│ │MP│ │TIN│ │SUM│ │OR│ │TIF│
 └┬─┘ └┬─┘ └┬─┘ └┬─┘ └┬─┘ └──┘ └┬─┘
  │    │    │    │    │          │
  ▼    ▼    ▼    ▼    ▼          ▼
┌──────────────────────────────────────┐
│     PostgreSQL (单实例, 多 Schema)     │
└──────────────────────────────────────┘
```

### 4.2 技术栈

#### 前端

| 组件 | 选型 |
|------|------|
| 框架 | React 18 + TypeScript |
| 构建工具 | Vite |
| UI 库 | Ant Design（响应式） |
| 服务端状态管理 | React Query |
| 客户端状态管理 | Zustand |
| PWA | vite-plugin-pwa |
| HTTP 客户端 | Axios |
| 路由 | React Router v6 |

#### 后端

| 组件 | 选型 |
|------|------|
| 框架 | Spring Boot 3.x (Java 17) |
| 语言 | Java 17 |
| 架构风格 | 纯 Spring Boot 微服务（无 Spring Cloud） |
| 认证 | JustAuth + Spring Security + JWT |
| ORM | Spring Data JPA (Hibernate) |
| 服务间通信 | WebClient |
| 熔断限流 | Resilience4j |
| API 文档 | SpringDoc (Swagger UI) |

#### 数据库 & 存储

| 组件 | 选型 |
|------|------|
| 数据库 | PostgreSQL 15+ |
| 数据库策略 | 单实例，每个服务独立 Schema |
| 文件存储 | 策略模式：Local（默认）/ Aliyun OSS / Tencent COS / S3 |

#### 部署

| 环境 | 工具 |
|------|------|
| 开发/调试 | Docker Compose |
| 生产 | Kubernetes (K8s) |
| 服务发现 | K8s Service DNS（开发：Docker DNS） |
| 配置管理 | K8s ConfigMap + Secret（开发：.env） |
| API 网关 | K8s Ingress (Nginx Ingress Controller) |

### 4.3 微服务拆分

| 服务 | 职责 | 端口（开发） |
|------|------|:---:|
| **auth-service** | 用户注册/登录、JWT 签发、第三方 OAuth、短信验证码 | 8001 |
| **profile-service** | 求职者档案、工作经历、证书、GitHub/Gitee 项目数据 | 8002 |
| **company-service** | 公司信息、HR/猎头信息 | 8003 |
| **rating-service** | 双向评分（upvote/downvote、星级评分）、评论 | 8004 |
| **resume-service** | 简历上传、授权审批流、下载链接管理 | 8005 |
| **storage-service** | 统一文件存储（策略模式） | 8006 |
| **notification-service** | 站内通知、消息推送（预留邮件/短信） | 8007 |

### 4.4 前端多端策略

| 端 | 方案 | 状态 |
|---|------|------|
| Web 浏览器 | React + Ant Design（响应式） | MVP 即做 |
| 移动端浏览器 | 响应式布局 + PWA | MVP 即做 |
| 原生 App（iOS/Android） | React Native，复用同一套后端 API | 预留，后续迭代 |

### 4.5 文件存储设计（策略模式）

```java
public interface FileStorageService {
    String upload(MultipartFile file, String folder);
    InputStream download(String fileKey);
    void delete(String fileKey);
    String getUrl(String fileKey);
}

// 实现：
// - LocalFileStorage       → 本地 /uploads（默认）
// - AliyunOssStorage       → 阿里云 OSS
// - TencentCosStorage      → 腾讯云 COS
// - S3CompatibleStorage    → AWS S3 / MinIO
```

配置切换（`application.yml`）：
```yaml
storage:
  type: local  # local | aliyun-oss | tencent-cos | s3
```

---

## 五、项目结构

```
findingjob/
├── docs/
│   └── PRD.md                    # 本文档
├── frontend/                     # React 前端
│   ├── src/
│   │   ├── api/                  # API 请求封装
│   │   ├── components/           # 通用组件
│   │   ├── pages/                # 页面
│   │   ├── hooks/                # 自定义 Hooks
│   │   ├── stores/               # Zustand stores
│   │   ├── types/                # TypeScript 类型
│   │   └── utils/                # 工具函数
│   ├── public/
│   ├── Dockerfile
│   └── package.json
├── backend/
│   ├── common/                   # 公共模块（工具类、通用 DTO）
│   ├── auth-service/             # 认证服务
│   ├── profile-service/          # 求职者档案服务
│   ├── company-service/          # 公司/HR 服务
│   ├── rating-service/           # 评分服务
│   ├── resume-service/           # 简历服务
│   ├── storage-service/          # 文件存储服务
│   ├── notification-service/     # 通知服务
│   └── gateway/                  # 网关配置（K8s Ingress 配置）
├── docker-compose.yml            # 开发环境编排
├── docker-compose.override.yml   # 开发环境覆盖
└── k8s/
    ├── namespace.yaml
    ├── postgresql/
    ├── auth-service/
    ├── profile-service/
    ├── company-service/
    ├── rating-service/
    ├── resume-service/
    ├── storage-service/
    ├── notification-service/
    └── ingress.yaml
```

---

## 六、MVP 范围与后续迭代

### MVP 包含（v1.0）

- [x] 多方式登录（微信/支付宝/GitHub/Gitee + 短信接口预留）
- [x] 求职者个人信息/工作经历/证书管理
- [x] GitHub/Gitee 关联，项目星标展示
- [x] HR/猎头注册与企业信息管理
- [x] 求职者搜索
- [x] 简历上传（不公开）+ 授权查看流程
- [x] 求职者对 HR/公司的 upvote/downvote
- [x] HR 对求职者的 1-5 星评分
- [x] 站内通知
- [x] 响应式 Web + PWA
- [x] Docker Compose 开发环境
- [x] K8s 部署配置
- [x] Swagger API 文档
- [x] 文件存储策略模式

### 后续迭代（v2.0+）

- [ ] 实时消息系统（WebSocket）
- [ ] 短信服务正式接入（阿里云/腾讯云）
- [ ] 邮件通知
- [ ] 高级搜索与筛选（多条件组合）
- [ ] 数据看板/统计分析
- [ ] React Native 移动端 App
- [ ] 国际化（i18n，中英双语）
- [ ] 付费功能（会员、置顶等）
- [ ] 内容审核与举报
- [ ] Seata 分布式事务
- [ ] 全链路监控（Zipkin）

---

## 七、安全设计

| 安全措施 | 说明 |
|---------|------|
| JWT 鉴权 | 所有 API 通过 JWT Token 鉴权 |
| 密码不落地 | OAuth 登录，无密码存储 |
| 简历访问控制 | 仅授权用户可在有效期内下载 |
| API 限流 | Ingress 层 + Resilience4j 限流 |
| 文件上传校验 | 限制文件类型（PDF/Word/图片）、大小 |
| CORS 配置 | 仅允许前端域名访问 |
| SQL 注入防护 | JPA 参数化查询 |
| XSS 防护 | 前端输出编码 |

---

## 八、开发规范

| 规范 | 说明 |
|------|------|
| Java 版本 | Java 17 LTS |
| Spring Boot | 3.x（最新稳定版） |
| 代码风格 | Google Java Style + Checkstyle |
| API 设计 | RESTful，统一响应格式 `{ code, message, data }` |
| 错误码 | 全局错误码枚举 |
| 日志 | SLF4J + Logback，JSON 格式（生产） |
| Git 提交 | Conventional Commits |
