package me.zhengjie.modules.jdfq.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.service.ProductChartService;
import me.zhengjie.service.dto.ProductQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/product/chart")
@RestController
public class ProductChartController {

    private final ProductChartService productChartService;

    /**
     * 产品今日统计，汇总
     * @return
     */
    @GetMapping("/today/total")
    public ResponseEntity<Object> todayTotalList() {
        return new ResponseEntity<>(productChartService.todayTotalList(), HttpStatus.OK);
    }

    /**
     * 产品今日统计
     * @return
     */
    @GetMapping("/today/list")
    public ResponseEntity<Object> todayList(ProductQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(productChartService.findByQueryToday(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 产品历史统计
     * @return
     */
    @GetMapping("/history/list")
    public ResponseEntity<Object> historyList(ProductQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(productChartService.findByQueryHistory(criteria, pageable), HttpStatus.OK);
    }
}
