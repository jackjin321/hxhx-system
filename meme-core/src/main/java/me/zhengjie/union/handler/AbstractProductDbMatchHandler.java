package me.zhengjie.union.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.HxUser;
import me.zhengjie.domain.Product;
import me.zhengjie.domain.ProductFilterRecord;
import me.zhengjie.exception.CommentException;
import me.zhengjie.repository.ProductFilterRecordRepository;
import me.zhengjie.union.annotations.LoanProductMatchHandler;
import me.zhengjie.union.config.ProductLoanMatchConfigProperties;
import me.zhengjie.union.config.properties.BaseLoanProductConfigProperties;
import me.zhengjie.union.vo.MatchResp;
import com.dtflys.forest.http.ForestRequest;
import me.zhengjie.union.vo.RegisterResp;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 融超产品撞库处理器
 *
 * @author Eathon
 */
@Slf4j
public abstract class AbstractProductDbMatchHandler<R> {

    //
    @Resource
    protected ProductFilterRecordRepository productFilterRecordRepository;
    //
    @Resource
    private ProductLoanMatchConfigProperties productLoanMatchConfigProperties;
//
//    @Resource
//    private StrRedisClient redisClient;

    /**
     * @param product
     * @return
     */
    protected boolean needPreHandler(Product product, HxUser user) {
        return false;
    }


    /**
     * @param product
     * @return
     */
    protected MatchResp preHandle(Product product, HxUser user) {
        return MatchResp.success(product.getId(), null);
    }

    protected RegisterResp preHandleRegister(Product product, HxUser user) {
        return RegisterResp.success(product.getId(), null);
    }

    /**
     * 构建参数
     *
     * @return {@link Map<String,Object>} 请求参数
     */
    protected abstract Map<String, Object> buildRequestParamMatch(Product product, HxUser user, BaseLoanProductConfigProperties configProperties);

    protected abstract Map<String, Object> buildRequestParamRegister(Product product, HxUser user, BaseLoanProductConfigProperties configProperties);

    /**
     * 选择性创建请求客户端 get / post
     * 自定义不同的contentType客户端
     *
     * @return {@link Forest}
     */
    protected abstract ForestRequest<?> getRequestCli(String url);

    /**
     * 获取响应结果类型
     *
     * @return {@link Class<R>}
     */
    protected abstract Class<R> getResponseType();

    /**
     * 处理响应
     *
     * @param response  响应结果
     * @param productId
     * @return
     */
    protected abstract MatchResp responseProcessMatch(R response, Long productId, String phoneMd5);

    protected abstract RegisterResp responseProcessRegister(R response, Long productId, String phoneMd5);

    /**
     * 获取处理器的yml配置信息
     */
    protected BaseLoanProductConfigProperties getConfigProperties() {
        me.zhengjie.union.annotations.LoanProductMatchHandler annotation = this.getClass().getAnnotation(LoanProductMatchHandler.class);
        String configKey = annotation.confKey();
        if (CharSequenceUtil.isBlank(configKey)) {
            throw new CommentException(annotation.name() + "未指定产品配置key");
        }
        Map<String, BaseLoanProductConfigProperties> config = productLoanMatchConfigProperties.getConfig();
        BaseLoanProductConfigProperties baseMktProductProperties = config.get(configKey);
        if (Objects.isNull(baseMktProductProperties)) {
            throw new CommentException(annotation.name() + "未查询到具体配置");
        }
        return baseMktProductProperties;
    }


    public RegisterResp registerAction(Product product, HxUser user) {
        // 异步撞库
        log.info("异步联登 {} {}", product.getId(), product.getProductName());
        RegisterResp matchResp = doRegisterAction(product, user);

        return matchResp;
    }

    private RegisterResp doRegisterAction(Product product, HxUser user) {
        try {
            BaseLoanProductConfigProperties configProperties = getConfigProperties();
            // 前置处理
            log.info("前置处理2");
            if (needPreHandler(product, user)) {
                RegisterResp preMatchResp = this.preHandleRegister(product, user);
                log.info("使用mktLoginUser:{}执行产品:{}联登,命中前置处理逻辑,结果:{}", JSONUtil.toJsonStr(user), JSONUtil.toJsonStr(product), preMatchResp);
                return preMatchResp;
            }
            // 构建请求参数
            log.info("构建请求参数 {}", configProperties.getRegisterUrl());
            Map<String, Object> requestParam = this.buildRequestParamRegister(product, user,configProperties);
            if (CollUtil.isEmpty(requestParam)) {
                return RegisterResp.fail(product.getId(), "参数构建异常");
            }
            // 构建基础请求
            ForestRequest<?> requestCli = this.getRequestCli(configProperties.getRegisterUrl());
            log.info("{}执行联登请求参数:{}", this.getClass().getName(), JSONUtil.toJsonStr(requestParam));
            // 执行请求
            R execute = requestCli
                    .setCharset(StandardCharsets.UTF_8.displayName())
                    .addBody(requestParam)
                    .execute(getResponseType());
            log.info("{}执行撞库请求结果:{}", this.getClass().getName(), JSONUtil.toJsonStr(execute));
            // 处理响应
            RegisterResp matchResp = this.responseProcessRegister(execute, product.getId(), user.getPhoneMd5());
            // 保存撞库记录
            log.info("[{}]联登请求Md5:{},Channel:{},产品:{},请求参数:{},响应结果:{}",
                    matchResp.getStatus().getDescribe(), user.getPhoneMd5(),
                    user.getChannelId(), product.getId(),
                    JSONUtil.toJsonStr(requestParam), JSONUtil.toJsonStr(execute));

            return matchResp;
        } catch (Exception e) {
            log.error("执行{}联登请求异常:", product.getProductName() + "-" + product.getProductName(), e);
            return RegisterResp.error(product.getId(), "联登异常");
        }
    }

