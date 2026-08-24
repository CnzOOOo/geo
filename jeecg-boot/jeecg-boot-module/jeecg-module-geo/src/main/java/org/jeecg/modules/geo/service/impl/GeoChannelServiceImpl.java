package org.jeecg.modules.geo.service.impl;

import org.jeecg.modules.geo.entity.GeoChannel;
import org.jeecg.modules.geo.mapper.GeoChannelMapper;
import org.jeecg.modules.geo.service.IGeoChannelService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO channel service implementation.
 */
@Service
public class GeoChannelServiceImpl extends ServiceImpl<GeoChannelMapper, GeoChannel> implements IGeoChannelService {
}
