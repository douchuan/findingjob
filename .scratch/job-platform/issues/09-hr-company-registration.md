# 09 — HR 注册 + 企业信息管理 + 营业执照上传

**What to build:** HR 用户注册时填写企业信息（名称、行业、规模、描述、Logo），上传营业执照进入待审核状态。未审核的 HR 只能浏览公开信息。

**Blocked by:** 02 — Auth 登录

**Status:** ready-for-agent

- [ ] company-service 实现 Company 表（id, name, industry, size, description, logo_url, verification_status）和 HRProfile 表（user_id, company_id, position, phone）
- [ ] HR 用户首次登录时填写企业信息并上传营业执照图片（调用 storage-service）
- [ ] 营业执照图片存储到 `/uploads/licenses/`，限制图片类型和 5MB 大小
- [ ] Company 初始 verification_status 为 `pending`
- [ ] company-service API：HR 查看/编辑企业信息
- [ ] 前端（HR 端）：企业信息填写页面（含营业执照上传）
- [ ] 前端：未认证 HR 首页展示「等待审核中」提示
- [ ] API 权限：未认证 HR 只能访问公开信息端点（公司列表、L1 求职者信息），搜索/简历请求/评价返回 403
- [ ] 测试：HR 提交企业信息后 status 为 pending
- [ ] 测试：未认证 HR 访问搜索 API 返回 403
