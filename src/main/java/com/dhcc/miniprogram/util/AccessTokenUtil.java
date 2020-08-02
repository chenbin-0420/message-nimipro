package com.dhcc.miniprogram.util;

import com.dhcc.miniprogram.config.WechatConfig;
import com.dhcc.miniprogram.service.MpAccessTokenService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * 尝试3次获取AccessToken
     */
    private static final int MAX_COUNT = 3;

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(AccessTokenUtil.class);

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
            // 同步获取accessToken
            synchronizedAccessToken();
        }
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 定时器，每隔1个半小时自动刷新 accessToken
     */
    @Scheduled(fixedRate = 5400000)
    public void refreshAccessToken() {
        // 获取 accessToken
        setAccessToken( synchronizedAccessToken() );
    }

    /**
     * 同步获取AccessToken
     * @return accessToken
     */
    public String synchronizedAccessToken(){
        // 同步
        synchronized(this) {
            // 循环3次
            for (int i = 0; i < MAX_COUNT; i++) {
                // 获取accessToken
                String accessTokenLasted = accessTokenService.getAccessToken(wechatConfig.getAppId(), wechatConfig.getSecret()).getAccess_token();
                // 不为 null , 返回 accessToken
                if (StringUtils.isNotEmpty(accessTokenLasted)) {
                    log.info(String.format("#AccessTokenUtil #synchronizedAccessToken 旧accessToken={old:%s},新accessToken={new:%s}",this.accessToken,accessTokenLasted));
                    setAccessToken(accessTokenLasted);
                    return accessTokenLasted;
                }
            }
            // 置空
            setAccessToken(null);
            log.info(String.format("#AccessTokenUtil #synchronizedAccessToken 旧accessToken={old:%s},新accessToken={new:%s}",this.accessToken,null));
            return null;
        }
    }
}
