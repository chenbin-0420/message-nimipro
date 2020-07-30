package com.dhcc;

import com.dhcc.miniprogram.config.WechatConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;

/**
 * @author cb
 * @date 2020/7/27
 * 小程序消息启动类
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({ WechatConfig.class })
public class MessageMiniproApplication {

    private static final Logger log = LoggerFactory.getLogger(MessageMiniproApplication.class);

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MessageMiniproApplication.class, args);
        Environment environment = applicationContext.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("server.port");
        String path = environment.getProperty("server.context-path");
        path = path == null ? "" : path;
        log.info("\n--------------------------------------------------------------\n\t" +
                "Application DhccBootApplication is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "code-create: \thttp://" + ip + ":" + port + path + "/gencode.html\n\t" +
                "线程数："+ Runtime.getRuntime().availableProcessors()+"\n\t"+
                "--------------------------------------------------------------");
    }

}
