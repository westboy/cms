package com.zhiliao.component.spring;


import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class TaskExecutorConfiguration  implements AsyncConfigurer{



    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadTask = new ThreadPoolTaskExecutor();
        threadTask.setCorePoolSize(5);
        threadTask.setCorePoolSize(10);
        threadTask.setQueueCapacity(25);
        threadTask.initialize();
        return threadTask;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

}
