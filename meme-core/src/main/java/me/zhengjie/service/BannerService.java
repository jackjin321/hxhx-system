package me.zhengjie.service;

import me.zhengjie.domain.AdBanner;
import me.zhengjie.result.ResultModel;
import me.zhengjie.vo.ParamBannerQuery;
import me.zhengjie.service.dto.AppQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface BannerService {


    /**
     * 根据【页面】和【位置】查询对应的【广告位】
     *
     * @param paramBannerQuery 广告位查询参数
     * @return
     */
    List<AdBanner> getBannerListByPageAndPos(ParamBannerQuery paramBannerQuery, HttpServletRequest request);
    ResponseEntity<Object> getBannerListByPageAndPosV1(ParamBannerQuery paramBannerQuery, HttpServletRequest request);

    ResultModel toBannerUrlV2(ParamBannerQuery paramBannerQuery, HttpServletRequest request, String channelTokenHeader);

//    ResultModel toProductUrlV2(ParamBannerQuery paramBanner);
//    ResultModel findOne(ParamBannerQuery paramBanner);
//    String findOneByLogin(ParamBannerQuery paramBanner);
    Object queryAll(AppQueryCriteria criteria, Pageable pageable);

    /**
     * 创建
     *
     * @param resources /
     */
    void create(AdBanner resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(AdBanner resources);

    /**
     * 删除
     * @param ids /
     */
    void delete(Set<Long> ids);
}
