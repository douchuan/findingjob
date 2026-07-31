import React from 'react';
import { Button, Form, Input, Space } from 'antd';
import { GithubOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';

export const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { setToken, setUser } = useAuthStore();

  const handleGithubLogin = async () => {
    // MVP: redirect to GitHub OAuth
    // In production: window.location.href = `${API_BASE}/api/auth/oauth/github`;
    console.log('GitHub OAuth login');
  };

  const onFinish = (values: { phone: string; code: string }) => {
    console.log('Phone login:', values);
    // MVP: mock login
    setToken('mock-jwt-token');
    navigate('/');
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <div style={{ width: 400, padding: 40, background: '#fff', borderRadius: 8 }}>
        <h1 style={{ textAlign: 'center', marginBottom: 32 }}>FindingJob</h1>
        <p style={{ textAlign: 'center', color: '#666', marginBottom: 24 }}>
          以技能为核心的求职平台
        </p>

        <Button
          icon={<GithubOutlined />}
          block
          size="large"
          onClick={handleGithubLogin}
          style={{ marginBottom: 24 }}
        >
          GitHub 登录
        </Button>

        <div style={{ textAlign: 'center', margin: '16px 0', color: '#999' }}>— 或 —</div>

        <Form onFinish={onFinish}>
          <Form.Item name="phone" rules={[{ required: true, message: '请输入手机号' }]}>
            <Input placeholder="手机号" size="large" />
          </Form.Item>
          <Form.Item name="code" rules={[{ required: true, message: '请输入验证码' }]}>
            <Space.Compact style={{ width: '100%' }}>
              <Input placeholder="验证码" size="large" />
              <Button type="primary">发送验证码</Button>
            </Space.Compact>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block size="large">
              登录 / 注册
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};
