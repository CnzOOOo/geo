import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getMerchantList } from '../merchant/merchant.api';
import { getQuestionList } from '../questionBank/questionBank.api';

export const columns: BasicColumn[] = [
  { title: '标题', dataIndex: 'title', width: 300, align: 'left' },
  { title: '标题类型', dataIndex: 'titleType', width: 110 },
  { title: '状态', dataIndex: 'status', width: 80 },
  { title: '审核状态', dataIndex: 'reviewStatus', width: 100 },
  { title: '发布时间', dataIndex: 'publishedAt', width: 160 },
  { title: '规范链接', dataIndex: 'canonicalUrl', width: 260 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '标题', field: 'title', component: 'Input', colProps: { span: 8 } },
  { label: '标题类型', field: 'titleType', component: 'Input', colProps: { span: 8 } },
  { label: '状态', field: 'status', component: 'InputNumber', colProps: { span: 8 } },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  {
    field: 'merchantId',
    label: '商家',
    component: 'ApiSelect',
    required: true,
    componentProps: {
      api: getMerchantList,
      labelField: 'merchantName',
      valueField: 'id',
      params: { pageNo: 1, pageSize: 50, status: 1 },
      pageConfig: { isPage: true, pageField: 'pageNo', pageSizeField: 'pageSize', totalField: 'total', listField: 'records' },
    },
  },
  {
    field: 'questionId',
    label: '关联问题',
    component: 'ApiSelect',
    componentProps: {
      api: getQuestionList,
      labelField: 'question',
      valueField: 'id',
      params: { pageNo: 1, pageSize: 50, status: 1 },
      pageConfig: { isPage: true, pageField: 'pageNo', pageSizeField: 'pageSize', totalField: 'total', listField: 'records' },
    },
  },
  { field: 'title', label: '标题', component: 'Input', required: true },
  {
    field: 'titleType',
    label: '标题类型',
    component: 'Select',
    componentProps: {
      options: [
        { label: '问答式', value: 'QUESTION' },
        { label: '对比式', value: 'COMPARE' },
        { label: '避坑式', value: 'AVOID' },
      ],
    },
  },
  { field: 'summary', label: '摘要', component: 'InputTextArea' },
  { field: 'contentMd', label: 'Markdown 正文', component: 'InputTextArea', componentProps: { rows: 12 } },
  { field: 'canonicalUrl', label: '规范链接', component: 'Input' },
  { field: 'eeatExperienceScore', label: '经验分', component: 'InputNumber', componentProps: { min: 1, max: 10 } },
  { field: 'eeatExpertiseScore', label: '专业分', component: 'InputNumber', componentProps: { min: 1, max: 10 } },
  { field: 'eeatAuthorityScore', label: '权威分', component: 'InputNumber', componentProps: { min: 1, max: 10 } },
  { field: 'eeatTrustScore', label: '可信分', component: 'InputNumber', componentProps: { min: 1, max: 10 } },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '草稿', value: 0 },
        { label: '待审核', value: 1 },
        { label: '已发布', value: 2 },
        { label: '已下线', value: 3 },
      ],
    },
  },
  {
    field: 'reviewStatus',
    label: '审核状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '未审核', value: 0 },
        { label: '审核中', value: 1 },
        { label: '已通过', value: 2 },
        { label: '已驳回', value: 3 },
      ],
    },
  },
];
