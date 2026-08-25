import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { merchantSelectProps } from '../merchant/merchantSelect';

export const columns: BasicColumn[] = [
  { title: '渠道名称', dataIndex: 'channelName', width: 200 },
  { title: '平台', dataIndex: 'platform', width: 120 },
  { title: '启用', dataIndex: 'enabled', width: 80 },
  { title: '每日限频', dataIndex: 'rateLimit', width: 100 },
  { title: '状态', dataIndex: 'status', width: 80 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '渠道名称', field: 'channelName', component: 'Input', colProps: { span: 8 } },
  {
    label: '平台',
    field: 'platform',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '微信公众号', value: 'wechat_mp' },
        { label: '自有站/Webhook', value: 'webhook' },
        { label: '知乎', value: 'zhihu' },
        { label: 'CSDN', value: 'csdn' },
        { label: '简书', value: 'jianshu' },
        { label: '头条号', value: 'toutiao' },
        { label: '百家号', value: 'baijiahao' },
        { label: '搜狐号', value: 'sohu' },
        { label: '网易号', value: 'wangyi' },
        { label: 'B站', value: 'bilibili' },
        { label: '企鹅号', value: 'qiehao' },
        { label: '小红书', value: 'xiaohongshu' },
        { label: '抖音', value: 'douyin' },
      ],
    },
  },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  {
    field: 'merchantId',
    label: '商家',
    component: 'ApiSelect',
    componentProps: merchantSelectProps,
  },
  {
    field: 'platform',
    label: '平台',
    component: 'Select',
    required: true,
    componentProps: {
      options: [
        { label: '微信公众号', value: 'wechat_mp' },
        { label: '自有站/Webhook', value: 'webhook' },
        { label: '知乎', value: 'zhihu' },
        { label: 'CSDN', value: 'csdn' },
        { label: '简书', value: 'jianshu' },
        { label: '头条号', value: 'toutiao' },
        { label: '百家号', value: 'baijiahao' },
        { label: '搜狐号', value: 'sohu' },
        { label: '网易号', value: 'wangyi' },
        { label: 'B站', value: 'bilibili' },
        { label: '企鹅号', value: 'qiehao' },
        { label: '小红书', value: 'xiaohongshu' },
        { label: '抖音', value: 'douyin' },
      ],
    },
  },
  { field: 'channelName', label: '渠道名称', component: 'Input', required: true },
  { field: 'configEncrypted', label: '加密配置', component: 'InputTextArea' },
  {
    field: 'enabled',
    label: '启用',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '停用', value: 0 },
        { label: '启用', value: 1 },
      ],
    },
  },
  { field: 'rateLimit', label: '每日限频', component: 'InputNumber', defaultValue: 0 },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '草稿', value: 0 },
        { label: '启用', value: 1 },
        { label: '停用', value: 2 },
      ],
    },
  },
];
