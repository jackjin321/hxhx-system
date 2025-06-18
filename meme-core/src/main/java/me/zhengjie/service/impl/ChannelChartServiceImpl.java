package me.zhengjie.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.access.DateUtils;
import me.zhengjie.domain.*;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.*;
import me.zhengjie.service.ChannelChartService;
import me.zhengjie.service.ChannelLogService;
import me.zhengjie.service.dto.ChannelQueryCriteria;
import me.zhengjie.service.dto.AppQueryCriteria;
import me.zhengjie.service.mapstruct.ChannelStatMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.vo.ChannelLogVO;
import me.zhengjie.vo.ProductLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelChartServiceImpl implements ChannelChartService {

    private final ChannelChartRepository channelChartRepository;

    private final ChannelRepository channelRepository;

    private final ProductLogRepository productLogRepository;

    private final ProductRepository productRepository;

    private final XfChannelUserRepository xfChannelUserRepository;

    private final ChannelStatMapper channelStatMapper;

    @Autowired
    private ChannelLogService channelLogService;


    @Override
    public ChannelChart todayTotalList() {
        List<Channel> channelList = channelRepository.findAll();
        LocalDate localDate = DateUtils.nowLocalDate();
        ChannelChart totalChart = new ChannelChart();

        totalChart.setTodayUv(Long.valueOf("0"));
        totalChart.setTodayPv(Long.valueOf("0"));
        totalChart.setRegisterNum(Long.valueOf("0"));
        totalChart.setProductPv(Long.valueOf("0"));
        totalChart.setProductUv(Long.valueOf("0"));
        totalChart.setTotalCost(BigDecimal.ZERO);

        Map<Long, BigDecimal> productMap = productRepository.findAll().stream().collect(Collectors.toMap(Product::getId, Product::getPrice));
        //final BigDecimal totalCost = BigDecimal.ZERO;
        Date ddd = new Date();//当天结算
        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(ddd).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(ddd).getTime());
        channelList.forEach(item -> {
            ChannelChart channelChart = createChart(startTime, endTime, item, productMap);
            if (!item.getId().equals(1L) && !item.getId().equals(8L) && !item.getId().equals(13L)) {
                totalChart.setTodayPv(totalChart.getTodayPv() + channelChart.getTodayPv());
                totalChart.setTodayUv(totalChart.getTodayUv() + channelChart.getTodayUv());
                totalChart.setTotalCost(totalChart.getTotalCost().add(item.getPrice().multiply(new BigDecimal(channelChart.getTodayUv()))));
//            totalChart.setTodayRegister(totalChart.getTodayRegister() + channelChart.getTodayRegister());
            } else {
            }
            totalChart.setRegisterNum(totalChart.getRegisterNum() + channelChart.getRegisterNum());
            totalChart.setProductPv(totalChart.getProductPv() + channelChart.getProductPv());
            totalChart.setProductUv(totalChart.getProductUv() + channelChart.getProductUv());

        });

        //产品相关的
//        ProductChart totalProductChart = pro.todayTotalList();
//        totalChart.setProductUv(totalProductChart.getTodayPv());
//        totalChart.setProductUv(totalProductChart.getTodayUv());
        if (totalChart.getProductUv() > 0L) {
            totalChart.setOutputValue(totalChart.getTotalCost().divide(new BigDecimal(totalChart.getProductUv()), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            totalChart.setOutputValue(BigDecimal.ZERO);
        }
        return totalChart;
    }

    @Override
    public Map<String, Object> findByQueryStatToday(ChannelQueryCriteria criteria, Pageable pageable) {
        Long sysUserId = SecurityUtils.getCurrentUserId();

        XfChannelUser channelUser = xfChannelUserRepository.findBySysUserId(sysUserId);
        if (ObjectUtil.isNull(channelUser)) {
            throw new BadRequestException("系统错误");
        }
        criteria.setId(channelUser.getChannelId());
//        System.out.println(criteria);
        Page<Channel> channelPage = channelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);

        Date ddd = new Date();//当天结算
        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(ddd).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(ddd).getTime());
        Map<Long, BigDecimal> productMap = productRepository.findAll().stream().collect(Collectors.toMap(Product::getId, Product::getPrice));
        Page<ChannelChart> channelChartPage = channelPage.map(p -> createChart(startTime, endTime, p, productMap));

        List<ChannelChart> channelChartList = channelChartPage.toList().stream().sorted(Comparator.comparing(ChannelChart::getTodayUv,
                Comparator.reverseOrder())).collect(Collectors.toList());
        return PageUtil.toPage(channelStatMapper.toDto(channelChartList),
                channelChartPage.getTotalElements());
    }

    @Override
    public Map<String, Object> findByQueryToday(ChannelQueryCriteria criteria, Pageable pageable) {

        Page<Channel> channelPage = channelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        Date ddd = new Date();//当天结算
        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(ddd).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(ddd).getTime());
        Map<Long, BigDecimal> productMap = productRepository.findAll().stream().collect(Collectors.toMap(Product::getId, Product::getPrice));
        Page<ChannelChart> channelChartPage = channelPage.map(p -> createChart(startTime, endTime, p, productMap));

        return PageUtil.toPage(channelChartPage.toList().stream().sorted(Comparator.comparing(ChannelChart::getTodayUv,
                        Comparator.reverseOrder())).collect(Collectors.toList()),
                channelChartPage.getTotalElements());
    }

    @Override
    public Map<String, Object> findByQueryStatHistory(ChannelQueryCriteria criteria, Pageable pageable) {

        Long sysUserId = SecurityUtils.getCurrentUserId();

        XfChannelUser channelUser = xfChannelUserRepository.findBySysUserId(sysUserId);
        if (ObjectUtil.isNull(channelUser)) {
            throw new BadRequestException("系统错误");
        }
        criteria.setChannelId(channelUser.getChannelId());

        Page<ChannelChart> page = channelChartRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
//        return PageUtil.toPage(page);
        return PageUtil.toPage(page.map(channelStatMapper::toDto));
    }

    @Override
    public Map<String, Object> findByQueryHistory(ChannelQueryCriteria criteria, Pageable pageable) {
        Page<ChannelChart> page = channelChartRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public ChannelChart createChart(Timestamp startTime, Timestamp endTime, Channel channel,
                                    Map<Long, BigDecimal> productPriceMap) {

        AppQueryCriteria criteria = new AppQueryCriteria();
        List<Timestamp> abc = new ArrayList<>();

        abc.add(startTime);
        abc.add(endTime);
        criteria.setCreateTime(abc);
//        criteria.setChannelId(channel.getId());//登录渠道
        criteria.setRegChannelId(channel.getId());//注册渠道

        List<ChannelLog> channelLogs = channelLogService.queryAllV2(criteria);//PV统计
        //Set<ChannelLog> channelLogSet = new TreeSet<>(channelLogs);//UV统计
//        long todayRegister = channelLogs.stream().filter(p -> p.getIsRegister()).count();//注册数
        List<ChannelLogVO> channelLogVOList = channelLogs.stream().map(p -> {
            ChannelLogVO channelLogVO = new ChannelLogVO();
            channelLogVO.setUuid(p.getUuid());
            channelLogVO.setAccessIp(p.getAccessIp());
            return channelLogVO;
        }).distinct().collect(Collectors.toList());


        List<String> accessIps = channelLogs.stream().map(ChannelLog::getAccessIp).collect(Collectors.toList());
        List<String> abcIps = accessIps.stream().distinct().collect(Collectors.toList());

        ChannelChart channelChart = new ChannelChart();

        channelChart.setIpPv(Long.valueOf(accessIps.size()));
        channelChart.setIpUv(Long.valueOf(abcIps.size()));
        channelChart.setPrice(channel.getPrice());
        channelChart.setPriceType(channel.getPriceType());
        channelChart.setChannelId(channel.getId());
        channelChart.setChannelName(channel.getChannelName());
//        channelChart.setTodayUv(Long.valueOf(channelLogSet.size()));//UV统计
        channelChart.setTodayUv(Long.valueOf(channelLogVOList.size()));//UV统计
        channelChart.setTodayPv(Long.valueOf(channelLogs.size()));//PV统计

        long todayRegister = channelLogs.stream().filter(p -> {
            if (ObjectUtil.isNotEmpty(p.getIsRegister())) {
                return p.getIsRegister();
            } else {
                return false;
            }
        }).map(pp -> pp.getUserId()).distinct().count();

        long todayOldLogin = channelLogs.stream().filter(p -> {
            if (ObjectUtil.isAllNotEmpty(p.getIsRegister(), p.getIsLogin())) {
                return !p.getIsRegister() && p.getIsLogin();
            } else {
                return false;
            }
        }).map(pp -> pp.getUserId()).distinct().count();
        long todayAllLogin = channelLogs.stream().filter(p -> {
            if (ObjectUtil.isNotEmpty(p.getIsLogin())) {
                return p.getIsLogin();
            } else {
                return false;
            }
        }).map(pp -> pp.getUserId()).distinct().count();

        channelChart.setLoginNum(Long.valueOf(todayAllLogin));//总登录数（包含新老户）
        channelChart.setRegisterNum(todayRegister);
        Long forCRegister = multiply(todayRegister, channel.getRegisterBuckleRate());
        channelChart.setForCRegister(forCRegister);
        channelChart.setOldLoginNum(todayOldLogin);

        AppQueryCriteria criteria1 = new AppQueryCriteria();
        criteria1.setCreateTime(abc);
        criteria1.setChannelId(channel.getId());//注册渠道

        List<ProductLog> promoteLogList = promoteLogList(criteria1, channel);//产品点击日志
        Set<ProductLog> promoteLogSet = new TreeSet<>(promoteLogList);
        long newProductUv = promoteLogList.stream().filter(p -> {
            if (ObjectUtil.isNotEmpty(p.getUserStatus())) {
                return p.getUserStatus().equals("new");
            } else {
                return false;
            }
        }).map(pp -> pp.getUserId()).distinct().count();
        long oldProductUv = promoteLogList.stream().filter(p -> {
            if (ObjectUtil.isNotEmpty(p.getUserStatus())) {
                return p.getUserStatus().equals("old");
            } else {
                return true;
            }
        }).map(pp -> pp.getUserId()).distinct().count();
        channelChart.setNewProductUv(newProductUv);
        channelChart.setOldProductUv(oldProductUv);
        if (todayRegister > 0L) {
            channelChart.setNewProductRate(new BigDecimal(newProductUv).multiply(new BigDecimal("100")).divide(new BigDecimal(todayRegister), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            channelChart.setNewProductRate(BigDecimal.ZERO);
        }
        if (todayOldLogin > 0L) {
            channelChart.setOldProductRate(new BigDecimal(oldProductUv).multiply(new BigDecimal("100")).divide(new BigDecimal(todayOldLogin), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            channelChart.setOldProductRate(BigDecimal.ZERO);
        }

        channelChart.setProductPv(Long.valueOf(promoteLogList.size()));
        channelChart.setProductUv(Long.valueOf(promoteLogSet.size()));
        //产值逻辑改成 单价*uv数 / 产品uv数
        if (promoteLogSet.size() > 0) {
//            channelChart.setOutputValue(new BigDecimal(promoteLogSet.size()).multiply(new BigDecimal("100")).divide(new BigDecimal(channelLogSet.size()), 2, BigDecimal.ROUND_HALF_UP));
            channelChart.setOutputValue(new BigDecimal(channelLogVOList.size())
                    .multiply(channel.getPrice())
//                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(promoteLogSet.size()), 2, BigDecimal.ROUND_HALF_UP));
        } else {
            channelChart.setOutputValue(BigDecimal.ZERO);
        }

        return channelChart;
    }

    /**
     * 产品点击
     *
     * @param criteria
     * @param channel
     * @return
     */
    private List<ProductLog> promoteLogList(AppQueryCriteria criteria, Channel channel) {

        List<ProductLog> productLogList = productLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return productLogList;
    }

    private long multiply(long total, String rate) {
        BigDecimal bigDecimalTotal = BigDecimal.valueOf(total).multiply(new BigDecimal(rate));
        return bigDecimalTotal.longValue();
    }

    public static void main(String[] args) {
        Date ddd = new Date();//当天结算
        Timestamp startTime1 = new Timestamp(DateUtil.beginOfDay(DateUtil.offsetDay(ddd, -1)).getTime());
        Timestamp endTime1 = new Timestamp(DateUtil.endOfDay(DateUtil.offsetDay(ddd, -1)).getTime());
        Timestamp startTime = new Timestamp(DateUtil.beginOfDay(ddd).getTime());
        Timestamp endTime = new Timestamp(DateUtil.endOfDay(ddd).getTime());
        System.out.println(startTime);
        System.out.println(endTime);
        System.out.println(startTime1);
        System.out.println(endTime1);
    }
}
