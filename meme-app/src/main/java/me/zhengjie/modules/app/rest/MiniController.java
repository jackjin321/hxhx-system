package me.zhengjie.modules.app.rest;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.HxSysConfig;
import me.zhengjie.domain.HxUser;
import me.zhengjie.domain.HxUserReport;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.app.service.AppUserService;
import me.zhengjie.modules.util.TestClient_V2;
import me.zhengjie.repository.HxUserReportRepository;
import me.zhengjie.result.ResultBuilder;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.BannerService;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.HxSysConfigService;
import me.zhengjie.service.ProductService;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.IPUtils;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.vo.MemberAuth;
import me.zhengjie.vo.ParamBannerQuery;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final HxSysConfigService sysConfigService;
    private final HxUserReportRepository hxUserReportRepository;

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
        //System.out.println(paramBannerQuery);
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
                                          HttpServletRequest request) throws Exception {


        Channel channel = channelService.getChannelInfo(channelCode, uuid);
        log.info("新版身份证二要素验证接口 {}", JSONObject.toJSONString(channel));
        if (ObjectUtil.isNotEmpty(channel) && channel.getAuthStatus().equals("on")) {
            /**
             * 新版身份证二要素验证接口
             */
            log.info("新版身份证二要素验证接口");
            String idCardFlag = appUserService.checkIdentity(memberAuth);
            if (ObjectUtil.isNotEmpty(idCardFlag)) {
                throw new BadRequestException(idCardFlag);
            }
        }


        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("process", channel.getProcess());
            put("user", 1);
        }};
        authInfo.put("port", "B");
        if (ObjectUtil.isNotEmpty(channel) && "on".equals(channel.getRadarStatus())) {
            log.info("全景雷达接口");
            Long userId = SecurityUtils.getCurrentUserIdByApp();

            HxUserReport userReport1 = hxUserReportRepository.findFirstByRealNameAndIdCard(memberAuth.getRealName(), memberAuth.getIdCard());
            if (ObjectUtil.isNotEmpty(userReport1)) {
                log.info("全景雷达报告已存在");
                //判断一下userReport
                String portStatus = productService.getPortStatus(userReport1);
                authInfo.put("port", portStatus);
                return ResponseEntity.ok(authInfo);
            }
            //二要素通过后，一步查询全景雷达报告，缓存到redis里，
            String transId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + RandomStringUtils.randomNumeric(16);
            String res = TestClient_V2.checkResult(transId, memberAuth.getRealName(), memberAuth.getIdCard(), SecurityUtils.getCurrentUserPhoneByApp());
            HxUserReport userReport = new HxUserReport();
            userReport.setTransId(transId);
            userReport.setUserId(userId);
//            userReport.setPhone(memberAuth.getPhone());
            userReport.setPhone(SecurityUtils.getCurrentUserPhoneByApp());
            userReport.setRealName(memberAuth.getRealName());
            userReport.setIdCard(memberAuth.getIdCard());
            userReport.setProvince(memberAuth.getProvince());
            userReport.setCity(memberAuth.getCity());
            userReport.setIdCard(memberAuth.getIdCard());
            userReport.setReport(res);
            userReport.setOverdue("0");//逾期
            userReport.setPerformance("0");//履约
            userReport.setApplyReport("no");//申请雷达
            userReport.setBehaviorReport("no");//行为雷达
            /**
             * 通过，就是展示B面，不通过，就展示A面
             * 1、6个月内逾期笔数超过2笔，跳A面
             * 2、只有申请雷达的全部拒，跳A面
             * 3、 有行为雷达的 全部通过，跳B面
             *
             * B22170025  近 6 个月 M0+ 逾期贷款笔数
             *
             */
            //处理report
            if (ObjectUtil.isNotEmpty(res)) {
                JSONObject object = JSONObject.parseObject(res);
                String code = object.getString("code");
                JSONObject resultDetail = object.getJSONObject("result_detail");
                if (ObjectUtil.isNotEmpty(resultDetail)) {
                    JSONObject currentReportDetail = resultDetail.getJSONObject("current_report_detail");//信用现状

                    JSONObject behaviorReportDetail = resultDetail.getJSONObject("behavior_report_detail");

                    JSONObject applyReportDetail = resultDetail.getJSONObject("apply_report_detail");
                    if (ObjectUtil.isNotEmpty(currentReportDetail)) {

                    }
                    if (ObjectUtil.isNotEmpty(behaviorReportDetail)) {//行为雷达
                        userReport.setBehaviorReport("yes");
                        String B22170025 = behaviorReportDetail.getString("B22170025");
                        if (ObjectUtil.isNotEmpty(B22170025)) {
                            userReport.setOverdue(B22170025);
                        }
                    }
                    if (ObjectUtil.isNotEmpty(applyReportDetail)) {
                        userReport.setApplyReport("yes");
                    }
                }
            }
            //判断一下userReport
            String portStatus = productService.getPortStatus(userReport);
            authInfo.put("port", portStatus);
            hxUserReportRepository.save(userReport);
        }

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
        //Long userId = SecurityUtils.getCurrentUserIdByApp();
        return new ResponseEntity<>(productService.queryListV2(criteria, request), HttpStatus.OK);
    }

    /**
     * 废弃
     * 返回广告位列表
     *
     * @param criteria
     * @param pageable
     * @return
     */
//    @GetMapping(value = "/find/top/list")
//    public ResponseEntity<Object> findTopList(AppQueryCriteria criteria, Pageable pageable) {
//        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
//    }

    /**
     * 废弃
     * 随机返回一个广告位
     *
     * @param criteria
     * @param pageable
     * @return
     */
//    @GetMapping(value = "/find/top")
//    public ResponseEntity<Object> findTop(AppQueryCriteria criteria, Pageable pageable,
//                                          @RequestHeader(name = "uuid", required = false) String uuid,
//                                          @RequestHeader(name = "channel-code", required = false) String channelCode) {
//        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
//    }

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

    //    @GetMapping(value = "/url")
    @AnonymousGetMapping(value = "/url")
    public ResponseEntity<Object> getUrl(AppQueryCriteria criteria, Pageable pageable) {

        HxSysConfig apkUrl = sysConfigService.findByKey("apk.url");
        HxSysConfig iosUrl = sysConfigService.findByKey("ios.url");
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("apkUrl", apkUrl.getValue());
            put("iosUrl", iosUrl.getValue());
        }};
        return new ResponseEntity<>(authInfo, HttpStatus.OK);
    }

    //    @PostMapping(value = "/channel")
    @AnonymousPostMapping(value = "/channel")
    public ResponseEntity<Object> channel(@RequestBody JSONObject entity,
                                          HttpServletRequest request) {
        String realIP = IPUtils.getIpAddr(request);
        String browser = entity.getString("browser");
        String platform = entity.getString("platform");
        String deviceId = entity.getString("deviceId");
        String uuid = entity.getString("uuid");//流量
        String channelId = entity.getString("channelId");//流量
        String osId = entity.getString("osId");
        String osName = entity.getString("osName");
        return channelService.getChannelTokenByPlatformV1(uuid, browser, platform, deviceId, channelId, realIP, osId, osName, request);
    }

    @PostMapping(value = "/uploadLogo")
    public ResponseEntity<Object> uploadLogo(@RequestParam MultipartFile file) {
        return new ResponseEntity<>(productService.updateAvatar(file), HttpStatus.OK);
    }
}
