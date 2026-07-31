import React, { useEffect, useState } from 'react';
import { Card, List, Tag, Button, Modal, Form, Input, message, Descriptions } from 'antd';
import { EyeOutlined, LikeOutlined, DislikeOutlined } from '@ant-design/icons';
import { get, post } from '../api';

interface Company {
  id: number;
  name: string;
  industry: string;
  size: string;
  description: string;
  logoUrl: string;
}

interface CompanyStats {
  companyId: number;
  positiveCount: number;
  negativeCount: number;
  positiveTags: string[];
  negativeTags: string[];
}

interface Rating {
  id: number;
  tags: string[];
  comment: string;
  createdAt: string;
}

const POSITIVE_TAGS = ['招聘规范', '承诺兑现', '沟通及时', '流程透明'];
const NEGATIVE_TAGS = ['招聘不规范', '承诺未兑现', '面试不当', '薪资不符'];

export const CompanyBrowsePage: React.FC = () => {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [loading, setLoading] = useState(false);
  const [detailModalOpen, setDetailModalOpen] = useState(false);
  const [rateModalOpen, setRateModalOpen] = useState(false);
  const [selectedCompany, setSelectedCompany] = useState<Company | null>(null);
  const [stats, setStats] = useState<CompanyStats | null>(null);
  const [ratings, setRatings] = useState<Rating[]>([]);
  const [form] = Form.useForm();

  useEffect(() => { loadCompanies(); }, []);

  const loadCompanies = async () => {
    setLoading(true);
    try {
      const response = await get<{ content: Company[] }>('/company/public?page=0&pageSize=50');
      setCompanies(response.data.content);
    } catch {
      // empty
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetail = async (company: Company) => {
    setSelectedCompany(company);
    try {
      const [statsRes, ratingsRes] = await Promise.all([
        get<CompanyStats>(`/rating/company/${company.id}/stats`),
        get<Rating[]>(`/rating/company/${company.id}`),
      ]);
      setStats(statsRes.data);
      setRatings(ratingsRes.data);
    } catch {
      setStats(null);
      setRatings([]);
    }
    setDetailModalOpen(true);
  };

  const handleRate = async (values: { tags: string[]; comment: string }) => {
    if (!selectedCompany) return;
    try {
      await post(`/rating/company/${selectedCompany.id}`, values);
      message.success('评价成功');
      setRateModalOpen(false);
      form.resetFields();
      handleViewDetail(selectedCompany);
    } catch {
      message.error('评价失败');
    }
  };

  return (
    <div>
      <Card title="浏览公司" style={{ marginBottom: 16 }}>
        {companies.length === 0 ? (
          <p>暂无已认证公司</p>
        ) : (
          <List
            dataSource={companies}
            grid={{ gutter: 16, xs: 1, sm: 2, md: 3 }}
            renderItem={company => (
              <List.Item>
                <Card
                  title={company.name}
                  extra={
                    <Button size="small" icon={<EyeOutlined />} onClick={() => handleViewDetail(company)}>
                      详情
                    </Button>
                  }
                >
                  <p>行业: {company.industry || '未填写'}</p>
                  <p>规模: {company.size || '未填写'}</p>
                  {company.description && <p>{company.description.slice(0, 50)}...</p>}
                </Card>
              </List.Item>
            )}
          />
        )}
      </Card>

      {/* Company Detail Modal */}
      <Modal
        title={selectedCompany?.name}
        open={detailModalOpen}
        onCancel={() => setDetailModalOpen(false)}
        footer={[
          <Button key="rate" type="primary" onClick={() => setRateModalOpen(true)}>
            评价该公司
          </Button>,
        ]}
        width={700}
      >
        {selectedCompany && (
          <>
            <Descriptions column={1} bordered size="small">
              <Descriptions.Item label="行业">{selectedCompany.industry || '-'}</Descriptions.Item>
              <Descriptions.Item label="规模">{selectedCompany.size || '-'}</Descriptions.Item>
              <Descriptions.Item label="描述">{selectedCompany.description || '-'}</Descriptions.Item>
            </Descriptions>

            {stats && (
              <Card title="评价统计" size="small" style={{ marginTop: 16 }}>
                <div style={{ marginBottom: 8 }}>
                  <LikeOutlined style={{ color: '#52c41a' }} /> 规范 {stats.positiveCount} 次
                  {' / '}
                  <DislikeOutlined style={{ color: '#ff4d4f' }} /> 不规范 {stats.negativeCount} 次
                </div>
                {stats.positiveTags.length > 0 && (
                  <div>
                    {stats.positiveTags.map(t => <Tag color="green" key={t}>{t}</Tag>)}
                  </div>
                )}
                {stats.negativeTags.length > 0 && (
                  <div>
                    {stats.negativeTags.map(t => <Tag color="red" key={t}>{t}</Tag>)}
                  </div>
                )}
              </Card>
            )}

            {ratings.length > 0 && (
              <Card title="最近评价" size="small" style={{ marginTop: 16 }}>
                {ratings.slice(0, 5).map(r => (
                  <div key={r.id} style={{ marginBottom: 8, paddingBottom: 8, borderBottom: '1px solid #f0f0f0' }}>
                    <div>
                      {r.tags.map(t => {
                        const isPositive = POSITIVE_TAGS.includes(t);
                        return <Tag color={isPositive ? 'green' : 'red'} key={t}>{t}</Tag>;
                      })}
                    </div>
                    {r.comment && <p style={{ margin: '4px 0 0' }}>{r.comment}</p>}
                  </div>
                ))}
              </Card>
            )}
          </>
        )}
      </Modal>

      {/* Rate Modal */}
      <Modal
        title="评价公司"
        open={rateModalOpen}
        onOk={() => form.submit()}
        onCancel={() => setRateModalOpen(false)}
      >
        <Form form={form} onFinish={handleRate} layout="vertical">
          <Form.Item name="tags" label="标签" rules={[{ required: true, message: '请选择标签' }]}>
            <Select mode="multiple" placeholder="选择评价标签">
              {[...POSITIVE_TAGS, ...NEGATIVE_TAGS].map(t => (
                <Select.Option key={t} value={t}>{t}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="comment" label="评论（可选）">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};
