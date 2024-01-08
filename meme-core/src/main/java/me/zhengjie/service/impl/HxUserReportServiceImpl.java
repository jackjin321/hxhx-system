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
import me.zhengjie.domain.HxUser;
import me.zhengjie.domain.HxUserReport;
import me.zhengjie.domain.Product;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.repository.HxUserReportRepository;
import me.zhengjie.repository.ProductRepository;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.HxUserReportService;
import me.zhengjie.service.ProductService;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.service.dto.HxUserQueryCriteria;
import me.zhengjie.service.dto.ProductDto;
import me.zhengjie.service.mapstruct.ProductMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HxUserReportServiceImpl implements HxUserReportService {

    private final HxUserReportRepository hxUserReportRepository;

//
//    @Resource
//    private RedisUtils redisUtils;

    @Override
    public Map<String, Object> queryAll(HxUserQueryCriteria criteria, Pageable pageable) {
        Page<HxUserReport> page = hxUserReportRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(p -> {
            if (ObjectUtil.isAllNotEmpty(p.getPhone())) {
                p.setPhone(CommonUtil.signPhone(p.getPhone()));
            }
            return p;
        }));
    }

}
