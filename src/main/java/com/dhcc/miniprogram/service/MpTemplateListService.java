package com.dhcc.miniprogram.service;

import com.dhcc.basic.service.BaseService;
import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.dto.DtoTemplateListQuery;
import com.dhcc.miniprogram.model.MpTemplateList;

import java.util.List;

/**
 * 小程序模板列表-Service接口
 * @author cb
 * @since 2020-07-03
 */
public interface MpTemplateListService extends BaseService<MpTemplateList, String> {

    /**
     * 更新模板列表
     * @return 返回基础结果
     */
    DtoBasicResult updateTemplateList();

    /**
     * 查询有效的模板列表对象
     * @return 模板列表
     */
    List<DtoTemplateListQuery> getValidateTemplateList();
}
