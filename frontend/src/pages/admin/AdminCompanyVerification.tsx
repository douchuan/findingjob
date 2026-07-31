import React from 'react';
import { Card, Empty, Table } from 'antd';

export const AdminCompanyVerification: React.FC = () => {
  return (
    <Card title="企业认证审核">
      <Empty description="暂无待审核的企业" />
    </Card>
  );
};
