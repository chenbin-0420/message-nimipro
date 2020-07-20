package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dhcc.basic.dao.query.SimpleCondition;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.MiniproUrlConfig;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dao.MpUserDao;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
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
     * 手机号：PURE_PHONE_NUMBER
     * 手机区号：COUNTRY_CODE
     */
    private static final String PURE_PHONE_NUMBER = "purePhoneNumber";
    private static final String COUNTRY_CODE = "countryCode";

    @Autowired
    private WechatConfig wechatConfig;

    @Override
    @Transactional
    public DtoIdenInfoResult userLogin(DtoLoginRequest login) {
        // 记录登录日志
        log.info("小程序登录入参：" + JSON.toJSONString(login));
        // 初始化 idenInfoResult 对象
        DtoIdenInfoResult idenInfoResult = new DtoIdenInfoResult();
        // 检查入参
        CheckInParamUtil.checkInParam(idenInfoResult,login);
        // errCode不为空，抛异常
        if(idenInfoResult.getErrcode ()!= null){
            return idenInfoResult;
        }
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
        try {
            log.info("接口名称：" + MiniproUrlConfig.GET_CODE2SESSION_URL.getName());
            log.info("接口参数：" + MiniproUrlConfig.GET_CODE2SESSION_URL.getUrl());
            log.info("接口参数：" + paramMap);
            // 发送Get请求，并接收字符串的结果
            String result = HttpClientUtil.doGet(httpClient, MiniproUrlConfig.GET_CODE2SESSION_URL.getUrl(), paramMap, headerMap);
            // 字符串转化为唯一信息类
            DtoIdenInfo idenInfo = JSON.parseObject(result, DtoIdenInfo.class);
            // 获取对象成功
            if(BusinessCodeEnum.RECEIVE_SUCCESS.getCode().equals(idenInfo.getErrcode())){
                // 根据openId查询用户是否存在
                SimpleCondition sc = new SimpleCondition().addParm("openId", idenInfo.getOpenid());
                MpUser user = dao.findOne(sc);
                // 不存在添加用户
                if (user == null) {
                    // 1.添加用户信息,获取用户信息、设置 appId 并保存mpUser
                    MpUser mpUser = DtoIdenInfo.toPO(idenInfo);
                    mpUser.setAppId(appId);
                    save(mpUser);
                    // 2.返回idenInfoResult对象 标识成功，前端判断是否需要
                    idenInfoResult = new DtoIdenInfoResult(BusinessCodeEnum.LOGIN_PHONE_NOT_EXISTS.getCode(), BusinessCodeEnum.LOGIN_PHONE_NOT_EXISTS.getMsg())
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
                    idenInfoResult = new DtoIdenInfoResult(mark ? BusinessCodeEnum.LOGIN_PHONE_NOT_EXISTS.getCode() : BusinessCodeEnum.LOGIN_PHONE_EXISTS.getCode(),
                            mark ? BusinessCodeEnum.LOGIN_PHONE_NOT_EXISTS.getMsg() : user.getPhoneNum()).setData(DtoIdenInfoFilter.toFilter(idenInfo).setSession_key(null));
                }
            } else{
                idenInfoResult.setErrcode(idenInfo.getErrcode()).setErrmsg(idenInfo.getErrmsg());
            }
            // 记录日志
            log.info("登录日志："+JSON.toJSONString(idenInfoResult));
            // 解析字符串并返回结果
            return idenInfoResult;
        } catch (Exception e) {
            // 记录日志和抛异常
            log.error(BusinessCodeEnum.LOGIN_SERVER_EXCEPTION.getMsg(), e);
            return new DtoIdenInfoResult(BusinessCodeEnum.LOGIN_SERVER_EXCEPTION.getCode(), BusinessCodeEnum.LOGIN_SERVER_EXCEPTION.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DtoPhoneNumberResult getPhoneNumber(DtoPhoneNumberRequest phoneNumberRequest) {
        // 记录入参日志
        log.info("获取手机号入参：" + JSON.toJSONString(phoneNumberRequest));
        // 初始化 phoneNumberResult
        DtoPhoneNumberResult phoneNumberResult = new DtoPhoneNumberResult();
        // 检查获取手机号入参
        CheckInParamUtil.checkInParam(phoneNumberResult,phoneNumberRequest);
        // 参数为空，返回phoneNumberResult
        if(phoneNumberResult.getErrcode() != null){
            return phoneNumberResult;
        }
        // 根据 openId 查询 MpUser
        MpUser user = dao.findOne("a.openId = ?", new Object[]{ phoneNumberRequest.getOpenId() });
        // 检查 sessionKey 为空
        if (user == null || StringUtils.isEmpty(user.getSessionKey())) {
            // 设置 errcode、errmsg 并返回 phoneNumberResult
            phoneNumberResult.setErrcode(BusinessCodeEnum.GET_PHONE_NUMBER_SESSION_KEY_EMPTY.getCode()).setErrmsg(BusinessCodeEnum.GET_PHONE_NUMBER_SESSION_KEY_EMPTY.getMsg());
            return phoneNumberResult;
        }
        // 使用 AES/CBC/PKCS7Padding 解密
        String decrypt;
        try {
            // 获取 sessionKey
            String sessionKey = user.getSessionKey();
            log.info("接口名称：获取手机号");
            log.info("接口参数：" + JSON.toJSONString(new String[]{sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData()}));
            decrypt = AESUtil.pkcs7PaddingDecrypt(sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchProviderException e) {
            // 记录异常
            log.error(BusinessCodeEnum.GET_PHONE_NUMBER_EXCEPTION.getMsg(), e);
            // 设置 errcode、errmsg 并返回 phoneNumberResult
            phoneNumberResult.setErrcode(BusinessCodeEnum.GET_PHONE_NUMBER_EXCEPTION.getCode()).setErrmsg(BusinessCodeEnum.GET_PHONE_NUMBER_EXCEPTION.getMsg());
            return phoneNumberResult;
        }
        // 手机号解析结果
        log.info("获取手机号解析结果：" + decrypt);
        // 解析字符串
        JSONObject jsonObject = JSON.parseObject(decrypt);
        // PURE_PHONE_NUMBER : 手机号 ，COUNTRY_CODE ：手机区号
        // 获取手机号
        String phoneNumber = jsonObject.getString(PURE_PHONE_NUMBER);
        if (StringUtils.isNotEmpty(phoneNumber)) {
            // 获取用户ID，用户ID不为空则删除
            DtoUserId dtoUserId = dao.getUserIdByPhone(phoneNumber);
            // 不是同一个用户，删除前面绑定手机号的用户
            if(!user.getId().equals(dtoUserId.getId())){
                dao.deleteById(dtoUserId.getId());
            }
            // 用户绑定手机号、手机区号、修改时间并修改用户信息
            user.setPhoneNum(phoneNumber);
            user.setPhonePrefix(jsonObject.getString(COUNTRY_CODE));
            user.setModifyTime(DateUtil.getCurrentDate());
            update(user);
            // 设置成功编码
            phoneNumberResult = new DtoPhoneNumberResult(BusinessCodeEnum.REQUEST_SUCCESS.getCode(), "").setData(new DtoPhoneNumber(phoneNumber));
        } else {
            // 设置失败编码，失败信息
            phoneNumberResult = new DtoPhoneNumberResult(BusinessCodeEnum.GET_PHONE_NUMBER_FAIL.getCode(), "获取手机号失败");
        }
        log.info("获取手机号返回结果：" + JSON.toJSONString(phoneNumberResult));
        return phoneNumberResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DtoPhoneNumberResult changePhone(DtoPhoneNumberRequest phoneNumberRequest) {
        // 记录入参日志
        log.info("换绑手机号入参：" + JSON.toJSONString(phoneNumberRequest));
        // 响应类
        DtoPhoneNumberResult phoneNumberResult = new DtoPhoneNumberResult();
        // 检查获取手机号入参
        CheckInParamUtil.checkInParam(phoneNumberResult,phoneNumberRequest);
        // errCode不为空，返回phoneNumberResult
        if(phoneNumberResult.getErrcode() != null){
            return phoneNumberResult;
        }

        // 根据 openId 查询 MpUser
        MpUser user = dao.findOne("a.openId = ?", new Object[]{phoneNumberRequest.getOpenId()});
        // 检查 sessionKey 为空
        if (user == null || StringUtils.isEmpty(user.getSessionKey())) {
            // 返回换绑手机号为空
            phoneNumberResult.setErrcode(BusinessCodeEnum.CHANGE_PHONE_SESSION_KEY_EMPTY.getCode()).setErrmsg(BusinessCodeEnum.CHANGE_PHONE_SESSION_KEY_EMPTY.getMsg());
            return phoneNumberResult;
        }
        // 使用 AES/CBC/PKCS7Padding 解密
        String decrypt;
        try {
            String sessionKey = user.getSessionKey();
            log.info("接口名称：换绑手机号");
            log.info("接口参数：" + JSON.toJSONString(new String[]{sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData()}));
            decrypt = AESUtil.pkcs7PaddingDecrypt(sessionKey, phoneNumberRequest.getIv(), phoneNumberRequest.getEncryptedData());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException |
                BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException | NoSuchProviderException e) {
            log.error("换绑手机号异常", e);
            // 设置换绑手机号异常
            phoneNumberResult.setErrcode(BusinessCodeEnum.CHANGE_PHONE_EXCEPTION.getCode()).setErrmsg(BusinessCodeEnum.CHANGE_PHONE_EXCEPTION.getMsg());
            return phoneNumberResult;
        }
        // 手机号解析结果
        log.info("换绑手机号解析结果：" + decrypt);
        // 解析字符串
        JSONObject jsonObject = JSON.parseObject(decrypt);
        // PURE_PHONE_NUMBER : 手机号 ，COUNTRY_CODE ：手机区号
        // 获取手机号
        String phoneNumber = jsonObject.getString(PURE_PHONE_NUMBER);
        if (StringUtils.isNotEmpty(phoneNumber)) {
            // 获取用户ID，用户ID不为空则删除
            DtoUserId dtoUserId = dao.getUserIdByPhone(phoneNumber);
            // 用户存在并且修改的手机号不为同一用户，则删除已绑定用户
            if(!user.getId().equals(dtoUserId.getId())){
                dao.deleteById(dtoUserId.getId());
            }
            // 手机号不同
            if(!phoneNumber.equals(user.getPhoneNum())){
                // 1、用户绑定手机号、手机区号、修改时间并修改用户信息
                user.setPhoneNum(phoneNumber);
                user.setPhonePrefix(jsonObject.getString(COUNTRY_CODE));
                user.setModifyTime(DateUtil.getCurrentDate());
                update(user);
                ///2、换绑手机号
                phoneNumberResult = new DtoPhoneNumberResult(BusinessCodeEnum.REQUEST_SUCCESS.getCode(), "换绑手机号成功").setData(new DtoPhoneNumber(phoneNumber));
            } else {
                // 存在手机号
                phoneNumberResult = new DtoPhoneNumberResult(BusinessCodeEnum.CHANGE_PHONE_NUMBER_SAME.getCode(),
                        BusinessCodeEnum.CHANGE_PHONE_NUMBER_SAME.getMsg()).setData(new DtoPhoneNumber(phoneNumber));
            }
        } else {
            // 设置失败编码，失败信息
            phoneNumberResult = new DtoPhoneNumberResult(BusinessCodeEnum.CHANGE_PHONE_FAIL.getCode(), BusinessCodeEnum.CHANGE_PHONE_FAIL.getMsg());
        }
        log.info("换绑手机号返回结果：" + JSON.toJSONString(phoneNumberResult));
        return phoneNumberResult;
    }

    @Override
    public List<DtoUser> getDtoUserList(List<String> phoneList) {
        return dao.getDtoUserList(phoneList);
    }

    @Override
    public Object getOpenIdByPhone(String phone) {
        return dao.getOpenIdByPhone(phone);
    }
}
