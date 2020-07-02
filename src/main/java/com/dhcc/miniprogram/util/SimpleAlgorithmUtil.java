package com.dhcc.miniprogram.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;

/**
 * @author cb
 * @date 2020/6/19
 * description：简单算法封装类
 */
public class SimpleAlgorithmUtil {

    /**
     * 生成微信加密签名
     * @param arr 加密所需数据
     * @return 微信加密签名
     */
    public static String genSignature(String[] arr) {
        Arrays.sort(arr);

        StringBuilder encryptStr = new StringBuilder();
        for (String s : arr) {
            encryptStr.append(s);
        }
        return DigestUtils.sha1Hex(encryptStr.toString());
    }
}
