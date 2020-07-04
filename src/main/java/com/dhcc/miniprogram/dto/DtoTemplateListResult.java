package com.dhcc.miniprogram.dto;

import com.dhcc.miniprogram.model.MpTemplateList;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author cb
 * @date 2020/7/3
 * description：返回模板列表结果类
 */
public class DtoTemplateListResult extends DtoBasicResult {
    /**
     * 模板列表列表
     */
    private List<DtoTemplateList> data;

    public List<DtoTemplateList> getData() {
        return data;
    }

    public void setData(List<DtoTemplateList> data) {
        this.data = data;
    }

    public DtoTemplateListResult(Integer errcode, String errmsg) {
        super(errcode, errmsg);
    }

    public DtoTemplateListResult(){}

    /**
     * 模板列表结果转模板列表集合
     * @param templateListResult 模板列表结果
     * @return 模板列表集合
     */
    public static List<MpTemplateList> toPO(DtoTemplateListResult templateListResult) {
        // 创建模板列表集合
        List<MpTemplateList> templateLists = new LinkedList<>();
        // 获取运输模板列表集合
        List<DtoTemplateList> getTemplateLists = templateListResult.getData();
        // 判断模板列表是否为空
        if(CollectionUtils.isNotEmpty(getTemplateLists)){
            // 循环运输模板列表集合
            for (DtoTemplateList getTemplateList : getTemplateLists) {
                // 获取模板列表
                MpTemplateList templateList = DtoTemplateList.toPO(getTemplateList);
                // 添加模板列表到集合
                templateLists.add(templateList);
            }
        }
        // 返回模板列表
        return templateLists;
    }
}
