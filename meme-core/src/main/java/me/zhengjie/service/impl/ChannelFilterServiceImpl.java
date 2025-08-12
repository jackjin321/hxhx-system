package me.zhengjie.service.impl;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.Channel;
import me.zhengjie.domain.Product;
import me.zhengjie.domain.ProductChannelFilter;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.ChannelRepository;
import me.zhengjie.repository.ProductChannelFilterRepository;
import me.zhengjie.repository.ProductRepository;
import me.zhengjie.service.ChannelFilterService;
import me.zhengjie.service.dto.ChannelFilterQueryCriteria;
import me.zhengjie.service.dto.ProductFilterQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChannelFilterServiceImpl implements ChannelFilterService {

    @Autowired
    private ProductChannelFilterRepository productChannelFilterRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public Map<String, Object> queryAll(ChannelFilterQueryCriteria criteria, Pageable pageable) {
        Page<ProductChannelFilter> page = productChannelFilterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    //已经屏蔽的
    @Override
    public Map<String, Object> queryAllV2(ChannelFilterQueryCriteria criteria, Pageable pageable) {
        //
        Page<ProductChannelFilter> page = productChannelFilterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        //List<String> productIds = page.stream().map(ProductChannelFilter::getProductId).collect(Collectors.toList());//已经屏蔽的
        return PageUtil.toPage(page);
    }

    //未屏蔽的
    @Override
    public Map<String, Object> queryAllV3(ChannelFilterQueryCriteria criteria, Pageable pageable) {
        //
        List<ProductChannelFilter> page = productChannelFilterRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        List<Long> productIds = page.stream().map(ProductChannelFilter::getProductId).collect(Collectors.toList());//已经屏蔽的
        log.info(productIds.toString());
        ProductFilterQueryCriteria criteria1 = new ProductFilterQueryCriteria();
        criteria1.setId(productIds);
        if (ObjectUtil.isAllNotEmpty(criteria.getProductName())) {
            criteria1.setProductName(criteria.getProductName());
        }
        Page<Product> productPage = productRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria1, criteriaBuilder), pageable);

        return PageUtil.toPage(productPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ProductChannelFilter resources) {
        //判断是否存在
//        boolean exist = productChannelFilterRepository.existsByQuery(helper -> helper.equal("productId", resources.getProductId())
//                .equal("channelId", resources.getChannelId()).and().endAnd());
//        if (exist) {
//            throw new BadRequestException("productId and channelId is exist");
//        }

        int exist = productChannelFilterRepository.countByProductIdAndChannelId(resources.getProductId(), resources.getChannelId());
        if (exist > 0) {
            throw new BadRequestException("productId and channelId is exist");
        }
        Optional<Product> productOptional = productRepository.findById(resources.getProductId());
        if (!productOptional.isPresent()) {
            throw new BadRequestException("productId is not found");
        }
        Optional<Channel> channelOptional = channelRepository.findById(resources.getChannelId());
        if (!channelOptional.isPresent()) {
            throw new BadRequestException("channelId is not found");
        }
        Product product = productOptional.get();
        Channel channel = channelOptional.get();
        resources.setProductName(product.getProductName());
        resources.setChannelName(channel.getChannelName());
        productChannelFilterRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        productChannelFilterRepository.deleteAllByIdInBatch(ids);
        // 删除缓存
        //redisUtils.delByKeys(CacheKey.JOB_ID, ids);
    }
}
