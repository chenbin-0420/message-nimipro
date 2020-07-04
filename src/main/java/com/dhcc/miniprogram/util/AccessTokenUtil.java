package com.dhcc.miniprogram.util;

import com.dhcc.basic.exception.BusinessException;
import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.service.MpAccessTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author cb
 * @date 2020/6/23
 * description：
 */
@Component
public class AccessTokenUtil {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private MpAccessTokenService accessTokenService;

    private static final int MAX_COUNT = 3;

    /**
     * accessToken : 访问令牌
     */
    private String accessToken;

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
                String accessTokenLasted = accessTokenService.getAccessToken(wechatConfig.getAppId(), wechatConfig.getSecret()).getAccess_token();
                // 不为 null , 返回 accessToken
                if (StringUtils.isNotEmpty(accessTokenLasted)) {
                    setAccessToken( accessTokenLasted );
                    return accessTokenLasted;
                }
            }
            throw new BusinessException("服务器异常,accessToken为空");
        }
        return accessToken;
    }

    private void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 定时器，每隔1个半小时自动刷新 accessToken
     */
    @Scheduled(fixedRate = 5400000)
    public void refreshAccessToken() {
        // 获取 accessToken
        setAccessToken( accessTokenService.getAccessToken(wechatConfig.getAppId(), wechatConfig.getSecret()).getAccess_token() );
    }
}
