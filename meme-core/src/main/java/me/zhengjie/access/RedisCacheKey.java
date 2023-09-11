package me.zhengjie.access;

import cn.hutool.core.date.DateUtil;

import java.util.Arrays;


public class RedisCacheKey {

    //内部测试账号
    public static final String[] oppoTest = {};

    public static String productApplyNumKey(String productId) {
        return String.format("productApplyNum::%s", productId);
    }

    /**
     * http请求
     */
    public static final String channelTokenKey = "channel-token";
    public static final String appSource = "app-name";

    public static void main(String[] args){
        System.out.println(isTestMobile(""));
    }
    /**
     * 渠道访问信息
     *
     * @return
     */
    public static String channelObjectKey(String channelObjectKey) {
        return String.format("channelObject::%s", channelObjectKey);
    }

    public static Boolean isTestMobile(String phone) {
        return Arrays.asList(oppoTest).contains(phone);
    }

    /**
     * 渠道登录信息
     *
     * @param authToken
     * @return
     */
    public static String loginInfoKey(String authToken) {
        return String.format("loginInfo::%s", authToken);
    }


    public static String productRealAccessLink() {
        return String.format("accessLink::cacheKey");
    }

    public static String channelObjectPreDayKey() {
        return String.format("channelObject::%s", DateUtils.toStr(DateUtils.nowLocalDate().minusDays(1)));
    }

    public static String loginCodeKey(String phone) {
        return String.format("loginCode::%s", phone);
    }

    public static String applyCodeKey(String phone) {
        return String.format("applyCode::%s", phone);
    }

    public static String pushLogKey(String phone) {
        String today = DateUtils.nowLocalDateStr();
        return String.format("pushLog::%s::%s", today, phone);
    }

    public static String clickProductKey(String memberId, String productId) {
        String today = DateUtils.nowLocalDateStr();
        return String.format("clickProduct::%s::%s::%s", today, memberId, productId);
    }

    public static String largeProductKey(String memberId, String planId) {
        String today = DateUtils.nowLocalDateStr();
        return String.format("largeProduct::%s::%s::%s", today, memberId, planId);
    }

    public static String memberInfoKey(String phone) {
        String today = DateUtils.nowLocalDateStr();
        return String.format("MemberInfo::%s::%s", today, phone);
    }

    public static String loginUnionKey(String phone) {
        return String.format("loginUnion::%s", phone);
    }

    public static String infoLoginUnionKey(String phone) {
        return String.format("infoLoginUnion::%s", phone);
    }

    /**
     * 认证登陆信息
     *
     * @param phone
     * @return
     */
    public static String license(String phone) {
        return String.format("license::%s", phone);
    }


    /**
     * 访问信息header
     *
     * @return
     */
    public static String memberHeader() {
        return "license";
    }


    public static String memberInfoElementsKey(String phoneMd5) {
        return String.format("memberInfo_elements::%s", phoneMd5);
    }

    public static String limitProduct(String productId) {
        return String.format("limitProduct::%s::%s", DateUtil.formatDate(DateUtil.date()), productId);
    }

    public static String addressList() {
        return "loan-supermarket::ProvinceList";
    }

    public static String toProductRefresh(String productId) {
        return "toProductRefresh::cacheKey" + productId;
    }

    //费率key
    public static final String RATE_KEY = "product_rate_key:%s";

    /**
     * 获取线下后台业务员id计算key
     *
     * @param dispatchId
     * @return
     */
    public static String getSalesmanIdNumRedisKey(String dispatchId) {
        return String.format("salesmanIdNum:%s", dispatchId);
    }

    /**
     * 获取线下后台业务员id计算key
     *
     * @param dispatchId
     * @return
     */
    public static String getSalesmanNumRedisKey(String dispatchId, String city) {
        return String.format("salesman::cityNum::%s::%s", dispatchId, city);
    }

    /**
     * 线下审核手机号redis Key
     *
     * @param phone
     * @return
     */
    public static String getTestPhoneKey(String phone) {
        return String.format("test_phone::%s", phone);
    }

    /**
     * 扣费订单号缓存key
     *
     * @param orderNo
     * @return
     */
    public static String deductionOrder(String pushAccountId, String orderNo) {
        return String.format("deduction_order::%s::%s", pushAccountId, orderNo);
    }

    /**
     * 推送城市数量
     *
     * @param dispatchId
     * @param city
     * @return
     */
    public static String getPushCityCountKey(Long dispatchId, String city) {
        return String.format("PUSH_CITY_COUNT::%s::%s::%s", DateUtil.formatDate(DateUtil.date()), dispatchId, city);
    }

    /**
     * 投放计划每日推送数量
     *
     * @param dispatchId
     * @return
     */
    public static String deliveryPlanCountKey(Long dispatchId, Long deliveryPlanId) {
        return String.format("DISPATCH::PLANID::%s::%s::%s", DateUtil.formatDate(DateUtil.date()), dispatchId, deliveryPlanId);
    }
}
