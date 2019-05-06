package com.shixinke.utils.web.example.controller;

import com.dianping.cat.Cat;
import com.shixinke.utils.web.annotation.cat.CatLog;
import com.shixinke.utils.web.common.ResponseDTO;
import com.shixinke.utils.web.example.request.CouponSearchDTO;
import com.shixinke.utils.web.example.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shixinke
 * @version 1.0
 * @Description
 * @Date 19-3-29 下午3:47
 */
@RestController
@Slf4j
@RequestMapping("/cat")
public class CatController {

    @Autowired
    private CouponService couponService;

    @RequestMapping("list")
    @CatLog
    public ResponseDTO list(CouponSearchDTO couponSearchDTO) {
        Cat.logError(new Exception("test"));
        return ResponseDTO.success(couponService.queryList(couponSearchDTO));
    }


}
