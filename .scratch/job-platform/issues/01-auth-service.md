# Auth Service - 用户认证与登录

**Status:** needs-triage  
**Type:** task  
**Blocked by:** 

实现 auth-service，支持多方式登录和 JWT 鉴权。

## 需求

- OAuth 登录：微信、支付宝、GitHub、Gitee（JustAuth）
- 手机号 + 验证码登录（MVP 用模拟验证码）
- JWT Token 签发，所有 API 通过 JWT 鉴权
- 角色选择（求职者/HR，MVP 互斥）
- 用户注销（7 天冷静期）

## 验收条件

1. OAuth 登录成功后自动创建用户并返回 JWT
2. 手机号验证码登录返回 JWT
3. 所有 API 端点需要有效 JWT
4. 角色互斥：一个账号只能一个角色
5. 注销申请后 7 天内可撤销，7 天后删除个人信息
