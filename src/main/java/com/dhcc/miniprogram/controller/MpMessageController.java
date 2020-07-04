package com.dhcc.miniprogram.controller;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.controller.BaseController;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.miniprogram.api.MpMessageApi;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.IsSubEnum;
import com.dhcc.miniprogram.service.*;
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

    @Autowired
    private MpTemplateListService templateListService;

    @Autowired
    private MpAccessTokenService accessTokenService;

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
    public DtoAccessTokenResult getAccessToken(
            @RequestParam(name = "appid") String appid,
            @RequestParam(name = "appSecret") String appSecret) {
        return accessTokenService.getAccessToken(appid,appSecret);
    }

    @Override
    @PostMapping(value = "/login.do")
    @ApiOperation(value = "登录凭证校验",notes = "登录凭证校验")
    public DtoIdenInfoResult userLogin(DtoLoginRequest login) {
        return userService.userLogin(login);
    }

    @Override
    @PostMapping(value="/sendNimiProSubMsg.do")
    @ApiOperation(value = "发送订阅消息",notes = "发送订阅消息")
    public DtoBasicResult sendSubscribeMessageByPhone(@RequestBody DtoSubscribeMessageRequest request) {
        return messageService.sendSubscribeMessageByPhone(request);
    }

    @Override
    @PostMapping(value = "/getPhoneNum.do")
    @ApiOperation(value = "获取手机号",notes = "获取手机号")
    public DtoPhoneNumberResult getPhoneNum(DtoPhoneNumberRequest dtoPhoneNumberRequest) {
        return userService.getPhoneNum(dtoPhoneNumberRequest);
    }

    private static String[] tempIds;
    private static String[] partTempIds;
    private static String[] titles;
    static{
        tempIds = new String[]{
                "CNW2KwhfJEO_yDIMYljup8Ohex8i2IU1AKejxjt5VeU",
                "2mz7F_lVYHcm0wCWbnVMAKqWanS57qCb8Xg15tAm5U0",
                "5u7FSvghMpuyIDsNcK9X8uuisVrMPWUnLjcm1SAHlt4",
                "C5YHldH57nsI74HdMDpl3J7lW2J3nn7OYJW4atMxm6Q"
        };
        partTempIds = new String[]{ tempIds[0],tempIds[1] };
        titles = new String[]{"业务办理进度通知","事项办理进度通知","文书送达","预约结果通知"};
    }

    @Override
    @PostMapping(value = "/getTemplateAuthResult.do")
    @ApiOperation(value = "获取授权模板结果集",notes = "获取授权模板结果集")
    public DtoTemplateAuthResult getTemplateAuthResult(DtoTemplateAuthRequest getTemplateAuthRequest) {
        // 记录入参
        log.info("模板授权请求参数："+JSON.toJSONString(getTemplateAuthRequest));
        List<DtoTemplateAuth> dtoTemplateAuths = new LinkedList<>();
        for (int i = 0; i < tempIds.length; i++) {
            DtoTemplateAuth getTemplateAuthResult = new DtoTemplateAuth();
            getTemplateAuthResult.setTemplateIds(new String[]{tempIds[i]});
            if(i == 0){
                getTemplateAuthResult.setTemplateIds(partTempIds);
            }
            getTemplateAuthResult.setTitle(titles[i]);
            getTemplateAuthResult.setDesc("政务服务大厅");
            getTemplateAuthResult.setIsSub(IsSubEnum.FALSE.getCode());
            getTemplateAuthResult.setType(1);
            getTemplateAuthResult.setOrder(i+1);
            dtoTemplateAuths.add(getTemplateAuthResult);
        }

        DtoTemplateAuthResult returnTemplateAuthResult = new DtoTemplateAuthResult(200,"成功");
        returnTemplateAuthResult.setData(dtoTemplateAuths);
        log.info(JSON.toJSONString(returnTemplateAuthResult));

        return returnTemplateAuthResult;
    }

    @Override
    @PostMapping(value = "/insertTemplateAuth.do")
    @ApiOperation(value = "添加模板授权给用户",notes = "添加模板授权给用户")
    public DtoTemplateAuthResult insertTemplateAuth(DtoTemplateAuthRequest dtoTemplateAuthRequest) {
        // 记录获取模板授权请求日志
        log.info("请求参数："+JSON.toJSONString(dtoTemplateAuthRequest));
        // 添加订阅模板授权
        templateAuthService.insertTemplateAuth(dtoTemplateAuthRequest);
        //
        List<DtoTemplateAuth> dtoTemplateAuths = new LinkedList<>();
        for (int i = 0; i < tempIds.length; i++) {
            DtoTemplateAuth getTemplateAuthResult = new DtoTemplateAuth();
            getTemplateAuthResult.setTemplateIds(new String[]{tempIds[i]});
            getTemplateAuthResult.setTemplateIds(new String[]{tempIds[i]});
            if(tempIds[i].equals(dtoTemplateAuthRequest.getTemplateIds()[0])){
                if(i == 0){
                    getTemplateAuthResult.setTemplateIds(partTempIds);
                }
                getTemplateAuthResult.setIsSub(IsSubEnum.TRUE.getCode());
            } else {
                if(i == 0){
                    getTemplateAuthResult.setTemplateIds(partTempIds);
                }
                getTemplateAuthResult.setIsSub(IsSubEnum.FALSE.getCode());
            }
            getTemplateAuthResult.setTitle(titles[i]);
            getTemplateAuthResult.setDesc("政务服务大厅");
            getTemplateAuthResult.setType(1);
            getTemplateAuthResult.setOrder(i+1);
            dtoTemplateAuths.add(getTemplateAuthResult);
        }

        DtoTemplateAuthResult returnTemplateAuthResult = new DtoTemplateAuthResult(200,"成功");
        returnTemplateAuthResult.setData(dtoTemplateAuths);
        log.info(JSON.toJSONString(returnTemplateAuthResult));

        return returnTemplateAuthResult;
    }

    @Override
    @PostMapping("/updateTemplateList.do")
    @ApiOperation(value = "更新模板列表",notes = "更新模板列表")
    public DtoBasicResult updateTemplateList() {
        return templateListService.updateTemplateList();
    }

}
