package me.zhengjie.modules.union.handler;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.constants.RedisKeyPattern;
import me.zhengjie.domain.*;
import me.zhengjie.enums.H5CooperationModeEnum;
import me.zhengjie.enums.ResultStatusEnum;
import me.zhengjie.exception.CommentException;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.modules.union.dto.H5ChannelUnionLoginResultDTO;
import me.zhengjie.repository.ChannelMatchRecordRepository;
import me.zhengjie.repository.ChannelRegisterRecordRepository;
import me.zhengjie.service.ChannelService;
import me.zhengjie.modules.union.annotations.LoanChannelMatchHandler;
import me.zhengjie.modules.union.config.ChannelLoanMatchConfigProperties;
import me.zhengjie.modules.union.config.properties.BaseLoanChannelConfigProperties;
import me.zhengjie.modules.union.dto.ChannelMatchStrategyR;
import me.zhengjie.modules.union.dto.H5ChannelMatchR;
import me.zhengjie.modules.union.dto.H5ChannelRegisterR;
import me.zhengjie.modules.union.vo.H5ChannelMatchAccessRequest;
import me.zhengjie.modules.union.vo.H5ChannelRegisterBaseRequest;
import me.zhengjie.utils.MD5Util;
import me.zhengjie.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 渠道撞库处理器
 *
 * @author Eathon
 */
@Slf4j
public abstract class AbstractChannelDbMatchHandler<R> {

    //
    @Resource
    protected ChannelMatchRecordRepository channelMatchRecordRepository;
    @Resource
    protected ChannelRegisterRecordRepository channelRegisterRecordRepository;
    //
    @Resource
    private ChannelLoanMatchConfigProperties channelLoanMatchConfigProperties;

    @Resource
    private ChannelService channelService;
    @Resource
    private RedisUtils redisClient;

    @Resource
    private AppUserService appUserService;

//    protected ChannelAccessibleStrategy selectStrategy(Map<String, Object> metaParamMap) {
//        return SpringUtil.getBean(H5ChannelMatchStrategyEnum.getDefaultStrategy());
//    }

    /**
     * 对参数进行解密或其他处理,如无需处理,直接返回入参即可
     *
     * @param requestParamJson 请求参数json
     */
    protected abstract String resolveRequestParamJson(String requestParamJson);

    /**
     * 外部请求转换本地json对象
     *
     * @param requestJson 请求json体
     * @return {@link H5ChannelMatchAccessRequest} 请求对象
     */
    protected abstract H5ChannelRegisterBaseRequest convertRequest(JSONObject requestJson, Channel channelConf);

    /**
     * 解析响应
     *
     * @param registerUrl 请求参数
     * @param requestObj
     * @return 响应结果
     */
    protected abstract R onSuccess(String registerUrl, H5ChannelRegisterBaseRequest requestObj);

    /**
     * 失败
     *
     * @param msg
     * @param requestOptional
     * @return
     */
    protected abstract R onFail(String msg, Optional<H5ChannelRegisterBaseRequest> requestOptional);

    /**
     * 外部请求转换本地json对象
     *
     * @param requestParamJson 请求参数json
     * @param metaParamMap
     */
    protected abstract String decryptRequestParamJson(String requestParamJson, Map<String, Object> metaParamMap);

    /**
     * 外部请求转换本地json对象
     *
     * @param jsonObj@return {@link H5ChannelMatchAccessRequest} 请求对象
     */
    protected abstract H5ChannelMatchAccessRequest convertRequest(JSONObject jsonObj, Map<String, Object> metaParamMap);

    /**
     * 撞库前置个性化逻辑
     *
     * @param request      请求参数
     * @param channel      渠道id
     * @param metaParamMap
     * @return {@link R} 响应结果
     */
    protected abstract H5ChannelMatchR preProgress(H5ChannelMatchAccessRequest request, Channel channel, Map<String, Object> metaParamMap);

    /**
     * 撞库成功
     *
     * @return {@link R}
     */
    protected abstract R onSuccess(H5ChannelMatchAccessRequest request, H5ChannelMatchR resp, Channel channelConf, Map<String, Object> metaParamMap);

    /**
     * 撞库失败出发
     *
     * @param msg 失败原因
     * @return {@link R} 结果
     */
    protected abstract R onFail(String msg);


