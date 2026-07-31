import React from 'react';
import { Outlet } from 'react-router-dom';
import { Layout, Menu } from 'antd';
import { DashboardOutlined, TeamOutlined, WarningOutlined, BarChartOutlined } from '@ant-design/icons';

const { Sider, Content } = Layout;

export const AdminLayout: React.FC = () => {
  const menuItems = [
    { key: '/admin/dashboard', icon: <DashboardOutlined />, label: '数据看板' },
    { key: '/admin/companies', icon: <TeamOutlined />, label: '企业认证审核' },
    { key: '/admin/reports', icon: <WarningOutlined />, label: '举报处理' },
    { key: '/admin/users', icon: <TeamOutlined />, label: '用户管理' },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider theme="light" width={220}>
        <div style={{ padding: '16px', fontSize: 16, fontWeight: 'bold', borderBottom: '1px solid #f0f0f0' }}>
          管理后台
        </div>
        <Menu mode="inline" items={menuItems} defaultSelectedKeys={['/admin/dashboard']} />
      </Sider>
      <Content style={{ margin: 16 }}>
        <Outlet />
      </Content>
    </Layout>
  );
};
