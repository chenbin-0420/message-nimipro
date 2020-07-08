package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhcc.basic.dao.query.SimpleCondition;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.MiniproUrlConfig;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dao.MpUserDao;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.GrantTypeEnum;
import com.dhcc.miniprogram.model.MpUser;
import com.dhcc.miniprogram.service.MpUserService;
import com.dhcc.miniprogram.util.AESUtil;
import com.dhcc.miniprogram.util.CheckInParamUtil;
import com.dhcc.miniprogram.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
service层一般情况下与model/dao是一对一的关系！【主要负责】这一个model的增删改查，保持原子性以提高复用度。
所以要求此service的【增删改的输入对象】和【查的输出对象】必需是此model。
不要把此model作为查询条件，但输出并非此model的方法也放在此service内，这样会给调用者带来困扰！

注意BaseServiceImpl里已经有save/update/delete统一方法，直接使用即可。
这些方法里已经处理了：非空检查，唯一性检查，删除前被使用检查，树状表path处理。
不要自己再重复写！！！

可以重写beforeSave/beforeUpdate/beforeDelete方法，在里面补充增删改前的检查判断。
可以重写afterSave/afterUpdate/afterDelete方法，在里面调用其他service对其他model做增删改。

对其他model做增删改，应该使用它的service，而不是dao。避免业务逻辑被跳过。
对其他model做查询，可以直接使用它的dao。但最好统一使用service。

牢记事务是在service层控制的，
service关键方法上应该有@Transactional注解。
查询方法上则用@Transactional(readOnly=true)

*/

/**
 * test_user-Service实现
 *
 * @author cb
 * @since 2020-06-23
 */
@Service
public class MpUserServiceImpl extends BaseServiceImpl<MpUserDao, MpUser, String> implements MpUserService {
    @Override
    protected void beforeSave(MpUser object) {
        //新增前
    }

    @Override
    protected void afterSave(MpUser object, MpUser savedObject) {
        //新增后
    }

    @Override
    protected void beforeUpdate(MpUser object) {
        //修改前
    }

    @Override
    protected void afterUpdate(MpUser object, MpUser savedObject) {
        //修改后
    }

    @Override
    protected void beforeDelete(MpUser object) {
        //删除前
    }

    @Override
    protected void afterDelete(MpUser object) {
        //删除后
    }

    private static final Logger log = LoggerFactory.getLogger(MpUserServiceImpl.class);
    /**
     * 成功标识：RESPONSE_SUCCESS
     * 失败标识：RESPONSE_FAIL
     * 存在手机号标识：RESPONSE_EXIST_PHONE
     * 获取手机号异常码 ：RESPONSE_GET_PHONE_SERVER_EXCEPTION
     * 登录异常码 ：RESPONSE_LOGIN_SERVER_EXCEPTION
     * 手机号：PURE_PHONE_NUMBER
     * 手机区号：COUNTRY_CODE
     */
    private static final Integer RESPONSE_SUCCESS = 200;
    private static final Integer RESPONSE_FAIL = 420;
    private static final Integer RESPONSE_EXIST_PHONE = 421;
    private static final Integer RESPONSE_GET_PHONE_SERVER_EXCEPTION = 429;
    private static final Integer RESPONSE_LOGIN_SERVER_EXCEPTION = 419;
    private static final String PURE_PHONE_NUMBER = "purePhoneNumber";
    private static final String COUNTRY_CODE = "countryCode";

    /**
     * RESPONSE_PHONE_EXISTS：手机号存在
     * RESPONSE_PHONE_NOT_EXISTS：手机号不存在,需要获取手机号
     */
    private static final Integer RESPONSE_LOGIN_PHONE_EXISTS = 1;
    private static final Integer RESPONSE_LOGIN_PHONE_NOT_EXISTS = 0;

    @Autowired
    private WechatConfig wechatConfig;

