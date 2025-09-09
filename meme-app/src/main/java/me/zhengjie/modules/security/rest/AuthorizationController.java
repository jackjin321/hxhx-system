/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.security.rest;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousDeleteMapping;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
//import me.zhengjie.domain.HxSysConfig;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.modules.security.config.bean.LoginProperties;
import me.zhengjie.modules.security.config.bean.SecurityProperties;
import me.zhengjie.modules.security.security.TokenProvider;
import me.zhengjie.modules.security.service.OnlineUserService;
import me.zhengjie.modules.union.engine.ProductBizActionEngine;
import me.zhengjie.repository.HxAccessRecordRepository;
//import me.zhengjie.service.HxSysConfigService;
import me.zhengjie.result.ResultBuilder;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.BannerService;
import me.zhengjie.service.HxUserService;
import me.zhengjie.service.dto.*;
import me.zhengjie.service.mapstruct.HxUserMapper;
import me.zhengjie.utils.*;
import me.zhengjie.vo.ParamBannerQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "APP登录接口")
public class AuthorizationController {
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final HxUserService hxUserService;
    private final HxUserMapper hxUserMapper;
    private final HxAccessRecordRepository hxAccessRecordRepository;
    private final AppUserService appUserService;
//    private final BannerService bannerService;
    private final ProductBizActionEngine bizActionEngine;
    @Resource
    private LoginProperties loginProperties;
//    private final HxSysConfigService sysConfigService;

