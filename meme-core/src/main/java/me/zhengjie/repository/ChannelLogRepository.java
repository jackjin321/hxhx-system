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

import me.zhengjie.domain.ChannelLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author zhanghouying
 * @date 2019-08-24
 */
public interface ChannelLogRepository extends JpaRepository<ChannelLog, Long>, JpaSpecificationExecutor<ChannelLog> {


    @Modifying
    @Query(value = " update xf_channel_log set user_id = ?1,reg_channel_id  = ?2, reg_channel_name = ?3 ,is_register = ?4, is_login = ?5 where id = ?6 ", nativeQuery = true)
    void updateSubCntById(Long userId, Long regChannelId, String regChannelName, Boolean isRegister, Boolean isLogin, Long id);
}
