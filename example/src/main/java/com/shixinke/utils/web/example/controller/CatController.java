package com.shixinke.utils.web.example.controller;

import com.shixinke.utils.web.annotation.cat.CatLog;
import com.shixinke.utils.web.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
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

    @CatLog
    @RequestMapping("list")
    public ResponseDTO list() {
        log.info("hello");
        return ResponseDTO.success();
    }
}