    protected BaseLoanChannelConfigProperties getChannelConfig() {
        LoanChannelMatchHandler annotation = this.getClass().getAnnotation(LoanChannelMatchHandler.class);
        if (CharSequenceUtil.isBlank(annotation.confKey())) {
            throw new CommentException(annotation.name() + "渠道未正确配置");
        }
        String configKey = annotation.confKey();
        Map<String, BaseLoanChannelConfigProperties> config = channelLoanMatchConfigProperties.getConfig();
        BaseLoanChannelConfigProperties baseChannelConfigProperties = config.get(configKey);
        if (Objects.isNull(baseChannelConfigProperties)) {
            throw new CommentException(annotation.name() + "未查询到具体配置");
        }
        return baseChannelConfigProperties;
    }


    public String buildTicket(String phoneMd5, Channel channelConf) {
        return Base64.encode(phoneMd5) + channelConf.getId();
    }

    /**
     * 执行联登
     *
     * @param requestJson 请求参数json
     * @param channelConf 渠道配置
     * @return {@link R}
     */
    public R doRegister(String requestJson, Channel channelConf) {
        Optional<H5ChannelRegisterBaseRequest> requestOpt = resolveRequestParamRegister(requestJson, channelConf);
        if (!requestOpt.isPresent()) {
            return onFail("请求参数解析失败", requestOpt);
        }
        H5ChannelRegisterBaseRequest requestObj = requestOpt.get();
        H5ChannelRegisterR h5ChannelRegisterR = doRegisterAction(channelConf, requestObj);

        log.info("渠道执行联登操作,联登用户MD5:{},联登渠道:{},联登结果:{}", channelConf.getId(), MD5Util.md5Hex(requestObj.getPhone()), cn.hutool.json.JSONUtil.toJsonStr(h5ChannelRegisterR));
        // 保存进件快照
        this.saveChannelRegisterRecord(() -> {
            ChannelRegisterRecord channelRegisterRecord = new ChannelRegisterRecord();
//            channelRegisterRecord.setCity(request.getCity());
//            channelRegisterRecord.setAccessIp(realIP);
            channelRegisterRecord.setResult(h5ChannelRegisterR.getReason());
            channelRegisterRecord.setStatus(h5ChannelRegisterR.getStatus().getCode());
            channelRegisterRecord.setChannelId(channelConf.getId());
            channelRegisterRecord.setPhone(requestObj.getPhone());
            return channelRegisterRecord;
        });
        // this.saveMktUnionRegisterSnapshot(channelConf,h5ChannelRegisterR);
        if (!h5ChannelRegisterR.isSuccess()) {
            return onFail(h5ChannelRegisterR.getReason(), requestOpt);
        }
        return onSuccess(h5ChannelRegisterR.getRedirectUrl(), requestObj);
    }

    /**
     * 解析请求参数
     *
     * @param requestJson
     * @param channelConf
     * @return
     */
    private Optional<H5ChannelRegisterBaseRequest> resolveRequestParamRegister(String requestJson, Channel channelConf) {
        try {
            requestJson = this.resolveRequestParamJson(requestJson);
            if (CharSequenceUtil.isBlank(requestJson) || !JSONUtil.isJson(requestJson)) {
                log.warn("[H5渠道进件]:{}参数状态有误,不是一个json", channelConf.getId());
                return Optional.empty();
            }
            log.info("[H5渠道进件]-H5渠道请求参数:{}", requestJson);
            JSONObject jsonObj = new JSONObject(requestJson);
            H5ChannelRegisterBaseRequest requestObj = this.convertRequest(jsonObj, channelConf);
            if (Objects.isNull(requestObj)) {
                log.warn("[H5渠道进件]:对象转换失败,渠道ID:{}", channelConf.getId());
                return Optional.empty();
            }
            if (CharSequenceUtil.isBlank(requestObj.getPhone())) {
                log.warn("[H5渠道进件]:手机号为空,渠道ID:{}", channelConf.getId());
                return Optional.empty();
            }
            if (!PhoneUtil.isMobile(requestObj.getPhone())) {
                log.warn("[H5渠道进件]:手机号格式不正确,渠道ID:{}", channelConf.getId());
                return Optional.empty();
            }
            return Optional.of(requestObj);
        } catch (Exception e) {
            log.error("[H5渠道进件]:请求参数解析异常");
        }
        return Optional.empty();
    }

