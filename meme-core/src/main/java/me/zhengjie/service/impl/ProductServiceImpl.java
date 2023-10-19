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
import lombok.RequiredArgsConstructor;
import me.zhengjie.access.RedisCacheKey;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.Channel;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.domain.Product;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.repository.ProductRepository;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.IAccessService;
import me.zhengjie.service.ProductService;
import me.zhengjie.service.dto.ProductDto;
import me.zhengjie.service.mapstruct.ProductMapper;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ProductServiceImpl implements ProductService {

    private final ChannelRepository channelRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileProperties properties;
    private final ChannelService channelService;

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
        System.out.println(channel);
        criteria.setPortStatus(channel.getPortStatus());
        criteria.setStatus("onShelves");
        List<Product> productList = productRepository.findByPortStatusAndStatusOrderBySortAsc(channel.getPortStatus(), "onShelves");
        return productMapper.toDto(productList).stream().map(p -> {
            p.setApplyNum(this.getApplyNum(p.getId().toString()));
            return p;
        }).collect(Collectors.toList());
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
}
