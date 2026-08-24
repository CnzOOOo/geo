import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/knowledge/list',
  save = '/geo/knowledge/add',
  edit = '/geo/knowledge/edit',
  queryById = '/geo/knowledge/queryById',
  deleteKnowledge = '/geo/knowledge/delete',
  deleteBatch = '/geo/knowledge/deleteBatch',
}

export const getKnowledgeList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdateKnowledge = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getKnowledgeById = (params) => defHttp.get({ url: Api.queryById, params });
export const deleteKnowledge = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteKnowledge, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeleteKnowledge = (params, handleSuccess) => {
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
