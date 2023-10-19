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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.HxSysConfig;
import me.zhengjie.service.HxSysConfigService;
import me.zhengjie.service.dto.HxSysConfigQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @website
* @author me zhengjie
* @date 2023-06-12
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统配置管理")
@RequestMapping("/api/sys/config")
public class HxSysConfigController {

    private final HxSysConfigService hxSysConfigService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
//    @PreAuthorize("@el.check('hxSysConfig:list')")
    public void exportHxSysConfig(HttpServletResponse response, HxSysConfigQueryCriteria criteria) throws IOException {
        hxSysConfigService.download(hxSysConfigService.queryAll(criteria), response);
    }

    @GetMapping(value = "/list")
    @Log("查询系统配置")
    @ApiOperation("查询系统配置")
//    @PreAuthorize("@el.check('hxSysConfig:list')")
    public ResponseEntity<Object> queryHxSysConfig(HxSysConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(hxSysConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    @Log("新增系统配置")
    @ApiOperation("新增系统配置")
//    @PreAuthorize("@el.check('hxSysConfig:add')")
    public ResponseEntity<Object> createHxSysConfig(@Validated @RequestBody HxSysConfig resources){
        return new ResponseEntity<>(hxSysConfigService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "edit")
    @Log("修改系统配置")
    @ApiOperation("修改系统配置")
//    @PreAuthorize("@el.check('hxSysConfig:edit')")
    public ResponseEntity<Object> updateHxSysConfig(@Validated @RequestBody HxSysConfig resources){
        hxSysConfigService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除系统配置")
    @ApiOperation("删除系统配置")
//    @PreAuthorize("@el.check('hxSysConfig:del')")
    public ResponseEntity<Object> deleteHxSysConfig(@RequestBody Long[] ids) {
        hxSysConfigService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}