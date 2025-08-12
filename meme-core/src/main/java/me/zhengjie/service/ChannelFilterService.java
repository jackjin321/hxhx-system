package me.zhengjie.service;

import me.zhengjie.domain.ProductChannelFilter;
import me.zhengjie.service.dto.ChannelFilterQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

public interface ChannelFilterService {

    Map<String, Object> queryAll(ChannelFilterQueryCriteria criteria, Pageable pageable);

    Map<String, Object> queryAllV2(ChannelFilterQueryCriteria criteria, Pageable pageable);

    Map<String, Object> queryAllV3(ChannelFilterQueryCriteria criteria, Pageable pageable);

    void create(ProductChannelFilter resources);

    void delete(Set<Long> ids);
}
