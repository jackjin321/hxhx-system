package me.zhengjie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 管理后台-广告位VO
 * @Description
 * @Author sanjin
 * @Date 2021/7/6 17:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBannerVO implements Serializable {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 标题
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
     * 展示平台 android|ios|all
     */
    private String showPlatform;

    /**
     * 展示开始时间
     */
    private String showStartTime;

    /**
     * 展示结束时间
     */
    private String showEndTime;

    /**
     * onlyWhiteListCanSee|仅白名单可见,notOnlyWhiteListCanSee|非白名单可见
     */
    private String isOnlyWhitelistCanSee;

    /**
     * on|开启 off|关闭
     */
    private String status;

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

    /**
     * 用户状态类型排序数字，数字越大的排在前面
     */
    private Integer userStatusTypeSortNum;

    /**
     * 排序数字，数字越大的排在前面
     */
    private Integer sortNum;

    /**
     * 屏蔽的渠道ID,用英文逗号隔开
     */
    private String hideChannelIds;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

}
