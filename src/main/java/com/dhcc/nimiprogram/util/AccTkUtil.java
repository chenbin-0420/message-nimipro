package com.dhcc.nimiprogram.util;

import com.dhcc.nimiprogram.config.WechatConfig;
import com.dhcc.nimiprogram.service.NpMessageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author cb
 * @date 2020/6/23
 * description：
 */
@Component
public class AccTkUtil {

    @Autowired
    private WechatConfig wechatConfig;

    @Lazy
    @Autowired
    private NpMessageService npMessageService;

    private static final int MAX_COUNT = 3;

    /**
     * accessToken : 访问令牌
     */
    private static String accessToken = null;

    /**
     * accessToken
     *
     * @return accessToken
     */
    public String getAccessToken() {
        // 获取accessToken 如果 accessToken 为 null 则去获取 accessToken ，
        // 若 accessToken 总是为 null , 则尝试 3 次获取
        if (StringUtils.isEmpty(accessToken)) {
            // 循环3次
            for (int i = 0; i < MAX_COUNT; i++) {
                // 获取accessToken
                String accessTokenLasted = npMessageService.getAccessToken(wechatConfig.getAppId(), wechatConfig.getSecret()).getAccess_token();
                // 不为 null , 返回 accessToken
                if (StringUtils.isNotEmpty(accessTokenLasted)) {
                    setAccessToken( accessTokenLasted );
                    return accessTokenLasted;
                }
            }
        }
        return accessToken;
    }

    private static void setAccessToken(String accessToken) {
        AccTkUtil.accessToken = accessToken;
    }

    /**
     * 定时器，每隔1个半小时自动刷新 accessToken
     */
    @Scheduled(fixedRate = 5400000)
    public void refreshAccessToken() {
        // 获取 accessToken
        setAccessToken( npMessageService.getAccessToken(wechatConfig.getAppId(), wechatConfig.getSecret()).getAccess_token() );
    }
}
