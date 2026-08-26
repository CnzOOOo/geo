import { FormSchema } from '/@/components/Table';

export const formSchema: FormSchema[] = [
  {
    field: 'articleTitle',
    label: '文章标题',
    component: 'Input',
    componentProps: { disabled: true },
  },
  {
    field: 'articleId',
    label: '文章ID',
    component: 'Input',
    componentProps: { disabled: true },
  },
  {
    field: 'channelIds',
    label: '发布渠道',
    component: 'Select',
    required: true,
    componentProps: {
      mode: 'multiple',
      options: [],
      showSearch: true,
      optionFilterProp: 'label',
      placeholder: '请选择发布渠道',
    },
  },
  {
    field: 'immediateExecute',
    label: '创建后立即执行',
    component: 'Switch',
    defaultValue: true,
  },
];
