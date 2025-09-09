package me.zhengjie.modules.union.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "sr.product")
public class ProductMatchConfigProperties {

    /**
     * 熔断阈值
     */
    private Long fuseThreshold = 50L;


    /**
     * 结果缓存时间
     */
    private Long cacheTime = 30L;




    private Map<String, BaseLoanProductConfigProperties> group;


}
