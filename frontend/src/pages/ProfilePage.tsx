import React, { useEffect, useState } from 'react';
import { Card, Descriptions, Tag, Typography, Form, Input, InputNumber, Button, Space, message, Upload, Select, List, DatePicker, Modal } from 'antd';
import { UserOutlined, UploadOutlined, PlusOutlined, DeleteOutlined, GithubOutlined } from '@ant-design/icons';
import dayjs from 'dayjs';
import { useAuthStore } from '../stores/authStore';
import { get, put, post, del } from '../api';
import { useAuthStore } from '../stores/authStore';
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
}

interface UserSkill {
  id: number;
  skillId: number;
  skillName: string;
  category: string;
  level: string;
  verifiedCount: number;
}

interface WorkExp {
  id: number;
  company: string;
  position: string;
  startDate: string;
  endDate: string | null;
  description: string;
}

interface Cert {
  id: number;
  name: string;
  issuer: string;
  issueDate: string;
  imageUrl: string;
}

interface GitHubProject {
  id: number;
  repoName: string;
  description: string;
  language: string;
  starCount: number;
  url: string;
  isOwner: boolean;
}

const LEVEL_OPTIONS = [
  { label: '了解', value: 'BEGINNER' },
  { label: '熟练', value: 'FAMILIAR' },
  { label: '精通', value: 'EXPERT' },
];

const LEVEL_COLORS: Record<string, string> = {
  BEGINNER: 'default',
  FAMILIAR: 'blue',
  EXPERT: 'green',
};

