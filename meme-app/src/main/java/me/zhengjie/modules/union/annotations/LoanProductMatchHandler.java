package me.zhengjie.modules.union.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface LoanProductMatchHandler {

    /**
     * 处理器标识
     *
     * @return
     */
    String name();

    /**
     * 配置标识
     *
     * @return
     */
    String confKey() default "";

}
