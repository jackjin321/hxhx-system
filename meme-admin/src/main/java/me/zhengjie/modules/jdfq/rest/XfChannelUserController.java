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

import me.zhengjie.annotation.Log;
import me.zhengjie.domain.XfChannelUser;
import me.zhengjie.modules.jdfq.service.XfChannelUserService;
import me.zhengjie.service.dto.XfChannelUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

/**
* @website https://eladmin.vip
* @author zhengjie
* @date 2025-06-15
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "渠道用户管理")
@RequestMapping("/api/xfChannelUser")
public class XfChannelUserController {

    private final XfChannelUserService xfChannelUserService;

//    @Log("导出数据")
//    @ApiOperation("导出数据")
//    @GetMapping(value = "/download")
//    @PreAuthorize("@el.check('xfChannelUser:list')")
//    public void exportXfChannelUser(HttpServletResponse response, XfChannelUserQueryCriteria criteria) throws IOException {
//        xfChannelUserService.download(xfChannelUserService.queryAll(criteria), response);
//    }

    @GetMapping
    @Log("查询渠道用户")
    @ApiOperation("查询渠道用户")
    @PreAuthorize("@el.check('xfChannelUser:list')")
    public ResponseEntity<Object> queryXfChannelUser(XfChannelUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(xfChannelUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增渠道用户")
    @ApiOperation("新增渠道用户")
    @PreAuthorize("@el.check('xfChannelUser:add')")
    public ResponseEntity<Object> createXfChannelUser(@Validated @RequestBody XfChannelUser resources){
        return new ResponseEntity<>(xfChannelUserService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改渠道用户")
    @ApiOperation("修改渠道用户")
    @PreAuthorize("@el.check('xfChannelUser:edit')")
    public ResponseEntity<Object> updateXfChannelUser(@Validated @RequestBody XfChannelUser resources){
        xfChannelUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除渠道用户")
    @ApiOperation("删除渠道用户")
    @PreAuthorize("@el.check('xfChannelUser:del')")
    public ResponseEntity<Object> deleteXfChannelUser(@RequestBody Long[] ids) {
        xfChannelUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}