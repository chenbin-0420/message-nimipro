package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dto.DtoFacialRecognitionResult;
import com.dhcc.miniprogram.service.MpFacialRecognitionService;
import com.dhcc.miniprogram.util.AccessTokenUtil;
import com.dhcc.miniprogram.util.DateUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author cb
 * @date 2021/3/31
 * description：
 */
@Service
public class MpFacialRecognitionServiceImpl implements MpFacialRecognitionService {

    private Logger log = LoggerFactory.getLogger(MpFacialRecognitionServiceImpl.class);

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Override
    public DtoFacialRecognitionResult facialRecognition(String verifyResult) {
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        StringBuilder recognitionUrl = new StringBuilder(wechatConfig.getFacialRecognitionUrl())
                .append("?access_token=").append(accessTokenUtil.getAccessToken());
        StringBuilder verifyResultBody = new StringBuilder("{ \"verify_result\": \"").append(verifyResult).append("\" }");
        HashMap<String, String> headMap = new HashMap<>(4);
        headMap.put("Date", DateUtil.getCurrentDate().toString());
        headMap.put("Content-Type","application/json;encoding=utf-8");
        headMap.put("Connection","close");
        log.info("Url:{}",recognitionUrl.toString());
        log.info("heads:{}",headMap);
        log.info("body:{}",verifyResultBody);
        try {
            String result = HttpClientUtil.doPost(httpClient, recognitionUrl.toString(), verifyResultBody.toString(), headMap);
            log.info("result:{}",result);
            return JSON.parseObject(result, DtoFacialRecognitionResult.class);
        } catch (Exception e) {
            log.error("人脸识别异常："+e.getMessage());
        }
        return null;
    }
}
