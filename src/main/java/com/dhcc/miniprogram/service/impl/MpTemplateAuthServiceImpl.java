package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.dao.query.SimpleCondition;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.dao.MpTemplateAuthDao;
import com.dhcc.miniprogram.dto.*;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
import com.dhcc.miniprogram.enums.TemplateStatusEnum;
import com.dhcc.miniprogram.model.MpTemplateAuth;
import com.dhcc.miniprogram.service.MpTemplateAuthService;
import com.dhcc.miniprogram.service.MpTemplateListService;
import com.dhcc.miniprogram.util.CheckInParamUtil;
import com.dhcc.miniprogram.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
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
 * 小程序订阅消息模板授权-Service实现
 * @author cb
 * @since 2020-07-02
 */
@Service
public class MpTemplateAuthServiceImpl extends BaseServiceImpl<MpTemplateAuthDao, MpTemplateAuth, String> implements MpTemplateAuthService {
	@Override
	protected void beforeSave(MpTemplateAuth object) {
		//新增前
	}
	@Override
	protected void afterSave(MpTemplateAuth object, MpTemplateAuth savedObject) {
		//新增后
	}
	@Override
	protected void beforeUpdate(MpTemplateAuth object) {
		//修改前
	}
	@Override
	protected void afterUpdate(MpTemplateAuth object, MpTemplateAuth savedObject) {
		//修改后
	}
	@Override
	protected void beforeDelete(MpTemplateAuth object) {
		//删除前
	}
	@Override
	protected void afterDelete(MpTemplateAuth object) {
		//删除后
	}

	private static final Logger log = LoggerFactory.getLogger(MpTemplateAuthServiceImpl.class);

	@Autowired
	private MpTemplateListService templateListService;

	@Autowired
	private WechatConfig wechatConfig;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public DtoTemplateAuthResult insertTemplateAuth(DtoUpdateTemplateAuthRequest templateAuthRequest) {
		// 打印日志
		log.info("添加模板授权参数："+ JSON.toJSONString(templateAuthRequest));
		// 声明模板授权结果
		DtoTemplateAuthResult templateAuthResult = new DtoTemplateAuthResult();
		// 检查入参
		CheckInParamUtil.checkInParam(templateAuthResult,templateAuthRequest);
		// errCode 不为空，返回模板授权结果
		if(templateAuthResult.getErrcode() != null){
			log.info("模板授权参数为空："+ JSON.toJSONString(templateAuthResult));
			return templateAuthResult;
		}
		// 获取手机号
		String phone = templateAuthRequest.getPhone();
		// 添加或修改模板授权
		for (DtoUpdateTemplateStatus updateTemplate : templateAuthRequest.getTemplates()) {
			String templateId = updateTemplate.getTemplateId();
			// 通过名称转换模板状态枚举
			TemplateStatusEnum templateStatusEnum = TemplateStatusEnum.convertTemplateStatusEnumByName(updateTemplate.getStatus());
			if(StringUtils.isNotEmpty(templateId)){
				DtoTemplateListQuery dtoTemplateListQuery = templateListService.getValidateTemplateListByTemplateId(templateId);
				if( dtoTemplateListQuery != null ){
					// 根据模板ID和手机号查询模板授权结果
					SimpleCondition sc = new SimpleCondition()
							.addParm("templateId", updateTemplate.getTemplateId())
							.addParm("phone",phone);
					MpTemplateAuth templateAuth = dao.findOne(sc);
					// 模板授权结果为空，添加模板授权
					if(templateAuth == null) {
						// 用户在订阅界面，允许接收模板消息时添加模板授权
						if(TemplateStatusEnum.ACCEPT.equals(templateStatusEnum)){
							templateAuth = DtoUpdateTemplateAuthRequest.toPO(templateAuthRequest);
							// 模板授权属性设置 模板ID、标题、描述、类型、排列顺序并添加模板授权信息
							templateAuth.setTemplateId(templateId);
							templateAuth.setTitle(dtoTemplateListQuery.getTitle());
							templateAuth.setTmplDesc(wechatConfig.getExternalDesc());
							templateAuth.setType(dtoTemplateListQuery.getType());
							templateAuth.setTmplOrder(1);
							// 设置已订阅
							templateAuth.setIsSub(TemplateStatusEnum.ACCEPT.getValue());
							// 添加模板授权
							save(templateAuth);
							// 记日志
							log.info("添加模板授权结果:"+JSON.toJSONString(templateAuth));
						}
					} else {
						// 模板授权状态与数据库中模板授权状态不一致修改模板授权状态
						if(!templateStatusEnum.getValue().equals(templateAuth.getIsSub())){
							// 根据模板状态值修改模板状态
							templateAuth.setIsSub(templateStatusEnum.getValue());
							templateAuth.setModifyTime(DateUtil.getCurrentDate());
							templateAuth.setModifyUser(templateAuthRequest.getOpenId());
							update(templateAuth);
							log.info("修改模板授权结果:"+JSON.toJSONString(templateAuth));
						}
					}
				} else {
					log.error(BusinessCodeEnum.TEMPLATE_AUTH_FAIL.getMsg());
					templateAuthResult = new DtoTemplateAuthResult(BusinessCodeEnum.TEMPLATE_AUTH_FAIL.getCode(),BusinessCodeEnum.TEMPLATE_AUTH_FAIL.getMsg());
					// 返回基础的模板授权结果
					return getBasicTemplateAuthResult(templateAuthResult);
				}
			}
		}
		// 模板授权 设置模板数组、标题、描述
		templateAuthResult = new DtoTemplateAuthResult(BusinessCodeEnum.REQUEST_SUCCESS.getCode(),"获取模板成功");
		// 返回基础的模板授权结果
		return getBasicTemplateAuthResult(templateAuthResult);
	}