    public R doMatch(String requestJson, Channel channelConf, String realIP) {

        log.info("[H5渠道撞库过程]渠道请求参数,渠道ID:{}>>{}", channelConf.getId(), requestJson);
        Map<String, Object> metaParamMap = new HashMap<>();
        if (!H5CooperationModeEnum.needMatch(channelConf.getChannelType())) {
            return this.onFail("当前渠道不支持此操作!");
        }
        // 解析请求参数
        Optional<H5ChannelMatchAccessRequest> requestOpt = resolveRequestParam(requestJson, channelConf, metaParamMap);
        if (!requestOpt.isPresent()) {
            return this.onFail("请求参数解析异常");
        }
        H5ChannelMatchAccessRequest request = requestOpt.get();
        // 幂等检查
//        String checkKey = RedisKeyPattern.LOAN_H5_CHANNEL_MATCH_ACTION_CHECK.formatted(channelConf.getId(), request.getPhoneMd5());
//        if (!redisClient.setIfAbsent(checkKey, "M", 3L, TimeUnit.SECONDS)) {
//            return this.onFail("超频调用!");
//        }

        //H5营销渠道撞库埋点
        log.info("H5营销渠道撞库埋点");
        Long channelId = channelConf.getId();
        String phoneMd5 = request.getPhoneMd5();
        // 解析请求参数json, 应对渠道方各种加密策略
        H5ChannelMatchR<?> matchAccessRespVO = doMatchAction(request, channelConf, metaParamMap);

        if (matchAccessRespVO.isAccess()) {
            //H5营销渠道撞库成功埋点
            //asyncEventPublisher.publishEvent(build(BIZ_H5_IMPACT_SUCCESS_USER_UV, phoneMd5, channelId));
            //asyncEventPublisher.publishEvent(LoanUserEventBuriedSnapshot.build(LoanUserBizBuriedEnum.BIZ_H5_IMPACT_SUCCESS_USER_UV, null, phoneMd5));
            // 撞库联登渠道成本埋点
            //asyncEventPublisher.publishEvent(new LoanRevenueEventBuriedSnapshot().setPoint(LoanRevenueBizBuriedEnum.BIZ_00001).setChannelId(channelId).setPhoneMd5(phoneMd5));
        }
        // 存储撞库结果
        this.saveH5ChannelMatchResult(matchAccessRespVO, request, channelConf);

        // 保存渠道撞库记录
        this.saveChannelFilterRecord(() -> {
            ChannelMatchRecord channelMatchRecord = new ChannelMatchRecord();
            channelMatchRecord.setCity(request.getCity());
            channelMatchRecord.setAccessIp(realIP);
            if (matchAccessRespVO.isAccess()) {
                channelMatchRecord.setStatus(ResultStatusEnum.SUCCESS.getCode());
            } else {
                channelMatchRecord.setStatus(ResultStatusEnum.FAIL.getCode());
            }
            channelMatchRecord.setChannelId(channelId);
            channelMatchRecord.setPhoneMd5(phoneMd5);
            return channelMatchRecord;
        });
        log.info("[H5渠道撞库过程]渠道撞库结果,渠道ID:{}>>结果:{}", channelConf.getId(), JSONUtil.toJsonStr(matchAccessRespVO));
        if (!matchAccessRespVO.isAccess()) {
            return this.onFail(matchAccessRespVO.getMsg());
        }
        return this.onSuccess(request, matchAccessRespVO, channelConf, metaParamMap);
    }

    /**
     * 保存撞库记录
     *
     * @param supplier 撞库记录提供者
     */
    private void saveChannelFilterRecord(Supplier<ChannelMatchRecord> supplier) {

        ChannelMatchRecord record = supplier.get();
        try {
            channelMatchRecordRepository.save(record);
        } catch (Exception e) {
            log.error("{} 撞库记录入库失败", cn.hutool.json.JSONUtil.toJsonStr(record), e);
        }
    }


    /**
     * 保存联登记录
     *
     * @param supplier 撞库记录提供者
     */
    private void saveChannelRegisterRecord(Supplier<ChannelRegisterRecord> supplier) {

        ChannelRegisterRecord record = supplier.get();
        try {
            channelRegisterRecordRepository.save(record);
        } catch (Exception e) {
            log.error("{} 撞库记录入库失败", cn.hutool.json.JSONUtil.toJsonStr(record), e);
        }
    }

