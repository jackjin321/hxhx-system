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

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Channel;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.ChannelService;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ChannelController {

    private final ChannelService channelService;


    @GetMapping(value = "/list")
//    @PreAuthorize("@el.check('channel:list')")
    public ResponseEntity<Object> queryApp(AppQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(channelService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增渠道")
    @PostMapping
//    @PreAuthorize("@el.check('channel:add')")
    public ResponseEntity<Object> createApp(@Validated @RequestBody Channel resources) {
        if (verifyBuckle(resources)) {
            throw new BadRequestException("RegisterBuckleRate is out of limits");
        }
        channelService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改渠道")
    @PutMapping
//    @PreAuthorize("@el.check('channel:edit')")
    public ResponseEntity<Object> updateApp(@Validated @RequestBody Channel resources) {
        if (verifyBuckle(resources)) {
            throw new BadRequestException("RegisterBuckleRate is out of limits");
        }
        channelService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @Log("删除渠道")
//    @ApiOperation(value = "删除渠道")
//    @DeleteMapping
////    @PreAuthorize("@el.check('product:del')")
//    public ResponseEntity<Object> deleteApp(@RequestBody Set<Long> ids) {
//        if(ids.contains(1L) || ids.contains(13L) || ids.contains(8L)){
//            throw new BadRequestException("禁止删除APP渠道信息");
//        }
//        channelService.delete(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }


    /**
     * 检验 注册扣量比率 or 进件扣量比例 or 单价
     *
     * @param channel channel
     * @return boolean
     */
    private boolean verifyBuckle(Channel channel) {
        double reg = Double.parseDouble(channel.getRegisterBuckleRate());
//        double into = Double.parseDouble(channel.getIntoBuckleRate());

//        return ((reg > 1 || reg < 0) || (into > 1 || into < 0));
        return (reg > 1 || reg < 0);
    }
}
