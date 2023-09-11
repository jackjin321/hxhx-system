//package me.zhengjie.modules.jdfq.rest;
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.RequiredArgsConstructor;
//import me.zhengjie.result.ResultModel;
//import me.zhengjie.service.BannerService;
//import me.zhengjie.service.ChannelService;
//import me.zhengjie.service.ProductService;
//import me.zhengjie.service.dto.AppQueryCriteria;
//import me.zhengjie.utils.IPUtils;
//import me.zhengjie.vo.ParamBannerQuery;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.Valid;
//
///**
// * 获取产品列表
// * 获取产品链接
// * 获取广告为
// */
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/mini")
//public class MiniController {
//
//    private final ProductService productService;
//    private final ChannelService channelService;
//
//    private final BannerService bannerService;
//
//    /**
//     * 根据页面和位置查询广告位
//     *
//     * @param request 请求记录
//     * @return
//     */
//    @PostMapping("/getBannerListByPageAndPos")
//    public ResponseEntity<Object> getBannerListByPageAndPos(@RequestBody @Valid ParamBannerQuery paramBannerQuery, HttpServletRequest request) {
////    public ResultModel<List<AdBanner>> getBannerListByPageAndPos(@RequestBody @Valid ParamBannerQuery paramBannerQuery, HttpServletRequest request) {
////        List<AdBanner> bannerVoList = bannerService.getBannerListByPageAndPos(paramBannerQuery, request);
//        return bannerService.getBannerListByPageAndPosV1(paramBannerQuery, request);
//
////        return ResultBuilder.data(bannerVoList);
////        return new ResponseEntity<>(ResultBuilder.data(bannerVoList), HttpStatus.OK);
//    }
//
//    @PostMapping("/toBanner")
//    public ResultModel toBannerUrl(@RequestBody @Valid ParamBannerQuery paramBannerQuery,
//                                   @RequestHeader(name = "channel-token", required = false) String channelTokenHeader,
//                                   HttpServletRequest request) {
//
//        return bannerService.toBannerUrlV2(paramBannerQuery, request, channelTokenHeader);
//
//    }
//
//    @PostMapping("/toProduct")
//    public ResultModel toProductUrl(@RequestBody @Valid ParamBannerQuery paramBannerQuery,
//                                    @RequestHeader(name = "channel-token", required = false) String channelTokenHeader,
//                                    HttpServletRequest request) {
//
//        return bannerService.toProductUrlV2(paramBannerQuery.getProductId());
//
//    }
//
//    @GetMapping(value = "/product/list")
//    public ResponseEntity<Object> productList(AppQueryCriteria criteria, Pageable pageable) {
//        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/find/top/list")
//    public ResponseEntity<Object> findTopList(AppQueryCriteria criteria, Pageable pageable) {
//        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/find/top")
//    public ResponseEntity<Object> findTop(AppQueryCriteria criteria, Pageable pageable) {
//        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/test")
//    public ResponseEntity<Object> get(AppQueryCriteria criteria, Pageable pageable) {
//        return new ResponseEntity<>(null, HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/channel")
//    public ResponseEntity<Object> channel(@RequestBody JSONObject entity,
//                                          @RequestHeader(name = "channel-token", required = false) String channelToken,
//                                          HttpServletRequest request) {
//        String realIP = IPUtils.getIpAddr(request);
//        String platform = entity.getString("platform");
//        String deviceId = entity.getString("deviceId");
//        String channelId = entity.getString("channelId");
//        String osId = entity.getString("osId");
//        String osName = entity.getString("osName");
//        return channelService.getChannelTokenByPlatformV1(channelToken, platform, deviceId, channelId, realIP, osId, osName, request);
////        return new ResponseEntity<>(channelService.getChannelTokenByPlatformV3(channelToken, platform, deviceId, channelId, realIP, osId, osName, request), HttpStatus.OK);
////        return new ResponseEntity<>(channelService.getChannelTokenByPlatformV3(channelToken, platform, deviceId, channelId, realIP, osId, osName, request), HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/uploadLogo")
//    public ResponseEntity<Object> uploadLogo(@RequestParam MultipartFile file) {
//        return new ResponseEntity<>(productService.updateAvatar(file), HttpStatus.OK);
//    }
//}
