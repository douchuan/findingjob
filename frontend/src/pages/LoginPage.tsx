import React, { useState } from 'react';
import { Button, Form, Input, Space, Card, Segmented, message } from 'antd';
import { GithubOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';
import { post } from '../api';
import type { UserRole } from '../types';

export const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { setToken, setUser } = useAuthStore();
  const [selectedRole, setSelectedRole] = useState<string>('JOBSEEKER');

  const handleDevLogin = async () => {
    try {
      const response = await post<{
        token: string;
        roleSelected: boolean;
        role: string;
        userId: number;
        name: string;
        avatar: string;
      }>('/auth/dev-login', {
        name: `dev_${selectedRole.toLowerCase()}`,
        role: selectedRole,
      });

      setToken(response.data.token);
      setUser({
        id: response.data.userId,
        phone: null,
        name: response.data.name,
        avatar: response.data.avatar,
        role: response.data.role as UserRole,
        status: 'ACTIVE',
        createdAt: new Date().toISOString(),
      });
      message.success(`已以 ${selectedRole} 角色登录`);
      navigate('/');
    } catch (error) {
      message.error('登录失败，请确认后端已启动');
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <div style={{ width: 400, padding: 40, background: '#fff', borderRadius: 8 }}>
        <h1 style={{ textAlign: 'center', marginBottom: 8 }}>FindingJob</h1>
        <p style={{ textAlign: 'center', color: '#666', marginBottom: 24 }}>
          以技能为核心的求职平台
        </p>

        {/* Dev login */}
        <Card size="small" style={{ marginBottom: 24, background: '#fffbe6' }}>
          <p style={{ textAlign: 'center', margin: '0 0 12px', color: '#888', fontSize: 12 }}>
            开发模式 · 选择角色直接登录
          </p>
          <Segmented
            value={selectedRole}
            onChange={setSelectedRole}
            block
            options={[
              { label: '👤 求职者', value: 'JOBSEEKER' },
              { label: '💼 HR', value: 'HR' },
              { label: '🛡️ 管理员', value: 'ADMIN' },
            ]}
            style={{ marginBottom: 12 }}
          />
          <Button type="primary" block size="large" onClick={handleDevLogin}>
            直接进入 ({selectedRole === 'JOBSEEKER' ? '求职者' : selectedRole === 'HR' ? 'HR' : '管理员'})
          </Button>
        </Card>

        <div style={{ textAlign: 'center', margin: '16px 0', color: '#999' }}>— 或 —</div>

        <Button
          icon={<GithubOutlined />}
          block
          size="large"
          style={{ marginBottom: 16 }}
          onClick={() => message.info('MVP 阶段请使用上方开发模式登录')}
        >
          GitHub 登录（未接入）
        </Button>

        <Form
          onFinish={() => message.info('MVP 阶段请使用上方开发模式登录')}
        >
          <Form.Item name="phone">
            <Input placeholder="手机号" size="large" />
          </Form.Item>
          <Form.Item name="code">
            <Space.Compact style={{ width: '100%' }}>
              <Input placeholder="验证码" size="large" />
              <Button type="primary">发送验证码</Button>
            </Space.Compact>
          </Form.Item>
          <Form.Item>
            <Button htmlType="submit" block size="large" disabled>
              手机登录（未接入）
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};