    /**
     * 保存h5渠道匹配记录
     *
     * @param matchR      渠道匹配结果
     * @param request     请求参数
     * @param channelConf 渠道配置
     */
    private void saveH5ChannelMatchResult(H5ChannelMatchR matchR, H5ChannelMatchAccessRequest request, Channel channelConf) {
//        try {
//            h5ChannelMatchRecordSnapshotService.saveSnapshot(() -> new LoanH5ChannelMatchRecordSnapshot()
//                    .setChannelName(channelConf.getChannelName())
//                    .setChannelId(channelConf.getChannelId())
//                    .setChannelType(channelConf.getChannelType())
//                    .setPhoneMd5(request.getPhoneMd5())
//                    .setIpAddr(IRequestContextHolder.getRequestInfo().getIpAddress())
//                    .setMsg(matchR.getMsg())
//                    .setMatchScenario(match.getType())
//                    .setSucceed(matchR.isAccess()));
//        } catch (Exception e) {
//            log.error("[H5渠道撞库过程]保存渠道撞库记录异常,渠道id:{}", channelConf.getId(), e);
//        }
    }

    /**
     * 解析渠道请求参数
     *
     * @param originRequestStr 请求json
     * @param channelConf      渠道配置
     * @return {@link Optional<H5ChannelMatchAccessRequest>}
     */
    private Optional<H5ChannelMatchAccessRequest> resolveRequestParam(String originRequestStr, Channel channelConf, Map<String, Object> metaParamMap) {
        originRequestStr = this.decryptRequestParamJson(originRequestStr, metaParamMap);
        if (CharSequenceUtil.isBlank(originRequestStr) || !JSONUtil.isJson(originRequestStr)) {
            log.warn("[H5渠道撞库过程]请求参数格式有误,参数为空或参数不是一个json,渠道id:{}", channelConf.getId());
            return Optional.empty();
        }
        log.info("[H5渠道撞库过程]H5渠道请求参数:{}", originRequestStr);
        JSONObject jsonObj = new JSONObject(originRequestStr);
        // 请求转换
        H5ChannelMatchAccessRequest request = this.convertRequest(jsonObj, metaParamMap);
        if (Objects.isNull(request)) {
            log.warn("[H5渠道撞库过程]请求参数转换本地请求异常渠道id:{}", channelConf.getId());
            return Optional.empty();
        }
        return Optional.of(request);
    }

    /**
     * 执行撞库动作
     *
     * @param request     请求参数
     * @param channelConf 渠道配置
     * @return {@link H5ChannelMatchR} 撞库结果
     */
    private H5ChannelMatchR<?> doMatchAction(H5ChannelMatchAccessRequest request, Channel channelConf, Map<String, Object> metaParamMap) {

//        String lockK = RedisKeyPattern.LOAN_H5_CHANNEL_MATCH_LOCK.formatted(channelConf.getId() + request.getPhoneMd5());
        try {
//            if (!redissonLockUtil.tryLock(lockK, 3, 3)) {
//                return H5ChannelMatchR.fail("请求过快,请稍后再试!");
//            }
            // 解析渠道id
            if (!channelService.checkChannelAccessibleOnMatch(channelConf, request.getCity())) {
                return H5ChannelMatchR.fail("渠道状态不可用!");
            }
            H5ChannelMatchR<?> matchAccessResp = preProgress(request, channelConf, metaParamMap);
            if (Objects.nonNull(matchAccessResp)) {
                return matchAccessResp;
            }

            // 找撞库策略

            // 执行撞库
            ChannelMatchStrategyR<?> accessibleCheckR = this.accessibleCheck(request, channelConf);
            if (!accessibleCheckR.isSuccess()) {
                return H5ChannelMatchR.fail("撞库失败", accessibleCheckR.getData());
            }
            // 放置撞库成功临时缓存
            String ticket = this.pushAccessibleCache(request.getPhoneMd5(), channelConf);
            return H5ChannelMatchR.success(accessibleCheckR.getData(), ticket);
        } catch (Exception e) {
            log.error("[H5渠道撞库过程]匹配过程异常", e);
            return H5ChannelMatchR.fail(e);
        } finally {
            //redisClient.unlock(lockK);
        }
    }


