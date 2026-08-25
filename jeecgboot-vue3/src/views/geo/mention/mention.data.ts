import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { getMonitorTaskList } from '../monitorTask/monitorTask.api';
import { merchantSelectProps } from '../merchant/merchantSelect';

export const columns: BasicColumn[] = [
  { title: '引擎', dataIndex: 'engine', width: 120 },
  { title: '问题', dataIndex: 'query', width: 260 },
  { title: '是否提到', dataIndex: 'mentioned', width: 90 },
  { title: '位置', dataIndex: 'position', width: 80 },
  { title: '准确率', dataIndex: 'accuracyScore', width: 100 },
  { title: '情绪', dataIndex: 'sentiment', width: 100 },
  { title: '时间', dataIndex: 'occurredAt', width: 160 },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '引擎',
    field: 'engine',
    component: 'AutoComplete',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { value: '百度' },
        { value: 'DeepSeek' },
        { value: '豆包' },
        { value: 'ChatGPT' },
        { value: 'Google' },
        { value: 'Bing' },
        { value: 'Perplexity' },
        { value: '腾讯元宝' },
        { value: 'manual' },
      ],
    },
  },
  { label: '问题', field: 'query', component: 'Input', colProps: { span: 8 } },
  {
    label: '是否提到',
    field: 'mentioned',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '未提到', value: 0 },
        { label: '提到', value: 1 },
      ],
    },
  },
];

export const formSchema: FormSchema[] = [
  { field: 'id', label: 'id', component: 'Input', show: false },
  {
    field: 'monitorTaskId',
    label: '监测任务',
    component: 'ApiSelect',
    componentProps: {
      api: getMonitorTaskList,
      labelField: 'name',
      valueField: 'id',
      params: { pageNo: 1, pageSize: 200 },
      pageConfig: { isPage: true, pageField: 'pageNo', pageSizeField: 'pageSize', totalField: 'total', listField: 'records' },
      showSearch: true,
      allowClear: true,
      placeholder: '请选择监测任务',
    },
  },
  {
    field: 'merchantId',
    label: '商家',
    component: 'ApiSelect',
    componentProps: merchantSelectProps,
  },
  {
    field: 'engine',
    label: '引擎',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      options: [
        { value: '百度' },
        { value: 'DeepSeek' },
        { value: '豆包' },
        { value: 'ChatGPT' },
        { value: 'Google' },
        { value: 'Bing' },
        { value: 'Perplexity' },
        { value: '腾讯元宝' },
        { value: 'manual' },
      ],
    },
  },
  { field: 'query', label: '问题', component: 'InputTextArea' },
  { field: 'occurredAt', label: '时间', component: 'DatePicker', componentProps: { showTime: true, valueFormat: 'YYYY-MM-DD HH:mm:ss' } },
  { field: 'answerText', label: '回答内容', component: 'InputTextArea', componentProps: { rows: 8 } },
  {
    field: 'mentioned',
    label: '是否提到',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '未提到', value: 0 },
        { label: '提到', value: 1 },
      ],
    },
  },
  { field: 'position', label: '位置', component: 'InputNumber' },
  { field: 'sourceUrlsJson', label: '来源链接 JSON', component: 'InputTextArea' },
  { field: 'accuracyScore', label: '准确率', component: 'InputNumber' },
  {
    field: 'sentiment',
    label: '情绪',
    component: 'Select',
    componentProps: {
      allowClear: true,
      options: [
        { label: '正面', value: '正面' },
        { label: '中性', value: '中性' },
        { label: '负面', value: '负面' },
      ],
    },
  },
  { field: 'rawJson', label: '原始 JSON', component: 'InputTextArea' },
];
