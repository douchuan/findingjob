import React from 'react';
import { Card, Form, Input, Select, Button, Empty } from 'antd';

export const SearchPage: React.FC = () => {
  return (
    <div>
      <Card title="搜索人才" style={{ marginBottom: 16 }}>
        <Form layout="inline">
          <Form.Item name="skill" label="技能">
            <Input placeholder="输入技能关键词，如 Java" style={{ width: 200 }} />
          </Form.Item>
          <Form.Item name="experience" label="经验">
            <Select placeholder="经验年限" style={{ width: 150 }}>
              <Select.Option value="1">1年以下</Select.Option>
              <Select.Option value="3">1-3年</Select.Option>
              <Select.Option value="5">3-5年</Select.Option>
              <Select.Option value="10">5-10年</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name="position" label="期望职位">
            <Input placeholder="期望职位" style={{ width: 150 }} />
          </Form.Item>
          <Form.Item>
            <Button type="primary">搜索</Button>
          </Form.Item>
        </Form>
      </Card>

      <Card>
        <Empty description="暂无搜索结果" />
      </Card>
    </div>
  );
};
