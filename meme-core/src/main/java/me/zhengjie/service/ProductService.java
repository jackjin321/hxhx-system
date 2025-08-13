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
package me.zhengjie.service;

import me.zhengjie.domain.Channel;
import me.zhengjie.domain.HxUserReport;
import me.zhengjie.domain.Product;
import me.zhengjie.result.ResultModel;
import me.zhengjie.service.dto.ProductDto;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
public interface ProductService {

    Boolean checkProduct(Product product);
    Boolean checkChannelFilter(Product product, Channel channel);
    String hitLogin(Product product);
    String getPortStatus(HxUserReport userReport);

    Integer getApplyNum(String productId);

    Map<String, String> updateAvatar(MultipartFile file);

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    Object queryAll(AppQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    List<ProductDto> queryList(AppQueryCriteria criteria, HttpServletRequest request);
    List<ProductDto> queryListV2(AppQueryCriteria criteria, HttpServletRequest request);

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    ProductDto findById(Long id);


    /**
     * 创建
     *
     * @param resources /
     */
    void create(Product resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Product resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void delete(Set<Long> ids);

}
