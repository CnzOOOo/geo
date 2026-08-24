package org.jeecg.modules.geo.service.impl;

import org.jeecg.modules.geo.entity.GeoMention;
import org.jeecg.modules.geo.mapper.GeoMentionMapper;
import org.jeecg.modules.geo.service.IGeoMentionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO mention service implementation.
 */
@Service
public class GeoMentionServiceImpl extends ServiceImpl<GeoMentionMapper, GeoMention> implements IGeoMentionService {
}