    @Override
    @Transactional
    public DtoIdenInfoResult userLogin(DtoLoginRequest login) {
        // 记录登录参数日志
        log.info("小程序登录入参：" + JSON.toJSONString(login));
        // 检查入参
        CheckInParamUtil.checkInParam(login);
        // 获取 httpClient
        CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
        // 小程序ID
        String appId = wechatConfig.getAppId();
        // 设置 Url 带参
        Map<String, String> paramMap = new HashMap<>(3);
        paramMap.put("appid", appId);
        paramMap.put("secret", wechatConfig.getSecret());
        paramMap.put("js_code", login.getCode());
        paramMap.put("grant_type", GrantTypeEnum.AUTHORIZATION_CODE.getCode());
        // 设置请求体参数
        Map<String, String> headerMap = new HashMap<>(1);
        DtoIdenInfo idenInfo;
        DtoIdenInfoResult idenInfoResult;
        try {
            log.info("接口名称：" + MiniproUrlConfig.GET_CODE2SESSION_URL.getName());
            log.info("接口参数：" + MiniproUrlConfig.GET_CODE2SESSION_URL.getUrl());
            log.info("接口参数：" + paramMap);
            // 发送Get请求，并接收字符串的结果
            String result = HttpClientUtil.doGet(httpClient, MiniproUrlConfig.GET_CODE2SESSION_URL.getUrl(), paramMap, headerMap);
            idenInfo = JSON.parseObject(result, DtoIdenInfo.class);
            // 根据openId查询用户是否存在
            SimpleCondition sc = new SimpleCondition()
                    .addParm("openId", idenInfo.getOpenid())
                    .addParm("app_id", appId);
            MpUser user = dao.findOne(sc);
            // 不存在添加用户
            if (user == null) {
                // 1.添加用户信息,获取用户信息、设置 appId 并保存mpUser
                MpUser mpUser = DtoIdenInfo.toPO(idenInfo);
                mpUser.setAppId(appId);
                save(mpUser);
                // 2.返回idenInfoResult对象 标识成功，前端判断是否需要
                idenInfo.setErrcode(RESPONSE_LOGIN_PHONE_NOT_EXISTS);
                idenInfo.setErrmsg("需要获取手机号");
                idenInfoResult = new DtoIdenInfoResult(RESPONSE_LOGIN_PHONE_NOT_EXISTS,"需要获取手机号")
                        .setData(DtoIdenInfoFilter.toFilter(idenInfo).setSession_key(null));
            } else {
                // 1.用户非第一次登录，修改信息，修改Session_key、modifyTime
                if (StringUtils.isNotEmpty(idenInfo.getSession_key())) {
                    // 修改sessionKey
                    user.setSessionKey(idenInfo.getSession_key());
                }
                // 修改时间设置为当前时间并更新user
                user.setModifyTime(DateUtil.getCurrentDate());
                update(user);
                // 2.返回 idenInfo 对象
                // 标识成功，前端判断是否需要获取手机号
                boolean mark = user.getPhoneNum() == null;
                // 用户手机号为空，用户需要获取手机号，否则不需要
                idenInfoResult = new DtoIdenInfoResult(mark ? RESPONSE_LOGIN_PHONE_NOT_EXISTS : RESPONSE_LOGIN_PHONE_EXISTS,
                        mark ? "需要获取手机号" : user.getPhoneNum()).setData(DtoIdenInfoFilter.toFilter(idenInfo).setSession_key(null));
            }
            // 记录日志
            log.info(JSON.toJSONString(idenInfoResult));
            // 解析字符串并返回结果
            return idenInfoResult;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.debug("小程序登录异常", e);
            return new DtoIdenInfoResult(RESPONSE_LOGIN_SERVER_EXCEPTION,"小程序登录异常");
        }
    }

