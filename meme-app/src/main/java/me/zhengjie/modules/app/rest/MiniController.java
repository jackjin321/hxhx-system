package me.zhengjie.modules.app.rest;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.domain.Channel;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.result.ResultBuilder;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.BannerService;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.ProductService;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.IPUtils;
import me.zhengjie.vo.MemberAuth;
import me.zhengjie.vo.ParamBannerQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取产品列表
 * 获取产品链接
 * 获取广告为
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mini")
@Slf4j
public class MiniController {

    private final ProductService productService;
    private final ChannelService channelService;

    private final BannerService bannerService;

    private final AppUserService appUserService;

    /**
     * 根据页面和位置查询广告位
     *
     * @param request 请求记录
     * @return
     */
    @PostMapping("/getBannerListByPageAndPos")
    public ResponseEntity<Object> getBannerListByPageAndPos(@RequestBody @Valid ParamBannerQuery paramBannerQuery, HttpServletRequest request) {
        return bannerService.getBannerListByPageAndPosV1(paramBannerQuery, request);
    }

    /**
     * 点击广告位
     *
     * @param paramBannerQuery
     * @param channelTokenHeader
     * @param request
     * @return
     */
    @PostMapping("/toBanner")
    public ResultModel toBannerUrl(@RequestBody @Valid ParamBannerQuery paramBannerQuery,
                                   @RequestHeader(name = "channel-token", required = false) String channelTokenHeader,
                                   HttpServletRequest request) {

        return bannerService.toBannerUrlV2(paramBannerQuery, request, channelTokenHeader);

    }

    /**
     * 点击产品
     *
     * @param paramBannerQuery
     * @param uuid
     * @param channelCode
     * @param request
     * @return
     */
    @PostMapping("/toProduct")
    public ResultModel toProductUrl(@RequestBody @Valid ParamBannerQuery paramBannerQuery,
                                    @RequestHeader(name = "uuid", required = false) String uuid,
                                    @RequestHeader(name = "channel-code", required = false) String channelCode,
                                    HttpServletRequest request) {
        System.out.println(paramBannerQuery);
        paramBannerQuery.setUuid(uuid);
        paramBannerQuery.setChannelCode(channelCode);
        String realIP = IPUtils.getIpAddr(request);
        paramBannerQuery.setRealIP(realIP);
        return bannerService.toProductUrlV2(paramBannerQuery);

    }


    @PostMapping("/saveMsg")
    public ResponseEntity<Object> saveMsg(@RequestBody @Valid MemberAuth memberAuth,
                                          @RequestHeader(name = "uuid", required = false) String uuid,
                                          @RequestHeader(name = "channel-code", required = false) String channelCode,
                                          HttpServletRequest request) {

        Channel channel = channelService.getChannelInfo(channelCode, uuid);
        if (ObjectUtil.isAllEmpty(channel) && channel.getAuthStatus().equals("on")) {
            /**
             * 新版身份证二要素验证接口
             */
            log.info("新版身份证二要素验证接口");
            String idCardFlag = appUserService.checkIdentity(memberAuth);
            if (ObjectUtil.isAllNotEmpty(idCardFlag)) {
                throw new BadRequestException(idCardFlag);
            }
        }


        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", 2);
            put("user", 1);
        }};
        return ResponseEntity.ok(authInfo);
    }

    /**
     * 产品列表
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @GetMapping(value = "/product/list")
    public ResponseEntity<Object> productList(AppQueryCriteria criteria, Pageable pageable, HttpServletRequest request) {
        return new ResponseEntity<>(productService.queryList(criteria, request), HttpStatus.OK);
    }

    /**
     * 返回广告位列表
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @GetMapping(value = "/find/top/list")
    public ResponseEntity<Object> findTopList(AppQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 随机返回一个广告位
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @GetMapping(value = "/find/top")
    public ResponseEntity<Object> findTop(AppQueryCriteria criteria, Pageable pageable,
                                          @RequestHeader(name = "uuid", required = false) String uuid,
                                          @RequestHeader(name = "channel-code", required = false) String channelCode) {
        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 随机跳转一个产品
     *
     * @param criteria
     * @param pageable
     * @return
     */
    @PostMapping(value = "/find/one")
    public ResultModel findOne(AppQueryCriteria criteria, Pageable pageable,
                               @RequestHeader(name = "uuid", required = false) String uuid,
                               @RequestHeader(name = "channel-code", required = false) String channelCode,
                               HttpServletRequest request) {
        ParamBannerQuery paramBannerQuery = new ParamBannerQuery();
        paramBannerQuery.setUuid(uuid);
        paramBannerQuery.setChannelCode(channelCode);
        String realIP = IPUtils.getIpAddr(request);
        paramBannerQuery.setRealIP(realIP);
        return bannerService.findOne(paramBannerQuery);
    }

    @GetMapping(value = "/test")
    public ResponseEntity<Object> get(AppQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //    @PostMapping(value = "/channel")
    @AnonymousPostMapping(value = "/channel")
    public ResponseEntity<Object> channel(@RequestBody JSONObject entity,
                                          HttpServletRequest request) {
        String realIP = IPUtils.getIpAddr(request);
        String platform = entity.getString("platform");
        String deviceId = entity.getString("deviceId");
        String uuid = entity.getString("uuid");//流量
        String channelId = entity.getString("channelId");//流量
        String osId = entity.getString("osId");
        String osName = entity.getString("osName");
        return channelService.getChannelTokenByPlatformV1(uuid, platform, deviceId, channelId, realIP, osId, osName, request);
    }

    @PostMapping(value = "/uploadLogo")
    public ResponseEntity<Object> uploadLogo(@RequestParam MultipartFile file) {
        return new ResponseEntity<>(productService.updateAvatar(file), HttpStatus.OK);
    }
}
