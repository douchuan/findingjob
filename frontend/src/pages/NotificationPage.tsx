import React from 'react';
import { Card, Empty, List } from 'antd';

export const NotificationPage: React.FC = () => {
  return (
    <Card title="通知中心">
      <Empty description="暂无通知" />
    </Card>
  );
};
