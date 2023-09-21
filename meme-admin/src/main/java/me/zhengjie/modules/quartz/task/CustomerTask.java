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
package me.zhengjie.modules.quartz.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.*;
import me.zhengjie.repository.*;
import me.zhengjie.service.ChannelChartService;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.HxUserService;
import me.zhengjie.service.ProductChartService;
import me.zhengjie.utils.date.DateUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2019-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerTask {

    private final ChannelRepository channelRepository;
    private final ProductRepository productRepository;
    private final ChannelChartService channelChartService;
    private final ChannelChartRepository channelChartRepository;
    private final ProductChartService productChartService;
    private final ProductChartRepository productChartRepository;

    public void runChannel() {
        Date ddd = new Date();//当天结算

        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(DateUtil.offsetDay(ddd, -1)).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(DateUtil.offsetDay(ddd, -1)).getTime());
        log.info("run 渠道统计 开始实现 {} {}", startTime.toString(), endTime.toString());
        Map<Long, BigDecimal> productMap = productRepository.findAll().stream().collect(Collectors.toMap(Product::getId, Product::getPrice));

        List<ChannelChart> channelCharts = new ArrayList<>();
        List<Channel> channelList = channelRepository.findAll();
        if (channelList.size() > 0) {
            channelList.forEach(channel -> {
                ChannelChart channelChart = channelChartService.createChart(startTime, endTime, channel, productMap);
                channelCharts.add(channelChart);
            });
        }
        channelChartRepository.saveAll(channelCharts);
        log.info("run 执行成功");
    }

    public void runProduct() {
        List<Product> productList = productRepository.findAll();

        Date ddd = new Date();//当天结算
        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(DateUtil.offsetDay(ddd, -1)).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(DateUtil.offsetDay(ddd, -1)).getTime());

        log.info("run 产品统计 开始实现 {} {}", startTime.toString(), endTime.toString());
        List<ProductChart> productCharts = new ArrayList<>();
        productList.forEach(product -> {
            ProductChart promoteChart = productChartService.createChart(startTime, endTime, product);
            productCharts.add(promoteChart);
        });
        productChartRepository.saveAll(productCharts);
        log.info("run 执行成功");
    }

    public void run1(String str) {
        log.info("run1 执行成功，参数为： {}" + str);
    }

    public void run2() {
        log.info("run2 执行成功2");
    }
}
