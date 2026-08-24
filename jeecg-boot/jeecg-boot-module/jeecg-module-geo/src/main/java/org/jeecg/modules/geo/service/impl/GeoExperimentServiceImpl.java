package org.jeecg.modules.geo.service.impl;

import org.jeecg.modules.geo.entity.GeoExperiment;
import org.jeecg.modules.geo.mapper.GeoExperimentMapper;
import org.jeecg.modules.geo.service.IGeoExperimentService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO experiment service implementation.
 */
@Service
public class GeoExperimentServiceImpl extends ServiceImpl<GeoExperimentMapper, GeoExperiment> implements IGeoExperimentService {
}
