import React, { useEffect, useState } from 'react';
import { Card, Statistic, Row, Col, Progress, Typography } from 'antd';
import { UserOutlined, TeamOutlined, FileTextOutlined, LikeOutlined, CheckCircleOutlined } from '@ant-design/icons';
import { get } from '../../api';

const { Title, Text } = Typography;

interface PlatformStats {
  totalUsers: number;
  activeUsers: number;
  jobseekers: number;
  hrCount: number;
  adminCount: number;
  pendingDeletion: number;
}

// MVP targets
const TARGETS = {
  jobseekers: 100,
  companies: 10,
  resumeRequests: 50,
};

export const AdminDashboard: React.FC = () => {
  const [stats, setStats] = useState<PlatformStats | null>(null);
  const [companyCount, setCompanyCount] = useState(0);

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const [statsRes, companiesRes] = await Promise.all([
        get<PlatformStats>('/auth/admin/stats'),
        get<{ totalElements: number }>('/company/public?page=0&pageSize=1'),
      ]);
      setStats(statsRes.data);
      // For approved companies, we need a separate admin endpoint
      // MVP: show placeholder
      setCompanyCount(0);
    } catch (error) {
      console.error('Failed to load stats:', error);
    }
  };

  if (!stats) return <Text>Loading...</Text>;

  return (
    <div>
      <Title level={4}>MVP 目标进度</Title>

      <Row gutter={[16, 16]} style={{ marginBottom: 24 }}>
        <Col span={8}>
          <Card>
            <Statistic
              title="求职者注册数"
              value={stats.jobseekers}
              suffix={`/ ${TARGETS.jobseekers}`}
              prefix={<UserOutlined />}
            />
            <Progress
              percent={Math.min(100, Math.round((stats.jobseekers / TARGETS.jobseekers) * 100))}
              status={stats.jobseekers >= TARGETS.jobseekers ? 'success' : 'active'}
            />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic
              title="认证企业数"
              value={companyCount}
              suffix={`/ ${TARGETS.companies}`}
              prefix={<TeamOutlined />}
            />
            <Progress
              percent={Math.min(100, Math.round((companyCount / TARGETS.companies) * 100))}
              status={companyCount >= TARGETS.companies ? 'success' : 'active'}
            />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic
              title="简历请求数"
              value={0}
              suffix={`/ ${TARGETS.resumeRequests}`}
              prefix={<FileTextOutlined />}
            />
            <Progress
              percent={0}
              status="active"
            />
          </Card>
        </Col>
      </Row>

      <Title level={4}>用户统计</Title>

      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Card>
            <Statistic title="总用户数" value={stats.totalUsers} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="活跃用户" value={stats.activeUsers} prefix={<CheckCircleOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="HR 数" value={stats.hrCount} prefix={<TeamOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="管理员" value={stats.adminCount} />
          </Card>
        </Col>
      </Row>

      {stats.pendingDeletion > 0 && (
        <Card style={{ marginTop: 16 }}>
          <Statistic
            title="注销中的用户"
            value={stats.pendingDeletion}
            valueStyle={{ color: '#faad14' }}
          />
        </Card>
      )}
    </div>
  );
};