	@Override
	public DtoTemplateAuthResult getTemplateAuthResult() {
		// 模板授权 设置模板数组、标题、描述
		DtoTemplateAuthResult templateAuthResult = new DtoTemplateAuthResult(BusinessCodeEnum.REQUEST_SUCCESS.getCode(),"获取模板成功");
		return getBasicTemplateAuthResult(templateAuthResult);
	}

	/**
	 * 基础的模板授权结果
	 * @return 模板授权结果
	 */
	private DtoTemplateAuthResult getBasicTemplateAuthResult(DtoTemplateAuthResult templateAuthResult){
		// 初始化DtoTemplateAuth集合
		LinkedList<DtoTemplateAuth> authLinkedList = new LinkedList<>();
		// 声明并添加模板ID集合
		LinkedList<String> tmplIdList = new LinkedList<>();
		for (DtoTemplateListQuery templateListQuery : templateListService.getValidateTemplateList(null)) {
			tmplIdList.add(templateListQuery.getTemplateId());
		}
		// 设置模板ID、标题、描述
		DtoTemplateAuth dtoTemplateAuth = new DtoTemplateAuth(wechatConfig.getExternalTitle(),wechatConfig.getExternalDesc(),tmplIdList);
		// authLinkedList 添加 dtoTemplateAuth
		authLinkedList.add(dtoTemplateAuth);
		return templateAuthResult.setData(authLinkedList);
	}

	@Override
	public DtoTemplateAuthPhoneResult getTemplateAuthByPhoneList(DtoTemplateAuthPhoneRequest templateAuthPhoneRequest) {
		// 写入参日志
		log.info("获取用户模板授权列表入参："+JSON.toJSONString(templateAuthPhoneRequest));
		DtoTemplateAuthPhoneResult templateAuthPhoneResult = new DtoTemplateAuthPhoneResult();
		// 检查入参 ,验权
		CheckInParamUtil.checkInParam(templateAuthPhoneRequest,wechatConfig,templateAuthPhoneResult);
		// errCode 不为 null, 返回带错的对象
		if(templateAuthPhoneResult.getErrcode() != null){
			return templateAuthPhoneResult;
		}
		// 初始化 templateAuthPhones 对象
		LinkedList<DtoTemplateAuthPhone> templateAuthPhones = new LinkedList<>();
		try {
			// 查数据
			for (String phone : templateAuthPhoneRequest.getPhoneNumberList()) {
				if(StringUtils.isNotEmpty(phone)){
					// 添加用户授权模板   数据格式：手机号 模板ID列表
					templateAuthPhones.add(new DtoTemplateAuthPhone(phone,dao.getTemplateAuthByPhone(phone)));
				}
			}
			// 手机号对应模板列表
			templateAuthPhoneResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode()).setErrmsg(BusinessCodeEnum.REQUEST_SUCCESS.getMsg());
			templateAuthPhoneResult.setData(templateAuthPhones);
		} catch (Exception e) {
			// 打印异常信息
			log.error(BusinessCodeEnum.USER_AUTH_PHONE_EXCEPTION.getMsg(),e);
			// 抛异常信息
			templateAuthPhoneResult.setErrcode(BusinessCodeEnum.USER_AUTH_PHONE_EXCEPTION.getCode()).setErrmsg(BusinessCodeEnum.USER_AUTH_PHONE_EXCEPTION.getMsg());
		}
		// 封装返回
		return templateAuthPhoneResult;
	}

	@Override
	public DtoBasicResult getTemplateAuthByCondition(DtoTemplateAuthPhoneCondRequest request) {
		// 记录日志
		log.info("根据手机号、模板ID获取用户模板授权："+JSON.toJSONString(request));
		DtoBasicResult dtoBasicResult = new DtoBasicResult();
		// 检查入参
		CheckInParamUtil.checkInParam(request,dtoBasicResult,wechatConfig);
		if(dtoBasicResult.getErrcode() != null){
			return dtoBasicResult;
		}
		// 获取模板ID列表
		List<DtoTemplateId> templateIdList = dao.getTemplateAuthByPhoneAndTemplateId(request.getPhone(),request.getTemplateId());
		if(CollectionUtils.isEmpty(templateIdList)){
			// 用户没有授权操作
			dtoBasicResult.setErrcode(BusinessCodeEnum.USER_AUTH_TEMPLATE_EMPTY.getCode()).setErrmsg(BusinessCodeEnum.USER_AUTH_TEMPLATE_EMPTY.getMsg());
		} else {
			// 有授权操作
			dtoBasicResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode()).setErrmsg("获取用户授权模板成功");
		}
		log.info("获取用户授权模板结果："+JSON.toJSONString(dtoBasicResult));
		return dtoBasicResult;
	}

	@Override
	public MpTemplateAuth findByPhone(String phone) {
		return dao.findOne("a.phone = ?", new Object[]{ phone });
	}

}
