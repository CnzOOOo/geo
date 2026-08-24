package org.jeecg.modules.geo.service.impl;

import org.jeecg.modules.geo.entity.GeoMerchant;
import org.jeecg.modules.geo.mapper.GeoMerchantMapper;
import org.jeecg.modules.geo.service.IGeoMerchantService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO merchant service implementation.
 */
@Service
public class GeoMerchantServiceImpl extends ServiceImpl<GeoMerchantMapper, GeoMerchant> implements IGeoMerchantService {
}
