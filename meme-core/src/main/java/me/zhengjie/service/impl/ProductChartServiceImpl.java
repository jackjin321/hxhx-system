package me.zhengjie.service.impl;


import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.access.DateUtils;
import me.zhengjie.domain.Product;
import me.zhengjie.domain.ProductChart;
import me.zhengjie.domain.ProductLog;
import me.zhengjie.repository.ProductChartRepository;
import me.zhengjie.repository.ProductLogRepository;
import me.zhengjie.repository.ProductRepository;
import me.zhengjie.service.ProductChartService;
import me.zhengjie.service.ProductLogService;
import me.zhengjie.service.dto.ProductQueryCriteria;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductChartServiceImpl implements ProductChartService {

    private final ProductChartRepository productChartRepository;
    private final ProductRepository productRepository;
    private final ProductLogRepository productLogRepository;
    private final ProductLogService productLogService;

    @Override
    public ProductChart todayTotalList() {
        List<Product> productList = productRepository.findAll();
        LocalDate localDate = DateUtils.nowLocalDate();
        ProductChart totalChart = new ProductChart();
        totalChart.setTodayUv(Long.valueOf("0"));
        totalChart.setTodayPv(Long.valueOf("0"));
        totalChart.setFirstTo(Long.valueOf("0"));
        productList.forEach(item -> {
            ProductChart promoteChart = createChart(localDate, item);
            totalChart.setTodayPv(totalChart.getTodayPv() + promoteChart.getTodayPv());
            totalChart.setTodayUv(totalChart.getTodayUv() + promoteChart.getTodayUv());
            totalChart.setFirstTo(totalChart.getFirstTo() + promoteChart.getFirstTo());
        });
        return totalChart;
    }

    @Override
    public Map<String, Object> findByQueryToday(ProductQueryCriteria criteria, Pageable pageable) {
        Page<Product> productPage = productRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        LocalDate localDate = DateUtils.nowLocalDate();
        return PageUtil.toPage(productPage.map(p -> createChart(localDate, p)));
    }

    @Override
    public Map<String, Object> findByQueryHistory(ProductQueryCriteria criteria, Pageable pageable) {
        Page<ProductChart> page = productChartRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    public ProductChart createChart(LocalDate localDate, Product product) {
        AppQueryCriteria criteria = new AppQueryCriteria();
        List<Timestamp> abc = new ArrayList<>();
        Date ddd = new Date();//当天结算
//        Timestamp startTime = new Timestamp(DateUtil.offsetDay(ddd, -5).getTime());
        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(ddd).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(ddd).getTime());
        abc.add(startTime);
        abc.add(endTime);
        criteria.setCreateTime(abc);
        criteria.setProductId(product.getId());

//        List<ProductLog> promoteLogList = productLogService.promoteLogList(product, localDate);
        List<ProductLog> promoteLogList = productLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        Set<ProductLog> promoteLogSet = new TreeSet<>(promoteLogList);
        long firstToCount = promoteLogList.stream().filter(p -> p.getIsFirstTo()).count();

        List<String> accessIps = promoteLogList.stream().map(ProductLog::getAccessIp).collect(Collectors.toList());

        List<String> abcIps = accessIps.stream().distinct().collect(Collectors.toList());

        ProductChart promoteChart = new ProductChart();
        promoteChart.setPriceType(product.getPriceType());

        promoteChart.setIpPv(Long.valueOf(accessIps.size()));
        promoteChart.setIpUv(Long.valueOf(abcIps.size()));

        promoteChart.setProductId(product.getId());
        promoteChart.setProductName(product.getProductName());
        promoteChart.setTodayPv(Long.valueOf(promoteLogList.size()));

        promoteChart.setTodayUv(Long.valueOf(promoteLogSet.size()));//uv有差

        promoteChart.setFirstTo(firstToCount);
        promoteChart.setPrice(product.getPrice());

//        BigDecimal profit = BigDecimal.valueOf(promoteLogSet.size()).multiply(new BigDecimal(product.getPrice()));
//        promoteChart.setProfit(profit);

//        if (product.getPriceType().equals(2)) {
//            promoteChart.setProfit(BigDecimal.ZERO);
//        }
        return promoteChart;
    }
}
