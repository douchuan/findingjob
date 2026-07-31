import React, { useState } from 'react';
import { Card, Segmented, Button, Result, Typography } from 'antd';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { UserOutlined, TeamOutlined, SecurityScanOutlined } from '@ant-design/icons';
import { useAuthStore } from '../stores/authStore';
import { post } from '../api';
import type { UserRole } from '../types';

const { Title, Text } = Typography;

export const RoleSelectionPage: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { setToken, setUser } = useAuthStore();
  const [selected, setSelected] = useState<string>('JOBSEEKER');

  const userId = searchParams.get('userId');
  const name = searchParams.get('name');

  const handleConfirm = async () => {
    if (!userId) {
      return;
    }

    try {
      const response = await post<{
        token: string;
        roleSelected: boolean;
        role: string;
        userId: number;
        name: string;
        avatar: string;
      }>('/auth/select-role', { role: selected }, {
        headers: { 'X-User-Id': userId },
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
      navigate('/');
    } catch (error) {
      console.error('Role selection failed:', error);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#f0f2f5' }}>
      <Card style={{ width: 500 }}>
        <Title level={3} style={{ textAlign: 'center', marginBottom: 8 }}>
          欢迎{name ? `，${name}` : ''}
        </Title>
        <Text type="secondary" style={{ display: 'block', textAlign: 'center', marginBottom: 24 }}>
          请选择你的角色（选择后不可更改）
        </Text>

        <Segmented
          value={selected}
          onChange={setSelected}
          block
          options={[
            { label: '求职者', value: 'JOBSEEKER', icon: <UserOutlined /> },
            { label: 'HR/猎头', value: 'HR', icon: <TeamOutlined /> },
            { label: '管理员', value: 'ADMIN', icon: <SecurityScanOutlined /> },
          ]}
          style={{ marginBottom: 24 }}
        />

        <Card size="small" style={{ marginBottom: 16, background: '#fafafa' }}>
          {selected === 'JOBSEEKER' && (
            <Text>作为求职者，你可以：管理个人档案、展示技能和 GitHub 项目、接收 HR 的简历请求、评价公司。</Text>
          )}
          {selected === 'HR' && (
            <Text>作为 HR，你可以：管理企业信息、搜索求职者、申请查看简历、评价求职者职业素养。</Text>
          )}
          {selected === 'ADMIN' && (
            <Text>作为管理员，你可以：审核企业认证、处理举报、管理平台用户和数据看板。</Text>
          )}
        </Card>

        <Button type="primary" block size="large" onClick={handleConfirm}>
          确认选择
        </Button>
      </Card>
    </div>
  );
};
