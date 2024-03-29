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

import me.zhengjie.domain.HxSysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
* @website
* @author me zhengjie
* @date 2023-06-12
**/
public interface HxSysConfigRepository extends JpaRepository<HxSysConfig, Long>, JpaSpecificationExecutor<HxSysConfig> {
    /**
    * 根据 Key 查询
    * @param key /
    * @return /
    */
    HxSysConfig findByKey(String key);
    Optional<HxSysConfig> findFirstByKey(String key);
}