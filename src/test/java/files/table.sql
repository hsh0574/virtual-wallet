#创建虚拟账户信息表
CREATE TABLE `prefix_account` (
  `uadid` varchar(50) NOT NULL COMMENT '虚拟账户',
  `pay_sign` varchar(100) DEFAULT NULL COMMENT '支付密码',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态（0无效；1正常；2禁用；3转移；4转移中。默认为1）',
  `move_target` varchar(50) DEFAULT NULL COMMENT '转移目标账号',
  `move_balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '转移余额',
  `created` datetime NOT NULL COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近更新时间',
  `balance` int(11) NOT NULL DEFAULT '0' COMMENT '虚拟账户余额，默认0',
  `froms` int(11) NOT NULL DEFAULT '1' COMMENT '来源，1当前系统，默认1',
  `set_status` int(1) NOT NULL DEFAULT '0' COMMENT '设置提现账户状态（0未设置；1已设置）',
  `retain_fees` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '账户保留余额（提现时保留此数值的余额）',
  PRIMARY KEY (`uadid`),
  KEY `idx_usr_acc_qb_status` (`status`),
  KEY `idx_usr_acc_qb_created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户虚拟账户信息表';

#创建交易明细信息表
CREATE TABLE `prefix_account_detail` (
  `uaddid` varchar(50) NOT NULL COMMENT '虚拟账户明细流水',
  `uadid` varchar(50) NOT NULL COMMENT '所属虚拟账户',
  `types` int(11) NOT NULL COMMENT '类型（1支出、2收入）',
  `sub_types` int(11) NOT NULL DEFAULT '0' COMMENT '子类型(1奖金、2转账(入)、3充值、4退款、5取消订单、-1购买、-2转账（出）、-3提现)',
  `money` int(11) NOT NULL COMMENT '金额（收入为正数、支出为负数）',
  `created` datetime NOT NULL COMMENT '记录时间',
  `use_money` int(11) NOT NULL DEFAULT '0' COMMENT '已经使用数量（主要用于原路返回及过期）',
  `use_begin` datetime NOT NULL COMMENT '开始可用时间',
  `use_end` datetime DEFAULT NULL COMMENT '过期时间',
  `use_status` int(11) NOT NULL DEFAULT '0' COMMENT '使用状态（0无；1正常；2已用完；3已过期）',
  `up_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `remarks` varchar(100) DEFAULT NULL COMMENT '说明，展示给用户',
  `froms` int(11) NOT NULL DEFAULT '1' COMMENT '来源系统（1当前系统，默认1）',
  `out_trade_no` varchar(64) NOT NULL DEFAULT '' COMMENT '流水号（同一商户流水号不能重复）',
  `out_type` int(11) NOT NULL DEFAULT '0' COMMENT '来源类型（默认为0）',
  `out_type_name` varchar(32) NOT NULL DEFAULT '' COMMENT '来源备注',
  `out_other` varchar(32) DEFAULT '' COMMENT '其他说明（来源说明）',
  `req_ip` varchar(32) DEFAULT NULL COMMENT '操作IP',
  `only_note` int(11) NOT NULL DEFAULT '0' COMMENT '是否仅仅是记录，不显示（0不是[功能中显示]；1只是记录[功能中不显示]）',
  PRIMARY KEY (`uaddid`),
  UNIQUE KEY `idx_froms_out_trade_no` (`froms`,`out_trade_no`) USING BTREE,
  KEY `idx_prefix_acc_det_uadid` (`uadid`),
  KEY `idx_prefix_acc_det_tradeno` (`out_trade_no`),
  KEY `idx_prefix_acc_det_types` (`types`),
  KEY `idx_prefix_acc_det_subtypes` (`sub_types`),
  KEY `idx_prefix_acc_det_created` (`created`),
  KEY `idx_prefix_acc_det_froms` (`froms`),
  KEY `idx_prefix_acc_det_outtype` (`out_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='虚拟账户明细信息表';

#创建虚拟账户转账关联明细信息表
CREATE TABLE `coin_account_transfer_note` (
  `out_adid` varchar(50) NOT NULL COMMENT '转出记录编号',
  `out_account` varchar(50) NOT NULL COMMENT '转出账号',
  `in_adid` varchar(50) NOT NULL COMMENT '转进记录编号',
  `in_account` varchar(50) NOT NULL COMMENT '转进账号',
  PRIMARY KEY (`out_adid`),
  UNIQUE KEY `idx_acc_transfer_note_in_adid` (`in_adid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='虚拟账户转账关联明细信息表';

#创建虚拟账户提现信息表
CREATE TABLE `prefix_account_withdraw` (
  `uadwid` varchar(20) NOT NULL COMMENT '记录编号，主键',
  `dzqb_no` varchar(20) NOT NULL COMMENT '提现虚拟账户',
  `fees` decimal(10,2) NOT NULL COMMENT '提现金额',
  `fact_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实际提现到账金额',
  `counter_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '提现手续费',
  `bank_code` varchar(16) NOT NULL COMMENT '提现银行代码',
  `bank_name` varchar(64) NOT NULL COMMENT '提现银行名称',
  `sub_bank_name` varchar(64) NOT NULL COMMENT '银行支付信息',
  `province_code` varchar(6) NOT NULL COMMENT '开户省代码',
  `city_code` varchar(6) NOT NULL COMMENT '开户市代码',
  `bank_no` varchar(64) NOT NULL COMMENT '提现银行账号',
  `bank_user_name` varchar(64) NOT NULL COMMENT '提现银行用户姓名',
  `mode` int(11) NOT NULL DEFAULT '2' COMMENT '提现方式（1微信钱包；2银行卡。默认为1）',
  `remark` varchar(128) DEFAULT NULL COMMENT '用户填写备注说明',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态（0用户申请[待财审]、1提现中[提现接口请求成功]、2已关闭[最终态，申请失败或提现失败]、3提现成功[最终态]、10财审通过。默认为0）',
  `is_submit_error` int(11) NOT NULL DEFAULT '0' COMMENT '申请提交财付通是否异常（0无异常；1有异常。默认为0）',
  `is_ret_ticket` int(11) NOT NULL DEFAULT '0' COMMENT '是否退票（0否；1退票）',
  `serial_no` varchar(50) NOT NULL COMMENT '交易明细编号。这里为虚拟账户明细表的主键uaddid',
  `serial_interface` varchar(50) DEFAULT NULL COMMENT '申请提现流水号。（接口申请时，由接口返回。暂不用）',
  `close_type` int(11) NOT NULL DEFAULT '0' COMMENT '关闭类型（0无）',
  `close_reason` varchar(200) DEFAULT NULL COMMENT '关闭原因',
  `created` datetime NOT NULL COMMENT '申请时间',
  `update_date` datetime NOT NULL COMMENT '最后操作时间',
  `handler` varchar(50) DEFAULT NULL COMMENT '最近操作人',
  `batch` varchar(20) DEFAULT NULL COMMENT '提现导出批次',
  `batch_date` datetime DEFAULT NULL COMMENT '提交银行时间',
  `audit_date` datetime DEFAULT NULL COMMENT '财务审核通过时间',
  `audit_user` varchar(32) DEFAULT NULL COMMENT '审核人',
  PRIMARY KEY (`uadwid`),
  KEY `idx_prefix_withdraw_dzqbno` (`dzqb_no`),
  KEY `idx_prefix_withdraw_mode` (`mode`),
  KEY `idx_prefix_withdraw_status` (`status`),
  KEY `idx_prefix_withdraw_serialno` (`serial_no`),
  KEY `idx_prefix_withdraw_created` (`created`),
  KEY `idx_prefix_withdraw_updatedate` (`update_date`),
  KEY `idx_prefix_withdraw_batch` (`batch`),
  KEY `idx_prefix_withdraw_batchdate` (`batch_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户提现信息表';

#创建使用记录关联获取记录信息
CREATE TABLE `coin_use_for_get` (
  `ufgid` varchar(64) NOT NULL COMMENT '主键编号',
  `unid` varchar(64) NOT NULL COMMENT '使用记录编号',
  `gnid` varchar(64) NOT NULL COMMENT '获取记录编号',
  `money` int(11) NOT NULL COMMENT '使用数量',
  `re_money` int(11) NOT NULL DEFAULT '0' COMMENT '退回数据（订单取消或售后退回。默认为0）',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态（1正常；2无效。默认为1）',
  `up_date` datetime NOT NULL COMMENT '最后处理时间',
  PRIMARY KEY (`ufgid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='使用记录关联获取记录信息';

#创建操作日志信息表
CREATE TABLE `prefix_handle_logs` (
  `bhlid` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增编号，主键',
  `table_name` varchar(50) NOT NULL COMMENT '操作对象表名称',
  `key_val` varchar(50) NOT NULL COMMENT '主键值',
  `types` int(11) NOT NULL COMMENT '操作类型（1添加；2修改；3删除；4其它）',
  `new_data` text COMMENT '新数据（json格式）',
  `old_data` text COMMENT '原始数据（json格式）',
  `created` datetime NOT NULL COMMENT '操作时间',
  `handler` varchar(50) NOT NULL COMMENT '操作人',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`bhlid`),
  KEY `idx_qb_hdl_logs_keyval` (`key_val`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志信息表';


######################配置库表信息##############################
#创建数据表主键编码规则表
CREATE TABLE `base_code_rule` (
  `table_english_name` varchar(30) NOT NULL COMMENT '表名',
  `table_chinese_name` varchar(30) DEFAULT NULL COMMENT '表的中文说明',
  `code_prefix` varchar(20) DEFAULT NULL COMMENT '（暂不用）编码规则前缀',
  `curr_date` varchar(8) NOT NULL COMMENT '当前日期戳',
  `sequence` bigint(20) NOT NULL COMMENT '累加的值，初始值可以自己定',
  `types` int(11) NOT NULL DEFAULT '1' COMMENT '生成类型。1按日期重新编号；2累加型',
  PRIMARY KEY (`table_english_name`),
  UNIQUE KEY `unique_basic_tb_en_name` (`table_english_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED COMMENT='编码规则表';

#创建系统参数信息表
CREATE TABLE `base_system_params` (
  `name` varchar(32) NOT NULL COMMENT '系统参数英文名称',
  `types` int(11) NOT NULL COMMENT '类型（0 混合型；1整型；2字符；3时间；4double类型）',
  `ints` int(11) DEFAULT NULL COMMENT '整型参数值',
  `doubles` decimal(8,2) DEFAULT NULL COMMENT 'double类型的参数值',
  `str` varchar(2000) DEFAULT NULL COMMENT '字符参数值',
  `dates` datetime DEFAULT NULL COMMENT '时间参数值',
  `remark` varchar(100) NOT NULL COMMENT '说明',
  `created` datetime NOT NULL COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `handler` varchar(32) NOT NULL COMMENT '操作人',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数信息表';