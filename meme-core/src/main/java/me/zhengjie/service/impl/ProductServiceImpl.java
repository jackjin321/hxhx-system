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
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import me.zhengjie.domain.HxUser;
import me.zhengjie.enums.PortStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.access.RedisCacheKey;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.HxUserReport;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.domain.Product;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.repository.HxUserReportRepository;
import me.zhengjie.repository.ProductChannelFilterRepository;
import me.zhengjie.repository.ProductRepository;
import me.zhengjie.service.ChannelService;
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
import java.io.File;
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

    private final ChannelService channelService;

    @Resource
    private RedisUtils redisUtils;




    @Override
    public Object queryAll(AppQueryCriteria criteria, Pageable pageable) {

        Page<Product> page = productRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
//        return PageUtil.toPage(page.map(productMapper::toDto));
        return PageUtil.toPage(page);
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
