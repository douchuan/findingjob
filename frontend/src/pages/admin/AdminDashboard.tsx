import React from 'react';
import { Card, Statistic, Row, Col } from 'antd';
import { UserOutlined, TeamOutlined, FileTextOutlined, LikeOutlined } from '@ant-design/icons';

export const AdminDashboard: React.FC = () => {
  return (
    <div>
      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Card>
            <Statistic title="求职者注册数" value={0} prefix={<UserOutlined />} suffix="/ 100" />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="认证企业数" value={0} prefix={<TeamOutlined />} suffix="/ 10" />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="简历请求数" value={0} prefix={<FileTextOutlined />} suffix="/ 50" />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="评价数" value={0} prefix={<LikeOutlined />} />
          </Card>
        </Col>
      </Row>
    </div>
  );
};
