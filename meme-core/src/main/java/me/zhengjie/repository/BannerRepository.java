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
package me.zhengjie.repository;

import me.zhengjie.domain.AdBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
public interface BannerRepository extends JpaRepository<AdBanner, Long>, JpaSpecificationExecutor<AdBanner> {

    List<AdBanner> findByPageAndPos(String page, String pos);

    List<AdBanner> findByChannelIdAndPageAndPos(Long channelId, String page, String pos);

    List<AdBanner> findByChannelIdAndPageAndPosAndStatusOrderBySortNumDesc(Long channelId, String page, String pos, String status);
    List<AdBanner> findByChannelIdAndPageAndPosAndStatusOrderBySortNumAsc(Long channelId, String page, String pos, String status);
}
