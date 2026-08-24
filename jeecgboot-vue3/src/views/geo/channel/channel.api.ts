import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/channel/list',
  save = '/geo/channel/add',
  edit = '/geo/channel/edit',
  queryById = '/geo/channel/queryById',
  deleteChannel = '/geo/channel/delete',
  deleteBatch = '/geo/channel/deleteBatch',
}

export const getChannelList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdateChannel = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getChannelById = (params) => defHttp.get({ url: Api.queryById, params });
export const deleteChannel = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteChannel, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeleteChannel = (params, handleSuccess) => {
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
