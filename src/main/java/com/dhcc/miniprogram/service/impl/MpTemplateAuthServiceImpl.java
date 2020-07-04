package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.dao.query.SimpleCondition;
import com.dhcc.miniprogram.dto.DtoTemplateAuthRequest;
import com.dhcc.miniprogram.dto.DtoTemplateAuthResult;
import com.dhcc.miniprogram.model.MpTemplateList;
import com.dhcc.miniprogram.service.MpTemplateListService;
import com.dhcc.miniprogram.util.CheckInParamUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.miniprogram.dao.MpTemplateAuthDao;
import com.dhcc.miniprogram.model.MpTemplateAuth;
import com.dhcc.miniprogram.service.MpTemplateAuthService;
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
	private MpTemplateAuthService self;

	@Autowired
	private MpTemplateListService templateListService;

	/**
	 * RESPONSE_TEMPLATE_AUTH_SUCCESS ：订阅模板授权成功
	 * RESPONSE_TEMPLATE_AUTH_REPEAT：订阅模板已授权
	 */
	private static final Integer RESPONSE_TEMPLATE_AUTH_SUCCESS = 200;
	private static final Integer RESPONSE_TEMPLATE_AUTH_REPEAT = 1041;

	@Override
	public DtoTemplateAuthResult insertTemplateAuth(DtoTemplateAuthRequest templateAuthRequest) {
		// 打印日志
		log.info("添加模板授权参数："+ JSON.toJSONString(templateAuthRequest));
		// 声明返回模板授权结果
		DtoTemplateAuthResult dtoReturnTemplateAuthResult = null;
		// 检查入参
		CheckInParamUtil.checkInParam(templateAuthRequest);
		// 获取手机号
		String phone = templateAuthRequest.getPhone();
		// 获取需订阅的模板
		String[] templateIds = templateAuthRequest.getTemplateIds();
		if(templateIds != null && templateIds.length > 0){
			for (String templateId : templateIds) {
				// 模板ID不为空，添加模板授权
				if(StringUtils.isNotEmpty(templateId)){
					// 根据模板ID和手机号查询模板授权
					SimpleCondition sc = new SimpleCondition()
							.addParm("templateId",templateId)
							.addParm("phone", phone);
					MpTemplateAuth templateAuth = self.findOne(sc);
					if(templateAuth == null){
						// 根据 templateId 条件查询对应模板列表
						SimpleCondition condition = new SimpleCondition().addParm("templateId",templateId);
						MpTemplateList templateList = templateListService.findOne(condition);
						// 模板授权属性设置 标题、描述、类型、排列顺序并添加模板授权信息
						templateAuth = DtoTemplateAuthRequest.toPO(templateAuthRequest);
						templateAuth.setTitle(templateList.getTitle());
						templateAuth.setDesc("政务服务大厅");
						templateAuth.setType(templateList.getType());
						templateAuth.setOrder(1);
						save(templateAuth);
						// 授权成功
						dtoReturnTemplateAuthResult = new DtoTemplateAuthResult(RESPONSE_TEMPLATE_AUTH_SUCCESS,"手机号为 "+phone+"，订阅模板授权成功，模板ID="+templateId);
					} else {
						// 订阅已授权
						dtoReturnTemplateAuthResult = new DtoTemplateAuthResult(RESPONSE_TEMPLATE_AUTH_REPEAT,"手机号为 "+phone+"，订阅模板已授权，模板ID="+templateId);
					}
					log.info("订阅模板授权结果："+JSON.toJSONString(dtoReturnTemplateAuthResult));
				}
			}
		}

		// 返回响应
		return dtoReturnTemplateAuthResult;
	}

	@Override
	public MpTemplateAuth findByPhone(String phone) {
		return dao.findOne("a.phone = ?", new Object[]{ phone });
	}
}
