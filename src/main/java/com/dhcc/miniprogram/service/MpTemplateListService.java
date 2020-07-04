package com.dhcc.miniprogram.service;

import com.dhcc.miniprogram.dto.DtoBasicResult;
import com.dhcc.miniprogram.model.MpTemplateList;
import com.dhcc.basic.service.BaseService;

/**
 * 小程序模板列表-Service接口
 * @author cb
 * @since 2020-07-03
 */
public interface MpTemplateListService extends BaseService<MpTemplateList, String> {

    /**
     * 更新所有的信息模板
     * @return 返回基础结果
     */
    DtoBasicResult updateTemplateList();
}
