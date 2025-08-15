package me.zhengjie.union;

import me.zhengjie.union.config.ProductLoanMatchConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "me.zhengjie.union")
@EnableConfigurationProperties(ProductLoanMatchConfigProperties.class)
public class DomainMatchAutoConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        System.out.println("============datasource starter mkt =============");
    }
}
