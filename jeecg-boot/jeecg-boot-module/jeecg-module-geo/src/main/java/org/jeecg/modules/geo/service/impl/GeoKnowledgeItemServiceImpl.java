package org.jeecg.modules.geo.service.impl;

import org.jeecg.modules.geo.entity.GeoKnowledgeItem;
import org.jeecg.modules.geo.mapper.GeoKnowledgeItemMapper;
import org.jeecg.modules.geo.service.IGeoKnowledgeItemService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO knowledge item service implementation.
 */
@Service
public class GeoKnowledgeItemServiceImpl extends ServiceImpl<GeoKnowledgeItemMapper, GeoKnowledgeItem> implements IGeoKnowledgeItemService {
}
