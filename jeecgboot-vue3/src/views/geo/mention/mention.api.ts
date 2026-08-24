import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/mention/list',
  save = '/geo/mention/add',
  edit = '/geo/mention/edit',
  queryById = '/geo/mention/queryById',
  deleteMention = '/geo/mention/delete',
  deleteBatch = '/geo/mention/deleteBatch',
}

export const getMentionList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdateMention = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getMentionById = (params) => defHttp.get({ url: Api.queryById, params });
export const deleteMention = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteMention, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeleteMention = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中的数据？',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => handleSuccess());
    },
  });
};
