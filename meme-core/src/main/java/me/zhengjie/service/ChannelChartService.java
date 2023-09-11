package me.zhengjie.service;


import me.zhengjie.domain.ChannelChart;
import me.zhengjie.service.dto.ChannelQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ChannelChartService {

    Map<String, Object> findByQueryToday(ChannelQueryCriteria criteria, Pageable pageable);

    Map<String, Object> findByQueryHistory(ChannelQueryCriteria criteria, Pageable pageable);

//    List<ChannelChart> queryAll(ChannelQueryCriteria criteria);

//    void download(List<ChannelChart> queryAll, HttpServletResponse response) throws IOException;

    ChannelChart todayTotalList();
}
