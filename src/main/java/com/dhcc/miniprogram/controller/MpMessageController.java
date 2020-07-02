package com.dhcc.miniprogram.controller;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.controller.BaseController;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.miniprogram.api.MpMessageApi;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.IsSubEnum;
import com.dhcc.miniprogram.service.MpMessageService;
import com.dhcc.miniprogram.service.MpTemplateAuthService;
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
import java.util.LinkedList;
import java.util.List;

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
    private MpMessageService messageService;

    @Autowired
    private MpUserService userService;

    @Autowired
    private MpTemplateAuthService templateAuthService;

    @Override
    @GetMapping("/verifyMsgFromWechat.do")
    @ApiOperation(value = "验证消息的确来自微信服务器",notes = "验证消息的确来自微信服务器")
    public void verifyMsgFromWechat() {

        HttpServletResponse response = getResponse();
        String echostr = messageService.verifyMsgFromWechat(getRequest());
        // 打印字符流
        PrintWriter out = null;
        try {
            // 获取字符流对象
            out = response.getWriter();
            // 输出微信加密签名
            out.write(echostr);
        } catch (IOException e) {
            log.debug("响应输出流异常", e);
            throw new BusinessException("响应输出流异常");
        } catch (Exception e) {
            log.debug("验证消息的确来自微信服务器", e);
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
    public DtoReturnTokenResult getAccessToken(
            @RequestParam(name = "appid") String appid,
            @RequestParam(name = "appSecret") String appSecret) {
        return messageService.getAccessToken(appid,appSecret);
    }

    @Override
    @PostMapping(value = "/login.do")
    @ApiOperation(value = "登录凭证校验",notes = "登录凭证校验")
    public DtoReturnIdenInfoResult login(DtoGetLoginRequest login) {
        return messageService.login(login);
    }

    @Override
    @PostMapping(value="/sendNimiProSubMsg.do")
    @ApiOperation(value = "发送订阅消息",notes = "发送订阅消息")
    public DtoReturnBasicResult sendMiniproSubMsg(@RequestBody DtoGetSubMsgRequest request) {
        return messageService.sendMiniproSubMsg(request);
    }

    @Override
    @PostMapping(value = "/getPhoneNum.do")
    @ApiOperation(value = "获取手机号",notes = "获取手机号")
    public DtoReturnPhoneResult getPhoneNum(DtoGetPhoneNumRequest dtoGetPhoneNumRequest) {
        return userService.getPhoneNum(dtoGetPhoneNumRequest);
    }

    @Override
    @PostMapping(value = "/getTemplateAuthResult.do")
    @ApiOperation(value = "获取模板授权结果",notes = "获取模板授权结果")
    public DtoReturnTemplateAuthResult getTemplateAuthResult(DtoGetTemplateAuthRequest getTemplateAuthRequest) {
//        [
//        {templateId:'Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts',title:'排队预约',desc:'政务服务大厅',isSub:'Y'},
//        {templateId:'Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts',title:'预约成功',desc:'政务服务大厅',isSub:'Y'},
//        {templateId:'Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts',title:'叫号通知',desc:'政务服务大厅',isSub:'Y'}
//        ]


        log.info("请求参数："+JSON.toJSONString(getTemplateAuthRequest));
        List<DtoGetTemplateAuthResult> dtoGetTemplateAuthResults = new LinkedList<>();
        String[] tempIds = {"排队预约","预约成功","叫号通知"};

        for (int i = 0; i < 3; i++) {
            DtoGetTemplateAuthResult getTemplateAuthResult = new DtoGetTemplateAuthResult();
            getTemplateAuthResult.setTemplateId("Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts");
            getTemplateAuthResult.setTitle(tempIds[i]);
            getTemplateAuthResult.setDesc("政务服务大厅");
            if( i > 1 ){
                getTemplateAuthResult.setIsSub(IsSubEnum.TRUE.getCode());
            } else {
                getTemplateAuthResult.setIsSub(IsSubEnum.FALSE.getCode());
            }
            getTemplateAuthResult.setType(1);
            getTemplateAuthResult.setOrder(i+1);
            dtoGetTemplateAuthResults.add(getTemplateAuthResult);
        }

        DtoReturnTemplateAuthResult returnTemplateAuthResult = new DtoReturnTemplateAuthResult();
        returnTemplateAuthResult.setData(dtoGetTemplateAuthResults);
        returnTemplateAuthResult.setErrcode(200L);
        returnTemplateAuthResult.setErrmsg("成功");
        log.info(JSON.toJSONString(returnTemplateAuthResult));

        return returnTemplateAuthResult;
    }

    @Override
    @PostMapping(value = "/insertTemplateAuth.do")
    @ApiOperation(value = "添加模板授权给用户",notes = "添加模板授权给用户")
    public DtoReturnTemplateAuthResult insertTemplateAuth(DtoGetTemplateAuthRequest dtoGetTemplateAuthRequest) {

        log.info("请求参数："+JSON.toJSONString(dtoGetTemplateAuthRequest));

        templateAuthService.insertTemplateAuth(dtoGetTemplateAuthRequest);

        List<DtoGetTemplateAuthResult> dtoGetTemplateAuthResults = new LinkedList<>();
        String[] tempIds = {"排队预约","预约成功","叫号通知"};

        for (int i = 0; i < 3; i++) {
            DtoGetTemplateAuthResult getTemplateAuthResult = new DtoGetTemplateAuthResult();
            getTemplateAuthResult.setTemplateId("Zbnxupg68pS98hOkLAZgGXrdP10rSLH4y8aQvy2DVts");
            getTemplateAuthResult.setTitle(tempIds[i]);
            getTemplateAuthResult.setDesc("政务服务大厅");
            if( i > 1 ){
                getTemplateAuthResult.setIsSub(IsSubEnum.TRUE.getCode());
            } else {
                getTemplateAuthResult.setIsSub(IsSubEnum.FALSE.getCode());
            }
            getTemplateAuthResult.setType(1);
            getTemplateAuthResult.setOrder(i+1);
            dtoGetTemplateAuthResults.add(getTemplateAuthResult);
        }

        DtoReturnTemplateAuthResult returnTemplateAuthResult = new DtoReturnTemplateAuthResult();
        returnTemplateAuthResult.setData(dtoGetTemplateAuthResults);
        log.info(JSON.toJSONString(returnTemplateAuthResult));

        return returnTemplateAuthResult;
    }
}
