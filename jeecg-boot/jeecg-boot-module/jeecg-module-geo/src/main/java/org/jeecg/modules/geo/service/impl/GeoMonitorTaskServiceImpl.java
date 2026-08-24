package org.jeecg.modules.geo.service.impl;

import java.util.Date;

import org.jeecg.modules.geo.ai.GeoAiClient;
import org.jeecg.modules.geo.entity.GeoMention;
import org.jeecg.modules.geo.entity.GeoMerchant;
import org.jeecg.modules.geo.entity.GeoMonitorTask;
import org.jeecg.modules.geo.mapper.GeoMonitorTaskMapper;
import org.jeecg.modules.geo.service.IGeoMentionService;
import org.jeecg.modules.geo.service.IGeoMerchantService;
import org.jeecg.modules.geo.service.IGeoMonitorTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO monitor task service implementation.
 */
@Service
public class GeoMonitorTaskServiceImpl extends ServiceImpl<GeoMonitorTaskMapper, GeoMonitorTask> implements IGeoMonitorTaskService {

    @Autowired
    private IGeoMentionService geoMentionService;

    @Autowired
    private IGeoMerchantService geoMerchantService;

    @Autowired
    private GeoAiClient geoAiClient;

    @Override
    public GeoMonitorTask runNow(String id) {
        GeoMonitorTask task = getById(id);
        if (task == null) {
            throw new IllegalArgumentException("monitor task not found");
        }
        task.setLastRunAt(new Date());
        updateById(task);

        if (task.getQuerySetJson() == null || task.getQuerySetJson().isBlank()) {
            return task;
        }

        JSONArray queries;
        try {
            queries = JSON.parseArray(task.getQuerySetJson());
        } catch (Exception e) {
            throw new IllegalArgumentException("querySetJson is invalid");
        }
        if (queries == null || queries.isEmpty()) {
            return task;
        }

        JSONObject engineConfig = new JSONObject();
        if (task.getEngineConfigJson() != null && !task.getEngineConfigJson().isBlank()) {
            try {
                engineConfig = JSON.parseObject(task.getEngineConfigJson());
            } catch (Exception ignored) {
                engineConfig = new JSONObject();
            }
        }

        boolean useAi = Boolean.TRUE.equals(engineConfig.getBoolean("useAi"))
                || "true".equalsIgnoreCase(engineConfig.getString("useAi"));
        String engine = engineConfig.getString("engine");
        if (engine == null || engine.isBlank()) {
            engine = "manual";
        }
        String merchantName = merchantName(task.getMerchantId());

        for (Object item : queries) {
            String query = extractQuery(item);
            if (query == null || query.isBlank()) {
                continue;
            }
            String answerText = "MANUAL_REVIEW_REQUIRED";
            int mentioned = 0;
            if (useAi) {
                try {
                    answerText = geoAiClient.chat("你是一名 AI 搜索监测器。请直接回答用户问题，并说明是否提到指定商家。", query);
                    if (merchantName != null && answerText != null && answerText.contains(merchantName)) {
                        mentioned = 1;
                    }
                } catch (Exception e) {
                    answerText = "AI_ERROR: " + e.getMessage();
                }
            }

            GeoMention mention = new GeoMention();
            mention.setMonitorTaskId(task.getId());
            mention.setMerchantId(task.getMerchantId());
            mention.setEngine(engine);
            mention.setQuery(query);
            mention.setOccurredAt(new Date());
            mention.setAnswerText(answerText);
            mention.setMentioned(mentioned);
            mention.setPosition(0);
            geoMentionService.save(mention);
        }
        return task;
    }

    private String merchantName(String merchantId) {
        if (merchantId == null || merchantId.isBlank()) {
            return null;
        }
        GeoMerchant merchant = geoMerchantService.getById(merchantId);
        return merchant == null ? null : merchant.getMerchantName();
    }

    private String extractQuery(Object item) {
        if (item instanceof JSONObject) {
            return ((JSONObject) item).getString("query");
        }
        return String.valueOf(item);
    }
}
