CREATE TABLE `cust_photo` (
  `cust_id` varchar(128)  NOT NULL COMMENT '人员id',
  `user_name` varchar(128)  NOT NULL DEFAULT '' COMMENT '用户名称',
  `company_id` varchar(128)  NOT NULL COMMENT '公司id',
  `features` varchar(1000)  NOT NULL DEFAULT '' COMMENT '特征码',
  `base64` longtext   COMMENT '照片base64',
  `update_id` bigint DEFAULT NULL COMMENT '更新id',
  `revision` int NOT NULL DEFAULT '0' COMMENT '乐观锁',
  `crby` varchar(32)  NOT NULL DEFAULT '' COMMENT '创建人',
  `crtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `upby` varchar(32)  NOT NULL DEFAULT '' COMMENT '更新人',
  `uptime` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
  PRIMARY KEY (`cust_id`) USING BTREE
)  COMMENT='人员生物识别特征表';
