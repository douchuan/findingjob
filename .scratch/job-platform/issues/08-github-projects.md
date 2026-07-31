# 08 — GitHub/Gitee 关联 → 项目数据展示

**What to build:** 求职者可以授权关联 GitHub 或 Gitee 账号，系统拉取用户的高星项目（Top 10），在个人主页展示。这是可选加分项，非必填。

**Blocked by:** 03 — 求职者个人主页

**Status:** ready-for-agent

- [ ] profile-service 实现 GitHub/Gitee OAuth 授权回调，存储 access_token
- [ ] 授权后调用 GitHub/Gitee API 拉取用户仓库列表（名称、描述、语言、star 数、owner/contributor 角色）
- [ ] 按 star 数排序，取 Top 10 存储
- [ ] profile-service API：获取求职者的 GitHub/Gitee 项目列表
- [ ] 前端：个人主页增加「开源项目」区域，展示关联按钮和项目列表
- [ ] 前端：未关联的求职者显示「关联 GitHub/Gitee」引导
- [ ] 测试：Mock GitHub API，验证项目数据拉取和 Top 10 排序
- [ ] 测试：未关联 GitHub 的求职者主页不显示项目区域
