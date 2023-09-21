package me.zhengjie.service;

import me.zhengjie.domain.Product;
import me.zhengjie.domain.ProductChart;
import me.zhengjie.service.dto.ProductQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Map;

public interface ProductChartService {

    Map<String, Object> findByQueryToday(ProductQueryCriteria criteria, Pageable pageable);
    Map<String, Object> findByQueryHistory(ProductQueryCriteria criteria, Pageable pageable);
    ProductChart todayTotalList();
    ProductChart createChart(Timestamp startTime, Timestamp endTime, Product product);
}
