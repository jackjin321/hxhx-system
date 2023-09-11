package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.ChannelLog;
import me.zhengjie.repository.ChannelLogRepository;
import me.zhengjie.service.ChannelLogService;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelLogServiceImpl implements ChannelLogService {

    private final ChannelLogRepository channelLogRepository;

    @Override
    public Object queryAll(AppQueryCriteria criteria, Pageable pageable) {
        Page<ChannelLog> page = channelLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<ChannelLog> queryAllV2(AppQueryCriteria criteria) {
        List<ChannelLog> channelLogs = channelLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return channelLogs;
    }

}

