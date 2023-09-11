package me.zhengjie.service;

import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;

public interface ProductLogService {

    Object queryAll(AppQueryCriteria criteria, Pageable pageable);

}
