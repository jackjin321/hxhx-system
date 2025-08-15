package me.zhengjie.union.config;


import lombok.Data;
import me.zhengjie.union.config.properties.BaseLoanProductConfigProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author Eathon
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "loan.product")
public class ProductLoanMatchConfigProperties {
    /**
     * keu 为注解中的confKey
     */
    private Map<String, BaseLoanProductConfigProperties> config;
}
