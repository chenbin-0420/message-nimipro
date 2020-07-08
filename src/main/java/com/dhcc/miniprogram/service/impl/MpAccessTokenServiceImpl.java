package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.exception.BusinessException;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.MiniproUrlConfig;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dao.MpAccessTokenDao;
import com.dhcc.miniprogram.dto.DtoAccessTokenResult;
import com.dhcc.miniprogram.enums.GrantTypeEnum;
import com.dhcc.miniprogram.enums.IsDelEnum;
import com.dhcc.miniprogram.model.MpAccessToken;
import com.dhcc.miniprogram.service.MpAccessTokenService;
import com.dhcc.miniprogram.util.CheckInParamUtil;
import com.dhcc.miniprogram.util.DateUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
 * 小程序访问令牌-Service实现
 * @author cb
 * @since 2020-06-29
 */
@Service
public class MpAccessTokenServiceImpl extends BaseServiceImpl<MpAccessTokenDao, MpAccessToken, String> implements MpAccessTokenService {
	@Override
	protected void beforeSave(MpAccessToken object) {
		//新增前
	}
	@Override
	protected void afterSave(MpAccessToken object, MpAccessToken savedObject) {
		//新增后
	}
	@Override
	protected void beforeUpdate(MpAccessToken object) {
		//修改前
	}
	@Override
	protected void afterUpdate(MpAccessToken object, MpAccessToken savedObject) {
		//修改后
	}
	@Override
	protected void beforeDelete(MpAccessToken object) {
		//删除前
	}
	@Override
	protected void afterDelete(MpAccessToken object) {
		//删除后
	}

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(MpAccessTokenServiceImpl.class);

	@Autowired
	private WechatConfig wechatConfig;

	@Override
	@Transactional
	public DtoAccessTokenResult getAccessToken(String appid, String appSecret) {
		// 记录获取AccessToken入参日志
		log.info("获取AccessToken：{ appid="+appid+",secret="+appSecret+"}");
		// 检查入参
		CheckInParamUtil.checkInParam(appid,appSecret);
		// 获取 httpClient
		CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
		// 设置 Url 带参
		HashMap<String, String> paramMap = new HashMap<>(3);
		paramMap.put("grant_type", GrantTypeEnum.CLIENT_CREDENTIAL.getCode());
		paramMap.put("appid", appid);
		paramMap.put("secret", appSecret);
		// 设置请求体参数
		HashMap<String, String> headerMap = new HashMap<>(1);
		try {
			log.info("接口名称：" + MiniproUrlConfig.GET_ACCESS_TOKEN_URL.getName());
			log.info("接口参数：" + MiniproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl());
			log.info("接口参数：" + paramMap);
			// 发送Get请求，并接收字符串的结果
			String result = HttpClientUtil.doGet(httpClient, MiniproUrlConfig.GET_ACCESS_TOKEN_URL.getUrl(), paramMap, headerMap);
			// 解析字符串为 DtoAccessTokenResult 对象
			DtoAccessTokenResult dtoAccessTokenResult = JSON.parseObject(result, DtoAccessTokenResult.class);
			// 访问令牌
			MpAccessToken mpAccessToken = DtoAccessTokenResult.toPO(dtoAccessTokenResult);
			// 设置小程序ID、秘钥、创建时间、是否删除 默认是F
			mpAccessToken.setAppid(wechatConfig.getAppId());
			mpAccessToken.setSecret(wechatConfig.getSecret());
			mpAccessToken.setCreateTime(DateUtil.getCurrentDate());
			mpAccessToken.setIsDel(IsDelEnum.F.getCode());
			// 添加 mpAccessToken
			save(mpAccessToken);
			// 解析字符串并返回结果
			return dtoAccessTokenResult;
		} catch (Exception e) {
			// 记录日志和抛异常
			log.debug("获取AccessToken异常", e);
			throw new BusinessException("获取微信Token异常");
		}
	}
}
