package me.zhengjie.modules.union.config;


import lombok.Data;
import me.zhengjie.modules.union.config.properties.BaseLoanChannelConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Eathon
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "loan.channel")
public class ChannelLoanMatchConfigProperties {
    /**
     * keu 为注解中的confKey
     */
    private Map<String, BaseLoanChannelConfigProperties> config;
}
