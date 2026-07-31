# 01 — 项目脚手架：Spring Boot 模块骨架 + React 应用 + Docker Compose

**What to build:** 搭建项目的整体结构，所有 Spring Boot 微服务模块编译通过，React 前端可启动，Docker Compose 一键拉起 PostgreSQL 和所有服务（空壳），Swagger UI 可访问。这是后续所有功能的基础设施。

**Blocked by:** None — can start immediately.

**Status:** ready-for-agent

- [ ] 创建 7 个 Spring Boot 微服务模块（auth/profile/company/rating/resume/storage/notification）+ common 模块，均基于 Spring Boot 3.x + Java 17
- [ ] 每个服务有独立的 `application.yml`、Spring Data JPA 配置、PostgreSQL Schema 配置
- [ ] common 模块包含：统一响应格式 `{ code, message, data }`、全局错误码枚举、JWT 工具类、异常处理
- [ ] React 前端项目（Vite + TypeScript + Ant Design + React Router），首页可访问
- [ ] Axios 拦截器配置 JWT 透传
- [ ] Docker Compose：PostgreSQL + 7 个服务（空壳）+ Nginx，`docker-compose up` 后所有服务启动
- [ ] 每个服务暴露 `/actuator/health` 端点，健康检查通过
- [ ] SpringDoc (Swagger UI) 在每个服务可用
- [ ] 项目根目录 `.gitignore` 正确，编译无 warning
