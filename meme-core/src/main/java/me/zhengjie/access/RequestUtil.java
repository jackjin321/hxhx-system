package me.zhengjie.access;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 请求工具类
 * @Author sanjin
 * @Date 2021/6/28 15:11
 */
public class RequestUtil {

    public static String getAppPlatform(HttpServletRequest request){
        return request.getHeader("app-platform");
    }

    public static String getAppVersionCode(HttpServletRequest request) {
        return request.getHeader("versionCode");
    }

    public static Long getAppMemberId(HttpServletRequest request){
        return (Long)(request.getAttribute("memberId"));
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getChannelCode(HttpServletRequest request) {
        return request.getHeader("channelCode");
    }

    /**
     * 获取Token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

    /**
     * 获取ChannelToken
     * @param request
     * @return
     */
    public static String getChannelToken(HttpServletRequest request) {
        return request.getHeader("channel-token");
    }

}
