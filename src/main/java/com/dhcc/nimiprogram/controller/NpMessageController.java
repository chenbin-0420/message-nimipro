package com.dhcc.nimiprogram.controller;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.controller.BaseController;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.nimiprogram.api.NpMessageApi;
import com.dhcc.nimiprogram.dto.*;
import com.dhcc.nimiprogram.service.NpMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author cb
 * @date 2020/6/16
 * description： 小程序发送封装类
 */
@Api(value = "小程序发送消息",description = "小程序发送消息")
@RequestMapping("/sendMessageApi")
@RestController
public class NpMessageController extends BaseController implements NpMessageApi {

    private static final Logger log = LoggerFactory.getLogger(NpMessageController.class);

    @Autowired
    private NpMessageService npMessageService;

    @Override
    @GetMapping("/verifyMsgFromWechat.do")
    @ApiOperation(value = "验证消息的确来自微信服务器",notes = "验证消息的确来自微信服务器")
    public void verifyMsgFromWechat() {

        HttpServletResponse response = getResponse();
        String echostr = npMessageService.verifyMsgFromWechat(getRequest());
        // 打印字符流
        PrintWriter out = null;
        try {
            // 获取字符流对象
            out = response.getWriter();
            // 输出微信加密签名
            out.write(echostr);
        } catch (IOException e) {
            log.debug("响应输出流异常\n", e);
            throw new BusinessException("响应输出流异常");
        } catch (Exception e) {
            log.debug("验证消息的确来自微信服务器\n", e);
            throw new BusinessException("验证消息的确来自微信服务器异常");
        } finally {
            log.info("关闭微信服务器资源");
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    @PostMapping(value = "/getAccessToken.do")
    @ApiOperation(value = "获取AccessToken",notes = "获取AccessToken")
    public DtoAccTkRst getAccessToken(
            @RequestParam(name = "appid") String appid,
            @RequestParam(name = "appSecret") String appSecret) {
        return npMessageService.getAccessToken(appid,appSecret);
    }

    @Override
    @PostMapping(value = "/login.do")
    @ApiOperation(value = "登录凭证校验",notes = "登录凭证校验")
    public DtoNpIdenInfoRst login(DtoNpLoginReq login) {
        return npMessageService.login(login);
    }

    @Override
    @PostMapping(value="/sendNimiProSubMsg.do",produces = "application/json")
    @ApiOperation(value = "发送订阅消息",notes = "发送订阅消息")
    public DtoBasicRst sendNimiProSubMsg(@RequestBody DtoNpSubMsgReq request) {
        return npMessageService.sendNimiProSubMsg(request);
    }

}
