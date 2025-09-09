package me.zhengjie.modules.union.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.zhengjie.modules.union.config.properties.ProductMatchThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class BatchProductMatchThreadPoolConfig {

    @Bean(name = "loanProductMatchExecutor")
    public ThreadPoolExecutor productMatchExecutor(ProductMatchThreadPoolProperties properties) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("pmt-%d").build();
        log.info("loanProductMatchExecutor 开始执行");
        return new ThreadPoolExecutor(properties.getCorePoolSize(),
                properties.getMaximumPoolSize(),
                properties.getKeepAlive(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                namedThreadFactory);
    }


}
