package com.dhcc.miniprogram.controller;

import com.dhcc.basic.controller.BaseController;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.basic.util.Message;
import com.dhcc.miniprogram.api.MpMessageApi;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
import com.dhcc.miniprogram.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    private MpMessageService messageService;

    @Autowired
    private MpUserService userService;

    @Autowired
    private MpTemplateAuthService templateAuthService;

    @Autowired
    private MpTemplateListService templateListService;

    @Autowired
    private MpAccessTokenService accessTokenService;

    @Autowired
    private MpFacialRecognitionService facialRecognService;

    @Autowired
    private WechatConfig wechatConfig;

    @Override
    @GetMapping("/verifyMsgFromWechat.do")
    @ApiOperation(value = "验证消息来自微信服务器",notes = "验证消息来自微信服务器")
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
            log.error(BusinessCodeEnum.VERIFY_SERVER_IO_EXCEPTION.getMsg(), e);
            throw new BusinessException(BusinessCodeEnum.VERIFY_SERVER_IO_EXCEPTION.getMsg());
        } catch (Exception e) {
            log.error(BusinessCodeEnum.VERIFY_SERVER_EXCEPTION.getMsg(), e);
            throw new BusinessException(BusinessCodeEnum.VERIFY_SERVER_EXCEPTION.getMsg());
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
        return accessTokenService.getAccessToken(appid,appSecret);
    }

    @Override
    @PostMapping(value = "/login.do")
    @ApiOperation(value = "小程序登录",notes = "小程序登录")
    public Message<DtoIdenInfoResult> userLogin(DtoLoginRequest login) {
        return new Message<DtoIdenInfoResult>().setData(userService.userLogin(login));
    }

    @Override
    @PostMapping(value="/sendNimiProSubMsg.do")
    @ApiOperation(value = "指定人发送订阅消息",notes = "指定人发送订阅消息")
    public Message<DtoBasicResult> sendSubscribeMessageByPhone(@RequestBody DtoSubscribeMessageRequest request) {
        return new Message<DtoBasicResult>().setData(messageService.sendSubscribeMessageByPhone(request));
    }

    @Override
    @PostMapping(value = "/getPhoneNum.do")
    @ApiOperation(value = "获取手机号",notes = "获取手机号")
    public Message<DtoPhoneNumberResult> getPhoneNumber(DtoPhoneNumberRequest dtoPhoneNumberRequest) {
        return new Message<DtoPhoneNumberResult>().setData(userService.getPhoneNumber(dtoPhoneNumberRequest));
    }

    @Override
    @PostMapping(value = "/changePhone.do")
    @ApiOperation(value = "更换手机号",notes = "更换手机号")
    public Message<DtoPhoneNumberResult> changePhone(DtoPhoneNumberRequest dtoPhoneNumberRequest) {
        return new Message<DtoPhoneNumberResult>().setData(userService.changePhone(dtoPhoneNumberRequest));
    }

    @Override
    @PostMapping(value = "/getTemplateAuthResult.do")
    @ApiOperation(value = "获取用户可授权模板列表",notes = "获取用户可授权模板列表")
    public Message<DtoTemplateAuthResult> getTemplateAuthResult(DtoTemplateAuthRequest getTemplateAuthRequest) {
        return new Message<DtoTemplateAuthResult>().setData(templateAuthService.getTemplateAuthResult());
    }

    @Override
    @PostMapping(value = "/insertTemplateAuth.do")
    @ApiOperation(value = "用户授权模板",notes = "用户授权模板")
    public Message<DtoTemplateAuthResult> insertTemplateAuth(@RequestBody DtoUpdateTemplateAuthRequest templateAuthRequest) {
        return new Message<DtoTemplateAuthResult>().setData(templateAuthService.insertTemplateAuth(templateAuthRequest));
    }

    @Override
    @PostMapping("/updateTemplateList.do")
    @ApiOperation(value = "更新模板列表",notes = "更新模板列表")
    public Message<DtoBasicResult> updateTemplateList() {
        return new Message<DtoBasicResult>().setData(templateListService.updateTemplateList());
    }

    @Override
    @PostMapping(value = "/sendMassMessageByPhoneList.do")
    @ApiOperation(value = "发送群发消息",notes = "发送群发消息")
    public Message<DtoBasicResult> sendMassMessageByPhoneList(@RequestBody DtoSubscribeMessageRequest request) {
        return new Message<DtoBasicResult>().setData(messageService.sendMassMessageByPhoneList(request));
    }

    @Override
    @PostMapping(value = "/getTemplateAuthByPhoneList.do")
    @ApiOperation(value = "根据手机号列表获取用户授权模板列表",notes = "根据手机号列表获取用户授权模板列表")
    public Message<DtoTemplateAuthPhoneResult> getTemplateAuthByPhoneList(@RequestBody DtoTemplateAuthPhoneRequest templateAuthPhoneRequest) {
        return new Message<DtoTemplateAuthPhoneResult>().setData(templateAuthService.getTemplateAuthByPhoneList(templateAuthPhoneRequest));
    }

    @Override
    @PostMapping(value = "/getTemplateAuthByCondition.do")
    @ApiOperation(value = "根据手机号和模板ID查询用户授权模板",notes = "根据手机号和模板ID查询用户授权模板")
    public Message<DtoBasicResult> getTemplateAuthByCondition(@RequestBody DtoTemplateAuthPhoneCondRequest request) {
        return new Message<DtoBasicResult>().setData(templateAuthService.getTemplateAuthByCondition(request));
    }

    @Override
    @GetMapping("/getResult.do")
    @ApiOperation(value = "获取结果",notes = "获取结果")
    public Message<String> getResult() {
        if(wechatConfig.getWechatApprove()){
            return new Message<String>(false,"查询成功").setData("430202199201294020");
        }
        return new Message<String>(true,"查询成功").setData("");
    }

    @Override
    @GetMapping("/facialRecognition.do")
    @ApiOperation(value = "人脸识别",notes = "人脸识别")
    public Message<DtoFacialRecognitionResult> facialRecognition(
            @ApiParam(name = "verifyResult",value = "验证结果",required = true)
            @RequestParam(value="verifyResult",defaultValue = "") String verifyResult ) {
        return new Message<DtoFacialRecognitionResult>().setData(facialRecognService.facialRecognition(verifyResult));
    }
}
