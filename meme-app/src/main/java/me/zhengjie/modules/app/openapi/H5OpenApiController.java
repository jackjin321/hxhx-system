package me.zhengjie.modules.app.openapi;


import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.union.engine.H5ChannelBizActionEngine;
import me.zhengjie.utils.IPUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open")
@Slf4j
public class H5OpenApiController {
    private final H5ChannelBizActionEngine h5ChannelBizActionEngine;

    @Log("渠道撞库")
    @ApiOperation("渠道撞库")
    @AnonymousPostMapping("/access/check/{accessCode}")
    public ResponseEntity<Object> accessCheck(@RequestBody String requestJson,
                                              @PathVariable("accessCode") String channelAccessKey,
                                              HttpServletRequest request) {
        String realIP = IPUtils.getIpAddr(request);
        return ResponseEntity.ok(h5ChannelBizActionEngine.doAccessCheck(requestJson, channelAccessKey, realIP));
    }

    @Log("快速联登")
    @ApiOperation("快速联登")
    @AnonymousPostMapping("/access/register/{accessCode}")
    public ResponseEntity<Object> accessRegister(@RequestBody @Validated String requestJson, @PathVariable("accessCode") String accessCode) {
        return ResponseEntity.ok(h5ChannelBizActionEngine.doRegister(requestJson, accessCode));
    }
}
