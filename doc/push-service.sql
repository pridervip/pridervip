/*
** 大火币App消息推送服务器sql脚本
*/

DROP table if exists `push_cid_userid_map`;
CREATE TABLE `push_cid_userid_map` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `cid` varchar(64) NOT NULL COMMENT '个推的推送设备cid',
  `device_token` varchar(64) NOT NULL COMMENT 'ios的deviceToken',
  `device_type` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '设备类型',
  `time_update` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  `time_create` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_cid` (`cid`),
  KEY `index_device_token` (`device_token`),
  KEY `index_user_id` (`user_id`),
  KEY `index_time_update` (`time_update`),
  UNIQUE KEY `index_user_id_cid` (`user_id`, `cid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='推送设备id对用户id映射表';


DROP table if exists `push_message`;
CREATE TABLE `push_message` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `recv_id` varchar(64) NOT NULL COMMENT '接收的设备cid',
  `recv_id_type` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '接收id的类：1为cid，2为app，3为ios deviceToken',
  `task_index` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '并发处理序号，每个服务器实例一个id，不能重复',
  `device_type` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '设备类型',
  `action_type` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '动作类型：1打开应用，2打开url，3透传消息',
  `source` varchar(32) NOT NULL DEFAULT '' COMMENT '推送的消息来源，只做标记使用',
  `title` varchar(256) NOT NULL DEFAULT '' COMMENT '推送的消息标题',
  `content` text NOT NULL COMMENT '推送的消息内容',
  `url` varchar(256) NOT NULL DEFAULT '' COMMENT '打开的url',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态：0初始化，1，正在发送，2，发送成功，3，发送失败',
  `try_times` int(2) unsigned NOT NULL DEFAULT '0' COMMENT '重试次数',
  `time_push` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '推送成功的时间',
  `time_create` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `index_status` (`status`),
  KEY `index_task_index` (`task_index`),
  KEY `index_time_push` (`time_push`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='推送消息表';


DROP table if exists `push_price_notice`;
CREATE TABLE `push_price_notice` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_index` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '并发处理序号，每个服务器实例一个id，不能重复',
  `user_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `cid` varchar(64) NOT NULL COMMENT '个推的推送设备cid',
  `device_token` varchar(64) NOT NULL DEFAULT '' COMMENT 'ios的deviceToken',
  `device_type` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '设备类型',
  `symbol_id` varchar(64) NOT NULL COMMENT '交易代码id',
  `price` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '价格，以聪为单位',
  `currency` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '现金类型：1为人民币，2为美元',
  `lang` varchar(16) NOT NULL DEFAULT '' COMMENT '语言类型',
  `price_type` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '价格类型：1为最新成交价，2为买一价，3为卖一价，4为24小时最高价，5为24小时最低价',
  `price_condition` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '条件：1为大于等于，2为小于等于',
  `status` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '消息状态：0为关闭，1为打开',
  `time_last_push` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '最后推送的时间',
  `price_last_push` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '最后推送的价格',
  `status_last_push` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '最后推送的状态，0为需要推送，1为已经推送，等待下次价格突破后，再变为0推送',
  `time_create` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cid_symbol_id_price_type_price_condition` (`cid`,`symbol_id`,`price_type`,`price_condition`),
  KEY `index_user_id` (`user_id`),
  KEY `index_cid` (`cid`),
  KEY `index_device_token` (`device_token`),
  KEY `index_symbol_id` (`symbol_id`),
  KEY `index_price` (`price`),
  KEY `time_last_push` (`time_last_push`),
  KEY `status_last_push` (`status_last_push`),
  KEY `index_search_notice` (`task_index`,`symbol_id`,`status`,`price_type`,`price_condition`,`currency`,`time_last_push`,`price`,`status_last_push`),
  KEY `index_search_notice_no_currency` (`task_index`,`symbol_id`,`status`,`price_type`,`price_condition`,`price`,`status_last_push`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='推送价格提醒表'

/*
** 独立添加索引
** ALTER TABLE `push_price_notice` ADD INDEX `index_search_notice_no_currency` (`task_index`, `symbol_id`, `status`, `price_type`, `price_condition`, `price`, `status_last_push`);
*/