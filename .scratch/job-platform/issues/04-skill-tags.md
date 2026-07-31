# 04 — 技能标签管理 + 预置词库

**What to build:** 求职者可以为自己的档案添加/删除技能标签，并为每个技能标注熟练度（了解/熟练/精通）。技能从预置词库中选择，支持搜索。管理员可预置技能词库。

**Blocked by:** 03 — 求职者个人主页

**Status:** ready-for-agent

- [ ] profile-service 实现 SkillTag 表（id, name, category, usage_count）和 UserSkill 表（user_id, skill_id, level, verified_count）
- [ ] 管理员预置技能词库（初始数据：常见编程语言/框架/工具，如 Java/React/Kubernetes/Python 等）
- [ ] profile-service API：求职者添加/删除/修改自己的技能标签，标注熟练度
- [ ] profile-service API：搜索技能词库（按名称模糊匹配）
- [ ] profile-service API：获取求职者的技能列表（含熟练度和 verified_count）
- [ ] 前端：个人主页增加「技能标签」区域，展示已添加的技能和熟练度标签
- [ ] 前端：技能添加弹窗/下拉搜索，从预置词库选择
- [ ] 测试：求职者可添加/删除技能
- [ ] 测试：技能搜索返回匹配结果
- [ ] 测试：熟练度仅支持三个枚举值
