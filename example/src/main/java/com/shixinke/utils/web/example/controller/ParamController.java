package com.shixinke.utils.web.example.controller;

import com.shixinke.utils.web.annotation.RequestParameter;
import com.shixinke.utils.web.common.ResponseDTO;
import com.shixinke.utils.web.example.request.UserSearchDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author shixinke
 * @version 1.0
 * @Description
 * @Date 19-3-29 下午3:47
 */
@RequestMapping("/param")
@RestController
@Slf4j
public class ParamController {
    @RequestMapping("search")
    public ResponseDTO search(@RequestParameter UserSearchDTO searchDTO) {
        log.info("searchDTO:{}", searchDTO);
        return ResponseDTO.success();
    }

    @RequestMapping("all")
    public ResponseDTO searchByParam(@RequestParameter UserSearchDTO searchDTO) {
        log.info("searchDTO={}", searchDTO);
        return ResponseDTO.success();
    }
}
