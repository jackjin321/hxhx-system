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

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.HxSysConfig;
import me.zhengjie.exception.CommentException;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.repository.HxSysConfigRepository;
import me.zhengjie.service.HxSysConfigService;

import me.zhengjie.service.dto.HxSysConfigDto;
import me.zhengjie.service.dto.HxSysConfigQueryCriteria;
import me.zhengjie.service.mapstruct.HxSysConfigMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author me zhengjie
 * @website
 * @description 服务实现
 * @date 2023-06-12
 **/
@Service
@RequiredArgsConstructor
public class HxSysConfigServiceImpl implements HxSysConfigService {

    private final HxSysConfigRepository hxSysConfigRepository;
    private final HxSysConfigMapper hxSysConfigMapper;

    @Override
    public Map<String, Object> queryAll(HxSysConfigQueryCriteria criteria, Pageable pageable) {
        Page<HxSysConfig> page = hxSysConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(hxSysConfigMapper::toDto));
    }

    @Override
    public List<HxSysConfigDto> queryAll(HxSysConfigQueryCriteria criteria) {
        return hxSysConfigMapper.toDto(hxSysConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public HxSysConfigDto findById(Long id) {
        HxSysConfig hxSysConfig = hxSysConfigRepository.findById(id).orElseGet(HxSysConfig::new);
        ValidationUtil.isNull(hxSysConfig.getId(), "HxSysConfig", "id", id);
        return hxSysConfigMapper.toDto(hxSysConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HxSysConfigDto create(HxSysConfig resources) {
        //resources.setId(IdUtil.simpleUUID());
        if (hxSysConfigRepository.findByKey(resources.getKey()) != null) {
            throw new EntityExistException(HxSysConfig.class, "key", resources.getKey());
        }
        return hxSysConfigMapper.toDto(hxSysConfigRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HxSysConfig resources) {
        HxSysConfig hxSysConfig = hxSysConfigRepository.findById(resources.getId()).orElseGet(HxSysConfig::new);
        ValidationUtil.isNull(hxSysConfig.getId(), "HxSysConfig", "id", resources.getId());
        HxSysConfig hxSysConfig1 = null;
        hxSysConfig1 = hxSysConfigRepository.findByKey(resources.getKey());
        if (hxSysConfig1 != null && !hxSysConfig1.getId().equals(hxSysConfig.getId())) {
            throw new EntityExistException(HxSysConfig.class, "key", resources.getKey());
        }
        hxSysConfig.copy(resources);
        hxSysConfigRepository.save(hxSysConfig);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            hxSysConfigRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<HxSysConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HxSysConfigDto hxSysConfig : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("配置名称", hxSysConfig.getName());
            map.put("key", hxSysConfig.getKey());
            map.put("值", hxSysConfig.getValue());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public HxSysConfig findByKey(String key) {
        Optional<HxSysConfig> optionalConstant = hxSysConfigRepository.findFirstByKey(key);
        if (optionalConstant.isPresent()) {
            return optionalConstant.get();
        } else {
            throw new CommentException("constant not exists for key %s", key);
        }
    }
}