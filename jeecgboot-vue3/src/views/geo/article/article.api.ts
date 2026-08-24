import { defHttp } from '/@/utils/http/axios';
import { Modal } from 'ant-design-vue';

enum Api {
  list = '/geo/article/list',
  save = '/geo/article/add',
  edit = '/geo/article/edit',
  queryById = '/geo/article/queryById',
  submitReview = '/geo/article/submitReview',
  publish = '/geo/article/publish',
  offline = '/geo/article/offline',
  deleteArticle = '/geo/article/delete',
  deleteBatch = '/geo/article/deleteBatch',
}

export const getArticleList = (params) => defHttp.get({ url: Api.list, params });
export const saveOrUpdateArticle = (params, isUpdate) => {
  const url = isUpdate ? Api.edit : Api.save;
  return defHttp.post({ url, params });
};
export const getArticleById = (params) => defHttp.get({ url: Api.queryById, params });
export const submitReviewArticle = (params, handleSuccess) => {
  return defHttp.post({ url: Api.submitReview, params }).then(() => handleSuccess());
};
export const publishArticle = (params, handleSuccess) => {
  return defHttp.post({ url: Api.publish, params }).then(() => handleSuccess());
};
export const offlineArticle = (params, handleSuccess) => {
  return defHttp.post({ url: Api.offline, params }).then(() => handleSuccess());
};
export const deleteArticle = (params, handleSuccess) => {
  return defHttp.delete({ url: Api.deleteArticle, params }, { joinParamsToUrl: true }).then(() => handleSuccess());
};
export const batchDeleteArticle = (params, handleSuccess) => {
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
