package me.zhengjie.modules.union.config.properties;

import lombok.Data;

/**
 * 贷超渠道注册联登配置
 */
@Data
public class BaseLoanChannelConfigProperties {

    /**
     * 应用标识, 可以代表任何产品分配给我放的应用id渠道id等, 完全自定义, 按需配置
     */
    private String appKey;
    /**
     * 密钥如果产品方要求加密, 此处可代表任意加密方式的密钥
     */
    private String appSecret;
    /**
     * Aes 加密的话有些产品需要IV
     */
    private String appIv;
    /**
     * rsa公钥
     */
    private String rsaPublicKey;
    /**
     * rsa私钥
     */
    private String rsaPrivateKey;

    /**
     * 冗余字段
     */
    private String other;

    private String matchUrl;

    private String registerUrl;

}
