package com.shixinke.utils.web.example.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.shixinke.utils.web.common.ResponseDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shixinke
 * @version 1.0
 * @Description
 * @Date 19-3-29 下午3:47
 */
@RestController
@RequestMapping("/sentinel")
public class SentinelController {

    @RequestMapping("flow")
    @SentinelResource(value = "sentinelFlow")
    public ResponseDTO flow() {
        return ResponseDTO.success();
    }
}
