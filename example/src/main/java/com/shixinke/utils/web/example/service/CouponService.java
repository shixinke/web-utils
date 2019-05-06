package com.shixinke.utils.web.example.service;

import com.shixinke.utils.web.example.request.CouponSearchDTO;

import java.util.List;
import java.util.Map;

/**
 * coupon service
 * @author shixinke
 */
public interface CouponService {
    /**
     * query coupon list
     * @param couponSearchDTO
     * @return
     */
    public List<Map<String, Object>> queryList(CouponSearchDTO couponSearchDTO);
}
