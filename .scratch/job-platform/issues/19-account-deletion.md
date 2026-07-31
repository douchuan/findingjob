# 19 — 用户注销 + 7 天冷静期 + 数据删除

**What to build:** 用户可以申请注销账号，7 天冷静期内可撤销。到期后删除个人信息（姓名、头像、简历、技能标签、工作经历、证书），评价内容匿名化保留（显示为「已注销用户」）。

**Blocked by:** 02 — Auth 登录

**Status:** ready-for-agent

- [ ] auth-service 实现注销 API：申请注销 → 标记 status = `pending_deletion`，记录 deletion_scheduled_at = now + 7 days
- [ ] auth-service 实现撤销注销 API：status 恢复为 `active`
- [ ] 定时任务：每日扫描 deletion_scheduled_at 已过期的用户，触发数据清理
- [ ] 数据清理：删除个人信息（User.name, avatar）、JobseekerProfile、WorkExperience、Certificate、UserSkill、ResumeFile
- [ ] 评价匿名化：Rating.from_user_id 关联的用户名替换为「已注销用户」，评价内容保留
- [ ] 已下载简历不追溯删除
- [ ] 前端：用户设置页面增加「注销账号」按钮，提示 7 天冷静期
- [ ] 前端：冷静期内用户看到「账号将在 X 天后注销，可撤销」提示
- [ ] 前端：撤销注销按钮
- [ ] 测试：申请注销后 status 变为 pending_deletion
- [ ] 测试：7 天内撤销恢复正常
- [ ] 测试：7 天后个人信息被删除
- [ ] 测试：评价内容匿名化保留
