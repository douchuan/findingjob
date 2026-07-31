# 07 — 证书上传 → 存储展示

**What to build:** 求职者可以上传证书（名称、颁发机构、获得时间、证书图片），图片通过 storage-service 存储，在个人主页展示。

**Blocked by:** 03 — 求职者个人主页，01 — storage-service 骨架

**Status:** ready-for-agent

- [ ] storage-service 实现 LocalFileStorage（默认），接口：upload/download/delete/getUrl
- [ ] profile-service 实现 Certificate 的 CRUD API（名称、颁发机构、获得时间、图片 URL）
- [ ] 证书图片上传调用 storage-service，存储到 `/uploads/certificates/`
- [ ] 文件类型校验（仅图片：jpg/png/gif），大小限制（5MB）
- [ ] 前端：个人主页增加「证书」区域，支持上传证书图片和填写信息
- [ ] 前端：证书列表展示（缩略图 + 名称 + 机构 + 时间）
- [ ] 测试：证书图片上传成功并返回 URL
- [ ] 测试：非图片类型上传被拒绝
- [ ] 测试：超过 5MB 的文件被拒绝
