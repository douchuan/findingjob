import React, { useEffect, useState } from 'react';
import { Card, Table, Tag, Button, Modal, Form, Input, message, Descriptions, Image } from 'antd';
import { CheckOutlined, CloseOutlined, EyeOutlined } from '@ant-design/icons';
import { get, post } from '../../api';

interface Company {
  id: number;
  name: string;
  industry: string;
  size: string;
  description: string;
  logoUrl: string;
  verificationStatus: string;
  licenseFileKey: string;
  reviewComment: string;
  createdAt: string;
}

export const AdminCompanyVerification: React.FC = () => {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [loading, setLoading] = useState(false);
  const [detailModalOpen, setDetailModalOpen] = useState(false);
  const [selectedCompany, setSelectedCompany] = useState<Company | null>(null);
  const [verifyModalOpen, setVerifyModalOpen] = useState(false);
  const [verifyAction, setVerifyAction] = useState<'APPROVED' | 'REJECTED'>('APPROVED');
  const [form] = Form.useForm();

  useEffect(() => { loadPending(); }, []);

  const loadPending = async () => {
    setLoading(true);
    try {
      const response = await get<{ content: Company[] }>('/company/admin/pending?page=0&pageSize=50');
      setCompanies(response.data.content);
    } catch {
      message.error('加载失败');
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetail = async (id: number) => {
    try {
      const response = await get<Company>(`/company/admin/${id}`);
      setSelectedCompany(response.data);
      setDetailModalOpen(true);
    } catch {
      message.error('加载详情失败');
    }
  };

  const handleVerify = (action: 'APPROVED' | 'REJECTED') => {
    setVerifyAction(action);
    setDetailModalOpen(false);
    setVerifyModalOpen(true);
  };

  const submitVerify = async () => {
    if (!selectedCompany) return;
    const comment = form.getFieldValue('comment') || '';
    try {
      await post(
        `/company/admin/verify/${selectedCompany.id}?status=${verifyAction}&comment=${encodeURIComponent(comment)}`,
        {}
      );
      message.success(`已${verifyAction === 'APPROVED' ? '通过' : '拒绝'}`);
      setVerifyModalOpen(false);
      form.resetFields();
      loadPending();
    } catch {
      message.error('操作失败');
    }
  };

  const columns = [
    { title: '企业名称', dataIndex: 'name', key: 'name' },
    { title: '行业', dataIndex: 'industry', key: 'industry' },
    { title: '规模', dataIndex: 'size', key: 'size' },
    {
      title: '状态',
      dataIndex: 'verificationStatus',
      key: 'verificationStatus',
      render: (status: string) => {
        const map: Record<string, { color: string; text: string }> = {
          PENDING: { color: 'orange', text: '待审核' },
          APPROVED: { color: 'green', text: '已通过' },
          REJECTED: { color: 'red', text: '已拒绝' },
        };
        const { color, text } = map[status] || {};
        return <Tag color={color}>{text}</Tag>;
      },
    },
    {
      title: '操作',
      key: 'action',
      render: (_: unknown, record: Company) => (
        <Button icon={<EyeOutlined />} onClick={() => handleViewDetail(record.id)}>
          审核
        </Button>
      ),
    },
  ];

  return (
    <div>
      <Card title="企业认证审核" extra={<Button onClick={loadPending}>刷新</Button>}>
        <Table
          columns={columns}
          dataSource={companies}
          rowKey="id"
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      {/* Detail Modal */}
      <Modal
        title="企业详情"
        open={detailModalOpen}
        onCancel={() => setDetailModalOpen(false)}
        footer={[
          <Button key="reject" danger icon={<CloseOutlined />} onClick={() => handleVerify('REJECTED')}>
            拒绝
          </Button>,
          <Button key="approve" type="primary" icon={<CheckOutlined />} onClick={() => handleVerify('APPROVED')}>
            通过
          </Button>,
        ]}
        width={700}
      >
        {selectedCompany && (
          <Descriptions column={1} bordered>
            <Descriptions.Item label="企业名称">{selectedCompany.name}</Descriptions.Item>
            <Descriptions.Item label="行业">{selectedCompany.industry || '未填写'}</Descriptions.Item>
            <Descriptions.Item label="规模">{selectedCompany.size || '未填写'}</Descriptions.Item>
            <Descriptions.Item label="描述">{selectedCompany.description || '未填写'}</Descriptions.Item>
            <Descriptions.Item label="营业执照">
              {selectedCompany.licenseFileKey ? (
                <div>文件 Key: {selectedCompany.licenseFileKey}</div>
              ) : (
                '未上传'
              )}
            </Descriptions.Item>
            <Descriptions.Item label="审核状态">
              <Tag color={
                selectedCompany.verificationStatus === 'APPROVED' ? 'green' :
                selectedCompany.verificationStatus === 'REJECTED' ? 'red' : 'orange'
              }>
                {selectedCompany.verificationStatus}
              </Tag>
            </Descriptions.Item>
            {selectedCompany.reviewComment && (
              <Descriptions.Item label="审核意见">{selectedCompany.reviewComment}</Descriptions.Item>
            )}
          </Descriptions>
        )}
      </Modal>

      {/* Verify Modal */}
      <Modal
        title={verifyAction === 'APPROVED' ? '通过审核' : '拒绝审核'}
        open={verifyModalOpen}
        onOk={submitVerify}
        onCancel={() => setVerifyModalOpen(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="comment"
            label={verifyAction === 'APPROVED' ? '审核意见（可选）' : '拒绝理由'}
            rules={verifyAction === 'REJECTED' ? [{ required: true, message: '请填写拒绝理由' }] : []}
          >
            <Input.TextArea rows={4} placeholder={verifyAction === 'REJECTED' ? '请输入拒绝理由' : '可选填写审核意见'} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};
