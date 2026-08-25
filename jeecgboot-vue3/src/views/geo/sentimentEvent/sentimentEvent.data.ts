import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { merchantSelectProps } from '../merchant/merchantSelect';

export const columns: BasicColumn[] = [
  { title: '平台', dataIndex: 'platform', width: 120 },
  { title: '事件类型', dataIndex: 'eventType', width: 120 },
  { title: '标题', dataIndex: 'title', width: 240 },
  { title: '情绪', dataIndex: 'sentiment', width: 100 },
  { title: '严重度', dataIndex: 'severity', width: 80 },
  { title: '状态', dataIndex: 'status', width: 80 },
  { title: '负责人', dataIndex: 'ownerId', width: 120 },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '平台',
    field: 'platform',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '大众点评', value: '大众点评' },
        { label: '美团', value: '美团' },
        { label: '百度地图', value: '百度地图' },
        { label: '高德地图', value: '高德地图' },
        { label: '小红书', value: '小红书' },
        { label: '抖音', value: '抖音' },
        { label: '知乎', value: '知乎' },
        { label: '微博', value: '微博' },
        { label: '新闻媒体', value: '新闻媒体' },
        { label: '论坛', value: '论坛' },
        { label: '其他', value: '其他' },
      ],
    },
  },
  {
    label: '情绪',
    field: 'sentiment',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '正面', value: '正面' },
        { label: '中性', value: '中性' },
        { label: '负面', value: '负面' },
      ],
    },
  },
  {
    label: '状态',
    field: 'status',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '待处理', value: 0 },
        { label: '处理中', value: 1 },
        { label: '已闭环', value: 2 },
        { label: '已归档', value: 3 },
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
    required: true,
    componentProps: merchantSelectProps,
  },
  {
    field: 'platform',
    label: '平台',
    component: 'Select',
    componentProps: {
      allowClear: true,
      options: [
        { label: '大众点评', value: '大众点评' },
        { label: '美团', value: '美团' },
        { label: '百度地图', value: '百度地图' },
        { label: '高德地图', value: '高德地图' },
        { label: '小红书', value: '小红书' },
        { label: '抖音', value: '抖音' },
        { label: '知乎', value: '知乎' },
        { label: '微博', value: '微博' },
        { label: '新闻媒体', value: '新闻媒体' },
        { label: '论坛', value: '论坛' },
        { label: '其他', value: '其他' },
      ],
    },
  },
  {
    field: 'eventType',
    label: '事件类型',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      options: [
        { value: '差评' },
        { value: 'AI 答错' },
        { value: '事实过期' },
        { value: '投诉' },
        { value: '虚假宣传' },
        { value: '安全事件' },
        { value: '价格争议' },
        { value: '服务纠纷' },
        { value: '其他' },
      ],
    },
  },
  { field: 'title', label: '标题', component: 'Input' },
  { field: 'content', label: '内容', component: 'InputTextArea' },
  {
    field: 'sentiment',
    label: '情绪',
    component: 'Select',
    componentProps: {
      options: [
        { label: '正面', value: '正面' },
        { label: '中性', value: '中性' },
        { label: '负面', value: '负面' },
      ],
    },
  },
  {
    field: 'severity',
    label: '严重度',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '未分级', value: 0 },
        { label: '低', value: 1 },
        { label: '较低', value: 2 },
        { label: '中', value: 3 },
        { label: '高', value: 4 },
        { label: '紧急', value: 5 },
      ],
    },
  },
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '待处理', value: 0 },
        { label: '处理中', value: 1 },
        { label: '已闭环', value: 2 },
        { label: '已归档', value: 3 },
      ],
    },
  },
  { field: 'ownerId', label: '负责人', component: 'Input' },
  { field: 'sourceUrl', label: '来源链接', component: 'Input' },
];
