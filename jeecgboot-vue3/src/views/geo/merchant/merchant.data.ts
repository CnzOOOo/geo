import { BasicColumn } from '/@/components/Table';
import { FormSchema } from '/@/components/Table';

export const columns: BasicColumn[] = [
  {
    title: '店铺名称',
    dataIndex: 'merchantName',
    width: 200,
    align: 'left',
  },
  {
    title: '分类',
    dataIndex: 'category',
    width: 120,
  },
  {
    title: '城市',
    dataIndex: 'city',
    width: 120,
  },
  {
    title: '地址',
    dataIndex: 'address',
    width: 260,
  },
  {
    title: '电话',
    dataIndex: 'phone',
    width: 140,
  },
  {
    title: '营业时间',
    dataIndex: 'openingHours',
    width: 180,
  },
  {
    title: '状态',
    dataIndex: 'status',
    width: 80,
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    label: '店铺名称',
    field: 'merchantName',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '城市',
    field: 'city',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    label: '分类',
    field: 'category',
    component: 'Input',
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'id',
    label: 'id',
    component: 'Input',
    show: false,
  },
  {
    field: 'merchantName',
    label: '店铺名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'aliases',
    label: '别名',
    component: 'Input',
  },
  {
    field: 'category',
    label: '分类',
    component: 'Input',
  },
  {
    field: 'province',
    label: '省',
    component: 'Input',
  },
  {
    field: 'city',
    label: '市',
    component: 'Input',
  },
  {
    field: 'district',
    label: '区',
    component: 'Input',
  },
  {
    field: 'address',
    label: '地址',
    component: 'InputTextArea',
  },
  {
    field: 'phone',
    label: '电话',
    component: 'Input',
  },
  {
    field: 'openingHours',
    label: '营业时间',
    component: 'Input',
  },
  {
    field: 'serviceArea',
    label: '服务范围',
    component: 'Input',
  },
  {
    field: 'website',
    label: '官网',
    component: 'Input',
  },
  {
    field: 'description',
    label: '简介',
    component: 'InputTextArea',
  },
  {
    field: 'status',
    label: '状态',
    component: 'InputNumber',
    defaultValue: 0,
    componentProps: {
      min: 0,
      max: 2,
    },
  },
];
