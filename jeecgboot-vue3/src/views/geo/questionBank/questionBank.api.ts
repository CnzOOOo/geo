import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/questionBank/list',
  save = '/geo/questionBank/add',
  edit = '/geo/questionBank/edit',
  queryById = '/geo/questionBank/queryById',
  deleteQuestion = '/geo/questionBank/delete',
  deleteBatch = '/geo/questionBank/deleteBatch',
}

export const getQuestionList = (params) => defHttp.get({ url: Api.list, params });

export const saveOrUpdateQuestion = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};

export const getQuestionById = (params) => defHttp.get({ url: Api.queryById, params });

export const deleteQuestion = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteQuestion, params }, { joinParamsToUrl: true }).then(() => {
    handleSuccess();
  });
};

export const batchDeleteQuestion = (params, handleSuccess) => {
  Modal.confirm({
    title: '确认删除',
    content: '是否删除选中的数据？',
    okText: '确认',
    cancelText: '取消',
    onOk: () => {
      return defHttp.delete({ url: Api.deleteBatch, data: params }, { joinParamsToUrl: true }).then(() => {
        handleSuccess();
      });
    },
  });
};
