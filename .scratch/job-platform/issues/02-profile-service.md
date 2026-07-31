# Profile Service - 求职者档案与技能标签

**Status:** needs-triage  
**Type:** task  
**Blocked by:** 01

实现 profile-service，管理求职者个人信息、技能标签、工作经历、证书和 GitHub/Gitee 项目数据。

## 需求

- 个人信息管理（姓名、头像、个人简介、期望职位）
- 技能标签系统：添加/删除/标注熟练度（了解/熟练/精通）
- 工作经历管理（多条）
- 证书管理（名称/机构/时间/图片）
- GitHub/Gitee OAuth 授权后拉取项目数据（Top 10 by stars）
- 技能验证：HR 可对求职者技能打「已验证」标记
- 预置技能词库管理

## 验收条件

1. 求职者可 CRUD 个人信息、技能、工作经历、证书
2. 技能标签从预置词库选择，支持搜索
3. 熟练度分为了解/熟练/精通
4. GitHub/Gitee 授权后拉取 Top 10 高星项目
5. HR 查看简历后可验证求职者技能（verified_count + 1）
6. 搜索时已验证技能优先排序
