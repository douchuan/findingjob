import React from 'react';
import { Card, Empty, Tag } from 'antd';

export const CompanyBrowsePage: React.FC = () => {
  return (
    <div>
      <Card
        title="浏览公司"
        extra={
          <div>
            <Tag>全部行业</Tag>
            <Tag>全部规模</Tag>
          </div>
        }
      >
        <Empty description="暂无已认证公司" />
      </Card>
    </div>
  );
};
