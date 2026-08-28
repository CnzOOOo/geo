package org.jeecg.modules.geo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Comparator;

import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoChannel;
import org.jeecg.modules.geo.entity.GeoPublishTask;
import org.jeecg.modules.geo.mapper.GeoPublishTaskMapper;
import org.jeecg.modules.geo.publish.GeoPublishAdapter;
import org.jeecg.modules.geo.publish.GeoPublishResult;
import org.jeecg.modules.geo.service.IGeoArticleService;
import org.jeecg.modules.geo.service.IGeoChannelService;
import org.jeecg.modules.geo.service.IGeoPublishTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO publish task service implementation.
 */
@Service
public class GeoPublishTaskServiceImpl extends ServiceImpl<GeoPublishTaskMapper, GeoPublishTask> implements IGeoPublishTaskService {

    @Autowired
    private List<GeoPublishAdapter> geoPublishAdapters;

    @Autowired
    private IGeoArticleService geoArticleService;

    @Autowired
    private IGeoChannelService geoChannelService;

    @Override
    public GeoPublishTask execute(String id) {
        GeoPublishTask task = getById(id);
        if (task == null) {
            throw new IllegalArgumentException("publish task not found");
        }
        if (Integer.valueOf(2).equals(task.getStatus())) {
            return task;
        }

        GeoArticle article = geoArticleService.getById(task.getArticleId());
        GeoChannel channel = geoChannelService.getById(task.getChannelId());
        if (article == null || channel == null) {
            task.setStatus(3);
            task.setErrorCode("DATA_MISSING");
            task.setErrorMsg("article or channel not found");
            updatePublishResult(task);
            return task;
        }

        task.setStatus(1);
        task.setRetryCount(task.getRetryCount() == null ? 1 : task.getRetryCount() + 1);
        updateById(task);

        try {
            GeoPublishAdapter adapter = geoPublishAdapters.stream()
                    .filter(item -> item.supports(channel.getPlatform(), channel))
                    .max(Comparator.comparingInt(GeoPublishAdapter::getPriority))
                    .orElseThrow(() -> new IllegalStateException("no publish adapter found for " + channel.getPlatform()));
            GeoPublishResult result = adapter.publish(article, channel);
            if (result.isSuccess()) {
                task.setStatus(2);
                task.setExternalId(result.getExternalId());
                task.setExternalUrl(result.getExternalUrl());
                task.setPublishedAt(new Date());
                task.setErrorCode(null);
                task.setErrorMsg(null);
            } else if ("MANUAL_REQUIRED".equals(result.getErrorCode())) {
                task.setStatus(4);
                task.setErrorCode(result.getErrorCode());
                task.setErrorMsg(result.getErrorMsg());
            } else {
                task.setStatus(3);
                task.setErrorCode(result.getErrorCode());
                task.setErrorMsg(result.getErrorMsg());
            }
        } catch (Exception e) {
            task.setStatus(3);
            task.setErrorCode("PUBLISH_EXCEPTION");
            task.setErrorMsg(e.getMessage());
        }
        updatePublishResult(task);
        return task;
    }

    @Override
    public boolean updatePublishResult(GeoPublishTask task) {
        if (task == null || task.getId() == null) {
            return false;
        }
        LambdaUpdateWrapper<GeoPublishTask> updateWrapper = Wrappers.<GeoPublishTask>lambdaUpdate();
        updateWrapper.eq(GeoPublishTask::getId, task.getId());
        if (task.getStatus() != null) {
            updateWrapper.set(GeoPublishTask::getStatus, task.getStatus());
        }
        if (task.getExternalId() != null) {
            updateWrapper.set(GeoPublishTask::getExternalId, task.getExternalId());
        }
        if (task.getExternalUrl() != null) {
            updateWrapper.set(GeoPublishTask::getExternalUrl, task.getExternalUrl());
        }
        updateWrapper.set(GeoPublishTask::getErrorCode, task.getErrorCode());
        updateWrapper.set(GeoPublishTask::getErrorMsg, task.getErrorMsg());
        if (task.getRetryCount() != null) {
            updateWrapper.set(GeoPublishTask::getRetryCount, task.getRetryCount());
        }
        if (task.getPublishedAt() != null) {
            updateWrapper.set(GeoPublishTask::getPublishedAt, task.getPublishedAt());
        }
        return update(updateWrapper);
    }
}
