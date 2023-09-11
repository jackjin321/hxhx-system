package me.zhengjie.enums;


import me.zhengjie.vo.AdminBannerPageVO;
import me.zhengjie.vo.AdminBannerPosVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BannerPageAndPosEnum {

    /**
     * 一、首页
     */
    HOME_AND_BANNER("home", "banner", "首页", "轮播图"),
    HOME_AND_Tag("home", "tag", "首页", "轮播图下方导航栏"),
    HOME_AND_List("home", "list", "首页", "导航栏下方产品列表"),

//    MINE_AND_BANNER("mine", "banner", "我的页面", "轮播图"),
    ;

    //页面
    private String page;

    //位置
    private String pos;

    //页面描述
    private String pageDesc;

    //位置描述
    private String posDesc;

    BannerPageAndPosEnum(String page, String pos, String pageDesc, String posDesc) {
        this.page = page;
        this.pos = pos;
        this.pageDesc = pageDesc;
        this.posDesc = posDesc;
    }


    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getPageDesc() {
        return pageDesc;
    }

    public void setPageDesc(String pageDesc) {
        this.pageDesc = pageDesc;
    }

    public String getPosDesc() {
        return posDesc;
    }

    public void setPosDesc(String posDesc) {
        this.posDesc = posDesc;
    }

    /**
     * 获取所有的页面
     *
     * @return
     */
    public static List<AdminBannerPageVO> getAllPageList() {
        List<AdminBannerPageVO> bannerPageVOList = new ArrayList<>();
        Map<String, String> bannerPageMap = new HashMap<>();
        for (BannerPageAndPosEnum enumDO : BannerPageAndPosEnum.values()) {
            String pageEnName = enumDO.getPage();
            String pageShowName = enumDO.getPageDesc();
            bannerPageMap.put(pageEnName, pageShowName);
        }
        for (String key : bannerPageMap.keySet()) {
            String paramShowName = bannerPageMap.get(key);
            AdminBannerPageVO pageVO = new AdminBannerPageVO();
            pageVO.setPageEnName(key);
            pageVO.setPageShowName(paramShowName);
            bannerPageVOList.add(pageVO);
        }
        return bannerPageVOList;
    }

    /**
     * 获取所有的位置
     *
     * @return
     */
    public static List<AdminBannerPageVO> getAllPosList() {
        List<AdminBannerPageVO> bannerPageVOList = new ArrayList<>();
        Map<String, String> bannerPageMap = new HashMap<>();
        for (BannerPageAndPosEnum enumDO : BannerPageAndPosEnum.values()) {
            String posEnName = enumDO.getPos();
            String posShowName = enumDO.getPosDesc();
            bannerPageMap.put(posEnName, posShowName);
        }
        for (String key : bannerPageMap.keySet()) {
            String paramShowName = bannerPageMap.get(key);
            AdminBannerPageVO pageVO = new AdminBannerPageVO();
            pageVO.setPageEnName(key);
            pageVO.setPageShowName(paramShowName);
            bannerPageVOList.add(pageVO);
        }
        return bannerPageVOList;
    }

    /**
     * 根据page获取所有的pos
     */
    public static List<AdminBannerPosVO> getPosListByPage(String page) {
        List<AdminBannerPosVO> posList = new ArrayList<>();
        for (BannerPageAndPosEnum enumDO : BannerPageAndPosEnum.values()) {
            String pageEnName = enumDO.getPage();
            if (pageEnName.equals(page)) {
                AdminBannerPosVO posVO = new AdminBannerPosVO();
                String posEnName = enumDO.getPos();
                String posShowName = enumDO.getPosDesc();
                posVO.setPosEnName(posEnName);
                posVO.setPosShowName(posShowName);
                posList.add(posVO);
            }
        }
        return posList;
    }

    /**
     * 根据页面描述获取页面名称
     *
     * @param pageEnName
     * @return
     */
    public static String getPageDescByPage(String pageEnName) {
        for (BannerPageAndPosEnum enumDO : BannerPageAndPosEnum.values()) {
            String page = enumDO.getPage();
            if (page.equals(pageEnName)) {
                return enumDO.getPageDesc();
            }
        }
        return "";
    }

    /**
     * 根据页面描述获取页面名称
     *
     * @param posEnName
     * @return
     */
    public static String getPosDescByPos(String posEnName) {
        for (BannerPageAndPosEnum enumDO : BannerPageAndPosEnum.values()) {
            String pos = enumDO.getPos();
            if (pos.equals(posEnName)) {
                return enumDO.getPosDesc();
            }
        }
        return "";
    }
}
