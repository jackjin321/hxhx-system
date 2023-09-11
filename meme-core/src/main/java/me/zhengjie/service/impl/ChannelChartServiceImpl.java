package me.zhengjie.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.access.DateUtils;
import me.zhengjie.domain.*;
import me.zhengjie.repository.ChannelChartRepository;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.repository.ProductLogRepository;
import me.zhengjie.repository.ProductRepository;
import me.zhengjie.service.ChannelChartService;
import me.zhengjie.service.ChannelLogService;
import me.zhengjie.service.dto.ChannelQueryCriteria;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelChartServiceImpl implements ChannelChartService {

    private final ChannelChartRepository channelChartRepository;

    private final ChannelRepository channelRepository;

    private final ProductLogRepository productLogRepository;

    private final ProductRepository productRepository;

    @Autowired
    private ChannelLogService channelLogService;


    @Override
    public ChannelChart todayTotalList() {
        List<Channel> channelList = channelRepository.findAll();
        LocalDate localDate = DateUtils.nowLocalDate();
        ChannelChart totalChart = new ChannelChart();

        totalChart.setTodayUv(Long.valueOf("0"));
        totalChart.setTodayPv(Long.valueOf("0"));
//        totalChart.setTodayRegister(Long.valueOf("0"));
        totalChart.setProductPv(Long.valueOf("0"));
        totalChart.setProductUv(Long.valueOf("0"));

        Map<Long, BigDecimal> productMap = productRepository.findAll().stream().collect(Collectors.toMap(Product::getId, Product::getPrice));


        channelList.forEach(item -> {
            ChannelChart channelChart = createChart(localDate, item, productMap);

            totalChart.setTodayPv(totalChart.getTodayPv() + channelChart.getTodayPv());
            totalChart.setTodayUv(totalChart.getTodayUv() + channelChart.getTodayUv());
//            totalChart.setTodayRegister(totalChart.getTodayRegister() + channelChart.getTodayRegister());

        });

        //产品相关的
//        ProductChart totalProductChart = pro.todayTotalList();
//        totalChart.setProductUv(totalProductChart.getTodayPv());
//        totalChart.setProductUv(totalProductChart.getTodayUv());

        return totalChart;
    }

    @Override
    public Map<String, Object> findByQueryToday(ChannelQueryCriteria criteria, Pageable pageable) {
        Page<Channel> channelPage = channelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        LocalDate localDate = DateUtils.nowLocalDate();
        Map<Long, BigDecimal> productMap = productRepository.findAll().stream().collect(Collectors.toMap(Product::getId, Product::getPrice));

        return PageUtil.toPage(channelPage.map(p -> createChart(localDate, p, productMap)));
    }

    @Override
    public Map<String, Object> findByQueryHistory(ChannelQueryCriteria criteria, Pageable pageable) {
        Page<ChannelChart> page = channelChartRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    public ChannelChart createChart(LocalDate localDate, Channel channel,
                                    Map<Long, BigDecimal> productPriceMap) {

        AppQueryCriteria criteria = new AppQueryCriteria();
        List<Timestamp> abc = new ArrayList<>();
        Date ddd = new Date();//当天结算
//        Timestamp startTime = new Timestamp(DateUtil.offsetDay(ddd, -5).getTime());
        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(ddd).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(ddd).getTime());
        abc.add(startTime);
        abc.add(endTime);
        criteria.setCreateTime(abc);
        criteria.setChannelId(channel.getId());

        List<ChannelLog> channelLogs = channelLogService.queryAllV2(criteria);//PV统计
        Set<ChannelLog> channelLogSet = new TreeSet<>(channelLogs);//UV统计
        long todayRegister = channelLogs.stream().filter(p -> p.getIsRegister()).count();//注册数

        ChannelChart channelChart = new ChannelChart();
        channelChart.setChannelId(channel.getId());
        channelChart.setChannelName(channel.getChannelName());
        channelChart.setTodayUv(Long.valueOf(channelLogSet.size()));//UV统计
        channelChart.setTodayPv(Long.valueOf(channelLogs.size()));//PV统计
//        channelChart.setTodayRegister(todayRegister);
//
//
//        List<ProductLog> promoteLogList = promoteLogList(criteria, channel);//产品点击日志
//        Set<ProductLog> promoteLogSet = new TreeSet<>(promoteLogList);
//        long firstTo = promoteLogList.stream().filter(p -> p.getIsFirstTo()).count();
//
//        //注册率=注册数/uv数
//        //产出值=产出uv/uv数
//        channelChart.setProductPv(Long.valueOf(promoteLogList.size()));//产品PV
//        channelChart.setProductUv(Long.valueOf(promoteLogSet.size()));//产品UV，渠道到产品UV
//        channelChart.setProductFirstTo(firstTo);


        return channelChart;
    }

    /**
     * 产品点击
     *
     * @param criteria
     * @param channel
     * @return
     */
    private List<ProductLog> promoteLogList(AppQueryCriteria criteria, Channel channel) {


        List<ProductLog> productLogList = productLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return productLogList;
    }


}
