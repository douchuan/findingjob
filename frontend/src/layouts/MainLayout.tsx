import React, { useState } from 'react';
import { Outlet, useNavigate, Link, useLocation } from 'react-router-dom';
import { Layout, Menu, Avatar, Badge, Dropdown } from 'antd';
import {
  HomeOutlined,
  SearchOutlined,
  TeamOutlined,
  BellOutlined,
  UserOutlined,
  LogoutOutlined,
  SettingOutlined,
} from '@ant-design/icons';
import { useAuthStore } from '../stores/authStore';

const { Header, Sider, Content } = Layout;

export const MainLayout: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuthStore();
  const [collapsed, setCollapsed] = useState(window.innerWidth < 768);

  const menuItems = [
    { key: '/', icon: <HomeOutlined />, label: <Link to="/">首页</Link> },
    { key: '/profile', icon: <UserOutlined />, label: <Link to="/profile">我的主页</Link> },
    { key: '/search', icon: <SearchOutlined />, label: <Link to="/search">搜索人才</Link> },
    { key: '/companies', icon: <TeamOutlined />, label: <Link to="/companies">浏览公司</Link> },
    {
      key: '/notifications',
      icon: <BellOutlined />,
      label: <Link to="/notifications">通知</Link>,
    },
  ];

  const userMenu = {
    items: [
      { key: 'profile', icon: <SettingOutlined />, label: '个人设置' },
      { key: 'logout', icon: <LogoutOutlined />, label: '退出登录' },
    ],
    onClick: ({ key }: { key: string }) => {
      if (key === 'logout') {
        logout();
        navigate('/login');
      }
    },
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed} breakpoint="md">
        <div
          style={{
            height: 32,
            margin: 16,
            color: '#fff',
            fontSize: collapsed ? 12 : 16,
            fontWeight: 'bold',
            textAlign: 'center',
          }}
        >
          {collapsed ? 'FJ' : 'FindingJob'}
        </div>
        <Menu
          theme="dark"
          selectedKeys={[location.pathname]}
          mode="inline"
          items={menuItems}
        />
      </Sider>
      <Layout>
        <Header style={{ padding: '0 16px', background: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }}>
          <Dropdown menu={userMenu}>
            <div style={{ cursor: 'pointer', display: 'flex', alignItems: 'center', gap: 8 }}>
              <Avatar icon={<UserOutlined />} src={user?.avatar} />
              <span>{user?.name || user?.phone}</span>
            </div>
          </Dropdown>
        </Header>
        <Content style={{ margin: 16, padding: 16, background: '#fff', borderRadius: 8 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};
