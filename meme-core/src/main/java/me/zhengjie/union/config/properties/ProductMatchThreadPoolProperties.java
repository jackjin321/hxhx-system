package me.zhengjie.union.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 产品撞库线程池配置
 *
 * @author 产品撞库线程池配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sr.thread-pool.match")
public class ProductMatchThreadPoolProperties {
    /**
     * 核心线程池参数
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors() * 10;
    /**
     * 最大线程池参数
     */
    private int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 20;
    /**
     * 线程存活时间(ms)
     */
    private int keepAlive = 30000;
    /**
     * 任务队列容量
     */
    private int queueCapacity = 2000;


}
