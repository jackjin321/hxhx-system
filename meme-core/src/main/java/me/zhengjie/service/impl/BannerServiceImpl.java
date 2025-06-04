package me.zhengjie.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.access.ChannelObject;
import me.zhengjie.access.RequestUtil;
import me.zhengjie.domain.*;
import me.zhengjie.enums.BannerPageAndPosEnum;
import me.zhengjie.repository.*;
import me.zhengjie.result.ResultBuilder;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.*;
import me.zhengjie.service.dto.HxUserDto;
import me.zhengjie.service.dto.HxUserDtoV2;
import me.zhengjie.service.dto.ProductDto;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.vo.ParamBannerQuery;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final ChannelRepository channelRepository;
    private final ProductRepository productRepository;
    private final ProductLogRepository productLogRepository;

    private final IAccessService accessService;
    private final ChannelService channelService;
    private final ProductService productService;
//    private final HxUserService hxUserService;

    private final HxUserRepository hxUserRepository;

    /**
     * 根据页面和位置查询对应的广告位
     *
     * @param paramBannerQuery 页面
     * @param request
     * @return
     */
    @Override
    public List<AdBanner> getBannerListByPageAndPos(ParamBannerQuery paramBannerQuery, HttpServletRequest request) {

        String platform = RequestUtil.getAppPlatform(request);

        //获取channelToken
        String channelTokenHeader = RequestUtil.getChannelToken(request);

        //根据token信息获取渠道信息，最近登录渠道信息
        Optional<ChannelObject> optional = accessService.getChannelObject(channelTokenHeader);
        if (!optional.isPresent()) {
            return new ArrayList<>();
        }
//            product.setPage(paramBannerQuery.getPage());
//            product.setPos(paramBannerQuery.getPos());
        ChannelObject channelObject = optional.get();
//        if (StringUtils.isBlank(platform)) {
//            log.error("请求头平台参数缺失");
//            throw new CustomException("请求头平台参数缺失");
//        }

        //如果非H5页面，校验channelToken
//        if (ObjectUtil.isAllEmpty(channelTokenHeader)) {
//            log.error("请求头平台参数缺失");
//            throw new CustomException("请求头平台参数缺失");
//        }
//        paramBannerQuery.setShowPlatform(platform);

        //根据平台查询对应的最新版本记录
        //如果是最新的版本记录，则隐藏对应渠道
//        if(!"h5".equals(platform)){
//            KlSysAppVersion newestAppVersionDTO = appVersionMapper.selectNewestVersionByPlatform(platform);
//            String newestAppVersion = newestAppVersionDTO.getVersionCode();
//            String appVersion = RequestUtil.getAppVersionCode(request);
//            Boolean isNewestAppVersion = appVersion.equals(newestAppVersion);
//            paramBannerQuery.setIsNewestAppVersion(isNewestAppVersion);
//            if (isNewestAppVersion) {
//                //根据token信息获取渠道信息，最近登录渠道信息
//                Optional<ChannelObject> optional = accessService.getChannelObject(channelToken);
//                if (optional.isPresent()) {
//                    ChannelObject channelObject = optional.get();
//                    Long channelId = channelObject.getChannelId();
//                    paramBannerQuery.setChannelId(channelId);
//                }
//            }
//        }

//        List<AdBanner> bannerList = bannerRepository.findByChannelIdAndPageAndPos(channelObject.getChannelId(), paramBannerQuery.getPage(), paramBannerQuery.getPos());
        List<AdBanner> bannerList = bannerRepository.findByChannelIdAndPageAndPosAndStatusOrderBySortNumAsc(channelObject.getChannelId(), paramBannerQuery.getPage(), paramBannerQuery.getPos(), "on");
        log.info("【原始查询】数据：");
        //printBannerVOList(bannerList);


        //跳转链接添加时间戳
        Long timestamp = System.currentTimeMillis();
        //如果是[我的订单],添加token
        String token = request.getHeader("token");
        // handleJumpUrlAddTimestamp(bannerList, timestamp, token);

        log.info("添加【token和时间戳】：");
//        printBannerVOList(bannerList);

        //处理乐车卡权益链接
        Long memberId = RequestUtil.getAppMemberId(request);
        //handleBannerOpenCardRightUrl(memberId,paramBannerQuery,bannerList);
        log.info("处理【权益链接】：");
//        printBannerVOList(bannerList);

        //处理业务类型
        //handleBusinessType(paramBannerQuery,bannerList,request);
        log.info("处理【业务类型】：");
//        printBannerVOList(bannerList);

        //处理弹窗-用户状态类型
        //bannerList = handlePopupWindowUserStatusType(memberId,paramBannerQuery,bannerList,timestamp,token);
        log.info("处理【用户状态类型】：");
//        printBannerVOList(bannerList);

        //处理【我的】页面，h5跳转链接为空的
//        handleMinePageBlankJumpUrl(memberId,paramBannerQuery,bannerList);
        return bannerList.stream().map(p -> {
            //p.setImageUrl("https://xiaofei.gzkmwl.net/imgstatic/avatar/" + p.getImageUrl());
            p.setImageUrl("https://xiaofei.shiweikai.cn/imgstatic/avatar/" + p.getImageUrl());
            return p;
        }).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> getBannerListByPageAndPosV1(ParamBannerQuery paramBannerQuery, HttpServletRequest request) {

        String platform = RequestUtil.getAppPlatform(request);

        //获取channelToken
        String channelTokenHeader = RequestUtil.getChannelToken(request);

        //根据token信息获取渠道信息，最近登录渠道信息
        Optional<ChannelObject> optional = accessService.getChannelObject(channelTokenHeader);
        if (!optional.isPresent()) {
//            return new ArrayList<>();
//            return new ArrayList<>();
            return new ResponseEntity<>(ResultBuilder.data(new ArrayList<>()), HttpStatus.OK);
        }
//            product.setPage(paramBannerQuery.getPage());
//            product.setPos(paramBannerQuery.getPos());
        ChannelObject channelObject = optional.get();
//        if (StringUtils.isBlank(platform)) {
//            log.error("请求头平台参数缺失");
//            throw new CustomException("请求头平台参数缺失");
//        }
        Channel channel = channelRepository.getById(optional.get().getChannelId());
        HttpHeaders responseHeaders = new HttpHeaders();
        if (ObjectUtil.isAllNotEmpty(channel)) {
            responseHeaders.set("process", channel.getProcess());
        }

//        List<AdBanner> bannerList = bannerRepository.findByChannelIdAndPageAndPos(channelObject.getChannelId(), paramBannerQuery.getPage(), paramBannerQuery.getPos());
        List<AdBanner> bannerList = bannerRepository.findByChannelIdAndPageAndPosAndStatusOrderBySortNumAsc(channelObject.getChannelId(), paramBannerQuery.getPage(), paramBannerQuery.getPos(), "on");
        log.info("【原始查询】数据：");
        //printBannerVOList(bannerList);


        //跳转链接添加时间戳
        Long timestamp = System.currentTimeMillis();
        //如果是[我的订单],添加token
        String token = request.getHeader("token");
        // handleJumpUrlAddTimestamp(bannerList, timestamp, token);

        log.info("添加【token和时间戳】：");
//        printBannerVOList(bannerList);

        //处理乐车卡权益链接
        Long memberId = RequestUtil.getAppMemberId(request);
        //handleBannerOpenCardRightUrl(memberId,paramBannerQuery,bannerList);
        log.info("处理【权益链接】：");
//        printBannerVOList(bannerList);

        //处理业务类型
        //handleBusinessType(paramBannerQuery,bannerList,request);
        log.info("处理【业务类型】：");
//        printBannerVOList(bannerList);

        //处理弹窗-用户状态类型
        //bannerList = handlePopupWindowUserStatusType(memberId,paramBannerQuery,bannerList,timestamp,token);
        log.info("处理【用户状态类型】：");
//        printBannerVOList(bannerList);

        //处理【我的】页面，h5跳转链接为空的
//        handleMinePageBlankJumpUrl(memberId,paramBannerQuery,bannerList);
//        return bannerList.stream().map(p -> {
//            p.setImageUrl("https://xiaofei.gzkmwl.net/imgstatic/avatar/" + p.getImageUrl());
//            return p;
//        }).collect(Collectors.toList());

        return new ResponseEntity<>(ResultBuilder.data(bannerList.stream().map(p -> {
//            p.setImageUrl("https://xiaofei.gzkmwl.net/imgstatic/avatar/" + p.getImageUrl());
            p.setImageUrl("https://xiaofei.shiweikai.cn/imgstatic/avatar/" + p.getImageUrl());
            return p;
        }).collect(Collectors.toList())), responseHeaders, HttpStatus.OK);
    }

    @Override
    public ResultModel toProductUrlV2(ParamBannerQuery paramBanner) {
        Long userId = SecurityUtils.getCurrentUserIdByApp();

        Long regChannelId = SecurityUtils.getRegChannelIdByApp();
        String regChannelName = SecurityUtils.getRegChannelNameByApp();
        Optional<Channel> channelOptional = channelRepository.findById(regChannelId);
        Channel channel = null;
        if (channelOptional.isPresent()) {
            //System.out.println("//注册渠道");
            channel = channelOptional.get();//注册渠道
        } else {
            ///System.out.println("//登录渠道");
            channel = channelService.getChannelInfo(paramBanner.getChannelCode(), paramBanner.getUuid());
            ;//登录渠道
        }

        Optional<Product> productOptional = productRepository.findById(paramBanner.getProductId());
        //product.setSpaceId(spaceId);    @Override
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (!"onShelves".equals(product.getStatus())) { // 产品是否上线
                return ResultBuilder.fail("产品已经下架");
            }
            JSONObject dataMap = new JSONObject();
//            dataMap.put("url", product.getApplyLink());
            dataMap.put("url", productService.hitLogin(product));

            //Channel channel = channelService.getChannelInfo(paramBanner.getChannelCode(), paramBanner.getUuid());
            insertProductLog(userId, product, channel, paramBanner);
            return ResultBuilder.data(dataMap);
        } else {
            return ResultBuilder.fail("产品信息不存在");
        }
    }

    @Override
    public ResultModel findOne(ParamBannerQuery paramBanner) {
        Long userId = SecurityUtils.getCurrentUserIdByApp();
        Long regChannelId = SecurityUtils.getRegChannelIdByApp();
        String regChannelName = SecurityUtils.getRegChannelNameByApp();
        Optional<Channel> channelOptional = channelRepository.findById(regChannelId);
        Channel channel = null;
        if (channelOptional.isPresent()) {
            //System.out.println("//注册渠道");
            channel = channelOptional.get();//注册渠道
        } else {
            ///System.out.println("//登录渠道");
            channel = channelService.getChannelInfo(paramBanner.getChannelCode(), paramBanner.getUuid());
            //登录渠道
        }
//        Channel channel = channelService.getChannelInfo(paramBanner.getChannelCode(), paramBanner.getUuid());
        List<Product> productList = productRepository.findByPortStatusAndStatusOrderBySortAsc(channel.getPortStatus(), "onShelves");
        List<Product> filterList = productList.stream().filter(p -> {
            return productService.checkProduct(p);
        }).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(filterList) && filterList.size() > 0) {
            Product product = filterList.get(0);
            JSONObject dataMap = new JSONObject();
//            dataMap.put("url", product.getApplyLink());
            dataMap.put("url", productService.hitLogin(product));
            insertProductLog(userId, product, channel, paramBanner);
            return ResultBuilder.data(dataMap);
        } else {
            return ResultBuilder.fail("产品信息不存在");
        }
    }
    @Override
    public String findOneByLogin(ParamBannerQuery paramBanner) {
        //Long userId = SecurityUtils.getCurrentUserIdByApp();
        //Long regChannelId = SecurityUtils.getRegChannelIdByApp();
        //Optional<Channel> channelOptional = channelRepository.findById(regChannelId);
        Channel channel = channelService.getChannelInfo(paramBanner.getChannelCode(), paramBanner.getUuid());
        List<Product> productList = productRepository.findByPortStatusAndStatusOrderBySortAsc(channel.getPortStatus(), "onShelves");
        List<Product> filterList = productList.stream().filter(p -> {
            return productService.checkProduct(p);
        }).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(filterList) && filterList.size() > 0) {
            Product product = filterList.get(0);
            String url = productService.hitLogin(product);
            insertProductLog(paramBanner.getUserId(), product, channel, paramBanner);
            return url;
        } else {
            return null;
        }
    }

    private void insertProductLog(Long userId, Product product, Channel channel, ParamBannerQuery paramBanner) {
        Optional<HxUser> hxUserOptional = hxUserRepository.findById(userId);
        HxUser hxUser = hxUserOptional.get();
        ProductLog productLog = new ProductLog();
        productLog.setUserId(userId);
        productLog.setUuid(paramBanner.getUuid());

        Boolean isRegister = isSameDay(new Date(), hxUser.getRegisterTime());
        int abc = productLogRepository.countAbc(userId, product.getId());
        productLog.setUserStatus(isRegister ? "new" : "old");
        productLog.setChannelId(channel.getId());
        productLog.setPhone(hxUser.getPhone());
        productLog.setChannelName(channel.getChannelName());
        productLog.setChannelPort(channel.getPortStatus());
        productLog.setProductId(product.getId());
        productLog.setProductName(product.getProductName());
        productLog.setAccessIp(paramBanner.getRealIP());
        productLog.setIsFirstTo(abc > 0 ? false : true);
        productLogRepository.save(productLog);
    }

    public boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
