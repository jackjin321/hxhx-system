package me.zhengjie.modules.union.processor;

import me.zhengjie.modules.union.handler.AbstractChannelDbMatchHandler;
import me.zhengjie.modules.union.handler.AbstractProductDbMatchHandler;
import me.zhengjie.modules.union.holder.LoanChannelMatchHandlerHolder;
import me.zhengjie.modules.union.holder.LoanProductMatchHandlerHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoanProductMatchHandlerProcessor implements BeanPostProcessor {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.info("====== postProcessAfterInitialization {} ============", beanName);
        if (bean instanceof AbstractProductDbMatchHandler) {
            AbstractProductDbMatchHandler matchHandler = (AbstractProductDbMatchHandler) bean;
            LoanProductMatchHandlerHolder.register(matchHandler);
        }
        if (bean instanceof AbstractChannelDbMatchHandler) {
            AbstractChannelDbMatchHandler matchHandler = (AbstractChannelDbMatchHandler) bean;
            LoanChannelMatchHandlerHolder.register(matchHandler);
        }
        return bean;
    }

    public boolean isBeanInstanceOfAbstractClass(String beanName, Class<?> abstractClass) {
        log.warn("+++++++bean {} deal", beanName);
        Object bean = applicationContext.getBean(beanName);
        if (bean == null) {
            log.warn("bean {} not found", beanName);
            return false; // 或者抛出异常，视需求而定
        }
        return abstractClass.isAssignableFrom(bean.getClass());
    }
}
