package com.dhcc.miniprogram.controller;

import com.dhcc.basic.controller.BaseController;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.miniprogram.api.MpMessageApi;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.service.MpMessageService;
import com.dhcc.miniprogram.service.MpUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author cb
 * @date 2020/6/16
 * description： 小程序发送封装类
 */
@Api(value = "小程序发送消息",description = "小程序发送消息")
@RequestMapping("/sendMessageApi")
@RestController
public class MpMessageController extends BaseController implements MpMessageApi {

    private static final Logger log = LoggerFactory.getLogger(MpMessageController.class);

    @Autowired
    private MpMessageService mpMessageService;

    @Autowired
    private MpUserService userService;

    @Override
    @GetMapping("/verifyMsgFromWechat.do")
    @ApiOperation(value = "验证消息的确来自微信服务器",notes = "验证消息的确来自微信服务器")
    public void verifyMsgFromWechat() {

        HttpServletResponse response = getResponse();
        String echostr = mpMessageService.verifyMsgFromWechat(getRequest());
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
    public DtoAccessTokenResult getAccessToken(
            @RequestParam(name = "appid") String appid,
            @RequestParam(name = "appSecret") String appSecret) {
        return mpMessageService.getAccessToken(appid,appSecret);
    }

    @Override
    @PostMapping(value = "/login.do")
    @ApiOperation(value = "登录凭证校验",notes = "登录凭证校验")
    public DtoGetIdenInfoResult login(DtoGetLoginRequest login) {
        return mpMessageService.login(login);
    }

    @Override
    @PostMapping(value="/sendNimiProSubMsg.do")
    @ApiOperation(value = "发送订阅消息",notes = "发送订阅消息")
    public DtoBasicResult sendMiniproSubMsg(@RequestBody DtoGetSubMsgRequest request) {
        return mpMessageService.sendMiniproSubMsg(request);
    }

    @Override
    @PostMapping(value = "/getPhoneNum.do")
    @ApiOperation(value = "获取手机号",notes = "获取手机号")
    public DtoBasicResult getPhoneNum(DtoGetPhoneNumRequest dtoGetPhoneNumRequest) {
        return userService.getPhoneNum(dtoGetPhoneNumRequest);
    }

    @Override
    @PostMapping(value = "getTemplateAuthResult.do")
    @ApiOperation(value = "获取模板授权结果",notes = "获取模板授权结果")
    public String getTemplateAuthResult(DtoGetTemplateAuthResult dtoGetTemplateAuthResult) {
//        [
//        {templateId:'Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts',title:'排队预约',desc:'政务服务大厅',isSub:'WDY'},
//        {templateId:'Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts',title:'预约成功',desc:'政务服务大厅',isSub:'YDY'},
//        {templateId:'Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts',title:'叫号通知',desc:'政务服务大厅',isSub:'YDY'}
//        ]

        return null;
    }
}
