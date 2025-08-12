DROP TABLE IF EXISTS `xf_channel_user`;
CREATE TABLE `xf_channel_user`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `channel_id`  bigint NOT NULL COMMENT '渠道Id',
    `sys_user_id` bigint NOT NULL COMMENT 'sys_user_id',
    `username`    varchar(255) DEFAULT NULL COMMENT '用户名',
    `phone`       varchar(255) DEFAULT NULL COMMENT '手机号码',
    `password`    varchar(255) DEFAULT NULL COMMENT '密码',
    `status`      tinyint(1) DEFAULT '0' COMMENT '登录状态',

    `create_by`   varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='渠道用户';

alter table `sys_user`
    add column `user_type` int(4) DEFAULT '0' COMMENT '0系统用户，1渠道用户' after `is_admin`;

CREATE TABLE `sys_user`
(
    `user_id`        bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `dept_id`        bigint       DEFAULT NULL COMMENT '部门名称',
    `username`       varchar(255) DEFAULT NULL COMMENT '用户名',
    `nick_name`      varchar(255) DEFAULT NULL COMMENT '昵称',
    `gender`         varchar(2)   DEFAULT NULL COMMENT '性别',
    `phone`          varchar(255) DEFAULT NULL COMMENT '手机号码',
    `email`          varchar(255) DEFAULT NULL COMMENT '邮箱',
    `avatar_name`    varchar(255) DEFAULT NULL COMMENT '头像地址',
    `avatar_path`    varchar(255) DEFAULT NULL COMMENT '头像真实路径',
    `password`       varchar(255) DEFAULT NULL COMMENT '密码',
    `is_admin`       bit(1)       DEFAULT b'0' COMMENT '是否为admin账号',
    `enabled`        bigint       DEFAULT NULL COMMENT '状态：1启用、0禁用',
    `create_by`      varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by`      varchar(255) DEFAULT NULL COMMENT '更新者',
    `pwd_reset_time` datetime     DEFAULT NULL COMMENT '修改密码的时间',
    `create_time`    datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE KEY `UK_kpubos9gc2cvtkb0thktkbkes` (`email`) USING BTREE,
    UNIQUE KEY `username` (`username`) USING BTREE,
    UNIQUE KEY `uniq_username` (`username`),
    UNIQUE KEY `uniq_email` (`email`),
    KEY              `FK5rwmryny6jthaaxkogownknqp` (`dept_id`) USING BTREE,
    KEY              `FKpq2dhypk2qgt68nauh2by22jb` (`avatar_name`) USING BTREE,
    KEY              `inx_enabled` (`enabled`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='系统用户';


CREATE TABLE `xf_product_channel_filter`
(
    `id`        bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `product_id`   bigint NOT NULL COMMENT '产品id',
    `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品名称',
    `channel_id`   bigint NOT NULL COMMENT '渠道Id',
    `channel_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称',

    `create_by`   varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `un_product_id_channel_id` (`product_id`,`channel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='产品渠道过滤表';


DROP TABLE IF EXISTS `xf_banner`;
CREATE TABLE `xf_banner`
(
    `id`          bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `banner_name` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci  DEFAULT NULL COMMENT '内部使用，不对展示',
    `title`       varchar(255)                                                   DEFAULT NULL COMMENT '标题',
    `sub_title`   varchar(255)                                                   DEFAULT NULL COMMENT '副标题',
    `page`        varchar(255)                                                   DEFAULT NULL COMMENT '页面',
    `pos`         varchar(255)                                                   DEFAULT NULL COMMENT '位置',
    `jump_type`   varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci   DEFAULT NULL COMMENT 'h5|h5页面,native|原生页面,otherApp|其它App,noJump|不跳转',
    `jump_url`    varchar(255)                                                   DEFAULT NULL COMMENT '跳转Url,或者跳转页面',
    `image_url`   varchar(2550) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图片URL',
    `status`      varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci   DEFAULT NULL COMMENT 'on|开启 off|关闭',
    `sort_num`    int                                                            DEFAULT NULL COMMENT '排序，数字倒序',
    `product_id`  bigint NOT NULL COMMENT '产品Id',
    `channel_id`  bigint NOT NULL COMMENT '渠道Id',

    `create_by`   varchar(255)                                                   DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255)                                                   DEFAULT NULL COMMENT '更新者',
    `create_time` datetime                                                       DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime                                                       DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='广告位管理';

alter table `xf_channel_chart`
    add column `for_c_register` bigint NOT NULL COMMENT '渠道注册' after register_num;

alter table `xf_channel_chart`
    add column `ip_pv` bigint DEFAULT '0' COMMENT 'IPPV' after today_uv;
alter table `xf_channel_chart`
    add column `ip_uv` bigint DEFAULT '0' COMMENT 'IPUV' after ip_pv;

DROP TABLE IF EXISTS `xf_channel_chart`;
CREATE TABLE `xf_channel_chart`
(
    `id`               bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `channel_id`       bigint                                                       NOT NULL COMMENT '渠道Id',
    `channel_name`     varchar(512)                                                 NOT NULL COMMENT '渠道名称',
    `price`            decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
    `price_type`       varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型 uv, cpa',
    `login_num`        bigint                                                       NOT NULL COMMENT '总登录数（包含新老户）',
    `register_num`     bigint                                                       NOT NULL COMMENT '新户注册',
    `for_c_register`   bigint                                                       NOT NULL COMMENT '渠道注册',
    `old_login_num`    bigint                                                       NOT NULL COMMENT '老户登录',
    `today_pv`         bigint                                                       NOT NULL COMMENT '渠道PV',
    `today_uv`         bigint                                                       NOT NULL COMMENT '渠道UV',
    `ip_pv`            bigint                                                       NOT NULL COMMENT 'IPPV',
    `ip_uv`            bigint                                                       NOT NULL COMMENT 'IPUV',
    `new_product_uv`   bigint                                                       NOT NULL COMMENT '今日UV',
    `old_product_uv`   bigint                                                       NOT NULL COMMENT '今日UV',
    `new_product_rate` decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
    `old_product_rate` decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
    `product_pv`       bigint                                                       NOT NULL COMMENT '今日UV',
    `product_uv`       bigint                                                       NOT NULL COMMENT '今日UV',
    `output_value`     decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',

    `create_by`        varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by`        varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time`      datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time`      datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='渠道统计';


DROP TABLE IF EXISTS `xf_product_chart`;
CREATE TABLE `xf_product_chart`
(
    `id`           bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `category`     varchar(512)                                                 NOT NULL COMMENT '统计维度',
    `product_id`   bigint                                                       NOT NULL COMMENT '产品Id',
    `product_name` varchar(512)                                                 NOT NULL COMMENT '产品名称',
    `price`        decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
    `price_type`   varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型 uv, cpa',
    `banner_id`    bigint                                                       NOT NULL COMMENT '产品Id',
    `banner_name`  varchar(512)                                                 NOT NULL COMMENT '产品名称',
    `today_pv`     bigint                                                       NOT NULL COMMENT '今日PV',
    `today_uv`     bigint                                                       NOT NULL COMMENT '今日UV',
    `ip_pv`        bigint                                                       NOT NULL COMMENT 'IPPV',
    `ip_uv`        bigint                                                       NOT NULL COMMENT 'IPUV',
    `first_to`     bigint                                                       NOT NULL COMMENT '首次访问',

    `create_by`    varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by`    varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='产品统计';

alter table `xf_product_chart`
    add column `profit` decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品收益' after `first_to`;

alter table `xf_channel`
    add column `port_status` varchar(255) DEFAULT NULL COMMENT 'AB面，A，B' after channel_code;

alter table `xf_channel`
    add column `price_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型 uv, cpa' after auth_status;

alter table `xf_channel`
    add column `register_buckle_rate` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '注册扣量比率' after process;

alter table `xf_channel`
    add column `radar_status` varchar(255) DEFAULT 'off' COMMENT '雷达开关，on开启，off关闭' after auth_status;

DROP TABLE IF EXISTS `xf_channel`;
CREATE TABLE `xf_channel`
(
    `channel_id`           bigint                                                       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `channel_name`         varchar(255) DEFAULT NULL COMMENT '渠道名称',
    `channel_code`         varchar(255) DEFAULT NULL COMMENT '渠道编码',
    `port_status`          varchar(255) DEFAULT NULL COMMENT 'AB面，A，B',
    `process`              varchar(255) DEFAULT NULL COMMENT '流程控制',
    `register_buckle_rate` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '注册扣量比率',
    `auth_status`          varchar(255) DEFAULT NULL COMMENT '二要素开关，on开启，off关闭',
    `radar_status`         varchar(255) DEFAULT NULL COMMENT '雷达开关，on开启，off关闭',
    `price_type`           varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型 uv, cpa',
    `price`                decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '单价',
    `sort`                 int          DEFAULT NULL COMMENT '排序，数字倒序',
    `channel_remarks`      text COMMENT '渠道描述',
    `create_by`            varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by`            varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time`          datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time`          datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`channel_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='渠道管理';

alter table xf_channel_log
    add column `browser` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'browser' after platform;

DROP TABLE IF EXISTS `xf_channel_log`;
CREATE TABLE `xf_channel_log`
(
    `id`                bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`           bigint                                                        DEFAULT '0' COMMENT '用户id',
    `uuid`              varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'uuid',
    `phone`             varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
    `phone_md5`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号Md5',
    `channel_id`        bigint                                                        NOT NULL COMMENT '渠道Id',
    `channel_name`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称',
    `platform`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '平台类型，app，h5',
    `device_id`         varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备id',
    `device_brand`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备id',
    `last_channel_id`   bigint                                                        DEFAULT NULL COMMENT '登录渠道代码',
    `last_channel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录渠道名称',
    `os_id`             varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '设备id',
    `os_name`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
    `access_ip`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访问ip',
    `is_register`       tinyint(1) DEFAULT '0' COMMENT '注册标记',
    `is_login`          tinyint(1) DEFAULT '0' COMMENT '登陆标记',
    `create_time`       datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time`       datetime                                                      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `index_device_id` (`device_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='渠道访问日志';

alter table `xf_product`
    add column `product_type` varchar(32) DEFAULT 'uv' COMMENT '产品类型：uv，union' after `product_name`;

alter table `xf_product`
    add column `product_code` varchar(32) DEFAULT NULL COMMENT '产品编号' after `product_type`;

DROP TABLE IF EXISTS `xf_product`;
CREATE TABLE `xf_product`
(
    `product_id`      bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `product_name`    varchar(255)                                                           DEFAULT NULL COMMENT '应用名称',
    `port_status`     varchar(255)                                                           DEFAULT NULL COMMENT 'AB面，A，B',
    `price`           decimal(10, 4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
    `price_type`      varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '价格类型 uv, cpa',
    `apply_link`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请链接',
    `status`          varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '状态 offShelves|下架，waitForOnShelves|等待上架，onShelves|上架',
    `sort`            int                                                           NOT NULL COMMENT '排序，数字倒序',
    `image_url`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图标',

    `jump_type`       varchar(255)                                                           DEFAULT 'normal' COMMENT '跳转类型',
    `corner_mark`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角标',
    `min_amount`      int                                                           NOT NULL COMMENT '最低贷款金额',
    `max_amount`      int                                                           NOT NULL COMMENT '最高贷款金额',
    `max_month`       int                                                           NOT NULL COMMENT '最大贷款时间，单位，月',
    `min_month`       int                                                           NOT NULL COMMENT '最低贷款时间，单位，月',
    `rate`            varchar(20) COLLATE utf8mb4_unicode_ci                        NOT NULL DEFAULT '0.00' COMMENT '费率，100代表1%',
    `apply_condition` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请条件',


    `create_by`       varchar(255)                                                           DEFAULT NULL COMMENT '创建者',
    `update_by`       varchar(255)                                                           DEFAULT NULL COMMENT '更新者',
    `create_time`     datetime                                                               DEFAULT NULL COMMENT '创建日期',
    `update_time`     datetime                                                               DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='产品管理';

DROP TABLE IF EXISTS `xf_product_log`;
CREATE TABLE `xf_product_log`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`      bigint                                                        NOT NULL COMMENT '用户id',
    `uuid`         varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'uuid',
    `user_status`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '用户状态，新用户，老用户',
    `phone`        varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
    `phone_md5`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号Md5',
    `channel_id`   bigint                                                        NOT NULL COMMENT '渠道Id',
    `channel_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '渠道名称',
    `channel_port` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '渠道AB面',
    `product_id`   bigint                                                        NOT NULL COMMENT '产品Id',
    `product_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '产品名称',
    `banner_id`    bigint                                                        DEFAULT NULL COMMENT '产品Id',
    `page`         varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `page_name`    varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `pos`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `pos_name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `access_os`    varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
    `access_ip`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问ip',
    `is_first_to`  tinyint(1) DEFAULT '0' COMMENT '是否首次访问',
    `create_time`  datetime                                                      DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime                                                      DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='产品访问日志';
