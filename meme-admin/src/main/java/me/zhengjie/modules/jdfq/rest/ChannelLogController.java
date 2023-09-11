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
package me.zhengjie.modules.jdfq.rest;

import lombok.RequiredArgsConstructor;
import me.zhengjie.service.ChannelLogService;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel/log")
public class ChannelLogController {

    private final ChannelLogService channelLogService;


    @GetMapping(value = "/list")
//    @PreAuthorize("@el.check('channel:list')")
    public ResponseEntity<Object> queryApp(AppQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(channelLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }
}
