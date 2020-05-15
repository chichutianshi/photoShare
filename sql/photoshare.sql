/*
Navicat MySQL Data Transfer

Source Server         : 云mysql
Source Server Version : 50729
Source Host           : 59.110.162.115:3306
Source Database       : photoshare

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-05-15 13:38:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for allphotos
-- ----------------------------
DROP TABLE IF EXISTS `allphotos`;
CREATE TABLE `allphotos` (
  `photoId` varchar(50) NOT NULL,
  `ownerId` varchar(50) NOT NULL,
  `instruction` text,
  `location` varchar(50) DEFAULT NULL,
  `photoURL` text NOT NULL,
  `likeNum` int(11) NOT NULL DEFAULT '0',
  `createTime` varchar(45) DEFAULT NULL,
  `categories` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`photoId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for commentreply
-- ----------------------------
DROP TABLE IF EXISTS `commentreply`;
CREATE TABLE `commentreply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commentid` varchar(50) NOT NULL COMMENT '被评论的评论id',
  `fromid` varchar(45) NOT NULL,
  `fromname` varchar(45) NOT NULL,
  `content` varchar(512) NOT NULL,
  `createTime` varchar(45) NOT NULL,
  `fromURL` text NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for photocomment
-- ----------------------------
DROP TABLE IF EXISTS `photocomment`;
CREATE TABLE `photocomment` (
  `id` varchar(45) NOT NULL COMMENT '评论id',
  `photoId` varchar(45) NOT NULL COMMENT '照片id',
  `fromid` varchar(45) NOT NULL COMMENT '评论者id',
  `fromname` varchar(45) NOT NULL COMMENT '评论者昵称',
  `fromURL` text NOT NULL COMMENT '评论者头像',
  `content` varchar(512) NOT NULL COMMENT '评论内容',
  `createTime` varchar(45) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='评论者id';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(50) NOT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `province` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `openid` varchar(70) DEFAULT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `avatarURL` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