    protected H5ChannelRegisterR doRegisterAction(Channel channelConf, H5ChannelRegisterBaseRequest requestObj) {
        String phoneMd5 = MD5Util.md5Hex(requestObj.getPhone());
        //String lockK = RedisKeyPattern.LOAN_H5_CHANNEL_REGISTER_LOCK.formatted(phoneMd5);
        try {
//            if (!redissonLockUtil.tryLock(lockK, 3, 3)) {
//                return H5ChannelRegisterR.fail("请求频繁!", null);
//            }
            boolean needMatch = H5CooperationModeEnum.needMatch(channelConf.getChannelType());
            if (needMatch) {
//                ChannelMatchStrategyR<?> strategyR = matchAccessibleCheck(requestObj.getPhone(), channelConf);
//                if (!strategyR.isSuccess()) {
//                    return H5ChannelRegisterR.fail(strategyR.getMsg(), null);
//                }
            }

//            if (!h5ChannelConfService.checkChannelAccessibleOnRegister(channelConf, requestObj)) {
//                log.warn("[H5渠道进件]:当前渠道未不可用状态,渠道Id:{}", channelConf.getId());
//                return H5ChannelRegisterR.fail("渠道状态不可用!", null);
//            }

            // 执行联登
            H5ChannelUnionLoginResultDTO resultDTO = doH5LoginRegister(requestObj, channelConf);
            HxUser userInfo = resultDTO.getUserInfo();
            // H5渠道连登埋点统计
            Long channelId = channelConf.getId();
            Long userId = userInfo.getUserId();

            //RequestContextInfo requestInfo = IRequestContextHolder.getRequestInfo();
            // 记录日志
            log.info("来源渠道:{},用户Md5:{}联登成功", channelConf.getId(), MD5Util.md5Hex(requestObj.getPhone()));

//            if (SettlementModeEnum.CPA.getCode().equals(channelConf.getSettlementType())) {
//                // 联登结算
//                h5ChannelSettlementOrderService.settlementStrategy(userInfo.getId(), channelConf, channelSkuConf);
//            }

            return H5ChannelRegisterR.success(resultDTO.getLink(), resultDTO.getH5LoginUser().getUser().getUserId());
        } catch (Exception e) {
            log.error("[H5渠道进件]:过程异常,渠道ID:{}", channelConf.getId(), e);
            return H5ChannelRegisterR.error(e);
        }
    }

    /**
     * 进件生成用户结果信息
     *
     * @param request
     * @param channel
     * @return
     */
    protected H5ChannelUnionLoginResultDTO doH5LoginRegister(H5ChannelRegisterBaseRequest request, Channel channel) {
        H5ChannelUnionLoginResultDTO resultDTO = appUserService.chaUnionLogin(request, channel);
        String channelLink = channelService.getChannelLink(channel.getId());
        StringBuilder builder = CharSequenceUtil.builder(channelLink);
        String fullLink = builder.append("&token=").append(resultDTO.getTokenStr()).toString();
        resultDTO.setLink(fullLink);
        return resultDTO;
    }

    protected String pushAccessibleCache(String phoneMd5, Channel channelConf) {
        String ticket = buildTicket(phoneMd5, channelConf);
        String cacheKey = String.format(RedisKeyPattern.LOAN_H5_CHANNEL_MATCH_RESULT_CACHE, ticket);
        redisClient.set(cacheKey, "Y", 1, TimeUnit.HOURS);
        return ticket;
    }


    /**
     * 参数校验
     *
     * @param request
     * @param channel
     * @return
     */
    public abstract ChannelMatchStrategyR<?> requestParamCheck(H5ChannelMatchAccessRequest request, Channel channel);

    /**
     * 执行准入逻辑
     *
     * @param request
     * @param channel
     * @return
     */
    protected abstract ChannelMatchStrategyR<?> accessible(H5ChannelMatchAccessRequest request, Channel channel);


    public ChannelMatchStrategyR<?> accessibleCheck(H5ChannelMatchAccessRequest request, Channel channel) {
        ChannelMatchStrategyR<?> accessible = accessible(request, channel);
        log.info("渠道{}是否准入:{}", channel.getChannelName(), accessible);
        return accessible;
    }
}
