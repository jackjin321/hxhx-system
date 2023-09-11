package me.zhengjie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description 广告位VO
 * @Author sanjin
 * @Date 2021/6/30 17:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerVO implements Serializable {

    /**
     * 标题一
     */
    private String title;

    /**
     * 子标题
     */
    private String subTitle;

    /**
     * 页面
     */
    private String page;

    /**
     * 位置
     */
    private String pos;

    /**
     * 业务类型
     * normal|普通
     * addOil|加油
     * washCar|洗车
     * carPark|停车场
     * carViolation|车辆违章
     */
    private String businessType;

    /**
     * h5|h5,native|原生页面,otherApp|其它App,noJump|不跳转
     */
    private String jumpType;

    /**
     * 跳转Url,或者跳转页面
     */
    private String jumpUrl;

    /**
     * 跳转参数
     */
    private String jumpParams;

    /**
     * 图片URL
     */
    private String imgUrl;

    /**
     * 用户状态类型
     * notGetHighPriceSellCarEvaluate|没有获取高价卖车报价
     * notActiveCarOwnerLoan|没有完成车主贷进件激活
     * notBuyZeroBuyProd|没有购买0元购
     * notClickLoanMarketProd|没有点击小额贷产品
     * notCurrentOpenHappyCarCard|没有当前开通乐车卡用户
     * normal|通用版
     */
    private String userStatusType;

}
