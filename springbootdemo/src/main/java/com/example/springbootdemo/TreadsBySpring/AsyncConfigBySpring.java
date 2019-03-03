package com.example.springbootdemo.TreadsBySpring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfigBySpring {

    //接收报文核心线程数
    @Value("${book.core.poolsize}")
    private int bookCorePoolSize;
    //接收报文最大线程数
    @Value("${book.max.poolsize}")
    private int bookMaxPoolSize;
    //接收报文队列容量
    @Value("${book.queue.capacity}")
    private int bookQueueCapacity;
    //接收报文线程活跃时间（秒）
    @Value("${book.keepAlive.seconds}")
    private int bookKeepAliveSeconds;
    //接收报文默认线程名称
    @Value("${book.thread.name.prefix}")
    private String bookThreadNamePrefix;

    /**
     * bookTaskExecutor:(接口的线程池). <br/>
     * @return TaskExecutor taskExecutor接口
     * @since JDK 1.8
     */
    @Bean(name="BookTask")
    public ThreadPoolTaskExecutor bookTaskExecutor() {
        //newFixedThreadPool
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(bookCorePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(bookMaxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(bookQueueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(bookKeepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix(bookThreadNamePrefix);
        // 设置拒绝策略
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
