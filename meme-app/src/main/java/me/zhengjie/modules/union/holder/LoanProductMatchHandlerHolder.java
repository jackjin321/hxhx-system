package me.zhengjie.modules.union.holder;


import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.collect.Maps;
import me.zhengjie.modules.union.annotations.LoanProductMatchHandler;
import me.zhengjie.modules.union.handler.AbstractProductDbMatchHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class LoanProductMatchHandlerHolder {

    public static final String SUFFIX = "-matcher";

    private static final Map<String, AbstractProductDbMatchHandler<?>> HANDLER_MAP = Maps.newConcurrentMap();


    public static void register(AbstractProductDbMatchHandler<?> handler) {
        log.info("register handler : {}", handler.getClass().getSimpleName());
        LoanProductMatchHandler annotation = handler.getClass().getAnnotation(LoanProductMatchHandler.class);
        if (Objects.isNull(annotation)) {
            log.warn("{}该类未被[LoanProductMatchHandler]注解标注,无法注册", handler.getClass());
            throw new IllegalArgumentException("请标注[LoanProductMatchHandler]注解");
        }
        String value = annotation.name();
        if (!value.endsWith(SUFFIX)){
            throw new IllegalArgumentException("处理器["+ value +"]请以[-matcher]结尾");
        }
        HANDLER_MAP.put(value, handler);
        log.info("register handler : {}", HANDLER_MAP.keySet());
    }


    public static AbstractProductDbMatchHandler<?> getHandler(String code) {
        if (CharSequenceUtil.isBlank(code)) {
            return null;
        }
        String handlerCode = code + SUFFIX;
        AbstractProductDbMatchHandler<?> abstractApiProductMatchHandler = HANDLER_MAP.get(handlerCode);
        if (Objects.isNull(abstractApiProductMatchHandler)) {
            log.error("{}-无法获取其注册信息", code);
            return null;
        }
        return abstractApiProductMatchHandler;
    }

    public static boolean exists(String code) {
        if (CharSequenceUtil.isBlank(code)) {
            return false;
        }
        String handlerCode = code + SUFFIX;
        return HANDLER_MAP.containsKey(handlerCode);
    }
}
