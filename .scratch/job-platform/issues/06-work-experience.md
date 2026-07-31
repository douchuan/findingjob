# 06 — 工作经历管理（多条）

**What to build:** 求职者可以添加多条工作经历（公司、职位、起止时间、描述），在个人主页展示。认证 HR 登录后可查看（L2 权限）。

**Blocked by:** 03 — 求职者个人主页

**Status:** ready-for-agent

- [ ] profile-service 实现 WorkExperience 的 CRUD API（公司、职位、start_date、end_date、description）
- [ ] 工作经历与 JobseekerProfile 关联（一对多）
- [ ] 前端：个人主页增加「工作经历」区域，支持添加/编辑/删除多条经历
- [ ] 前端：工作经历按时间倒序展示
- [ ] API 权限：仅求职者本人可编辑，任何人可查询（L2 需登录，MVP 先做基础查询，权限分层在后续 ticket 细化）
- [ ] 测试：求职者可 CRUD 工作经历
- [ ] 测试：end_date 可为空（当前在职）
