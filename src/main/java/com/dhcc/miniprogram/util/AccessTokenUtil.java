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
     * 获取 accessToken 开关，默认是关着的 true：开 ，false：关
     */
    private static boolean accessTokenSwitch = false;

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
        // 获取accessToken 如果 accessTokenSwitch 为 true 则去获取 accessToken ，
        // 若未获取到 accessToken , 则尝试 3 次获取
        if (accessTokenSwitch) {
            // 同步获取accessToken
            synchronizedAccessToken();
        }
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 定时器，每隔半小时自动刷新 accessToken
     */
    @Scheduled(fixedRate = 1800000)
    public void refreshAccessToken() {
        // accessTokenSwitch 设置为true(开)
        accessTokenSwitch = true;
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
                // accessTokenSwitch 开启,获取 accessToken
                if(accessTokenSwitch){
                    // 获取accessToken
                    String accessTokenLasted = accessTokenService.getAccessToken(wechatConfig.getAppId(), wechatConfig.getSecret()).getAccess_token();
                    // 不为 null , 返回 accessToken
                    if (StringUtils.isNotEmpty(accessTokenLasted)) {
                        // 打印info日志
                        log.info(String.format("#AccessTokenUtil #synchronizedAccessToken 旧accessToken={old:%s},新accessToken={new:%s}",this.accessToken,accessTokenLasted));
                        // 设置accessToken
                        setAccessToken(accessTokenLasted);
                        // 获取 accessTokenSwitch ：关闭
                        accessTokenSwitch = false;
                        return accessTokenLasted;
                    }
                }
            }
            return this.accessToken;
        }
    }
}