export const ProfilePage: React.FC = () => {
  const { user } = useAuthStore();
  const [profile, setProfile] = useState<ProfileData | null>(null);
  const [skills, setSkills] = useState<UserSkill[]>([]);
  const [experiences, setExperiences] = useState<WorkExp[]>([]);
  const [certificates, setCertificates] = useState<Cert[]>([]);
  const [githubProjects, setGithubProjects] = useState<GitHubProject[]>([]);
  const [allSkillTags, setAllSkillTags] = useState<{ id: number; name: string }[]>([]);
  const [loading, setLoading] = useState(false);
  const [editing, setEditing] = useState(false);
  const [skillModalOpen, setSkillModalOpen] = useState(false);
  const [expModalOpen, setExpModalOpen] = useState(false);
  const [form] = Form.useForm();
  const [skillForm] = Form.useForm();
  const [expForm] = Form.useForm();

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const [p, s, e, c, g, tags] = await Promise.all([
        get<ProfileData>('/profile/me'),
        get<UserSkill[]>('/profile/me/skills'),
        get<WorkExp[]>('/profile/me/experiences'),
        get<Cert[]>('/profile/me/certificates'),
        get<GitHubProject[]>('/profile/me/github'),
        get<{ id: number; name: string }[]>('/profile/skills'),
      ]);
      setProfile(p.data);
      setSkills(s.data);
      setExperiences(e.data);
      setCertificates(c.data);
      setGithubProjects(g.data);
      setAllSkillTags(tags.data);
      form.setFieldsValue(p.data);
    } catch (error) {
      message.error('加载个人资料失败');
    }
  };

  const handleDeleteAccount = async () => {
    Modal.confirm({
      title: '确认注销账号',
      content: '申请后7天内可撤销。7天后您的个人信息将被彻底删除，评价将匿名化保留。',
      onOk: async () => {
        try {
          await post('/auth/me/delete', {});
          message.success('注销申请已提交，7天冷静期开始');
        } catch {
          message.error('申请失败');
        }
      },
    });
  };

  const handleSaveProfile = async () => {
    setLoading(true);
    try {
      const values = form.getFieldsValue();
      await put<ProfileData>('/profile/me', values);
      setEditing(false);
      message.success('保存成功');
      loadData();
    } catch {
      message.error('保存失败');
    } finally {
      setLoading(false);
    }
  };

  const handleAddSkill = async () => {
    const values = skillForm.getFieldsValue();
    if (!values.skillId || !values.level) return;
    try {
      await post('/profile/me/skills', { skillId: values.skillId, level: values.level });
      setSkillModalOpen(false);
      skillForm.resetFields();
      loadData();
      message.success('技能添加成功');
    } catch {
      message.error('添加失败');
    }
  };

  const handleRemoveSkill = async (skillId: number) => {
    try {
      await del(`/profile/me/skills/${skillId}`);
      loadData();
      message.success('技能已移除');
    } catch {
      message.error('移除失败');
    }
  };

  const handleAddExperience = async () => {
    const values = expForm.getFieldsValue();
    try {
      const body = {
        ...values,
        startDate: values.startDate?.format('YYYY-MM-DD'),
        endDate: values.endDate?.format('YYYY-MM-DD') || null,
      };
      await post('/profile/me/experiences', body);
      setExpModalOpen(false);
      expForm.resetFields();
      loadData();
      message.success('工作经历添加成功');
    } catch {
      message.error('添加失败');
    }
  };

  const uploadProps: UploadProps = {
    name: 'file',
    action: '/api/storage/upload?folder=avatars',
    headers: { Authorization: `Bearer ${localStorage.getItem('jwt_token')}` },
    onChange(info) {
      if (info.file.status === 'done') {
        form.setFieldValue('avatar', info.file.response?.data?.fileKey);
        message.success('头像上传成功');
      }
    },
  };

  if (!profile) return <Text>加载中...</Text>;

  return (
    <div>
      {/* Personal Info */}
      <Card
        title="个人信息"
        extra={editing ? (
          <Space>
            <Button onClick={() => { setEditing(false); form.setFieldsValue(profile); }}>取消</Button>
            <Button type="primary" onClick={handleSaveProfile} loading={loading}>保存</Button>
          </Space>
        ) : (
          <Button onClick={() => setEditing(true)}>编辑</Button>
        )}
      >
        {editing ? (
          <Form form={form} layout="vertical">
            <Form.Item label="头像" name="avatar">
              <Upload {...uploadProps} maxCount={1} showUploadList={false}>
                <Button icon={<UploadOutlined />}>上传头像</Button>
              </Upload>
            </Form.Item>
            <Form.Item label="姓名" name="name"><Input /></Form.Item>
            <Form.Item label="个人简介" name="bio"><Input.TextArea rows={3} /></Form.Item>
            <Form.Item label="期望职位" name="expectedPosition"><Input placeholder="如：Java 后端开发工程师" /></Form.Item>
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
          </Descriptions>
        )}
      </Card>

      {/* Skills */}
      <Card
        title={`技能标签 (${skills.length})`}
        extra={<Button icon={<PlusOutlined />} onClick={() => setSkillModalOpen(true)}>添加技能</Button>}
        style={{ marginTop: 16 }}
      >
        {skills.length === 0 ? (
          <Text type="secondary">暂未添加技能</Text>
        ) : (
          <Space wrap>
            {skills.map(skill => (
              <Tag key={skill.id} color={LEVEL_COLORS[skill.level]} closable onClose={() => handleRemoveSkill(skill.skillId)}>
                {skill.skillName} · {skill.level === 'BEGINNER' ? '了解' : skill.level === 'FAMILIAR' ? '熟练' : '精通'}
                {skill.verifiedCount > 0 && ` · ${skill.verifiedCount}次验证`}
              </Tag>
            ))}
          </Space>
        )}
      </Card>

      {/* Work Experience */}
      <Card
        title={`工作经历 (${experiences.length})`}
        extra={<Button icon={<PlusOutlined />} onClick={() => setExpModalOpen(true)}>添加经历</Button>}
        style={{ marginTop: 16 }}
      >
        {experiences.length === 0 ? (
          <Text type="secondary">暂未添加工作经历</Text>
        ) : (
          <List
            dataSource={experiences}
            renderItem={exp => (
              <List.Item>
                <div>
                  <Title level={5}>{exp.position} @ {exp.company}</Title>
                  <Text type="secondary">
                    {exp.startDate} — {exp.endDate || '至今'}
                  </Text>
                  {exp.description && <div><Text>{exp.description}</Text></div>}
                </div>
              </List.Item>
            )}
          />
        )}
      </Card>

      {/* Certificates */}
      <Card title={`证书 (${certificates.length})`} style={{ marginTop: 16 }}>
        {certificates.length === 0 ? (
          <Text type="secondary">暂未添加证书</Text>
        ) : (
          <List
            dataSource={certificates}
            renderItem={cert => (
              <List.Item>
                <div>
                  <Title level={5}>{cert.name}</Title>
                  <Text type="secondary">{cert.issuer} · {cert.issueDate}</Text>
                </div>
              </List.Item>
            )}
          />
        )}
      </Card>

      {/* GitHub Projects */}
      <Card
        title={`开源项目 (${githubProjects.length})`}
        extra={<Button icon={<GithubOutlined />}>关联 GitHub</Button>}
        style={{ marginTop: 16 }}
      >
        {githubProjects.length === 0 ? (
          <Text type="secondary">关联 GitHub/Gitee 后可展示你的高星项目</Text>
        ) : (
          <List
            dataSource={githubProjects}
            renderItem={proj => (
              <List.Item>
                <div>
                  <Title level={5}>
                    <a href={proj.url} target="_blank" rel="noreferrer">{proj.repoName}</a>
                    {proj.isOwner && <Tag color="gold" style={{ marginLeft: 8 }}>Owner</Tag>}
                  </Title>
                  <Text type="secondary">{proj.description}</Text>
                  <div>
                    {proj.language && <Tag>{proj.language}</Tag>}
                    <Tag color="yellow">⭐ {proj.starCount}</Tag>
                  </div>
                </div>
              </List.Item>
            )}
          />
        )}
      </Card>

      {/* Account Deletion */}
      <Card title="账号设置" style={{ marginTop: 16 }}>
        <Button danger onClick={handleDeleteAccount}>申请注销账号</Button>
        <Text type="secondary" style={{ marginLeft: 12 }}>
          申请后7天冷静期，期间可撤销
        </Text>
      </Card>

      {/* Add Skill Modal */}
      <Modal title="添加技能" open={skillModalOpen} onOk={handleAddSkill} onCancel={() => setSkillModalOpen(false)}>
        <Form form={skillForm} layout="vertical">
          <Form.Item name="skillId" label="选择技能" rules={[{ required: true }]}>
            <Select
              showSearch
              placeholder="搜索技能..."
              options={allSkillTags.map(t => ({ label: t.name, value: t.id }))}
              filterOption={(input, opt) => (opt?.label ?? '').toLowerCase().includes(input.toLowerCase())}
            />
          </Form.Item>
          <Form.Item name="level" label="熟练度" rules={[{ required: true }]} initialValue="FAMILIAR">
            <Select options={LEVEL_OPTIONS} />
          </Form.Item>
        </Form>
      </Modal>

      {/* Add Experience Modal */}
      <Modal title="添加工作经历" open={expModalOpen} onOk={handleAddExperience} onCancel={() => setExpModalOpen(false)}>
        <Form form={expForm} layout="vertical">
          <Form.Item name="company" label="公司" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="position" label="职位" rules={[{ required: true }]}><Input /></Form.Item>
          <Form.Item name="startDate" label="开始时间"><DatePicker /></Form.Item>
          <Form.Item name="endDate" label="结束时间"><DatePicker /></Form.Item>
          <Form.Item name="description" label="描述"><Input.TextArea rows={3} /></Form.Item>
        </Form>
      </Modal>
    </div>
  );
};
