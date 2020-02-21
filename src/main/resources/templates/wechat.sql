/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50027
Source Host           : localhost:3306
Source Database       : wechat

Target Server Type    : MYSQL
Target Server Version : 50027
File Encoding         : 65001

Date: 2020-02-18 17:50:55
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `config`
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` int(11) unsigned NOT NULL auto_increment COMMENT '微信公众号配置信息表',
  `secret` varchar(100) NOT NULL,
  `appid` varchar(30) NOT NULL,
  `wechat_id` varchar(30) NOT NULL,
  `token` varchar(10) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO config VALUES ('1', '0631f2985c942485ba55f284ddb06af5', 'wx366bf4f1217335fa', 'renx', 'renx123');

-- ----------------------------
-- Table structure for `wxmenu`
-- ----------------------------
DROP TABLE IF EXISTS `wxmenu`;
CREATE TABLE `wxmenu` (
  `menu_id` int(11) NOT NULL COMMENT '菜单标识',
  `inner_appid` varchar(32) default '2' COMMENT '内部APPID',
  `name` varchar(128) NOT NULL COMMENT '菜单名字',
  `menu_type` int(11) default '0' COMMENT '菜单类型 0:模板; 1:网页 ',
  `uri` varchar(128) default NULL COMMENT '网页地址',
  `level` int(11) default '0' COMMENT '层级: -1:外部系统的菜单;0:微信菜单栏; 1:微信菜单栏的菜单;',
  `icon_uri` varchar(128) default NULL COMMENT '图标地址',
  `parent_id` int(11) default '0' COMMENT '父菜单标识',
  `status` int(11) default '0' COMMENT '状态: 0:正常; 1:无效;',
  PRIMARY KEY  (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信菜单表';

-- ----------------------------
-- Records of wxmenu
-- ----------------------------
INSERT INTO wxmenu VALUES ('1', 'renx', '个人中心', '0', '', '0', '', '0', '0');
INSERT INTO wxmenu VALUES ('2', 'renx', '精彩内容', '0', '', '0', '', '0', '0');
INSERT INTO wxmenu VALUES ('3', 'renx', '首页', '0', '', '0', '', '0', '0');
INSERT INTO wxmenu VALUES ('4', 'renx', '电子健康卡', '1', 'www.baidu.com', '0', '', '1', '0');
INSERT INTO wxmenu VALUES ('5', 'renx', '微信开发', '1', 'www.baidu.com', '0', '', '2', '0');
INSERT INTO wxmenu VALUES ('6', 'renx', '开发', '1', 'www.baidu.com', '0', '', '3', '0');

-- ----------------------------
-- Table structure for `wxuser`
-- ----------------------------
DROP TABLE IF EXISTS `wxuser`;
CREATE TABLE `wxuser` (
  `id` bigint(20) NOT NULL auto_increment COMMENT '主键ID自增',
  `user_id` varchar(36) NOT NULL COMMENT '内部用户ID',
  `inner_appid` varchar(32) NOT NULL COMMENT '内部APPID',
  `open_id` varchar(36) NOT NULL COMMENT '微信OPENID',
  `nick_name` varchar(128) NOT NULL COMMENT '微信昵称',
  `login_name` varchar(16) default NULL COMMENT 'WEB登录名称',
  `user_name` varchar(36) default NULL COMMENT '用户名称',
  `mobile` varchar(16) default NULL COMMENT '手机号码',
  `email_addr` varchar(36) default NULL COMMENT '邮箱地址',
  `password` varchar(36) default NULL COMMENT 'WEB登陆密码',
  `address` varchar(64) default NULL COMMENT '地址',
  `gender` varchar(4) default NULL COMMENT '性别',
  `city` varchar(64) default NULL COMMENT '城市',
  `province` varchar(64) default NULL COMMENT '省',
  `country` varchar(64) default NULL COMMENT '国家',
  `avatar_url` varchar(256) default NULL COMMENT '头像URL',
  `union_id` varchar(36) default NULL COMMENT '微信unionId',
  `create_date` datetime default NULL,
  `modify_date` datetime default NULL,
  `cert_type` int(10) default NULL COMMENT '证件类型',
  `certNo` varchar(64) default NULL COMMENT '证件号码',
  PRIMARY KEY  (`id`),
  KEY `idx_userid` (`user_id`),
  KEY `idx_appid_openid` (`inner_appid`,`open_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信用户表';

-- ----------------------------
-- Records of wxuser
-- ----------------------------
