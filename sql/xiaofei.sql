柱状图
用户登录数，注册用户数，认证完成用户，联系客服人数，支付成功人数，支付成功次数，支付点击次数，支付金额

-- 新增日志
-- channelId
-- userId
-- name；login，register，click_pay，click_customers
-- time
   `channel_id` bigint DEFAULT NULL COMMENT '渠道ID',
   `channel_name` varchar(255) DEFAULT NULL COMMENT '渠道名称',

-- name；login，register，click_pay，click_customer
CREATE TABLE `hx_access_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `channel_id` bigint DEFAULT NULL COMMENT '渠道ID',
  `channel_name` varchar(255) DEFAULT NULL COMMENT '渠道名称',
  `event_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '事件名称',

  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',

  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户访问记录';


DROP TABLE IF EXISTS `hx_user`;
CREATE TABLE `hx_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
   `channel_id` bigint DEFAULT NULL COMMENT '渠道ID',
   `channel_name` varchar(255) DEFAULT NULL COMMENT '渠道名称',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号码',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `enabled` tinyint DEFAULT '1' COMMENT '状态：1启用、0禁用',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近登录时间',
  `last_op_time` datetime DEFAULT NULL COMMENT '最近操作时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `pwd_reset_time` datetime DEFAULT NULL COMMENT '修改密码的时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `uniq_username` (`username`),
  UNIQUE KEY `uniq_phone` (`phone`),
  KEY `inx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='APP用户';


DROP TABLE IF EXISTS `xf_channel_chart`;
CREATE TABLE `xf_channel_chart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `channel_id` bigint NOT NULL COMMENT '渠道Id',
  `channel_name` varchar(512) NOT NULL COMMENT '渠道名称',


  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='渠道统计';

DROP TABLE IF EXISTS `xf_product_chart`;
CREATE TABLE `xf_product_chart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',

  `banner_id` bigint NOT NULL COMMENT '产品Id',
  `banner_name` varchar(512) NOT NULL COMMENT '内部使用，不对展示，pageName+posName+id',

  `product_id` bigint NOT NULL COMMENT '产品Id',
  `product_name` varchar(512) NOT NULL COMMENT '',

  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='产品统计';



alter table `xf_banner`
add column `name` varchar(1024) DEFAULT NULL COMMENT '内部使用，不对展示，pageName+posName+id' after id;

alter table `xf_banner`
add column `channel_id` bigint NOT NULL COMMENT '渠道Id' after product_id;

DROP TABLE IF EXISTS `xf_banner`;
CREATE TABLE `xf_banner` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `banner_name` varchar(512) DEFAULT NULL COMMENT '内部使用，不对展示，pageName+posName+id',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `sub_title` varchar(255) DEFAULT NULL COMMENT '副标题',
  `page` varchar(255) DEFAULT NULL COMMENT '页面',
  `pos` varchar(255) DEFAULT NULL COMMENT '位置',
  `jump_type` varchar(255) DEFAULT NULL COMMENT 'h5|h5页面,native|原生页面,otherApp|其它App,noJump|不跳转',
  `jump_url` varchar(255) DEFAULT NULL COMMENT '跳转Url,或者跳转页面',
  `img_url` varchar(2550) DEFAULT NULL COMMENT '图片URL',
  `status` varchar(32) DEFAULT NULL COMMENT 'on|开启 off|关闭',
   `sort_num` int default NULL COMMENT '排序，数字倒序',
   `channel_id` bigint NOT NULL COMMENT '渠道Id',

   `product_id` bigint NOT NULL COMMENT '产品Id',

  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='广告位管理';


