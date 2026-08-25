import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';
import { merchantSelectProps } from '../merchant/merchantSelect';

export const columns: BasicColumn[] = [
  { title: '任务名称', dataIndex: 'name', width: 200 },
  { title: '商家ID', dataIndex: 'merchantId', width: 220 },
  { title: '频率', dataIndex: 'cadence', width: 100 },
  { title: '启用', dataIndex: 'enabled', width: 80 },
  { title: '状态', dataIndex: 'status', width: 80 },
  { title: '上次运行', dataIndex: 'lastRunAt', width: 160 },
];

export const searchFormSchema: FormSchema[] = [
  { label: '任务名称', field: 'name', component: 'Input', colProps: { span: 8 } },
  {
    label: '启用',
    field: 'enabled',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      allowClear: true,
      options: [
        { label: '停用', value: 0 },
        { label: '启用', value: 1 },
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
  { field: 'name', label: '任务名称', component: 'Input', required: true },
  { field: 'querySetJson', label: '查询集 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  { field: 'engineConfigJson', label: '引擎配置 JSON', component: 'InputTextArea', componentProps: { rows: 6 } },
  {
    field: 'cadence',
    label: '频率',
    component: 'AutoComplete',
    componentProps: {
      allowClear: true,
      options: [
        { value: 'HOURLY' },
        { value: 'DAILY' },
        { value: 'WEEKLY' },
        { value: 'MONTHLY' },
        { value: '每天' },
        { value: '每周' },
        { value: '每月' },
      ],
    },
  },
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
  {
    field: 'status',
    label: '状态',
    component: 'Select',
    defaultValue: 0,
    componentProps: {
      options: [
        { label: '待运行', value: 0 },
        { label: '运行中', value: 1 },
        { label: '已停用', value: 2 },
      ],
    },
  },
];
