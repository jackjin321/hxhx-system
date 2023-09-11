package me.zhengjie.modules.jdfq.rest;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.AdBanner;
import me.zhengjie.enums.BannerPageAndPosEnum;
import me.zhengjie.service.BannerService;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banner")
public class BannerController {


    private final BannerService bannerService;

    @GetMapping(value = "/list")
    public ResponseEntity<Object> queryApp(AppQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(bannerService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 查询【广告位页面】列表
     *
     * @return
     */
    @GetMapping("/getAllPageList")
    public ResponseEntity<Object> getAllPageList() {
        return new ResponseEntity<>(BannerPageAndPosEnum.getAllPageList(), HttpStatus.OK);
    }

    /**
     * 查询【广告位位置】列表
     *
     * @return
     */
    @GetMapping("/getAllPosList")
    public ResponseEntity<Object> getAllPosList() {
        return new ResponseEntity<>(BannerPageAndPosEnum.getAllPosList(), HttpStatus.OK);
    }

    /**
     * 根据页面查询【广告位位置】列表
     * @param page 广告位页面
     * @return
     */
    @GetMapping("/getPosListByPage")
    public ResponseEntity<Object> getPosListByPage(@RequestParam("page") String page) {
        return new ResponseEntity<>(BannerPageAndPosEnum.getPosListByPage(page), HttpStatus.OK);
    }

    @Log("新增广告位")
    @PostMapping
    public ResponseEntity<Object> createApp(@Validated @RequestBody AdBanner resources) {
        bannerService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改广告位")
    @PutMapping
    public ResponseEntity<Object> updateApp(@Validated @RequestBody AdBanner resources) {
        bannerService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("广告位")
    @ApiOperation(value = "广告位")
    @DeleteMapping
//    @PreAuthorize("@el.check('product:del')")
    public ResponseEntity<Object> deleteApp(@RequestBody Set<Long> ids) {
        bannerService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
