package com.dhcc.nimiprogram.controller;

import com.dhcc.basic.controller.BaseController;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.nimiprogram.api.NimiproMessageApi;
import com.dhcc.nimiprogram.dto.*;
import com.dhcc.nimiprogram.service.NimiproMessageService;
import com.dhcc.nimiprogram.util.NimiProStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author cb
 * @date 2020/6/16
 * description： 小程序发送封装类
 */
@Api(value = "小程序发送消息",description = "小程序发送消息")
@RequestMapping("/sendMessageApi")
@RestController
public class NimiproMessageController extends BaseController implements NimiproMessageApi {

    private static final Logger log = LoggerFactory.getLogger(NimiproMessageController.class);

    @Autowired
    private NimiproMessageService nimiproMessageService;

    @Override
    @GetMapping("/verifyMsgFromWechat.do")
    @ApiOperation(value = "验证消息的确来自微信服务器",notes = "验证消息的确来自微信服务器")
    public void verifyMsgFromWechat() {

        HttpServletResponse response = getResponse();
        String echostr = nimiproMessageService.verifyMsgFromWechat(getRequest());
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
            if (out == null) {
                out.close();
            }
        }
    }

    @Override
    @PostMapping(value = "/getAccessToken.do")
    @ApiOperation(value = "获取AccessToken",notes = "获取AccessToken")
    public DtoAccTkRst getAccessToken(String appid, String appSecret) {
        return nimiproMessageService.getAccessToken(appid,appSecret);
    }

    @Override
    @GetMapping(value = "/login.do")
    @ApiOperation(value = "登录凭证校验",notes = "登录凭证校验")
    public DtoNimiproIdInfoRst login(DtoNimiproLoginReq login) {
        return nimiproMessageService.login(login);
    }

    @Override
    @PostMapping("/sendNimiProSubMsg.do")
    @ApiOperation(value = "发送订阅消息",notes = "发送订阅消息")
    public DtoBasicResult sendNimiProSubMsg(String token,DtoNimiproSubMsgReq request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        request.setTemplate_id("EEjpPH5n_AK53yg_kkbpyP-Z9dV2oVtyLjB64xwuwNI")
                .setLang("zh_CN")
                .setMiniprogram_state(NimiProStatusEnum.TRIAL.getCode())
                .buildData()
                // 添加模板参数信息
                .putData("thing2", "value", "好饿食堂")
                .putData("thing5", "value", "温馨提示，你还未订餐")
                .putData("thing6", "value", "打开小程序，订餐")
                .putData("time7", "value", localDateTime.format(formatter))
                .putData("name8", "value", "你好,先生");
        return nimiproMessageService.sendNimiProSubMsg(token,request);
    }
}
