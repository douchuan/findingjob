# 03 — 求职者个人主页 + 个人信息编辑

**What to build:** 求职者登录后看到自己的个人主页，可以编辑姓名、头像、个人简介和期望职位。信息保存后在个人主页和公开信息页展示。

**Blocked by:** 02 — Auth 登录

**Status:** ready-for-agent

- [ ] profile-service 实现 JobseekerProfile 的 CRUD API（姓名、头像 URL、个人简介、期望职位）
- [ ] 首次登录的求职者自动创建空的 JobseekerProfile
- [ ] 前端：求职者个人主页展示当前信息（卡片式布局）
- [ ] 前端：个人信息编辑表单，支持修改并保存
- [ ] 前端：头像上传（调用 storage-service 上传接口，暂用 Local 存储）
- [ ] API 权限：仅求职者本人可编辑自己的档案，任何人可查看（L1 公开信息，后续 ticket 细化权限）
- [ ] 测试：求职者可 CRUD 自己的个人信息
- [ ] 测试：HR 可访问求职者的公开信息端点
