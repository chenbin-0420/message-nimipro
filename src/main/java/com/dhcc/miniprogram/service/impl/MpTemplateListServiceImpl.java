package com.dhcc.miniprogram.service.impl;

import com.alibaba.fastjson.JSON;
import com.dhcc.basic.service.BaseServiceImpl;
import com.dhcc.basic.util.HttpClientUtil;
import com.dhcc.miniprogram.config.MiniproUrlConfig;
import com.dhcc.miniprogram.dao.MpTemplateListDao;
import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.dto.DtoTemplateList;
import com.dhcc.miniprogram.dto.DtoTemplateListQuery;
import com.dhcc.miniprogram.dto.DtoTemplateListResult;
import com.dhcc.miniprogram.enums.BusinessCodeEnum;
import com.dhcc.miniprogram.model.MpTemplateList;
import com.dhcc.miniprogram.service.MpTemplateListService;
import com.dhcc.miniprogram.util.AccessTokenUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
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
 * 小程序模板列表-Service实现
 * @author cb
 * @since 2020-07-03
 */
@Service
public class MpTemplateListServiceImpl extends BaseServiceImpl<MpTemplateListDao, MpTemplateList, String> implements MpTemplateListService {
	@Override
	protected void beforeSave(MpTemplateList object) {
		//新增前
	}
	@Override
	protected void afterSave(MpTemplateList object, MpTemplateList savedObject) {
		//新增后
	}
	@Override
	protected void beforeUpdate(MpTemplateList object) {
		//修改前
	}
	@Override
	protected void afterUpdate(MpTemplateList object, MpTemplateList savedObject) {
		//修改后
	}
	@Override
	protected void beforeDelete(MpTemplateList object) {
		//删除前
	}
	@Override
	protected void afterDelete(MpTemplateList object) {
		//删除后
	}

	private Logger log = LoggerFactory.getLogger(MpTemplateListServiceImpl.class);

	@Autowired
	private AccessTokenUtil accessTokenUtil;

	@Override
	public DtoBasicResult updateTemplateList() {
		// 创建 httpClient
		CloseableHttpClient httpClient = HttpClientUtil.getHttpClient();
		// 获取accessToken
		String accessToken = accessTokenUtil.getAccessToken();
		// Url带参 paramMap
		HashMap<String, String> paramMap = new HashMap<>(1);
		paramMap.put("access_token",accessToken);
		// 设置请求体 headersMap
		HashMap<String, String> headersMap = new HashMap<>(1);
		// 初始化基础结果异常
		DtoTemplateListResult templateListResult = new DtoTemplateListResult();
		// Get请求
		try {
			// 获取模板列表Url配置
			MiniproUrlConfig urlConfig = MiniproUrlConfig.GET_TEMPLATE_LIST;
			// 记录接口信息日志
			log.info("接口名称："+urlConfig.getName());
			log.info("接口Url："+urlConfig.getUrl());
			log.info("接口参数："+ JSON.toJSONString(paramMap));
			// 发送Get请求并接收响应体
			String result = HttpClientUtil.doGet(httpClient, urlConfig.getUrl(), paramMap, headersMap);
			// 记录获取模板列表日志
			log.info("获取模板列表结果："+result);
			// 解析字符串并返回模板列表结果
			templateListResult = JSON.parseObject(result, DtoTemplateListResult.class);
			// errCode 为 0，表示成功
			if(BusinessCodeEnum.RECEIVE_SUCCESS.getCode().equals(templateListResult.getErrcode())){
				// 获取 dtoTemplateLists
				List<DtoTemplateList> dtoTemplateLists = templateListResult.getData();
				// dtoTemplateLists 不为空
				if(CollectionUtils.isNotEmpty(dtoTemplateLists)){
					// 获取模板列表运输类集合迭代器
					Iterator<DtoTemplateList> iterator = dtoTemplateLists.iterator();
					// 判断迭代器是否有下一个
					while(iterator.hasNext()){
						// 获取dtoTemplateList
						DtoTemplateList dtoTemplateList = iterator.next();
						// 获取模板ID
						String priTmplId = dtoTemplateList.getPriTmplId();
						// 判断 priTmplId 不为空
						if(StringUtils.isNotEmpty(priTmplId)){
							// 根据 templateId 查询 MpTemplateList
							MpTemplateList templateList = dao.findOne("templateId = ?", new Object[]{ priTmplId });
							// 模板列表存在
							if(templateList != null){
								// 模板内容有改动
								if(!templateList.getContent().equalsIgnoreCase(dtoTemplateList.getContent())){
									// templateList设置 模板内容，模板事例并修改模板列表
									templateList.setContent(dtoTemplateList.getContent());
									templateList.setExample(dtoTemplateList.getExample());
									update(templateList);
								}
							} else {
								// 不存在模板列表，添加模板列表
								// dtoTemplateList 转 mpTemplateList
								MpTemplateList mpTemplateList = DtoTemplateList.toPO(dtoTemplateList);
								dao.save(mpTemplateList);
							}
						}
					}
				}
				// 返回基础结果
				return templateListResult.setErrcode(BusinessCodeEnum.REQUEST_SUCCESS.getCode()).setErrmsg("获取模板集合成功");
			} else {
				// 返回失败原因
				return templateListResult;
			}
		} catch (Exception e) {
			log.debug("获取模板列表异常",e);
			return templateListResult.setErrcode(BusinessCodeEnum.GET_TEMPLATE_LIST_EXCEPTION.getCode())
					.setErrmsg(BusinessCodeEnum.GET_TEMPLATE_LIST_EXCEPTION.getMsg());
		}
	}

	/**
	 * VALIDATION ：生效
	 */
	private static final int VALIDATION = 1;
	@Override
	public List<DtoTemplateListQuery> getValidateTemplateList() {
		return dao.querySqlEntity("SELECT mp.`template_id`,mp.`title`,mp.`type` FROM mp_template_list mp WHERE mp.`validation` = ?",new Object[]{ VALIDATION },DtoTemplateListQuery.class,null);
	}
}
