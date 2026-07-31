import React from 'react';
import { Button, Card, Result } from 'antd';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../stores/authStore';

export const HomePage: React.FC = () => {
  const { user } = useAuthStore();
  const navigate = useNavigate();

  return (
    <div>
      <Card>
        <Result
          status="success"
          title={`欢迎，${user?.name || user?.phone}！`}
          subTitle="以技能为核心的求职平台"
          extra={[
            <Button type="primary" key="profile" onClick={() => navigate('/profile')}>
              完善个人资料
            </Button>,
            <Button key="search" onClick={() => navigate('/search')}>
              {user?.role === 'HR' ? '搜索人才' : '浏览公司'}
            </Button>,
          ]}
        />
      </Card>
    </div>
  );
};
