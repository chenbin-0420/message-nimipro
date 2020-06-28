//package com.dhcc.nimiprogram.api;
//
//import com.alibaba.fastjson.JSON;
//import com.dhcc.basic.controller.BaseController;
//import com.dhcc.basic.dao.query.SimpleCondition;
//import com.dhcc.basic.exception.BusinessException;
//import com.dhcc.basic.util.HttpClientUtil;
//import com.dhcc.basic.util.Message;
//import com.dhcc.nimiprogram.config.NimiproUrlConfig;
//import com.dhcc.nimiprogram.config.WechatConfig;
//import com.dhcc.nimiprogram.dto.*;
//import com.dhcc.nimiprogram.model.TestUser;
//import com.dhcc.nimiprogram.service.TestUserService;
//import com.dhcc.nimiprogram.util.*;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author cb
// * @date 2020/6/16
// * description： 小程序发送封装类
// */
//@Api(value = "小程序发送消息",description = "小程序发送消息")
//@RequestMapping("/sendMessageApi")
//@RestController
//public class SendMessageApi extends BaseController {
//
//    private static final Logger log = LoggerFactory.getLogger(SendMessageApi.class);
//
//    @Autowired
//    private TestUserService testUserService;
//
//    @Autowired
//    private WechatConfig wechatConfig;
//
//    @Autowired
//    private AccTkUtil accTkUtil;
//
//    /**
//     * 验证消息的确来自微信服务器
//     */
//    @GetMapping("/verifyMsgFromWechat.do")
//    @ApiOperation(value = "验证消息的确来自微信服务器",notes = "验证消息的确来自微信服务器")
//    public void verifyMsgFromWechat() {
//        /**
//         * signature 微信加密签名
//         * timestamp 时间戳
//         * nonce 随机数
//         * echostr 随机字符串
//         */
//        HttpServletRequest request = getRequest();
//        HttpServletResponse response = getResponse();
//        String signature = request.getParameter("signature");
//        String timestamp = request.getParameter("timestamp");
//        String nonce = request.getParameter("nonce");
//        String echostr = request.getParameter("echostr");
//        // 打印字符流
//        PrintWriter out = null;
//
//        try {
//            // 获取字符流对象
//            out = response.getWriter();
//            // 创建生成微信加密签名数组
//            String[] arr = new String[]{ wechatConfig.getWechatToken(), timestamp, nonce };
//            log.info("接口名称：验证消息的确来自微信服务器");
//            log.info("接口参数：腾讯回调");
//            log.info("接口参数：" + JSON.toJSONString(new String[]{ wechatConfig.getWechatToken(), timestamp, nonce ,signature }));
//            // 判断微信加密签名是否相等
//            if (signature.equals(SimpleAlgorUtil.genSignature(arr))) {
//                // 输出微信加密签名
//                out.write(echostr);
//            } else {
//                // 抛异常
//                log.debug("验证消息的确来自微信服务器失败,参数数组{}",JSON.toJSON(arr));
//                throw new BusinessException("验证消息的确来自微信服务器失败");
//            }
//        } catch (IOException e) {
//            log.debug("响应输出流异常\n", e);
//            throw new BusinessException("响应输出流异常");
//        } catch (Exception e) {
//            log.debug("验证消息的确来自微信服务器\n", e);
//            throw new BusinessException("验证消息的确来自微信服务器");
//        } finally {
//            log.info("关闭微信服务器资源");
//            if (out == null) {
//                out.close();
//            }
//        }
//
//    }
//
//    /**
//     * 获取小程序全局唯一后台接口调用凭据（access_token）
//     * @param appid 小程序唯一凭证，即 AppID
//     * @param appSecret 小程序唯一凭证密钥，即 AppSecret
//     * @return Message<DtoAccTkRst>
//     */
//    @PostMapping(value = "/getAccessToken.do")
//    @ApiOperation(value = "获取AccessToken",notes = "获取AccessToken")
//    public Message<DtoAccTkRst> getAccessToken(
//            @RequestParam(name = "appid") String appid,
//            @RequestParam(name="appSecret") String appSecret) {
//
//        // 检查入参
//        CheckInParamUtil.checkInParam(appid,appSecret);
//
//        // 获取 httpClient
//        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
//        // 设置 Url 带参
//        HashMap<String, String> paramMap = new HashMap<>(3);
//        paramMap.put("grant_type", GrantTypeEnum.CLIENT_CREDENTIAL.getCode());
//        paramMap.put("appid", appid);
//        paramMap.put("secret", appSecret);
//        // 设置请求体参数
//        HashMap<String, String> headerMap = new HashMap<>(1);
//
//        try {
//            log.info("接口名称：" + NimiproUrlConfig.GET_ACCESS_TOKEN_URL.getName());
//            log.info("接口参数：" + NimiproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl());
//            log.info("接口参数：" + paramMap);
//            // 发送Get请求，并接收字符串的结果
//            String result = HttpClientUtil.doGet(httpClient, NimiproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl(), paramMap, headerMap);
//            // 解析字符串并返回结果
//            return new Message<DtoAccTkRst>(true,"").setData(JSON.parseObject(result, DtoAccTkRst.class));
//
//        } catch (Exception e) {
//            // 记录日志和抛异常
//            log.debug("获取微信Token异常\n", e);
//            throw new BusinessException("获取微信Token异常");
//
//        }
//    }
//
//    /**
//     * 登录凭证校验
//     * @param login 登录请求类
//     * @return DtoNimiproIdInfoRst
//     */
//    @GetMapping(value = "/login.do")
//    @ApiOperation(value = "登录凭证校验",notes = "登录凭证校验")
//    public Message<DtoNimiproIdInfoRst> login(DtoNimiproLoginReq login) {
//
//        // 检查入参
//        CheckInParamUtil.checkInParam(login);
//        // 获取 httpClient
//        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
//        // 设置 Url 带参
//        Map<String, String> paramMap = new HashMap<>(3);
//        paramMap.put("appid", login.getAppid());
//        paramMap.put("secret", login.getSecret());
//        paramMap.put("js_code", login.getJs_code());
//        paramMap.put("grant_type", GrantTypeEnum.AUTHORIZATION_CODE.getCode());
//        // 设置请求体参数
//        Map<String, String> headerMap = new HashMap<>(1);
//
//        try {
//            log.info("接口名称：" + NimiproUrlConfig.GET_CODE2SESSION_URL.getName());
//            log.info("接口参数：" + NimiproUrlConfig.GET_CODE2SESSION_URL.getUrl());
//            log.info("接口参数：" + paramMap);
//            // 发送Get请求，并接收字符串的结果
//            String jsonObj = HttpClientUtil.doGet(httpClient, NimiproUrlConfig.GET_CODE2SESSION_URL.getUrl(), paramMap, headerMap);
//            DtoNimiproIdInfoRst dtoNimiProIdInfoRst = JSON.parseObject(jsonObj, DtoNimiproIdInfoRst.class);
//            System.out.println("login:"+ JSON.toJSONString(dtoNimiProIdInfoRst));
//            // 根据openId查询用户是否存在
//            SimpleCondition sc = new SimpleCondition().addParm("openId",dtoNimiProIdInfoRst.getOpenid());
//            TestUser user = testUserService.findOne(sc);
//            // 不存在添加用户
//            if(user == null){
//                TestUser testUser = DtoNimiproIdInfoRst.toPO(dtoNimiProIdInfoRst);
//                testUser.setAppId(login.getAppid());
//                /* testUser.setCreateUser(); */
//                testUserService.save(testUser);
//            }else{
//                // 修改sessionKey
//                user.setSessionKey(dtoNimiProIdInfoRst.getSession_key());
//                user.setModifyTime(new Date(System.currentTimeMillis()));
//                testUserService.update(user);
//            }
//            // 解析字符串并返回结果
//            return new Message<DtoNimiproIdInfoRst>(true,"").setData(dtoNimiProIdInfoRst);
//        } catch (Exception e) {
//            // 记录日志和抛异常
//            log.debug("登录凭证校验异常",e);
//            throw new BusinessException("登录凭证校验异常");
//        }
//
//    }
//
//    /**
//     * 发送订阅消息
//     * @param request 小程序订阅消息请求类
//     * @return DtoBasicResult
//     */
//    @PostMapping("/sendNimiProSubMsg.do")
//    @ApiOperation(value = "发送订阅消息",notes = "发送订阅消息")
//    public Message<DtoBasicResult> sendNimiProSubMsg(DtoNimiproSubMsgReq request) {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
//        LocalDateTime localDateTime = LocalDateTime.now();
//        request.setTemplate_id("1JSC34sYGeoYlc-Q53zZVbQtzebkPKq50QhLKSckm0U")
//                .setLang("zh_CN")
//                .setMiniprogram_state(NimiProStatusEnum.TRIAL.getCode())
//                .buildData()
//                .putData("thing2", "value", "好饿食堂") // 添加模板参数信息
//                .putData("thing5", "value", "温馨提示，你还未订餐") // 添加模板参数信息
//                .putData("thing6", "value", "打开小程序，订餐") // 添加模板参数信息
//                .putData("time7", "value", localDateTime.format(formatter)) // 添加模板参数信息
//                .putData("name8", "value", "你好,先生"); // 添加模板参数信息
//
//        // 检查入参
//        CheckInParamUtil.checkInParam(accTkUtil.getAccessToken(), request);
//
//        // 获取httpClient
//        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
//        // 将对象转为Json字符串
//        String reqData = JSON.toJSONString(request);
//        // 设置请求体参数
//        HashMap<String, String> headerMap = new HashMap<>(1);
//
//        // 发送 http-post 请求
//        try {
//            log.info("接口名称：" + NimiproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getName());
//            log.info("接口参数：" + NimiproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl());
//            log.info("接口路径参数：?access_token=" + URLDecoder.decode(accTkUtil.getAccessToken(), StandardCharsets.UTF_8.name()));
//            log.info("接口JSON参数：" + reqData);
//            // 设置 Url 带参
//            String reqUrl = NimiproUrlConfig.SEND_SUBSCRIBE_MESSAGE.getUrl() + "?access_token=" + URLDecoder.decode(accTkUtil.getAccessToken(), StandardCharsets.UTF_8.name());
//            // 发送Post请求并接收字符串结果
//            String result = HttpClientUtil.doPost(httpClient, reqUrl, reqData, headerMap);
//            // 解析字符串并转为对象返回
//            return new Message<DtoBasicResult>().setData(JSON.parseObject(result, DtoBasicResult.class));
//        } catch (Exception e) {
//            // 记录日志和抛异常
//            log.debug("小程序发送订阅消息异常\n", e);
//            throw new BusinessException("小程序发送订阅消息异常");
//        }
//    }
//
//}
