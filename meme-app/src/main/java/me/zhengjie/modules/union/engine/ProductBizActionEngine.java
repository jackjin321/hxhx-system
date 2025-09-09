package me.zhengjie.modules.union.engine;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.access.RedisCacheKey;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.*;
import me.zhengjie.enums.PortStatusEnum;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.union.handler.AbstractProductDbMatchHandler;
import me.zhengjie.modules.union.holder.LoanProductMatchHandlerHolder;
import me.zhengjie.modules.union.vo.MatchResp;
import me.zhengjie.modules.union.vo.RegisterResp;
import me.zhengjie.repository.*;
import me.zhengjie.result.ResultBuilder;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.HxUserService;
import me.zhengjie.service.ProductService;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.service.dto.ProductDto;
import me.zhengjie.service.mapstruct.ProductMapper;
import me.zhengjie.utils.*;
import me.zhengjie.vo.ParamBannerQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductBizActionEngine {

    private final ProductService productService;
    private final ChannelService channelService;
    private final ChannelRepository channelRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final HxUserReportRepository hxUserReportRepository;
    private final HxUserRepository hxUserRepository;
    private final ProductLogRepository productLogRepository;

    private final ProductChannelFilterRepository productChannelFilterRepository;

    @Resource
    private RedisUtils redisUtils;



    public String getPortStatus(HxUserReport hxUserReport) {
        /**
         * 雷达报告通过，就是展示B面，不通过，就展示A面
         * 1、6个月内逾期笔数超过2笔，跳A面
         * 2、只有申请雷达的全部拒，跳A面
         * 3、有行为雷达的 全部通过，跳B面
         *
         * B22170025  近 6 个月 M0+ 逾期贷款笔数
         *
         */
        String portStatus = PortStatusEnum.PORT_B.getCode();
        if ("yes".equals(hxUserReport.getApplyReport()) && "no".equals(hxUserReport.getBehaviorReport())) {
            log.info("只有申请雷达的全部拒，跳A面");
            portStatus = PortStatusEnum.PORT_A.getCode();
        } else {
            if ("yes".equals(hxUserReport.getBehaviorReport())) {
                if (ObjectUtil.isNotEmpty(hxUserReport.getOverdue())) {
                    //6个月内逾期笔数超过2笔，跳A面
                    log.info("6个月内逾期笔数超过2笔，跳A面 {}", hxUserReport.getOverdue());
                    Integer abc = Integer.valueOf(hxUserReport.getOverdue());
                    if (abc > 2) {
                        portStatus = PortStatusEnum.PORT_A.getCode();
                    } else {
                        portStatus = PortStatusEnum.PORT_B.getCode();
                    }
                } else {
                    portStatus = PortStatusEnum.PORT_B.getCode();
                }
            }
        }
        return portStatus;
    }

    public List<ProductDto> queryList(AppQueryCriteria criteria, HttpServletRequest request) {
        String channelCode = request.getHeader("channel-code");//登录渠道编号
        String uuid = request.getHeader("uuid");
        //Long regChannelId = SecurityUtils.getRegChannelIdByApp();
        //String regChannelName = SecurityUtils.getRegChannelNameByApp();
//        Optional<Channel> channelOptional = channelRepository.findById(regChannelId);
//        Channel channel = null;
//        if (channelOptional.isPresent()) {
//            System.out.println("//注册渠道");
//            channel = channelOptional.get();//注册渠道
//        } else {
//            System.out.println("//登录渠道");
//            channel = channelService.getChannelInfo(channelCode, uuid);//登录渠道
//        }
        Channel channel = channelService.getChannelInfo(channelCode, uuid);//登录渠道
        //System.out.println(channel);
//        criteria.setPortStatus(channel.getPortStatus());
        //如果有雷达报告的，查一下雷达报告
        Long userId = SecurityUtils.getCurrentUserIdByApp();
        log.info("productList userId {}", userId);
        Optional<HxUserReport> reportOptional = hxUserReportRepository.findById(userId);
        String portStatus = channel.getPortStatus();
        if (reportOptional.isPresent()) {
            log.info("如果有雷达报告的，查一下雷达报告");
            HxUserReport hxUserReport = reportOptional.get();
            portStatus = this.getPortStatus(hxUserReport);
        }
        log.info("portStatus {}, channel.getPortStatus() {}", portStatus, channel.getPortStatus());
        criteria.setPortStatus(portStatus);
        criteria.setStatus("onShelves");
        List<Product> productList = productRepository.findByPortStatusAndStatusOrderBySortAsc(portStatus, "onShelves");
        return productMapper.toDto(productList).stream().map(p -> {
            p.setApplyNum(this.getApplyNum(p.getId().toString()));
            return p;
        }).collect(Collectors.toList());
    }

    public List<ProductDto> queryListV2(AppQueryCriteria criteria, HttpServletRequest request) {
        String channelCode = request.getHeader("channel-code");//登录渠道编号
        String uuid = request.getHeader("uuid");
        String realIP = IPUtils.getIpAddr(request);
        Channel channel = channelService.getChannelInfo(channelCode, uuid);//登录渠道
//        criteria.setPortStatus(channel.getPortStatus());
        //如果有雷达报告的，查一下雷达报告
        //Long userId = SecurityUtils.getCurrentUserIdByApp();
        //log.info("productList userId {}", userId);
        // Optional<HxUserReport> reportOptional = hxUserReportRepository.findById(userId);
        String portStatus = channel.getPortStatus();
//        if (reportOptional.isPresent()) {
//            log.info("如果有雷达报告的，查一下雷达报告");
//            HxUserReport hxUserReport = reportOptional.get();
//            portStatus = this.getPortStatus(hxUserReport);
//        }
        log.info("portStatus {}, channel.getPortStatus() {}", portStatus, channel.getPortStatus());
        criteria.setPortStatus(portStatus);
        criteria.setStatus("onShelves");
        List<Product> productList = productRepository.findByPortStatusAndStatusOrderBySortAsc(portStatus, "onShelves");
        List<Product> filterProductList = productList.stream().filter(product -> checkChannelFilter(product, channel)).collect(Collectors.toList());
        List<Product> filterList = filterProductList.stream().filter(p -> {
            log.info("check product {} ", p.getProductName());
            p.setIp(realIP);
            return checkProduct(p, channel);
        }).collect(Collectors.toList());
        return productMapper.toDto(filterList).stream().map(p -> {
            p.setApplyNum(this.getApplyNum(p.getId().toString()));
            return p;
        }).collect(Collectors.toList());
    }

    public Boolean checkProduct(Product product, Channel channel) {
        if ("uv".equals(product.getProductType())) {
            return true;
        } else {
            AbstractProductDbMatchHandler<?> handler = LoanProductMatchHandlerHolder.getHandler(product.getProductCode());
            if (Objects.isNull(handler)) {
                // 未配置联登处理器
                log.error("未配置联登处理器 {}", product.getProductCode());
                //throw new CommentException("未配置处理器,请联系商务");
                return false;
            }
            Long userId = SecurityUtils.getCurrentUserIdByApp();
            String loginUrl = getUnionUrlByProduct(userId.toString(), product.getId().toString());
            if (ObjectUtil.isNotEmpty(loginUrl)) {
                log.info("{} {} loginUrl {}", product.getId(), product.getProductName(), loginUrl);
                return true;
            }
            HxUser user = new HxUser();
            user.setUserId(SecurityUtils.getCurrentUserIdByApp());
            user.setPhone(SecurityUtils.getCurrentUserPhoneByApp());
            user.setChannelId(channel.getId());
            user.setPhoneMd5(DigestUtil.md5Hex(SecurityUtils.getCurrentUserPhoneByApp()));
            MatchResp resp = handler.matchAction(product, user);
            log.info("MatchResp {}", resp.isSucceed());
            if (resp.isSucceed()) {
                return true;
            }
            return false;
        }
    }

    public String loginProduct(Product product, Channel channel) {
        Long userId = SecurityUtils.getCurrentUserIdByApp();
        String loginUrl = getUnionUrlByProduct(userId.toString(), product.getId().toString());
        if (ObjectUtil.isNotEmpty(loginUrl)) {
            return loginUrl;
        }
        if ("uv".equals(product.getProductType())) {
            return product.getApplyLink();
        } else if ("union".equals(product.getProductType())) {
            AbstractProductDbMatchHandler<?> handler = LoanProductMatchHandlerHolder.getHandler(product.getProductCode());
            if (Objects.isNull(handler)) {
                // 未配置联登处理器
                log.error("未配置联登处理器 {}", product.getProductCode());
                //throw new CommentException("未配置处理器,请联系商务");
                return null;
            }
            HxUser user = new HxUser();
            user.setUserId(SecurityUtils.getCurrentUserIdByApp());
            user.setPhone(SecurityUtils.getCurrentUserPhoneByApp());
            user.setChannelId(channel.getId());
            user.setPhoneMd5(DigestUtil.md5Hex(SecurityUtils.getCurrentUserPhoneByApp()));
            RegisterResp resp = handler.registerAction(product, user);
            log.info("MatchResp {}", resp.isSucceed());
            if (resp.isSucceed()) {
                setUnionUrlByProduct(userId.toString(), product.getId().toString(), resp.getRedirectUrl());
                return resp.getRedirectUrl();
            }
        }
        return null;
    }


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
        Channel channelFinal = channel;
        List<Product> filterProductList = productList.stream().filter(product -> this.checkChannelFilter(product, channelFinal)).collect(Collectors.toList());
        List<Product> filterList = filterProductList.stream().filter(p -> {
            return this.checkProduct(p, channelFinal);
        }).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(filterList) && filterList.size() > 0) {
            Product product = filterList.get(0);

            JSONObject dataMap = new JSONObject();
//            dataMap.put("url", product.getApplyLink());
            dataMap.put("url", this.loginProduct(product, channelFinal));
            insertProductLog(userId, product, channel, paramBanner);
            return ResultBuilder.data(dataMap);
        } else {
            return ResultBuilder.fail("产品信息不存在");
        }
    }
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
            dataMap.put("url", this.loginProduct(product, channel));

            //Channel channel = channelService.getChannelInfo(paramBanner.getChannelCode(), paramBanner.getUuid());
            insertProductLog(userId, product, channel, paramBanner);
            return ResultBuilder.data(dataMap);
        } else {
            return ResultBuilder.fail("产品信息不存在");
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



    public String getUnionUrlByProduct(String userId, String productId) {
        String applyNumKey = RedisCacheKey.productUnionLinkKey(userId, productId);
        String cacheCode = (String) redisUtils.get(applyNumKey);
        return cacheCode;
    }

    public Boolean setUnionUrlByProduct(String userId, String productId, String url) {
        String applyNumKey = RedisCacheKey.productUnionLinkKey(userId, productId);
        //String cacheCode = (String) redisUtils.get(applyNumKey);
        return redisUtils.set(applyNumKey, url);
    }


    public Integer getApplyNum(String productId) {
        //查redis，没有，随机生成一个，存到redis，每次访问随机添加1
        String applyNumKey = RedisCacheKey.productApplyNumKey(productId);
        String cacheCode = (String) redisUtils.get(applyNumKey);
        if (ObjectUtil.isAllEmpty(cacheCode)) {
            Integer abc = RandomUtil.randomOneNumber(12345) + 1;
            cacheCode = abc.toString();
            redisUtils.set(applyNumKey, cacheCode);
        } else {
            Integer bcd = Integer.valueOf(cacheCode) + 1;
            redisUtils.set(applyNumKey, bcd.toString());
        }
        cacheCode = (String) redisUtils.get(applyNumKey);
        return Integer.valueOf(cacheCode);
    }


    public Boolean checkChannelFilter(Product product, Channel channel) {
        int exist = productChannelFilterRepository.countByProductIdAndChannelId(product.getId(), channel.getId());
        if (exist > 0) {
            return false;
        }
        return true;
    }




    private Boolean checkHit(Product product) {
        Long userId = SecurityUtils.getCurrentUserIdByApp();
        String hitUrl = getUnionUrlByProduct(userId.toString(), product.getId().toString());
        if (ObjectUtil.isNotEmpty(hitUrl)) {
            return true;
        }
        cn.hutool.json.JSONObject param = JSONUtil.createObj();
        param.put("md5Phone", DigestUtil.md5Hex(SecurityUtils.getCurrentUserPhoneByApp()));
        String url = "https://app.hfzbxx.cn/web/index.php?r=open/loan/check";
        HttpResponse response = HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(param), "application/json")
                .execute();
        int status = response.getStatus();
        String body = response.body();
        cn.hutool.json.JSONObject result = JSONUtil.parseObj(body);
//        System.out.println(result);
        log.info("{} request {}; response {}", SecurityUtils.getCurrentUserPhoneByApp(), JSONUtil.toJsonStr(param), body);
        if ("200".equals(result.getStr("code"))) {
            return true;
        }
        return false;
    }

    public String hitLogin(Product product) {
        Long userId = SecurityUtils.getCurrentUserIdByApp();
        String hitUrl = getUnionUrlByProduct(userId.toString(), product.getId().toString());
        if (ObjectUtil.isNotEmpty(hitUrl)) {
            return hitUrl;
        }

        cn.hutool.json.JSONObject param = JSONUtil.createObj();
        param.put("phone", SecurityUtils.getCurrentUserPhoneByApp());
        param.put("scene", "7nIG3JVY");
        param.put("regChannel", "dd");
        String url = "https://app.hfzbxx.cn/web/index.php?r=open/loan/add";
        HttpResponse response = HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(param), "application/json")
                .execute();
        int status = response.getStatus();
        String body = response.body();
        cn.hutool.json.JSONObject result = JSONUtil.parseObj(body);
