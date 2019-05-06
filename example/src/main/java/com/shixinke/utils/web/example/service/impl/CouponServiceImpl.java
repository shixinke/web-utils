package com.shixinke.utils.web.example.service.impl;

import com.shixinke.utils.web.example.mapper.CouponMapper;
import com.shixinke.utils.web.example.request.CouponSearchDTO;
import com.shixinke.utils.web.example.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * coupon service
 * @author shixinke
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;


    @Override
    public List<Map<String, Object>> queryList(CouponSearchDTO couponSearchDTO) {
        List<Map<String, Object>> list =  couponMapper.queryList(0, 0);
        return list;
    }
}