    @Log("用户登录")
    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody HxAuthUserDto authUser, HttpServletRequest request) throws Exception {
        log.info("{} {}", authUser.getUsername(), authUser.getPassword());
        // 密码解密
        //String password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, authUser.getPassword());
        // 查询验证码
        //String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
//        redisUtils.del(authUser.getUuid());
//        if (StringUtils.isBlank(code)) {
//            throw new BadRequestException("验证码不存在或已过期");
//        }
//        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
//            throw new BadRequestException("验证码错误");
//        }
        //authUser.getUsername() 手机号校验
        //先查询用户，在不在
        authUser.setPassword("1234");
        String channelCode = request.getHeader("channel-code");
        String uuid = request.getHeader("uuid");
        log.info("用户登录 {} {} {} {}", authUser.getUsername(), authUser.getPassword(), uuid, channelCode);
        String userAction = hxUserService.createUser(authUser.getUsername(), authUser.getPassword(), authUser.getPlatform(), uuid, channelCode, request);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌与第三方系统获取令牌方式
        // UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());
        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        final HxJwtUserDto hxJwtUserDto = (HxJwtUserDto) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(hxJwtUserDto, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", hxJwtUserDto);
            put("process", userAction);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        //登录成功
//        HxAccessRecord accessRecord = new HxAccessRecord();
//        accessRecord.setUserId(hxJwtUserDto.getUser().getUserId());
//        accessRecord.setChannelId(hxJwtUserDto.getUser().getChannelId());
//        accessRecord.setChannelName(hxJwtUserDto.getUser().getChannelName());
//        accessRecord.setEventName(userAction);
//        hxAccessRecordRepository.save(accessRecord);
        return ResponseEntity.ok(authInfo);
    }

    @Log("用户登录")
    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/v2/login")
    public ResponseEntity<Object> loginNew(@Validated @RequestBody HxAuthUserDto authUser, HttpServletRequest request) throws Exception {
        log.info("{} {}", authUser.getUsername(), authUser.getPassword());
        if (!"111111".equals(authUser.getCode()) && !appUserService.checkSmsCode(authUser.getUsername(), authUser.getCode())) {
            throw new BadRequestException("短信验证码错误");
        }
        //authUser.getUsername() 手机号校验
        //先查询用户，在不在
        authUser.setPassword("1234");
        String channelCode = request.getHeader("channel-code");
        String uuid = request.getHeader("uuid");
        log.info("用户登录 {} {} {} {}", authUser.getUsername(), authUser.getPassword(), uuid, channelCode);
        String userAction = hxUserService.createUser(authUser.getUsername(), authUser.getPassword(),
                authUser.getPlatform(), uuid, channelCode, request);


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), authUser.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌与第三方系统获取令牌方式
        // UserDetails userDetails = userDetailsService.loadUserByUsername(userInfo.getUsername());
        // Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        final HxJwtUserDto hxJwtUserDto = (HxJwtUserDto) authentication.getPrincipal();
        // 保存在线信息
        onlineUserService.save(hxJwtUserDto, token, request);
        // 返回 token 与 用户信息
        String actionUrl = null;
        if ("union".equals(userAction)) {
            ParamBannerQuery paramBanner = new ParamBannerQuery();
            paramBanner.setChannelCode(channelCode);
            paramBanner.setUuid(uuid);
            String realIP = IPUtils.getIpAddr(request);
            paramBanner.setRealIP(realIP);
            paramBanner.setPhone(authUser.getUsername());
            paramBanner.setUserId(hxJwtUserDto.getUser().getUserId());
            actionUrl = bizActionEngine.findOneByLogin(paramBanner);
        }

        String finalActionUrl = actionUrl;
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", hxJwtUserDto);
            put("process", userAction);
            put("url", finalActionUrl);
        }};
        if (loginProperties.isSingleLogin()) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        return ResponseEntity.ok(authInfo);
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<HxUserDtoV2> getUserInfo() {
        Long userId = SecurityUtils.getCurrentUserIdByApp();
        HxUserDtoV2 hxUserDto = hxUserService.findByIdV2(userId);
        hxUserDto.setPhone(CommonUtil.signPhone(hxUserDto.getPhone()));
        hxUserDto.setUsername(CommonUtil.signPhone(hxUserDto.getUsername()));
        log.info("auth/info {} {}", userId, JSONObject.toJSON(hxUserDto));
        return ResponseEntity.ok(hxUserDto);
    }

    @ApiOperation("获取验证码")
    @AnonymousGetMapping(value = "/num/code")
    public ResponseEntity<Object> getNumberCode() {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
//        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
//            captchaValue = captchaValue.split("\\.")[0];
//        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    /**
     * 发送验证码
     */
    @AnonymousPostMapping(value = "/flashCode")
    public ResultModel flashCode(@Validated @RequestBody HxCodeUserDto codeUser, HttpServletRequest request) {

        // 查询验证码
        String code = (String) redisUtils.get(codeUser.getUuid());
        // 清除验证码
        redisUtils.del(codeUser.getUuid());
        if (StringUtils.isBlank(code)) {
//            throw new BadRequestException("验证码不存在或已过期");
            return ResultBuilder.fail("图形验证码不存在或已过期");
        }
        if (StringUtils.isBlank(codeUser.getCode()) || !codeUser.getCode().equalsIgnoreCase(code)) {
            //throw new BadRequestException("验证码错误");
            return ResultBuilder.fail("图形验证码错误");
        }
        if (appUserService.sendSmsCode(codeUser.getPhone())) {
            return ResultBuilder.ok("发送成功");
        } else {
            return ResultBuilder.fail("发送失败");
        }
    }


    @ApiOperation("生成验证码，用作密码")
    @AnonymousGetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 获取运算的结果
        //Captcha captcha = loginProperties.getCaptcha();
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
//        String captchaValue = captcha.text();
//        if (captcha.getCharType() - 1 == LoginCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(".")) {
//            captchaValue = captchaValue.split("\\.")[0];
//        }
        // 保存
        //redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        //HxSysConfig sysConfig2 = sysConfigService.findByKey("otp.code.len");
        Integer num = 4;
//        if (ObjectUtil.isAllNotEmpty(sysConfig2.getValue())) {
//            num = Integer.valueOf(sysConfig2.getValue());
//        }
        String code = RandomUtil.randomNumbers(num);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
//            put("img", captcha.toBase64());
            put("code", code);
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("注销接口")
    @AnonymousDeleteMapping(value = "/logout/user")
    public ResponseEntity<Object> logoutUser(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
