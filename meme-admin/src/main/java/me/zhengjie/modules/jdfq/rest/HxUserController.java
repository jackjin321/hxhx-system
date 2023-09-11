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

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.mnt.websocket.MsgType;
import me.zhengjie.modules.mnt.websocket.SocketMsg;
import me.zhengjie.service.HxUserService;
import me.zhengjie.service.dto.HxUserDtoV2;
import me.zhengjie.service.dto.HxUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author me zhengjie
 * @website
 * @date 2023-06-12
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "用户管理管理")
@RequestMapping("/api/hxUser")
public class HxUserController {

    private final HxUserService hxUserService;


    @GetMapping(value = "/list")
    @Log("查询用户管理")
    @ApiOperation("查询用户管理")
//    @PreAuthorize("@el.check('hxUser:list')")
    public ResponseEntity<Object> queryHxUser(HxUserQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(hxUserService.queryAll(criteria, pageable), HttpStatus.OK);
    }


}