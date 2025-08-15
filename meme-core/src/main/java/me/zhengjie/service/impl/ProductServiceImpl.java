/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.zhengjie.domain.HxUser;
import me.zhengjie.union.handler.AbstractProductDbMatchHandler;
import me.zhengjie.union.holder.LoanProductMatchHandlerHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.access.RedisCacheKey;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.HxUserReport;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.domain.Product;
import me.zhengjie.exception.CommentException;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.repository.HxUserReportRepository;
import me.zhengjie.repository.ProductChannelFilterRepository;
import me.zhengjie.repository.ProductRepository;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.IAccessService;
import me.zhengjie.service.ProductService;
import me.zhengjie.service.dto.ProductDto;
import me.zhengjie.service.mapstruct.ProductMapper;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.union.handler.AbstractProductDbMatchHandler;
import me.zhengjie.union.vo.MatchResp;
import me.zhengjie.union.vo.RegisterResp;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ChannelRepository channelRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileProperties properties;
    private final ChannelService channelService;
    private final HxUserReportRepository hxUserReportRepository;
    private final ProductChannelFilterRepository productChannelFilterRepository;
    @Resource
    private RedisUtils redisUtils;

    @Override
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


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatar(MultipartFile multipartFile) {
        // 文件大小验证
        FileUtil.checkSize(properties.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        File file = FileUtil.upload(multipartFile, properties.getPath().getAvatar());

        return new HashMap<String, String>(1) {{
            put("avatar", file.getName());
        }};
    }

    @Override
    public Object queryAll(AppQueryCriteria criteria, Pageable pageable) {

        Page<Product> page = productRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
//        return PageUtil.toPage(page.map(productMapper::toDto));
        return PageUtil.toPage(page);
    }

    @Override
    public List<ProductDto> queryListV2(AppQueryCriteria criteria, HttpServletRequest request) {
        String channelCode = request.getHeader("channel-code");//登录渠道编号
        String uuid = request.getHeader("uuid");

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
            return checkProduct(p, channel);
        }).collect(Collectors.toList());
        return productMapper.toDto(filterList).stream().map(p -> {
            p.setApplyNum(this.getApplyNum(p.getId().toString()));
            return p;
        }).collect(Collectors.toList());
    }

    @Override
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

    @Override
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
        String portStatus = "B";
        if ("yes".equals(hxUserReport.getApplyReport()) && "no".equals(hxUserReport.getBehaviorReport())) {
            log.info("只有申请雷达的全部拒，跳A面");
            portStatus = "A";
        } else {
            if ("yes".equals(hxUserReport.getBehaviorReport())) {
                if (ObjectUtil.isNotEmpty(hxUserReport.getOverdue())) {
                    //6个月内逾期笔数超过2笔，跳A面
                    log.info("6个月内逾期笔数超过2笔，跳A面 {}", hxUserReport.getOverdue());
                    Integer abc = Integer.valueOf(hxUserReport.getOverdue());
                    if (abc > 2) {
                        portStatus = "A";
                    } else {
                        portStatus = "B";
                    }
                } else {
                    portStatus = "B";
                }
            }
        }
        return portStatus;
    }

    @Override
    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id).orElseGet(Product::new);
        ValidationUtil.isNull(product.getId(), "Product", "id", id);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Product resources) {
        verification(resources);
        productRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Product resources) {
        verification(resources);
        Product product = productRepository.findById(resources.getId()).orElseGet(Product::new);
        ValidationUtil.isNull(product.getId(), "Product", "id", resources.getId());
        product.copy(resources);
        productRepository.save(product);
    }

    private void verification(Product resources) {
        String opt = "/opt";
        String home = "/home";
//        if (!(resources.getUploadPath().startsWith(opt) || resources.getUploadPath().startsWith(home))) {
//            throw new BadRequestException("文件只能上传在opt目录或者home目录 ");
//        }
//        if (!(resources.getDeployPath().startsWith(opt) || resources.getDeployPath().startsWith(home))) {
//            throw new BadRequestException("文件只能部署在opt目录或者home目录 ");
//        }
//        if (!(resources.getBackupPath().startsWith(opt) || resources.getBackupPath().startsWith(home))) {
//            throw new BadRequestException("文件只能备份在opt目录或者home目录 ");
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            productRepository.deleteById(id);
        }
    }

    @Override
    public Boolean checkProduct(Product product, Channel channel) {
        if ("uv".equals(product.getProductType())) {
            return true;
        } else {
            if ("zxh".equals(product.getProductCode())) {
                return checkHit(product);
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
                    log.info("{} {} loginUrl {}",product.getId(), product.getProductName(), loginUrl);
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
    }

    @Override
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

//        log.info("{} request {}; response {}", SecurityUtils.getCurrentUserPhoneByApp(), JSONUtil.toJsonStr(param), body);
//        if ("200".equals(result.getStr("code"))) {
//            String loginUrl = result.getStr("redirect_url");
//            if (ObjectUtil.isNotEmpty(loginUrl)) {
//                setUnionUrlByProduct(userId.toString(), product.getId().toString(), loginUrl);
//            }
//            return loginUrl;
//        }
        return null;
    }


    @Override
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
        JSONObject param = JSONUtil.createObj();
        param.put("md5Phone", DigestUtil.md5Hex(SecurityUtils.getCurrentUserPhoneByApp()));
        String url = "https://app.hfzbxx.cn/web/index.php?r=open/loan/check";
        HttpResponse response = HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(param), "application/json")
                .execute();
        int status = response.getStatus();
        String body = response.body();
        JSONObject result = JSONUtil.parseObj(body);
//        System.out.println(result);
        log.info("{} request {}; response {}", SecurityUtils.getCurrentUserPhoneByApp(), JSONUtil.toJsonStr(param), body);
        if ("200".equals(result.getStr("code"))) {
            return true;
        }
        return false;
    }

    @Override
    public String hitLogin(Product product) {
        Long userId = SecurityUtils.getCurrentUserIdByApp();
        String hitUrl = getUnionUrlByProduct(userId.toString(), product.getId().toString());
        if (ObjectUtil.isNotEmpty(hitUrl)) {
            return hitUrl;
        }

        JSONObject param = JSONUtil.createObj();
        param.put("phone", SecurityUtils.getCurrentUserPhoneByApp());
        param.put("scene", "7nIG3JVY");
        param.put("regChannel", "dd");
        String url = "https://app.hfzbxx.cn/web/index.php?r=open/loan/add";
        HttpResponse response = HttpRequest.post(url)
                .body(JSONUtil.toJsonStr(param), "application/json")
                .execute();
        int status = response.getStatus();
        String body = response.body();
        JSONObject result = JSONUtil.parseObj(body);
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
}