//        System.out.println(localDate1);
//        System.out.println(localDate2);
        return localDate1.isEqual(localDate2);
    }

    @Override
    public ResultModel toBannerUrlV2(ParamBannerQuery paramBannerQuery, HttpServletRequest request, String channelTokenHeader) {
        Optional<Product> productOptional = productRepository.findById(paramBannerQuery.getProductId());
        paramBannerQuery.setUuid(channelTokenHeader);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (!"onShelves".equals(product.getStatus())) { // 产品是否上线
                return ResultBuilder.fail("产品已经下架");
            }

            //String channelToken = request.getHeader("channel-token");
            log.info("channelToken {}", channelTokenHeader);
            //根据token信息获取渠道信息，最近登录渠道信息
            Optional<ChannelObject> optional = accessService.getChannelObject(channelTokenHeader);
            if (!optional.isPresent()) {
                return ResultBuilder.fail("缺少渠道信息");
            }
//            product.setPage(paramBannerQuery.getPage());
//            product.setPos(paramBannerQuery.getPos());
            ChannelObject channelObject = optional.get();
            return toProductModelV2(paramBannerQuery, product, channelObject, request);//非联登产品
        } else {
//            return AjaxResult.error("product not found");
            return ResultBuilder.fail("产品信息不存在");
        }
    }

    private ResultModel toProductModelV2(ParamBannerQuery paramBannerQuery, Product product, ChannelObject channelObject, HttpServletRequest request) {
        insertPromoteLog(paramBannerQuery, product, channelObject);
        //判断联登，还是非联登
        //addClickProductScore(product, memberId, request);

        JSONObject dataMap = new JSONObject();
        dataMap.put("url", product.getApplyLink());
        dataMap.put("jump", product.getJumpType());
        return ResultBuilder.data(dataMap);
    }

    private void insertPromoteLog(ParamBannerQuery paramBannerQuery, Product product, ChannelObject channelObject) {

        ProductLog productExample = new ProductLog();
        productExample.setProductId(product.getId());

        Example<ProductLog> example = Example.of(productExample);
        long count = productLogRepository.count(example);
//        LocalDateTime now = DateUtils.nowLocalDateTime();
        ProductLog promoteLog = new ProductLog()
                .setAccessIp(channelObject.getAccessIp())
                .setAccessOs(channelObject.getOsName())
                .setChannelId(channelObject.getChannelId())//最近登录渠道ID
                .setChannelName(channelObject.getChannelName())//最近登录渠道名称
                .setUserId(0L)
                .setUuid(paramBannerQuery.getUuid())
                .setBannerId(paramBannerQuery.getId())
                .setProductId(product.getId())
                .setProductName(product.getProductName())
                .setIsFirstTo(count == 0 ? true : false)
                .setPage(paramBannerQuery.getPage())//访问位置
                .setPageName(BannerPageAndPosEnum.getPageDescByPage(paramBannerQuery.getPage()))//访问位置
                .setPos(paramBannerQuery.getPos())//访问位置
                .setPosName(BannerPageAndPosEnum.getPosDescByPos(paramBannerQuery.getPos()));//访问位置
        productLogRepository.save(promoteLog);
    }

    @Override
    public Object queryAll(AppQueryCriteria criteria, Pageable pageable) {
        Page<AdBanner> page = bannerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AdBanner resources) {
        //System.out.println(resources.toString());
        bannerRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AdBanner resources) {
        AdBanner banner = bannerRepository.findById(resources.getId()).orElseGet(AdBanner::new);
        ValidationUtil.isNull(banner.getId(), "AdBanner", "id", resources.getId());
        banner.copy(resources);
        bannerRepository.save(banner);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            bannerRepository.deleteById(id);
        }
    }
}
