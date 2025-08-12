package me.zhengjie.modules.jdfq.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.ProductChannelFilter;
import me.zhengjie.service.ChannelFilterService;
import me.zhengjie.service.dto.ChannelFilterQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RequestMapping("/api/channel/filter")
@RestController
public class ChannelFilterController {

    @Autowired
    private me.zhengjie.service.ChannelFilterService channelFilterService;

    @GetMapping("/list")
//    @PreAuthorize("@el.check('channel:list','member:list')")
    public ResponseEntity<Object> productList(ChannelFilterQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(channelFilterService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 已屏蔽的
     * @param criteria
     * @param pageable
     * @return
     */
    @GetMapping("/product/list")
//    @PreAuthorize("@el.check('channel:list','member:list')")
    public ResponseEntity<Object> productChannelList(ChannelFilterQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(channelFilterService.queryAllV2(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 未屏蔽的
     * @param criteria
     * @param pageable
     * @return
     */
    @GetMapping("/v2/product/list")
//    @PreAuthorize("@el.check('channel:list','member:list')")
    public ResponseEntity<Object> productChannelListV2(ChannelFilterQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(channelFilterService.queryAllV3(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增渠道屏蔽产品")
    @PostMapping("add")
//    @PreAuthorize("@el.check('channel:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ProductChannelFilter resources) {
        channelFilterService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("删除渠道屏蔽产品")
    @DeleteMapping
//    @PreAuthorize("@el.check('channel:del')")
    public ResponseEntity<Object> deleteChannel(@RequestBody Set<Long> ids) {
        channelFilterService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
