import React, { useEffect, useState } from 'react';
import { Card, Descriptions, Tag, Typography, Form, Input, InputNumber, Button, Space, message, Upload } from 'antd';
import { UserOutlined, UploadOutlined } from '@ant-design/icons';
import { useAuthStore } from '../stores/authStore';
import { get, put } from '../api';
import type { UploadProps } from 'antd';

const { Text, Title } = Typography;

interface ProfileData {
  id: number;
  userId: number;
  name: string;
  bio: string;
  expectedPosition: string;
  yearsOfExperience: number | null;
  avatar: string;
  githubToken: string;
  giteeToken: string;
}

export const ProfilePage: React.FC = () => {
  const { user } = useAuthStore();
  const [profile, setProfile] = useState<ProfileData | null>(null);
  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      const response = await get<ProfileData>('/profile/me');
      setProfile(response.data);
      form.setFieldsValue(response.data);
    } catch (error) {
      message.error('加载个人资料失败');
    }
  };

  const handleSave = async () => {
    setLoading(true);
    try {
      const values = form.getFieldsValue();
      const response = await put<ProfileData>('/profile/me', values);
      setProfile(response.data);
      setEditing(false);
      message.success('保存成功');
    } catch (error) {
      message.error('保存失败');
    } finally {
      setLoading(false);
    }
  };

  const uploadProps: UploadProps = {
    name: 'file',
    action: '/api/storage/upload?folder=avatars',
    headers: { Authorization: `Bearer ${localStorage.getItem('jwt_token')}` },
    onChange(info) {
      if (info.file.status === 'done') {
        const fileKey = info.file.response?.data?.fileKey;
        if (fileKey) {
          form.setFieldValue('avatar', fileKey);
          message.success('头像上传成功');
        }
      } else if (info.file.status === 'error') {
        message.error('头像上传失败');
      }
    },
  };

  if (!profile) {
    return <Text>加载中...</Text>;
  }

  return (
    <div>
      <Card
        title="个人信息"
        extra={
          editing ? (
            <Space>
              <Button onClick={() => { setEditing(false); form.setFieldsValue(profile); }}>取消</Button>
              <Button type="primary" onClick={handleSave} loading={loading}>保存</Button>
            </Space>
          ) : (
            <Button onClick={() => setEditing(true)}>编辑</Button>
          )
        }
      >
        {editing ? (
          <Form form={form} layout="vertical">
            <Form.Item label="头像" name="avatar">
              <Upload {...uploadProps} maxCount={1} showUploadList={false}>
                <Button icon={<UploadOutlined />}>上传头像</Button>
              </Upload>
            </Form.Item>
            <Form.Item label="姓名" name="name">
              <Input />
            </Form.Item>
            <Form.Item label="个人简介" name="bio">
              <Input.TextArea rows={3} />
            </Form.Item>
            <Form.Item label="期望职位" name="expectedPosition">
              <Input placeholder="如：Java 后端开发工程师" />
            </Form.Item>
            <Form.Item label="工作年限" name="yearsOfExperience">
              <InputNumber min={0} max={50} style={{ width: '100%' }} />
            </Form.Item>
          </Form>
        ) : (
          <Descriptions column={1} bordered>
            <Descriptions.Item label="姓名">
              <Space>
                {profile.avatar && <img src={profile.avatar} alt="" style={{ width: 32, height: 32, borderRadius: '50%' }} />}
                {profile.name || '未设置'}
              </Space>
            </Descriptions.Item>
            <Descriptions.Item label="个人简介">{profile.bio || '未填写'}</Descriptions.Item>
            <Descriptions.Item label="期望职位">{profile.expectedPosition || '未填写'}</Descriptions.Item>
            <Descriptions.Item label="工作年限">
              {profile.yearsOfExperience != null ? `${profile.yearsOfExperience} 年` : '未填写'}
            </Descriptions.Item>
            <Descriptions.Item label="角色">
              <Tag color="blue">{user?.role}</Tag>
            </Descriptions.Item>
          </Descriptions>
        )}
      </Card>

      <Card title="GitHub / Gitee 关联" style={{ marginTop: 16 }}>
        <Text type="secondary">关联后可展示你的高星项目（可选功能）</Text>
      </Card>

      <Card title="技能标签" style={{ marginTop: 16 }}>
        <Text type="secondary">请在后续版本中添加你的技能标签</Text>
      </Card>

      <Card title="工作经历" style={{ marginTop: 16 }}>
        <Text type="secondary">请在后续版本中添加你的工作经历</Text>
      </Card>
    </div>
  );
};
