package com.dhcc.miniprogram.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author cb
 * @date 2020/7/28
 * description：线程池配置类
 */
@Configuration
public class MyAsyncThreadPool implements AsyncConfigurer{

    /**
     * 创建日志
     */
    private final Logger log = LoggerFactory.getLogger(MyAsyncThreadPool.class);

    @Override
    @Bean("asyncExecutor")
    public Executor getAsyncExecutor() {
        log.info("初始化自定义线程池");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(50);
        //队列大小
        executor.setQueueCapacity(1000);
        //线程最大空闲时间
        executor.setKeepAliveSeconds(120);
        //指定用于新创建的线程名称的前缀。
        executor.setThreadNamePrefix("Async-Executor-");
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