    @Override
    @Transactional
    public DtoPhoneNumberResult getPhoneNumber(DtoPhoneNumberRequest phoneNumberRequest) {
        // 记录入参日志
        log.info("获取手机号入参：" + JSON.toJSONString(phoneNumberRequest));
        // 检查获取手机号入参
        CheckInParamUtil.checkInParam(phoneNumberRequest);
        // 根据 openId 查询 MpUser
        MpUser user = dao.findOne("a.openId = ?", new Object[]{phoneNumberRequest.getOpenId()});
        // 检查 sessionKey 为空
        String sessionKey = user.getSessionKey();
        if (StringUtils.isEmpty(sessionKey)) {
            return new DtoPhoneNumberResult(RESPONSE_GET_PHONE_SERVER_EXCEPTION,"获取手机号参数 sessionKey为空");
        }
        // 使用 AES/CBC/PKCS7Padding 解密
        String decrypt;
        try {
            log.info("接口名称：获取手机号");
            log.info("接口参数：" + JSON.toJSONString(new String[]{sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData()}));
            decrypt = AESUtil.pkcs7PaddingDecrypt(sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchProviderException e) {
            log.debug("小程序获取手机号异常", e);
            return new DtoPhoneNumberResult(RESPONSE_GET_PHONE_SERVER_EXCEPTION,"小程序获取手机号异常");
        }
        // 解析字符串
        JSONObject jsonObject = JSON.parseObject(decrypt);
        // 手机号解析结果
        log.info("获取手机号解析结果：" + JSON.toJSONString(jsonObject));
        // PURE_PHONE_NUMBER : 手机号 ，COUNTRY_CODE ：手机区号
        // 获取手机号
        String phoneNumber = jsonObject.getString(PURE_PHONE_NUMBER);
        // 响应类
        DtoPhoneNumberResult numberResult;
        if (StringUtils.isNotEmpty(phoneNumber)) {
            // 用户绑定手机号、手机区号、修改时间并修改用户信息
            user.setPhoneNum(jsonObject.getString(PURE_PHONE_NUMBER));
            user.setPhonePrefix(jsonObject.getString(COUNTRY_CODE));
            user.setModifyTime(DateUtil.getCurrentDate());
            update(user);
            // 设置成功编码
            numberResult = new DtoPhoneNumberResult(RESPONSE_SUCCESS, "获取手机号成功").setData(new DtoPhoneNumber(phoneNumber));
        } else {
            // 设置失败编码，失败信息
            numberResult = new DtoPhoneNumberResult(RESPONSE_FAIL, "获取手机号失败");
        }
        log.info("获取手机号返回结果：" + JSON.toJSONString(numberResult));
        return numberResult;
    }

    @Override
    @Transactional
    public DtoPhoneNumberResult changePhone(DtoPhoneNumberRequest phoneNumberRequest) {
        // 记录入参日志
        log.info("换绑手机号入参：" + JSON.toJSONString(phoneNumberRequest));
        // 检查获取手机号入参
        CheckInParamUtil.checkInParam(phoneNumberRequest);
        // 根据 openId 查询 MpUser
        MpUser user = dao.findOne("a.openId = ?", new Object[]{phoneNumberRequest.getOpenId()});
        // 检查 sessionKey 为空
        String sessionKey = user.getSessionKey();
        if (StringUtils.isEmpty(sessionKey)) {
            throw new BusinessException("换绑手机号参数 sessionKey为空");
        }
        // 使用 AES/CBC/PKCS7Padding 解密
        String decrypt;
        try {
            log.info("接口名称：获取手机号");
            log.info("接口参数：" + JSON.toJSONString(new String[]{sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData()}));
            decrypt = AESUtil.pkcs7PaddingDecrypt(sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchProviderException e) {
            log.debug("小程序获取手机号异常", e);
            throw new BusinessException("小程序获取手机号异常");
        }

        // 解析字符串
        JSONObject jsonObject = JSON.parseObject(decrypt);
        // 手机号解析结果
        log.info("换绑手机号解析结果：" + JSON.toJSONString(jsonObject));
        // PURE_PHONE_NUMBER : 手机号 ，COUNTRY_CODE ：手机区号
        // 获取手机号
        String phoneNumber = jsonObject.getString(PURE_PHONE_NUMBER);
        // 响应类
        DtoPhoneNumberResult numberResult;
        if (StringUtils.isNotEmpty(phoneNumber)) {
            // 手机号不同
            if(!phoneNumber.equals(user.getPhoneNum())){
                // 1、用户绑定手机号、手机区号、修改时间并修改用户信息
                user.setPhoneNum(jsonObject.getString(PURE_PHONE_NUMBER));
                user.setPhonePrefix(jsonObject.getString(COUNTRY_CODE));
                user.setModifyTime(DateUtil.getCurrentDate());
                update(user);
                ///2、换绑手机号
                numberResult = new DtoPhoneNumberResult(RESPONSE_SUCCESS, "换绑手机号成功").setData(new DtoPhoneNumber(phoneNumber));
            } else {
                // 存在手机号
                numberResult = new DtoPhoneNumberResult(RESPONSE_EXIST_PHONE, "存在手机号").setData(new DtoPhoneNumber(phoneNumber));
            }
        } else {
            // 设置失败编码，失败信息
            numberResult = new DtoPhoneNumberResult(RESPONSE_FAIL, "获取手机号失败");
        }
        log.info("换绑手机号返回结果：" + JSON.toJSONString(numberResult));
        return numberResult;
    }

    @Override
    public List<DtoUser> getDtoUserList(List<String> phoneList, String appId) {
        return dao.getDtoUserList(phoneList, appId);
    }
}
