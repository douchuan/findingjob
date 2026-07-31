# Storage Service - 统一文件存储

**Status:** needs-triage  
**Type:** task  
**Blocked by:** 

实现 storage-service，提供统一的文件存储接口（策略模式）。

## 需求

- 接口：`FileStorageService { upload, download, delete, getUrl }`
- 实现：LocalFileStorage（默认）、AliyunOssStorage、TencentCosStorage、S3CompatibleStorage
- 配置切换：`storage.type: local | aliyun-oss | tencent-cos | s3`
- 文件类型校验和大小限制
- 预签名 URL 生成（用于简历临时下载）

## 验收条件

1. 通过配置切换存储后端
2. 默认 Local 存储在 `/uploads`
3. 上传时校验文件类型和大小
4. 支持生成带过期时间的预签名 URL
