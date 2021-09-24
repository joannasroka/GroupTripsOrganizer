package com.sroka.grouptripsorganizer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableRetry
public class MailConfig {

    public static final String MAIL_THREAD_POOL_TASK_EXECUTOR_NAME = "mailThreadPoolTaskExecutor";

    @Bean(name = MAIL_THREAD_POOL_TASK_EXECUTOR_NAME)
    public Executor mailThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("AsyncMailExecutor-");
        executor.initialize();
        return threadPoolTaskExecutor;
    }
}
