# 基础设施 - Docker Compose + K8s 部署配置

**Status:** needs-triage  
**Type:** task  
**Blocked by:** 01, 02, 03, 04, 05, 06, 07

配置开发环境和生产环境的基础设施。

## 需求

- Docker Compose 开发环境（7 个服务 + PostgreSQL + Nginx）
- docker-compose.override.yml（开发覆盖配置）
- K8s 生产部署：
  - namespace.yaml
  - 每个服务的 Deployment + Service
  - PostgreSQL StatefulSet + PVC
  - Ingress（Nginx Ingress Controller）
  - ConfigMap + Secret
- Swagger API 文档（SpringDoc）
- API 限流配置（Ingress 层 + Resilience4j）

## 验收条件

1. `docker-compose up` 后所有服务正常运行
2. K8s 配置可一键部署
3. Swagger UI 可访问所有 API 文档
4. Ingress 限流生效