//        System.out.println(result);
        log.info("{} request {}; response {}", SecurityUtils.getCurrentUserPhoneByApp(), JSONUtil.toJsonStr(param), body);
        if ("200".equals(result.getStr("code"))) {
            String loginUrl = result.getStr("redirect_url");
            if (ObjectUtil.isNotEmpty(loginUrl)) {
                setUnionUrlByProduct(userId.toString(), product.getId().toString(), loginUrl);
            }
            return loginUrl;
        }
        return null;
    }


    public String findOneByLogin(ParamBannerQuery paramBanner) {
        //Long userId = SecurityUtils.getCurrentUserIdByApp();
        //Long regChannelId = SecurityUtils.getRegChannelIdByApp();
        //Optional<Channel> channelOptional = channelRepository.findById(regChannelId);
        Channel channel = channelService.getChannelInfo(paramBanner.getChannelCode(), paramBanner.getUuid());
        List<Product> productList = productRepository.findByPortStatusAndStatusOrderBySortAsc(channel.getPortStatus(), "onShelves");
        List<Product> filterList = productList.stream().filter(p -> {
            return this.checkProduct(p, channel);
        }).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(filterList) && filterList.size() > 0) {
            Product product = filterList.get(0);
            String url = this.hitLogin(product);
            insertProductLog(paramBanner.getUserId(), product, channel, paramBanner);
            return url;
        } else {
            return null;
        }
    }
}
