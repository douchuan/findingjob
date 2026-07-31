# 12 — 简历上传 → HR 请求 → 求职者审批 → 7 天下载链接

**What to build:** 求职者上传 PDF 简历（不超过 10MB），HR 可以向求职者发起简历查看请求，求职者审批通过后 HR 获得 7 天有效的预签名下载链接。过期后链接失效，需重新申请。

**Blocked by:** 11 — 信息权限分层

**Status:** ready-for-agent

- [ ] resume-service 实现 ResumeFile 表（id, user_id, file_key, file_size, uploaded_at）
- [ ] resume-service API：求职者上传 PDF 简历（仅 PDF，10MB 限制），调用 storage-service
- [ ] resume-service 实现 ResumeRequest 表（id, hr_id, jobseeker_id, status, download_url, expires_at）
- [ ] resume-service API：认证 HR 发起简历查看请求（状态: pending）
- [ ] resume-service API：求职者查看收到的请求列表，操作同意/拒绝
- [ ] 同意后生成 7 天有效的预签名下载 URL，状态变为 approved
- [ ] 拒绝后状态变为 rejected
- [ ] 下载 URL 过期后（expires_at 过期）无法访问
- [ ] 记录每次下载行为（用于后续技能验证权限判断）
- [ ] 前端（求职者端）：简历上传页面（PDF 上传 + 进度提示）
- [ ] 前端（求职者端）：简历请求管理页面（列表 + 同意/拒绝操作）
- [ ] 前端（HR 端）：求职者详情页增加「请求查看简历」按钮，显示请求状态
- [ ] 前端（HR 端）：审批通过的请求展示简历预览（浏览器 PDF 预览）+ 下载按钮
- [ ] 测试：PDF 文件上传成功，非 PDF 被拒绝
- [ ] 测试：HR 发起请求后求职者收到请求
- [ ] 测试：同意后生成有效下载链接
- [ ] 测试：链接过期后无法下载
