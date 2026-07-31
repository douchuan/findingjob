# 10 — 管理员审核企业营业执照

**What to build:** 管理员在后台看到待审核的营业执照列表，可以审核通过或拒绝。通过后 HR 获得完整权限，拒绝后 HR 收到通知。

**Blocked by:** 09 — HR 注册 + 企业信息

**Status:** ready-for-agent

- [ ] company-service API：管理员查询待审核/已通过/已拒绝的企业列表
- [ ] company-service API：管理员审核通过/拒绝企业，更新 verification_status
- [ ] company-service API：管理员可查看营业执照图片
- [ ] 前端（管理员后台 `/admin/*`）：企业认证审核页面（Ant Design Pro 表格 + 详情弹窗）
- [ ] 前端：审核操作（通过/拒绝按钮），拒绝时填写理由
- [ ] 前端：企业列表展示审核状态和操作历史
- [ ] 通过后 HR 的 verification_status 变为 `approved`，获得搜索/简历请求/评价权限
- [ ] 拒绝后 HR 收到站内通知（MVP 通知在 ticket 14 实现，此处先写数据库记录）
- [ ] 测试：管理员审核通过后 HR 可访问搜索 API
- [ ] 测试：拒绝后 HR 仍为未认证状态
