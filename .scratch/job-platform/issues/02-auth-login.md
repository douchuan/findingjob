# 02 — GitHub OAuth 登录 → 角色选择 → JWT 鉴权

**What to build:** 用户可以通过 GitHub OAuth 登录，首次登录时选择角色（求职者/HR），系统颁发 JWT。后续所有 API 通过 JWT 鉴权，角色互斥（求职者不能访问 HR 端点，反之亦然）。

**Blocked by:** 01 — 项目脚手架

**Status:** ready-for-agent

- [ ] auth-service 集成 JustAuth，实现 GitHub OAuth 登录流程（MVP 仅 GitHub，微信/支付宝/Gitee/手机号预留接口）
- [ ] OAuth 回调后检查用户是否存在：不存在则创建 User 记录（手机号预留空）
- [ ] 新用户首次登录需选择角色（jobseeker / hr / admin），选择后绑定到 User 记录，不可更改（MVP 角色互斥）
- [ ] 登录成功后颁发 JWT Token，包含 user_id、role、过期时间
- [ ] 所有 API 端点通过 Spring Security + JWT 鉴权，无 Token 返回 401，角色不匹配返回 403
- [ ] 前端：登录页展示 GitHub OAuth 按钮，点击后跳转授权，回调后存储 JWT
- [ ] 前端：首次登录展示角色选择界面，选择后跳转到对应首页
- [ ] 前端：Axios 拦截器自动附加 JWT，401 时跳转登录页
- [ ] 测试：Mock GitHub OAuth，验证登录成功后返回 JWT
- [ ] 测试：验证求职者 Token 访问 HR 端点返回 403