alter table xf_channel
add column `price` decimal(10,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '单价' after process;

alter table xf_channel
add column `auth_status` varchar(255) DEFAULT NULL COMMENT '二要素开关，on开启，off关闭' after process;

alter table xf_channel
add column   `recycle` tinyint default '0' COMMENT '回收 0 1已回收' after sort;

DROP TABLE IF EXISTS `xf_channel`;
CREATE TABLE `xf_channel` (
  `channel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `channel_name` varchar(255) DEFAULT NULL COMMENT '渠道名称',
  `channel_code` varchar(255) DEFAULT NULL COMMENT '渠道编码',
  `price` decimal(10,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '单价',
  `process` varchar(255) DEFAULT NULL COMMENT '流程控制',
  `auth_status` varchar(255) DEFAULT NULL COMMENT '二要素开关，on开启，off关闭',
  `sort` int default NULL COMMENT '排序，数字倒序',
  `recycle` tinyint default '0' COMMENT '回收 0 1已回收',
  `channel_remarks` text  default NULL COMMENT '渠道描述',

  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`channel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='渠道管理';

alter table xf_channel_log
add column `uuid` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'uuid' after user_id;

alter table xf_channel_log
add column `reg_channel_id` bigint DEFAULT 0 COMMENT '渠道Id' after phone_md5;
alter table xf_channel_log
add column `reg_channel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道名称' after reg_channel_id;

DROP TABLE IF EXISTS `xf_channel_log`;
CREATE TABLE `xf_channel_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint DEFAULT '0' COMMENT '用户id',
  `uuid` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'uuid',
  `phone` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `phone_md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号Md5',
   `reg_channel_id` bigint DEFAULT 0 COMMENT '渠道Id',
  `reg_channel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道名称',
  `channel_id` bigint NOT NULL COMMENT '渠道Id',
  `channel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称',
  `platform` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '平台类型，app，h5',
  `device_id` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备id',
  `device_brand` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备id',
  `last_channel_id` bigint DEFAULT NULL COMMENT '登录渠道代码',
  `last_channel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录渠道名称',
  `os_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备id',
  `os_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备名称',
  `access_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访问ip',
  `is_register` tinyint(1) DEFAULT '0' COMMENT '注册标记',
  `is_login` tinyint(1) DEFAULT '0' COMMENT '登陆标记',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_device_id` (`device_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1597296731689717761 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='渠道访问日志';


alter table xf_product_log
add column `banner_id` bigint NOT NULL COMMENT '产品Id' after product_id;

alter table xf_product_log
add column `uuid` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'uuid' after user_id;


DROP TABLE IF EXISTS `xf_product_log`;
CREATE TABLE `xf_product_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `uuid` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'uuid',
  `phone` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `phone_md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号Md5',
  `channel_id` bigint NOT NULL COMMENT '渠道Id',
  `channel_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称',
  `product_id` bigint NOT NULL COMMENT '产品Id',
  `banner_id` bigint NOT NULL COMMENT '产品Id',
  `product_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品名称',
--   `space_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问位置，1，productList 产品列表，2，indexRoute 首页路由',
  `page` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '',
  `page_name` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '',
  `pos` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '',
  `pos_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '',
  `access_os` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `access_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问ip',
  `is_first_to` tinyint(1) DEFAULT '0' COMMENT '是否首次访问',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='产品访问日志';

DROP TABLE IF EXISTS `xf_product`;
CREATE TABLE `xf_product` (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_name` varchar(255) DEFAULT NULL COMMENT '应用名称',
  `price` decimal(10,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
  `price_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型 uv, cpa',
  `corner_mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角标',
  `online_conn_max_count` int default '0' COMMENT '生产的连接最大访问次数 0 为不限制访问',
  `online_conn_timeout` int default '0'  COMMENT '生产的连接访问超时时间 单位 分钟 0 为可永久访问',
  `min_loan_amount` bigint NOT NULL COMMENT '最低贷款金额，10000代表1块钱',
  `max_loan_amount` bigint NOT NULL COMMENT '最高贷款金额，10000代表1块钱',
  `max_loan_month` int NOT NULL COMMENT '最大贷款时间，单位，月',
  `min_loan_month` int NOT NULL COMMENT '最低贷款时间，单位，月',
  `rate` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0.00' COMMENT '费率，100代表1%',
  `daily_uv_limit` int NOT NULL COMMENT '每日uv限额，0为不限',
  `apply_condition` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请条件',
  `apply_link` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请链接',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态 offShelves|下架，waitForOnShelves|等待上架，onShelves|上架',
  `sort` int NOT NULL COMMENT '排序，数字倒序',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图标',

  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='产品管理';

CREATE TABLE `xf_product` (
  `product_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `product_name` varchar(255) DEFAULT NULL COMMENT '应用名称',
  `price` decimal(10,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
  `price_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型 uv, cpa',
  `apply_link` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请链接',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态 offShelves|下架，waitForOnShelves|等待上架，onShelves|上架',
  `sort` int NOT NULL COMMENT '排序，数字倒序',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图标',
  `jump_type` varchar(255) DEFAULT 'normal' COMMENT '跳转类型',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=COMPACT COMMENT='产品管理';

alter table `xf_product`
add column `jump_type` varchar(255) DEFAULT 'normal' COMMENT '跳转类型' after image_url;

CREATE TABLE `ddh_product` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `product_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称',
  `price` decimal(10,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '产品单价',
  `company_info` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司信息',
  `price_type` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '价格类型 1、uv,2、cpa',
  `product_category_id` bigint NOT NULL COMMENT '产品类别id',
  `corner_mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角标',
  `apply_mark` int NOT NULL DEFAULT '80' COMMENT '已申请百分比 0~100',
  `online_conn_max_count` int NOT NULL COMMENT '生产的连接最大访问次数 0 为不限制访问',
  `online_conn_timeout` int NOT NULL COMMENT '生产的连接访问超时时间 单位 分钟 0 为可永久访问',
  `min_loan_amount` bigint NOT NULL COMMENT '最低贷款金额，10000代表1块钱',
  `max_loan_amount` bigint NOT NULL COMMENT '最高贷款金额，10000代表1块钱',
  `max_loan_month` int NOT NULL COMMENT '最大贷款时间，单位，月',
  `min_loan_month` int NOT NULL COMMENT '最低贷款时间，单位，月',
  `rate` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0.00' COMMENT '费率，100代表1%',
  `daily_uv_limit` int NOT NULL COMMENT '每日uv限额，0为不限',
  `apply_condition` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请条件',
  `apply_link` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '申请链接',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态 offShelves|下架，waitForOnShelves|等待上架，onShelves|上架',
  `sort` int NOT NULL COMMENT '排序，数字倒序',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图标',
  `is_vip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否是VIP vip|是VIP notVip|不是VIP',
  `is_filter_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'off' COMMENT '是否开启时间过滤 on/开启，off/关闭',
  `is_union_login` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否连登 notUnionLogin|非联登；unionLogin|联登',
  `third_admin_link` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '第三方后台地址',
  `third_admin_username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '第三方后台账号',
  `third_admin_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '第三方后台密码',
  `is_wechat_enabled` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '微信浏览器是否可打开H5  enabled|开启 disabled|禁用',
  `is_cooperation_sign` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '流量合作协议是否签署，notSign|未签署，haveSigned|已签署',
  `is_commitment_sign` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '合规承诺函是否签署，notSign|未签署，haveSigned|已签署',
  `is_promise_sign` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '承诺函是否签署：notSign|未签署，haveSigned|已签署',
  `is_protection` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '机构保护：notProtect|不保护；protect|保护机构',
  `show_start_time` time DEFAULT NULL COMMENT '开始时间',
  `show_end_time` time DEFAULT NULL COMMENT '结束时间',
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品类型 notMember|非会员产品 alipayRightsMember|是支付宝权益会员产品 thirdRightsMember|是三方权益会员产品',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='助贷模块-贷超产品表';


CREATE TABLE `ddh_channel_log` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户id',
  `phone` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `phone_md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号Md5',
  `channel_id` bigint NOT NULL COMMENT '渠道Id',
  `channel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称',
  `platform` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '平台类型，app，h5',
  `version_code` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访问version code信息',
  `version` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '访问version信息',
  `device_id` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `device_brand` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备id',
  `last_channel_id` bigint NOT NULL COMMENT '登录渠道代码',
  `last_channel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录渠道名称',
  `os_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `os_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `access_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问ip',
  `is_register` tinyint(1) DEFAULT '0' COMMENT '注册标记',
  `is_login` tinyint(1) DEFAULT '0' COMMENT '登陆标记',
  `is_into` tinyint(1) DEFAULT '0' COMMENT '进件标记',
  `is_active` tinyint(1) DEFAULT '0' COMMENT '激活标记',
  `is_first_login` tinyint(1) DEFAULT '0' COMMENT '首次登陆',
  `is_first_into` tinyint(1) DEFAULT '0' COMMENT '首次进件',
  `create_date` bigint NOT NULL COMMENT '创建时间戳',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_device_id` (`device_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='渠道访问日志';


CREATE TABLE `ddh_promote_log` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `phone` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `phone_md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号Md5',
  `channel_id` bigint NOT NULL COMMENT '渠道Id',
  `channel_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '渠道名称',
  `product_id` bigint NOT NULL COMMENT '产品Id',
  `product_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品名称',
  `click_location` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问位置，1，productList 产品列表，2，indexRoute 首页路由',
  `access_os` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `access_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问ip',
  `is_first_to` tinyint(1) DEFAULT '0' COMMENT '是否首次访问',
  `create_date` bigint NOT NULL COMMENT '创建时间戳',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='产品访问日志';