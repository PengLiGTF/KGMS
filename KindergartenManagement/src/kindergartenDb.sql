/*
SQLyog v10.2 
MySQL - 5.5.47 : Database - kindergartendb
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`kindergartendb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `kindergartendb`;

/*Table structure for table `classes` */

DROP TABLE IF EXISTS `classes`;

CREATE TABLE `classes` (
  `class_id` int(11) NOT NULL AUTO_INCREMENT,
  `class_name` varchar(100) DEFAULT NULL,
  `grade_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

/*Data for the table `classes` */

/*Table structure for table `fee_template` */

DROP TABLE IF EXISTS `fee_template`;

CREATE TABLE `fee_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fee_template_name` varchar(200) DEFAULT NULL,
  `fee_amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `fee_template` */

insert  into `fee_template`(`id`,`fee_template_name`,`fee_amount`) values (4,'一年级',1800);

/*Table structure for table `fun_operation` */

DROP TABLE IF EXISTS `fun_operation`;

CREATE TABLE `fun_operation` (
  `operation_id` varchar(50) NOT NULL,
  `operation_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `fun_operation` */

insert  into `fun_operation`(`operation_id`,`operation_name`) values ('btnAdd','增加'),('btnDelete','删除'),('btnEdit','修改'),('btnExport','导出'),('btnPrint','打印'),('btnQuery','查询');

/*Table structure for table `grade` */

DROP TABLE IF EXISTS `grade`;

CREATE TABLE `grade` (
  `grade_id` int(11) NOT NULL AUTO_INCREMENT,
  `grade_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`grade_id`),
  UNIQUE KEY `grade_name` (`grade_name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;

/*Data for the table `grade` */

/*Table structure for table `kinder` */

DROP TABLE IF EXISTS `kinder`;

CREATE TABLE `kinder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kinder_id` varchar(100) DEFAULT NULL,
  `kinder_name` varchar(100) DEFAULT NULL,
  `kinder_sex` char(1) DEFAULT NULL,
  `kinder_class_id` int(11) DEFAULT NULL,
  `kinder_status_Code` varchar(50) DEFAULT NULL,
  `kinder_grade_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

/*Data for the table `kinder` */

/*Table structure for table `kinder_fee_history` */

DROP TABLE IF EXISTS `kinder_fee_history`;

CREATE TABLE `kinder_fee_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fee_template_id` int(11) DEFAULT NULL,
  `kinder_id` varchar(100) DEFAULT NULL,
  `fee_days` int(11) DEFAULT NULL,
  `fee_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fee_expire_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `operator_user_id` varchar(100) DEFAULT NULL,
  `operator_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `privilege_money` int(11) DEFAULT NULL,
  `other_money` int(11) DEFAULT NULL,
  `actual_money` int(11) DEFAULT NULL,
  `pre_fee` int(11) DEFAULT NULL,
  `feeVoucher_status` int(11) DEFAULT 101,
  `fee_event` varchar(20) DEFAULT NULL,
  `deduction_prefee` int(11) DEFAULT NULL,
  `fee_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Data for the table `kinder_fee_history` */

/*Table structure for table `kinderidsequence` */

DROP TABLE IF EXISTS `kinderidsequence`;

CREATE TABLE `kinderidsequence` (
  `name` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `kinderidsequence` */

insert  into `kinderidsequence`(`name`,`current_value`,`increment`) values ('MovieSeq',3,5);

/*Table structure for table `kinderleave` */

DROP TABLE IF EXISTS `kinderleave`;

CREATE TABLE `kinderleave` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kinder_id` varchar(100) DEFAULT NULL,
  `kinder_name` varchar(100) DEFAULT NULL,
  `leave_days` int(11) DEFAULT NULL,
  `operator_id` varchar(50) DEFAULT NULL,
  `leave_start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `leave_end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `operator_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `leave_description` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `kinderleave` */

/*Table structure for table `module` */

DROP TABLE IF EXISTS `module`;

CREATE TABLE `module` (
  `module_id` varchar(50) NOT NULL,
  `module_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`module_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `module` */

insert  into `module`(`module_id`,`module_name`) values ('m_0001','用户与权限管理'),('m_0002','儿童信息管理'),('m_0003','财务管理'),('m_0004','系统管理');

/*Table structure for table `module_fun` */

DROP TABLE IF EXISTS `module_fun`;

CREATE TABLE `module_fun` (
  `fun_id` varchar(50) NOT NULL,
  `module_id` varchar(50) DEFAULT NULL,
  `fun_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`fun_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `module_fun` */

insert  into `module_fun`(`fun_id`,`module_id`,`fun_name`) values ('f_0001','m_0001','用户管理'),('f_0002','m_0001','权限管理'),('f_0003','m_0002','班级管理'),('f_0004','m_0002','儿童信息管理'),('f_0005','m_0003','收费标准管理'),('f_0006','m_0003','入园学生录入'),('f_0007','m_0003','预缴费学生管理'),('f_0008','m_0003','续费学生录入'),('f_0009','m_0003','请假条录入'),('f_0010','m_0003','下月到期学生统计'),('f_0011','m_0004','缴费反审核'),('f_0012','m_0003','学生缴费修改');

/*Table structure for table `mysequence` */

DROP TABLE IF EXISTS `mysequence`;

CREATE TABLE `mysequence` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `mysequence` */

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `role_id` varchar(20) NOT NULL,
  `role_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`role_id`,`role_name`) values ('r_0001','管理员'),('r_0002','操作员');

/*Table structure for table `role_fun` */

DROP TABLE IF EXISTS `role_fun`;

CREATE TABLE `role_fun` (
  `role_id` varchar(50) DEFAULT NULL,
  `fun_id` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_fun` */

insert  into `role_fun`(`role_id`,`fun_id`) values ('r_0002','f_0001'),('r_0002','f_0003'),('r_0002','f_0004'),('r_0002','f_0005'),('r_0002','f_0006'),('r_0002','f_0007'),('r_0002','f_0008'),('r_0002','f_0009'),('r_0002','f_0010'),('r_0002','f_0012'),('r_0001','f_0001'),('r_0001','f_0002'),('r_0001','f_0003'),('r_0001','f_0004'),('r_0001','f_0005'),('r_0001','f_0006'),('r_0001','f_0007'),('r_0001','f_0008'),('r_0001','f_0009'),('r_0001','f_0010'),('r_0001','f_0012'),('r_0001','f_0011');

/*Table structure for table `role_operation` */

DROP TABLE IF EXISTS `role_operation`;

CREATE TABLE `role_operation` (
  `role_id` varchar(50) DEFAULT NULL,
  `fun_id` varchar(50) DEFAULT NULL,
  `operation_id` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_operation` */

insert  into `role_operation`(`role_id`,`fun_id`,`operation_id`) values ('r_0002','f_0001','btnAdd'),('r_0002','f_0001','btnDelete'),('r_0002','f_0001','btnEdit'),('r_0002','f_0001','btnExport'),('r_0002','f_0001','btnPrint'),('r_0002','f_0001','btnQuery'),('r_0002','f_0003','btnAdd'),('r_0002','f_0003','btnDelete'),('r_0002','f_0003','btnEdit'),('r_0002','f_0003','btnExport'),('r_0002','f_0003','btnPrint'),('r_0002','f_0003','btnQuery'),('r_0002','f_0004','btnAdd'),('r_0002','f_0004','btnDelete'),('r_0002','f_0004','btnEdit'),('r_0002','f_0004','btnExport'),('r_0002','f_0004','btnPrint'),('r_0002','f_0004','btnQuery'),('r_0002','f_0005','btnAdd'),('r_0002','f_0005','btnDelete'),('r_0002','f_0005','btnEdit'),('r_0002','f_0005','btnExport'),('r_0002','f_0005','btnPrint'),('r_0002','f_0005','btnQuery'),('r_0002','f_0006','btnAdd'),('r_0002','f_0006','btnDelete'),('r_0002','f_0006','btnEdit'),('r_0002','f_0006','btnExport'),('r_0002','f_0006','btnPrint'),('r_0002','f_0006','btnQuery'),('r_0002','f_0007','btnAdd'),('r_0002','f_0007','btnDelete'),('r_0002','f_0007','btnEdit'),('r_0002','f_0007','btnExport'),('r_0002','f_0007','btnPrint'),('r_0002','f_0007','btnQuery'),('r_0002','f_0008','btnAdd'),('r_0002','f_0008','btnDelete'),('r_0002','f_0008','btnEdit'),('r_0002','f_0008','btnExport'),('r_0002','f_0008','btnPrint'),('r_0002','f_0008','btnQuery'),('r_0002','f_0009','btnAdd'),('r_0002','f_0009','btnDelete'),('r_0002','f_0009','btnEdit'),('r_0002','f_0009','btnExport'),('r_0002','f_0009','btnPrint'),('r_0002','f_0009','btnQuery'),('r_0002','f_0010','btnAdd'),('r_0002','f_0010','btnDelete'),('r_0002','f_0010','btnEdit'),('r_0002','f_0010','btnExport'),('r_0002','f_0010','btnPrint'),('r_0002','f_0010','btnQuery'),('r_0002','f_0012','btnAdd'),('r_0002','f_0012','btnDelete'),('r_0002','f_0012','btnEdit'),('r_0002','f_0012','btnExport'),('r_0002','f_0012','btnPrint'),('r_0002','f_0012','btnQuery'),('r_0001','f_0001','btnAdd'),('r_0001','f_0001','btnDelete'),('r_0001','f_0001','btnEdit'),('r_0001','f_0001','btnExport'),('r_0001','f_0001','btnPrint'),('r_0001','f_0001','btnQuery'),('r_0001','f_0002','btnAdd'),('r_0001','f_0002','btnDelete'),('r_0001','f_0002','btnEdit'),('r_0001','f_0002','btnExport'),('r_0001','f_0002','btnPrint'),('r_0001','f_0002','btnQuery'),('r_0001','f_0003','btnAdd'),('r_0001','f_0003','btnDelete'),('r_0001','f_0003','btnEdit'),('r_0001','f_0003','btnExport'),('r_0001','f_0003','btnPrint'),('r_0001','f_0003','btnQuery'),('r_0001','f_0004','btnAdd'),('r_0001','f_0004','btnDelete'),('r_0001','f_0004','btnEdit'),('r_0001','f_0004','btnExport'),('r_0001','f_0004','btnPrint'),('r_0001','f_0004','btnQuery'),('r_0001','f_0005','btnAdd'),('r_0001','f_0005','btnDelete'),('r_0001','f_0005','btnEdit'),('r_0001','f_0005','btnExport'),('r_0001','f_0005','btnPrint'),('r_0001','f_0005','btnQuery'),('r_0001','f_0006','btnAdd'),('r_0001','f_0006','btnDelete'),('r_0001','f_0006','btnEdit'),('r_0001','f_0006','btnExport'),('r_0001','f_0006','btnPrint'),('r_0001','f_0006','btnQuery'),('r_0001','f_0007','btnAdd'),('r_0001','f_0007','btnDelete'),('r_0001','f_0007','btnEdit'),('r_0001','f_0007','btnExport'),('r_0001','f_0007','btnPrint'),('r_0001','f_0007','btnQuery'),('r_0001','f_0008','btnAdd'),('r_0001','f_0008','btnDelete'),('r_0001','f_0008','btnEdit'),('r_0001','f_0008','btnExport'),('r_0001','f_0008','btnPrint'),('r_0001','f_0008','btnQuery'),('r_0001','f_0009','btnAdd'),('r_0001','f_0009','btnDelete'),('r_0001','f_0009','btnEdit'),('r_0001','f_0009','btnExport'),('r_0001','f_0009','btnPrint'),('r_0001','f_0009','btnQuery'),('r_0001','f_0010','btnAdd'),('r_0001','f_0010','btnDelete'),('r_0001','f_0010','btnEdit'),('r_0001','f_0010','btnExport'),('r_0001','f_0010','btnPrint'),('r_0001','f_0010','btnQuery'),('r_0001','f_0012','btnAdd'),('r_0001','f_0012','btnDelete'),('r_0001','f_0012','btnEdit'),('r_0001','f_0012','btnExport'),('r_0001','f_0012','btnPrint'),('r_0001','f_0012','btnQuery'),('r_0001','f_0011','btnAdd'),('r_0001','f_0011','btnDelete'),('r_0001','f_0011','btnEdit'),('r_0001','f_0011','btnExport'),('r_0001','f_0011','btnPrint'),('r_0001','f_0011','btnQuery');

/*Table structure for table `sequence` */

DROP TABLE IF EXISTS `sequence`;

CREATE TABLE `sequence` (
  `name` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sequence` */

insert  into `sequence`(`name`,`current_value`,`increment`) values ('kinderFeeIdSeq',100000000,1),('kinderIdSeq',38,1),('kinderPreFeeIdSeq',200000000,1),('kinderRenewFeeIdSeq',300000000,1);

/*Table structure for table `test` */

DROP TABLE IF EXISTS `test`;

CREATE TABLE `test` (
  `name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `test` */

insert  into `test`(`name`) values ('name0'),('name1'),('name2'),('name3'),('name4'),('name5'),('name6'),('name7'),('name8'),('name9'),('1'),('2009'),('2010');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` varchar(50) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `user_password` varchar(100) DEFAULT NULL,
  `user_sex` char(1) DEFAULT NULL,
  `user_age` int(11) DEFAULT NULL,
  `user_address` varchar(200) DEFAULT NULL,
  `user_mail` varchar(100) DEFAULT NULL,
  `user_phone` varchar(12) DEFAULT NULL,
  `is_first` char(1) DEFAULT 'T',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`user_id`,`user_name`,`user_password`,`user_sex`,`user_age`,`user_address`,`user_mail`,`user_phone`,`is_first`) values ('admin','管理员','1b3231655cebb7a1f783eddf27d254ca','M',18,'湖南长沙','admin@126.com','12345678911','F'),('lp','test','1b3231655cebb7a1f783eddf27d254ca','M',NULL,NULL,'','','T');

/*Table structure for table `user_oper_log` */

DROP TABLE IF EXISTS `user_oper_log`;

CREATE TABLE `user_oper_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) DEFAULT NULL,
  `oper_type` varchar(50) DEFAULT NULL,
  `oper_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_oper_log` */

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` varchar(50) DEFAULT NULL,
  `role_id` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_role` */

insert  into `user_role`(`user_id`,`role_id`) values ('admin','r_0001'),('lp','r_0002');

/* Function  structure for function  `currval` */

/*!50003 DROP FUNCTION IF EXISTS `currval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `currval`(seq_name varchar(50)) RETURNS int(11)
begin
   declare svalue integer default 0;
   set svalue = 0;
   select current_value into svalue from sequence where name=seq_name;
   return svalue;
end */$$
DELIMITER ;

/* Function  structure for function  `getdate` */

/*!50003 DROP FUNCTION IF EXISTS `getdate` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `getdate`(gdate datetime) RETURNS varchar(255) CHARSET utf8
BEGIN  
DECLARE x VARCHAR(255) DEFAULT '';  
SET x= date_format(gdate,'%Y年%m月%d日%h时%i分%s秒');  
RETURN x;  
END */$$
DELIMITER ;

/* Function  structure for function  `mysequence.nextval` */

/*!50003 DROP FUNCTION IF EXISTS `mysequence.nextval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `mysequence.nextval`() RETURNS bigint(20)
    READS SQL DATA
BEGIN
      insert mysequence values( NULL );
      delete from mysequence;
      return LAST_INSERT_ID();
END */$$
DELIMITER ;

/* Function  structure for function  `nextval` */

/*!50003 DROP FUNCTION IF EXISTS `nextval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` FUNCTION `nextval`(seq_name VARCHAR(50)) RETURNS int(11)
BEGIN
   UPDATE sequence
   SET          current_value = current_value + increment
   WHERE name = seq_name;
   RETURN currval(seq_name);
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
