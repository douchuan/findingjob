# 14 — 通知系统：简历请求/审批/到期

**What to build:** 实现站内通知系统。用户登录后拉取未读通知列表，可标记已读。覆盖三个触发事件：HR 发起简历请求 → 通知求职者；求职者同意/拒绝 → 通知 HR；简历授权到期 → 通知 HR。MVP 用数据库 + 前端轮询刷新。

**Blocked by:** 12 — 简历授权流程

**Status:** ready-for-agent

- [ ] notification-service 实现 Notification 表（id, user_id, type, content, is_read, created_at）
- [ ] notification-service API：获取当前用户的未读/全部通知列表
- [ ] notification-service API：标记通知为已读
- [ ] 触发器：HR 发起简历请求时自动生成求职者通知
- [ ] 触发器：求职者同意/拒绝请求时自动生成 HR 通知
- [ ] 触发器：简历链接到期时自动生成 HR 通知（定时任务或延迟队列，MVP 用 @Scheduled 轮询）
- [ ] 前端：通知中心页面（铃铛图标 + 未读数徽标 + 通知列表）
- [ ] 前端：登录后轮询未读通知（每 30 秒）
- [ ] 前端：点击通知跳转到对应页面（简历请求 → 请求管理页）
- [ ] 测试：发起简历请求后求职者收到通知
- [ ] 测试：通知标记已读后不再出现在未读列表
- [ ] 测试：链接到期后 HR 收到通知