    /**
     * @param product
     * @return
     */
    public MatchResp matchAction(Product product, HxUser user) {
        // 异步撞库
        log.info("异步撞库 {} {}", product.getId(), product.getProductName());
        MatchResp matchResp = doMatchAction(product, user);
        this.saveProductFilterRecord(() -> {
            ProductFilterRecord filterRecord = new ProductFilterRecord();
            filterRecord.setUserId(user.getUserId());
            //filterRecord.setCity(capital.getCity());
            filterRecord.setStatus(matchResp.getStatus().getStatus());
            filterRecord.setResult(matchResp.getMsg());
            filterRecord.setChannelId(user.getChannelId());
            filterRecord.setProductId(product.getId());
            filterRecord.setPhoneMd5(user.getPhoneMd5());
            //filterRecord.setCreateTime(new Timestamp(LocalDateTime.now().toLocalTime()));
            //filterRecord.setUpdateTime(LocalDateTime.now());
            return filterRecord;
        });
        return matchResp;
    }

    private MatchResp doMatchAction(Product product, HxUser user) {
        try {
            BaseLoanProductConfigProperties configProperties = getConfigProperties();
            // 前置处理
            log.info("前置处理1");
            if (needPreHandler(product, user)) {
                MatchResp preMatchResp = this.preHandle(product, user);
                log.info("使用mktLoginUser:{}执行产品:{}撞库,命中前置处理逻辑,结果:{}", JSONUtil.toJsonStr(user), JSONUtil.toJsonStr(product), preMatchResp);
                return preMatchResp;
            }
            // 构建请求参数
            log.info("构建请求参数 {}", configProperties.getMatchUrl());
            Map<String, Object> requestParam = this.buildRequestParamMatch(product, user, configProperties);
            if (CollUtil.isEmpty(requestParam)) {
                return MatchResp.fail(product.getId(), "参数构建异常");
            }
            // 构建基础请求
            ForestRequest<?> requestCli = this.getRequestCli(configProperties.getMatchUrl());
            log.info("{}执行撞库请求参数:{}", this.getClass().getName(), JSONUtil.toJsonStr(requestParam));
            // 执行请求
            R execute = requestCli
                    .setCharset(StandardCharsets.UTF_8.displayName())
                    .addBody(requestParam)
                    .execute(getResponseType());
            log.info("{}执行撞库请求结果:{}", this.getClass().getName(), JSONUtil.toJsonStr(execute));
            // 处理响应
            MatchResp matchResp = this.responseProcessMatch(execute, product.getId(), user.getPhoneMd5());
            // 保存撞库记录
            log.info("[{}]撞库请求Md5:{},Channel:{},产品:{},请求参数:{},响应结果:{}",
                    matchResp.getStatus().getDescribe(), user.getPhoneMd5(),
                    user.getChannelId(), product.getId(),
                    JSONUtil.toJsonStr(requestParam), JSONUtil.toJsonStr(execute));

            return matchResp;
        } catch (Exception e) {
            log.error("执行{}撞库请求异常:", product.getProductName() + "-" + product.getProductName(), e);
            return MatchResp.error(product.getId(), "撞库异常");
        }
    }

    /**
     * 数据源自渠道库源撞库策略
     *
     * @param productId 产品id
     * @param phoneMd5  手机md5
     * @return true 或 false
     */
//    private Optional<DbSourceMatchResp> hasMatchResultCache(Long productId, String phoneMd5) {
//        //String formatted = RedisKeyPattern.H5_CHANNEL_DBS_SUCCESS_CACHE.formatted(productId, phoneMd5);
//        String formatted = RedisKeyPattern.LOAN_CHANNEL_DBS_SUCCESS_CACHE.formatted(productId, phoneMd5);
//        String cache = redisClient.get(formatted);
//        if (CharSequenceUtil.isNotBlank(cache)) {
//            return Optional.of(JSONUtil.toBean(cache, DbSourceMatchResp.class));
//        }
//        return Optional.empty();
//    }

    /**
     * 保存撞库记录
     *
     * @param supplier 撞库记录提供者
     */
    private void saveProductFilterRecord(Supplier<ProductFilterRecord> supplier) {

        ProductFilterRecord record = supplier.get();
        try {
            productFilterRecordRepository.save(record);
        } catch (Exception e) {
            log.error("{} 撞库记录入库失败", JSONUtil.toJsonStr(record), e);
        }
    }

}
