import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/sentimentEvent/list',
  save = '/geo/sentimentEvent/add',
  edit = '/geo/sentimentEvent/edit',
  queryById = '/geo/sentimentEvent/queryById',
  deleteEvent = '/geo/sentimentEvent/delete',
  deleteBatch = '/geo/sentimentEvent/deleteBatch',
}

export const getSentimentEventList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdateSentimentEvent = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getSentimentEventById = (params) => defHttp.get({ url: Api.queryById, params });
export const deleteSentimentEvent = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteEvent, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeleteSentimentEvent = (params, handleSuccess) => {
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
