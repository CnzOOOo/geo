package org.jeecg.modules.geo.service.impl;

import org.jeecg.modules.geo.entity.GeoSentimentEvent;
import org.jeecg.modules.geo.mapper.GeoSentimentEventMapper;
import org.jeecg.modules.geo.service.IGeoSentimentEventService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO sentiment event service implementation.
 */
@Service
public class GeoSentimentEventServiceImpl extends ServiceImpl<GeoSentimentEventMapper, GeoSentimentEvent> implements IGeoSentimentEventService {
}
