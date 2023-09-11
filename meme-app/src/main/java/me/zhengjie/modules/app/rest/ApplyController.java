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
package me.zhengjie.modules.app.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.config.FileProperties;
import me.zhengjie.domain.EmailConfig;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.domain.vo.*;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.LocalStorageRepository;
import me.zhengjie.service.EmailService;

import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("apply")
@Api(tags = "APP接口")
@Slf4j
public class ApplyController {

    private final FileProperties properties;
    private final LocalStorageRepository localStorageRepository;

    @Log("APP上传图片")
    @ApiOperation("上传图片")
    @PostMapping("/pictures")
    public ResponseEntity<Object> uploadPicture(@RequestParam MultipartFile multipartFile) {
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        if (!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))) {
            throw new BadRequestException("only upload image");
        }
        LocalStorage localStorage = null;
        String name = null;
        FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        //String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException("upload image fail");
        }
        try {
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            localStorage = new LocalStorage(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize())
            );
            localStorageRepository.save(localStorage);
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
        localStorage.setPath(properties.getUrlPrefix() + localStorage.getRealName());
        return new ResponseEntity<>(localStorage, HttpStatus.OK);
    }
}
