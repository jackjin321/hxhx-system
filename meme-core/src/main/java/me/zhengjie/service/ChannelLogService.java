package me.zhengjie.service;

import me.zhengjie.domain.ChannelLog;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChannelLogService {


    Object queryAll(AppQueryCriteria criteria, Pageable pageable);
    List<ChannelLog> queryAllV2(AppQueryCriteria criteria);

}
