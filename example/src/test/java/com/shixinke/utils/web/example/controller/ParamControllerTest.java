package com.shixinke.utils.web.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.shixinke.utils.web.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;



/**
 * @author shixinke
 * crated 19-3-29 下午3:58
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ParamControllerTest {

    private MockHttpServletRequest request;

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;


    @Before
    public void before() {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("utf-8");
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void searchByEmpty() {
        buildSearchRequest(null);
    }

    @Test
    public void searchByUserId() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user_id", "10001");
        buildSearchRequest(jsonObject);
    }

    @Test
    public void searchByNickname() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname", "shixinke");
        buildSearchRequest(jsonObject);
    }

    @Test
    public void searchByAll() {
        JSONObject params = new JSONObject();
        params.put("item_ids", "1,2,3");
        buildRequest("/search/all", params);
    }

    private void buildSearchRequest(JSONObject jsonObject) {
        buildRequest("/param/search", jsonObject);
    }

    private void buildRequest(String uri, JSONObject jsonObject) {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(uri);
        requestBuilder.header("content-type", "application/json");
        JSONObject params = new JSONObject();
        String requestParams = "";
        if (jsonObject != null) {
            params.putAll(jsonObject);
            requestParams = params.toJSONString();
        }

        requestBuilder.content(requestParams);
        log.info("request={}", requestParams);
        ResponseDTO responseDTO = null;
        try {
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            log.info("result={}", result.getResponse().getContentAsString());
            responseDTO = JSONObject.parseObject(result.getResponse().getContentAsString(), ResponseDTO.class);
            log.info("response:{}", responseDTO);
        } catch (Exception e) {
            log.error("failed");
        }
        Assert.isTrue(responseDTO.getSuccess(), "failed");
    }

}
