import React from 'react';
import { Card, Descriptions, Tag, Typography } from 'antd';
import { useAuthStore } from '../stores/authStore';

const { Text } = Typography;

export const ProfilePage: React.FC = () => {
  const { user } = useAuthStore();

  return (
    <div>
      <Card title="个人信息" style={{ marginBottom: 16 }}>
        <Descriptions column={1}>
          <Descriptions.Item label="姓名">{user?.name || '未设置'}</Descriptions.Item>
          <Descriptions.Item label="手机号">{user?.phone || '未绑定'}</Descriptions.Item>
          <Descriptions.Item label="角色">
            <Tag color="blue">{user?.role}</Tag>
          </Descriptions.Item>
        </Descriptions>
      </Card>

      <Card title="技能标签" style={{ marginBottom: 16 }}>
        <Text type="secondary">请在后续版本中添加你的技能标签</Text>
      </Card>

      <Card title="工作经历" style={{ marginBottom: 16 }}>
        <Text type="secondary">请在后续版本中添加你的工作经历</Text>
      </Card>

      <Card title="开源项目" style={{ marginBottom: 16 }}>
        <Text type="secondary">关联 GitHub/Gitee 后可展示你的高星项目</Text>
      </Card>
    </div>
  );
};
