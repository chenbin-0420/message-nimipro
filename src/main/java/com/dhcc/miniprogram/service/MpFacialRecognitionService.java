package com.dhcc.miniprogram.service;

import com.dhcc.miniprogram.dto.DtoFacialRecognitionResult;

public interface MpFacialRecognitionService {
    /**
     * 人脸识别
     * @param verifyResult 核验结果
     * @return DtoFacialRecognitionResult
     */
    DtoFacialRecognitionResult facialRecognition(String verifyResult);
}
