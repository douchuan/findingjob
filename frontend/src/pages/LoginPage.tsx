import React, { useState } from 'react';
import { Button, Form, Input, Space, Card, Segmented, Result, Typography } from 'antd';
import { GithubOutlined } from '@ant-design/icons';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { post } from '../api';
import type { UserRole } from '../types';

const { Text } = Typography;

export const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { setToken, setUser } = useAuthStore();

  const handleGithubLogin = () => {
    // MVP: navigate directly to callback (no real OAuth)
    window.location.href = 'http://localhost:8001/api/auth/oauth/github/callback';
  };

  const onFinish = async (values: { phone: string; code: string }) => {
    try {
      const response = await post<{
        token: string;
        roleSelected: boolean;
        role: string;
        userId: number;
        name: string;
        avatar: string;
      }>('/auth/phone-login', values);

      if (response.data.roleSelected) {
        setToken(response.data.token);
        setUser({
          id: response.data.userId,
          phone: values.phone,
          name: response.data.name,
          avatar: response.data.avatar,
          role: response.data.role as UserRole,
          status: 'ACTIVE',
          createdAt: new Date().toISOString(),
        });
        navigate('/');
      } else {
        // Need to select role
        navigate(`/select-role?userId=${response.data.userId}&name=${response.data.name}`);
      }
    } catch (error) {
      console.error('Login failed:', error);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <div style={{ width: 400, padding: 40, background: '#fff', borderRadius: 8 }}>
        <h1 style={{ textAlign: 'center', marginBottom: 8 }}>FindingJob</h1>
        <p style={{ textAlign: 'center', color: '#666', marginBottom: 24 }}>
          以技能为核心的求职平台
        </p>

        <Button
          icon={<GithubOutlined />}
          block
          size="large"
          type="primary"
          onClick={handleGithubLogin}
          style={{ marginBottom: 24 }}
        >
          GitHub 登录
        </Button>

        <div style={{ textAlign: 'center', margin: '16px 0', color: '#999' }}>— 或 —</div>

        <Form onFinish={onFinish}>
          <Form.Item
            name="phone"
            rules={[
              { required: true, message: '请输入手机号' },
              { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' },
            ]}
          >
            <Input placeholder="手机号" size="large" />
          </Form.Item>
          <Form.Item name="code" rules={[{ required: true, message: '请输入验证码' }]}>
            <Space.Compact style={{ width: '100%' }}>
              <Input placeholder="验证码（任意6位）" size="large" />
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
