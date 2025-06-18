package me.zhengjie.modules.jdfq.rest;


import lombok.RequiredArgsConstructor;
import me.zhengjie.service.ChannelChartService;
import me.zhengjie.service.dto.ChannelQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/channel/stat")
@RestController
public class ChannelStatController {

    private final ChannelChartService channelChartService;
//    /**
//     * 渠道今日统计，汇总
//     * @return
//     */
//    @GetMapping("/today/total")
//    public ResponseEntity<Object> todayTotalList() {
//        return new ResponseEntity<>(channelChartService.todayTotalList(), HttpStatus.OK);
//    }

    /**
     * 渠道今日统计
     * @return
     */
    @GetMapping("/today/list")
    public ResponseEntity<Object> todayList(ChannelQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(channelChartService.findByQueryStatToday(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 渠道历史统计
     * @return
     */
    @GetMapping("/history/list")
    public ResponseEntity<Object> historyList(ChannelQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(channelChartService.findByQueryStatHistory(criteria, pageable), HttpStatus.OK);
    }

}
