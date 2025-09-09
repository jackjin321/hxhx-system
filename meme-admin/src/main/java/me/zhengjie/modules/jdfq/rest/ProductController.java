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
import me.zhengjie.domain.Product;
import me.zhengjie.modules.jdfq.service.XfProductService;
import me.zhengjie.service.ProductService;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    private final XfProductService xfProductService;

    @PostMapping(value = "/uploadLogo")
    public ResponseEntity<Object> uploadLogo(@RequestParam MultipartFile file) {
        return new ResponseEntity<>(xfProductService.updateAvatar(file), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
//    @PreAuthorize("@el.check('product:list')")
    public ResponseEntity<Object> queryApp(AppQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(productService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增产品")
    @PostMapping
//    @PreAuthorize("@el.check('product:add')")
    public ResponseEntity<Object> createApp(@Validated @RequestBody Product resources) {
//        System.out.println(resources);
        productService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改产品")
    @PutMapping
//    @PreAuthorize("@el.check('product:edit')")
    public ResponseEntity<Object> updateApp(@Validated @RequestBody Product resources) {
        productService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除产品")
    @ApiOperation(value = "删除产品")
    @DeleteMapping
//    @PreAuthorize("@el.check('product:del')")
    public ResponseEntity<Object> deleteApp(@RequestBody Set<Long> ids) {
        productService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
