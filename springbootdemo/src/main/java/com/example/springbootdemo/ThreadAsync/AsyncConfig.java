package com.example.springbootdemo.ThreadAsync;

import com.example.springbootdemo.TreadsBySpring.SyncBookHandler;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@EnableAsync
public class AsyncConfig {

    //接收报文核心线程数
    @Value("${config.core.poolsize}")
    private int configCorePoolSize;
    //接收报文最大线程数
    @Value("${config.max.poolsize}")
    private int configMaxPoolSize;
    //接收报文队列容量
    @Value("${config.queue.capacity}")
    private int configQueueCapacity;
    //接收报文线程活跃时间（秒）
    @Value("${config.keepAlive.seconds}")
    private int configKeepAliveSeconds;
    //接收报文默认线程名称
    @Value("${config.thread.name.prefix}")
    private String configThreadNamePrefix;
    private static final Logger LOG = LoggerFactory.getLogger(AsyncConfig.class);

    /**
     * configTaskExecutor:(接口的线程池). <br/>
     *
     * @return TaskExecutor taskExecutor接口
     * @since JDK 1.8
     */
    @Bean(name = "configTask")
    public ThreadPoolTaskExecutor configTaskExecutor() {
        //newFixedThreadPool
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(configCorePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(configMaxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(configQueueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(configKeepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix(configThreadNamePrefix);
        // 设置拒绝策略
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
//        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
//            @Override
//            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//                int activeCount = executor.getActiveCount();
//                LOG.info("执行更新订单的线程数：" + activeCount + "," + "已经超出设置最大线程数：" + executor.getMaximumPoolSize());
//                LOG.info("采用自定义固定的线程数量的策略");
//            }
//        });
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy() {
        });
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

}
