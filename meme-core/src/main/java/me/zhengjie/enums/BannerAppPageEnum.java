package me.zhengjie.enums;


import me.zhengjie.vo.BannerAppPageVO;

import java.util.ArrayList;
import java.util.List;

public enum BannerAppPageEnum {

    HOME_PAGE("homePage", "/pages/homePage/homePage", "首页"),
    PAWN_GOODS("pawnGoods", "/pages/pawnGoods/pawnGoods", "质押页面"),
    USER_CENTER("userCenter", "/pages/userCenter/userCenter", "我的"),
    LOAN_APPLY_FORM("applyForm", "/pages/ddhua/apply/apply", "完善信息"),
    LOAN_APPLY_RESULT("applyResult", "/pages/ddhua/apply/applyResult", "初审结果"),
    LOAN_PRODUCT_LIST("productList", "/pages/ddhua/product/index", "更多产品"),
    PAWN_APPLY_SIMPLIFY("pawnApplySimplify", "/pages/pawn/pawnApply/pawnApplySimplify", "申请质押"),
    ;

    //页面名称
//    private String pageType;

    private String page;

    //页面url
    private String pageUrl;

    //页面描述
    private String pageDesc;


    BannerAppPageEnum(String page, String pageUrl, String pageDesc) {
        this.page = page;
        this.pageUrl = pageUrl;
        this.pageDesc = pageDesc;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPageDesc() {
        return pageDesc;
    }

    public void setPageDesc(String pageDesc) {
        this.pageDesc = pageDesc;
    }


    /**
     * 获取所有的页面
     *
     * @return
     */
    public static List<BannerAppPageVO> getAllPageList() {
        List<BannerAppPageVO> bannerPageVOList = new ArrayList<>();
        for (BannerAppPageEnum enumDO : BannerAppPageEnum.values()) {
            BannerAppPageVO pageVO = new BannerAppPageVO();
            pageVO.setPageEnName(enumDO.getPage());
            pageVO.setPageUrl(enumDO.getPageUrl());
            pageVO.setPageShowName(enumDO.getPageDesc());
            bannerPageVOList.add(pageVO);
        }
        return bannerPageVOList;
    }

    /**
     * 根据页面描述获取页面名称
     *
     * @param pageEnName
     * @return
     */
    public static String getPageDescByPage(String pageEnName) {
        for (BannerAppPageEnum enumDO : BannerAppPageEnum.values()) {
            String page = enumDO.getPage();
            if (page.equals(pageEnName)) {
                return enumDO.getPageDesc();
            }
        }
        return "";
    }

}
