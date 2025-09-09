package me.zhengjie.modules.union.engine;


import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.Channel;
import me.zhengjie.exception.CommentException;
import me.zhengjie.service.ChannelService;
import me.zhengjie.modules.union.handler.AbstractChannelDbMatchHandler;
import me.zhengjie.modules.union.holder.LoanChannelMatchHandlerHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class H5ChannelBizActionEngine {
    private final ChannelService channelService;

    /**
     * 执行渠道匹配逻辑
     *
     * @param request
     * @param accessCode
     * @return
     */
    public Object doAccessCheck(@RequestBody String request, String accessCode,String realIP) {
        Optional<Channel> channelOptional = channelService.findByUrlCodeOfVo(accessCode);
        if (!channelOptional.isPresent()) {
            throw new CommentException("渠道不存在");
        }
        Channel channel = channelOptional.get();
        if (CharSequenceUtil.isBlank(channel.getChannelBean())) {
            throw new CommentException("请联系我方进行处理器配置");
        }

        AbstractChannelDbMatchHandler<?> handler = LoanChannelMatchHandlerHolder.getHandler(channel.getChannelBean());
        if (Objects.isNull(handler)) {
            // 未配置联登处理器
            log.error("未配置联登处理器 {}", channel.getChannelBean());
            throw new CommentException("未配置处理器,请联系商务");
        }
        return handler.doMatch(request, channel,realIP);
    }

    /**
     * 执行渠道匹配逻辑
     *
     * @param request
     * @param accessCode
     * @return
     */
    public Object doRegister(@RequestBody String request, String accessCode) {
        Optional<Channel> channelOptional = channelService.findByUrlCodeOfVo(accessCode);
        if (!channelOptional.isPresent()) {
            throw new CommentException("渠道不存在");
        }
        Channel channel = channelOptional.get();
        if (CharSequenceUtil.isBlank(channel.getChannelBean())) {
            throw new CommentException("请联系我方进行处理器配置");
        }

        AbstractChannelDbMatchHandler<?> handler = LoanChannelMatchHandlerHolder.getHandler(channel.getChannelBean());
        if (Objects.isNull(handler)) {
            // 未配置联登处理器
            log.error("未配置联登处理器 {}", channel.getChannelBean());
            throw new CommentException("未配置处理器,请联系商务");
        }
        return handler.doRegister(request, channel);
    }
}
