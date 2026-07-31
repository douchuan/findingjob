import React, { useState } from 'react';
import { Card, Form, Input, Select, Button, List, Tag, Empty, Avatar } from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import { get } from '../api';

interface SearchResult {
  userId: number;
  name: string;
  avatar: string;
  yearsOfExperience: number | null;
  expectedPosition: string;
  skillNames: string[];
  totalVerifiedSkills: number;
}

export const SearchPage: React.FC = () => {
  const [results, setResults] = useState<SearchResult[]>([]);
  const [loading, setLoading] = useState(false);
  const [form] = Form.useForm();

  const handleSearch = async (values: { skill?: string; experience?: string; position?: string }) => {
    setLoading(true);
    try {
      const params = new URLSearchParams();
      if (values.skill) params.set('skill', values.skill);
      if (values.experience) {
        const [min, max] = values.experience.split('-').map(Number);
        if (min) params.set('minExp', String(min));
        if (max) params.set('maxExp', String(max));
      }
      if (values.position) params.set('position', values.position);

      const response = await get<SearchResult[]>(`/profile/search?${params}`);
      setResults(response.data);
    } catch {
      // Empty results on error
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Card title="搜索人才">
        <Form form={form} layout="inline" onFinish={handleSearch}>
          <Form.Item name="skill" label="技能">
            <Input placeholder="输入技能关键词" style={{ width: 200 }} />
          </Form.Item>
          <Form.Item name="experience" label="经验">
            <Select placeholder="经验年限" style={{ width: 150 }} allowClear>
              <Select.Option value="1">1年以下</Select.Option>
              <Select.Option value="1-3">1-3年</Select.Option>
              <Select.Option value="3-5">3-5年</Select.Option>
              <Select.Option value="5-10">5-10年</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="position" label="期望职位">
            <Input placeholder="期望职位" style={{ width: 150 }} />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" icon={<SearchOutlined />} loading={loading}>
              搜索
            </Button>
          </Form.Item>
        </Form>
      </Card>

      <Card style={{ marginTop: 16 }}>
        {results.length === 0 ? (
          <Empty description="输入关键词搜索人才" />
        ) : (
          <List
            dataSource={results}
            renderItem={item => (
              <List.Item>
                <List.Item.Meta
                  avatar={<Avatar src={item.avatar} />}
                  title={item.name}
                  description={
                    <div>
                      {item.expectedPosition && <Tag color="blue">{item.expectedPosition}</Tag>}
                      {item.yearsOfExperience && <Tag>{item.yearsOfExperience}年经验</Tag>}
                      {item.totalVerifiedSkills > 0 && (
                        <Tag color="gold">{item.totalVerifiedSkills}次验证</Tag>
                      )}
                      <div style={{ marginTop: 4 }}>
                        {item.skillNames.slice(0, 5).map(s => (
                          <Tag key={s}>{s}</Tag>
                        ))}
                        {item.skillNames.length > 5 && <Tag>+{item.skillNames.length - 5}</Tag>}
                      </div>
                    </div>
                  }
                />
              </List.Item>
            )}
          />
        )}
      </Card>
    </div>
  );
};
