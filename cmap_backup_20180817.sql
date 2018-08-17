-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- 主機: localhost:3306
-- 產生時間： 2018 年 08 月 16 日 23:50
-- 伺服器版本: 5.7.17-log
-- PHP 版本： 7.1.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `cmap`
--
CREATE DATABASE IF NOT EXISTS `cmap` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `cmap`;

-- --------------------------------------------------------

--
-- 資料表結構 `config_version_info`
--

DROP TABLE IF EXISTS `config_version_info`;
CREATE TABLE `config_version_info` (
  `VERSION_ID` varchar(32) NOT NULL COMMENT 'UUID PK',
  `GROUP_ID` varchar(32) NOT NULL COMMENT '群組代碼',
  `GROUP_NAME` varchar(100) DEFAULT NULL COMMENT '群組名稱',
  `DEVICE_ID` varchar(32) NOT NULL COMMENT '設備代碼',
  `DEVICE_NAME` varchar(100) DEFAULT NULL COMMENT '設備名稱',
  `SYSTEM_VERSION` varchar(100) NOT NULL COMMENT '系統版本',
  `CONFIG_TYPE` varchar(30) NOT NULL COMMENT '組態檔類型',
  `CONFIG_VERSION` varchar(50) NOT NULL COMMENT '組態檔版本號',
  `FILE_FULL_NAME` varchar(100) NOT NULL,
  `DELETE_FLAG` varchar(1) NOT NULL COMMENT '刪除註記',
  `DELETE_TIME` timestamp NULL DEFAULT NULL COMMENT '刪除時間',
  `DELETE_BY` varchar(50) DEFAULT NULL COMMENT '刪除人',
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '資料首次建立時間',
  `CREATE_BY` varchar(50) NOT NULL COMMENT '資料首次建立人',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '資料最後異動時間',
  `UPDATE_BY` varchar(50) NOT NULL COMMENT '資料最後異動人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='版本備份記錄檔';

-- --------------------------------------------------------

--
-- 資料表結構 `device_list`
--

DROP TABLE IF EXISTS `device_list`;
CREATE TABLE `device_list` (
  `DEVICE_LIST_ID` varchar(32) NOT NULL,
  `GROUP_ID` varchar(32) NOT NULL,
  `GROUP_NAME` varchar(100) DEFAULT NULL,
  `DEVICE_ID` varchar(32) NOT NULL,
  `DEVICE_NAME` varchar(100) DEFAULT NULL,
  `DEVICE_IP` varchar(40) DEFAULT NULL,
  `SYSTEM_VERSION` varchar(100) DEFAULT NULL,
  `CONFIG_FILE_DIR_PATH` varchar(200) NOT NULL,
  `DELETE_FLAG` varchar(1) NOT NULL,
  `DELETE_TIME` timestamp NULL DEFAULT NULL,
  `DELETE_BY` varchar(50) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `CREATE_BY` varchar(50) NOT NULL,
  `UPDATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATE_BY` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `i18n`
--

DROP TABLE IF EXISTS `i18n`;
CREATE TABLE `i18n` (
  `i18n_id` int(11) NOT NULL,
  `I18N_KEY` varchar(50) NOT NULL,
  `I18N_VALUE` varchar(200) NOT NULL,
  `LOCALE_COUNTRY` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `menu_item`
--

DROP TABLE IF EXISTS `menu_item`;
CREATE TABLE `menu_item` (
  `MENU_ITEM_ID` varchar(32) NOT NULL COMMENT 'UUID PK',
  `MENU_CODE` varchar(50) NOT NULL COMMENT '選單代碼',
  `OPTION_LABEL` varchar(100) NOT NULL COMMENT '選項名稱',
  `OPTION_VALUE` varchar(100) NOT NULL COMMENT '選項值',
  `OPTION_ORDER` int(3) NOT NULL COMMENT '選項排序',
  `REMARK` varchar(200) DEFAULT NULL COMMENT '備註',
  `DELETE_FLAG` varchar(1) NOT NULL COMMENT '刪除註記',
  `DELETE_TIME` timestamp NULL DEFAULT NULL COMMENT '刪除時間',
  `DELETE_BY` varchar(50) DEFAULT NULL COMMENT '刪除人',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
  `CREATE_BY` varchar(50) NOT NULL COMMENT '建立人',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最後修改時間',
  `UPDATE_BY` varchar(50) NOT NULL COMMENT '最後修改人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表新增前先清除舊資料 `menu_item`
--

TRUNCATE TABLE `menu_item`;
--
-- 資料表的匯出資料 `menu_item`
--

INSERT INTO `menu_item` (`MENU_ITEM_ID`, `MENU_CODE`, `OPTION_LABEL`, `OPTION_VALUE`, `OPTION_ORDER`, `REMARK`, `DELETE_FLAG`, `DELETE_TIME`, `DELETE_BY`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`) VALUES
('0982089b-9c4e-11e8-b986-4ccc6a7f', 'SCHED_TYPE', '組態備份檔異地備援(FTP)', 'uploadBackupConfigFile2FTP', 2, '排程備份項目選單', 'N', NULL, NULL, '2018-08-06 02:43:01', 'SYS', '2018-08-06 02:43:01', 'SYS'),
('439dbe85-9923-11e8-8fdc-4ccc6a7f', 'MIS_FIRE_POLICY', '錯過啟動時間，不做處理', '2', 1, '排程策略選單', 'N', NULL, NULL, '2018-08-06 02:43:01', 'SYS', '2018-08-06 02:43:01', 'SYS'),
('4fec1b85-9923-11e8-8fdc-4ccc6a7f', 'MIS_FIRE_POLICY', '錯過啟動時間，立即啟動', '1', 2, '排程策略選單', 'N', NULL, NULL, '2018-08-06 02:43:01', 'SYS', '2018-08-06 02:43:01', 'SYS'),
('590c6f3a-9923-11e8-8fdc-4ccc6a7f', 'MIS_FIRE_POLICY', '忽略', '-1', 3, '排程策略選單', 'N', NULL, NULL, '2018-08-06 02:43:01', 'SYS', '2018-08-06 02:43:01', 'SYS'),
('751ce3ee-9922-11e8-8fdc-4ccc6a7f', 'SCHED_TYPE', '組態檔備份', 'backupConfig', 1, '排程備份項目選單', 'N', NULL, NULL, '2018-08-06 02:43:01', 'SYS', '2018-08-06 02:43:01', 'SYS'),
('ead9c202-96fb-11e8-8fdc-4ccc6a7f', 'CONFIG_TYPE', 'running-config', 'RUNNING_CONFIG', 1, NULL, 'N', NULL, NULL, '2018-08-03 09:02:14', 'SYS', '2018-08-02 16:00:00', 'SYS'),
('ead9cc3c-96fb-11e8-8fdc-4ccc6a7f', 'CONFIG_TYPE', 'startup-config', 'STARTUP_CONFIG', 2, NULL, 'N', NULL, NULL, '2018-08-03 09:02:14', 'SYS', '2018-08-03 09:02:14', 'SYS');

-- --------------------------------------------------------

--
-- 資料表結構 `provision_log_device`
--

DROP TABLE IF EXISTS `provision_log_device`;
CREATE TABLE `provision_log_device` (
  `LOG_DEVICE_ID` varchar(32) NOT NULL,
  `LOG_STEP_ID` varchar(32) NOT NULL,
  `ORDER_NUM` int(11) NOT NULL,
  `DEVICE_LIST_ID` varchar(32) NOT NULL,
  `REMARK` varchar(300) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATE_BY` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `provision_log_master`
--

DROP TABLE IF EXISTS `provision_log_master`;
CREATE TABLE `provision_log_master` (
  `LOG_MASTER_ID` varchar(32) NOT NULL,
  `USER_NAME` varchar(50) NOT NULL,
  `USER_IP` varchar(50) NOT NULL,
  `RESULT` varchar(50) NOT NULL,
  `MESSAGE` varchar(500) DEFAULT NULL,
  `BEGIN_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `END_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `SPEND_TIME_IN_SECONDS` int(11) NOT NULL,
  `REMARK` varchar(300) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `CREATE_BY` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `provision_log_retry`
--

DROP TABLE IF EXISTS `provision_log_retry`;
CREATE TABLE `provision_log_retry` (
  `LOG_RETRY_ID` varchar(32) NOT NULL,
  `LOG_STEP_ID` varchar(32) NOT NULL,
  `RETRY_ORDER` int(11) NOT NULL,
  `RESULT` varchar(50) NOT NULL,
  `MESSAGE` varchar(500) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATE_BY` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `provision_log_step`
--

DROP TABLE IF EXISTS `provision_log_step`;
CREATE TABLE `provision_log_step` (
  `LOG_STEP_ID` varchar(32) NOT NULL,
  `LOG_MASTER_ID` varchar(32) NOT NULL,
  `RESULT` varchar(50) NOT NULL,
  `MESSAGE` varchar(500) DEFAULT NULL,
  `SCRIPT_CODE` varchar(30) NOT NULL,
  `BEGIN_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `END_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `SPEND_TIME_IN_SECONDS` int(11) NOT NULL,
  `RETRY_TIMES` int(11) NOT NULL,
  `PROCESS_LOG` varchar(5000) DEFAULT NULL,
  `REMARK` varchar(300) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `CREATE_BY` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_simprop_triggers`
--

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `script_list_default`
--

DROP TABLE IF EXISTS `script_list_default`;
CREATE TABLE `script_list_default` (
  `SCRIPT_LIST_ID` varchar(32) NOT NULL COMMENT 'UUID PK',
  `SCRIPT_TYPE_ID` varchar(32) NOT NULL COMMENT '腳本類別PK',
  `SCRIPT_CODE` varchar(10) NOT NULL COMMENT '腳本代碼',
  `SCRIPT_NAME` varchar(100) NOT NULL COMMENT '腳本名稱',
  `SCRIPT_STEP_ORDER` int(3) NOT NULL COMMENT '腳本分段步驟順序',
  `SCRIPT_CONTENT` varchar(500) NOT NULL COMMENT '腳本內容',
  `EXPECTED_TERMINAL_SYMBOL` varchar(50) DEFAULT NULL COMMENT '預期腳本執行回傳結果終止符號',
  `OUTPUT` varchar(1) DEFAULT NULL COMMENT '資料輸出點',
  `HEAD_CUTTING_LINES` int(3) DEFAULT NULL COMMENT '內容起頭去除行數',
  `TAIL_CUTTING_LINES` int(3) DEFAULT NULL COMMENT '內容結尾去除行數',
  `REMARK` varchar(200) DEFAULT NULL COMMENT '附加備註',
  `ERROR_SYMBOL` varchar(50) DEFAULT NULL,
  `SCRIPT_DESCRIPTION` varchar(200) NOT NULL COMMENT '腳本說明',
  `DELETE_FLAG` varchar(1) NOT NULL COMMENT '刪除註記',
  `DELETE_TIME` timestamp NULL DEFAULT NULL COMMENT '刪除時間',
  `DELETE_BY` varchar(50) DEFAULT NULL COMMENT '刪除人',
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '資料首次建立時間',
  `CREATE_BY` varchar(50) NOT NULL COMMENT '資料首次建立人',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '資料最後異動時間',
  `UPDATE_BY` varchar(50) NOT NULL COMMENT '資料最後異動人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表新增前先清除舊資料 `script_list_default`
--

TRUNCATE TABLE `script_list_default`;
--
-- 資料表的匯出資料 `script_list_default`
--

INSERT INTO `script_list_default` (`SCRIPT_LIST_ID`, `SCRIPT_TYPE_ID`, `SCRIPT_CODE`, `SCRIPT_NAME`, `SCRIPT_STEP_ORDER`, `SCRIPT_CONTENT`, `EXPECTED_TERMINAL_SYMBOL`, `OUTPUT`, `HEAD_CUTTING_LINES`, `TAIL_CUTTING_LINES`, `REMARK`, `ERROR_SYMBOL`, `SCRIPT_DESCRIPTION`, `DELETE_FLAG`, `DELETE_TIME`, `DELETE_BY`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`) VALUES
('1d441c70-96f8-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 6, 'copy startup-config tftp:', '?', 'N', NULL, NULL, NULL, NULL, 'copy 組態檔 to TFTP', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('1d4426de-96f8-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 7, '[TFTP_IP]', '?', 'N', NULL, NULL, NULL, NULL, '輸入目標TFTP IP', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('1d4432b0-96f8-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 8, '[TFTP_OUTPUT_FILE_PATH]', '#', 'Y', NULL, NULL, 'STARTUP_CONFIG', 'ERROR', '目標輸出路徑及檔案名稱', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('288fb329-9628-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 1, 'enable', 'Password: ', 'N', NULL, NULL, NULL, NULL, '賦予管理權限', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('289008df-9628-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 2, '[ENABLE_PWD]', '#', 'N', NULL, NULL, NULL, NULL, '輸入Enable密碼', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('28901812-9628-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 3, 'copy running-config tftp:', '?', 'N', NULL, NULL, NULL, NULL, 'copy 組態檔 to TFTP', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('289028d3-9628-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 4, '[TFTP_IP]', '?', 'N', NULL, NULL, NULL, NULL, '輸入目標TFTP IP', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('28903318-9628-11e8-8fdc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_002', '備份腳本-by TFTP', 5, '[TFTP_OUTPUT_FILE_PATH]', '#', 'Y', NULL, NULL, 'RUNNING_CONFIG', 'ERROR', '目標輸出路徑及檔案名稱', 'N', NULL, NULL, '2018-08-02 07:46:00', 'SYS', '2018-08-02 07:46:00', 'SYS'),
('76cba6dd-90a0-11e8-affc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_001', '備份腳本', 1, 'enable', 'Password: ', 'N', NULL, NULL, NULL, NULL, '賦予管理權限', 'N', NULL, NULL, '2018-07-26 06:50:00', 'SYS', '2018-07-26 06:50:00', 'SYS'),
('76cbb780-90a0-11e8-affc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_001', '備份腳本', 2, '[ENABLE_PWD]', '#', 'N', NULL, NULL, NULL, NULL, '輸入Enable密碼', 'N', NULL, NULL, '2018-07-26 06:50:00', 'SYS', '2018-07-26 06:50:00', 'SYS'),
('8e66ba88-90a0-11e8-affc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_001', '備份腳本', 3, 'terminal length 0', '#', 'N', NULL, NULL, NULL, NULL, '取消分頁顯示', 'N', NULL, NULL, '2018-07-26 06:50:00', 'SYS', '2018-07-26 06:50:00', 'SYS'),
('a4eaadd4-90a0-11e8-affc-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_001', '備份腳本', 4, 'show running-config', '#', 'Y', 3, 1, 'running', NULL, '取得running config', 'N', NULL, NULL, '2018-07-26 06:50:00', 'SYS', '2018-07-26 06:50:00', 'SYS'),
('e8f96643-953a-11e8-ae43-4ccc6a7f', 'b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_001', '備份腳本', 5, 'show startup-config', '#', 'Y', 1, 1, 'startup', NULL, '取得startup config', 'N', NULL, NULL, '2018-08-01 03:27:00', 'SYS', '2018-08-01 03:27:00', 'SYS');

-- --------------------------------------------------------

--
-- 資料表結構 `script_type`
--

DROP TABLE IF EXISTS `script_type`;
CREATE TABLE `script_type` (
  `SCRIPT_TYPE_ID` varchar(32) NOT NULL COMMENT 'UUID PK',
  `SCRIPT_TYPE_CODE` varchar(10) NOT NULL COMMENT '腳本類別代碼',
  `SCRIPT_TYPE_NAME` varchar(30) NOT NULL COMMENT '腳本類別名稱',
  `DEFAULT_FLAG` varchar(1) NOT NULL COMMENT '預設類別',
  `DELETE_FLAG` varchar(1) NOT NULL COMMENT '刪除註記',
  `DELETE_TIME` timestamp NULL DEFAULT NULL COMMENT '刪除時間',
  `DELETE_BY` varchar(50) DEFAULT NULL COMMENT '刪除人',
  `CREATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '資料首次建立時間',
  `CREATE_BY` varchar(50) NOT NULL COMMENT '資料首次建立人',
  `UPDATE_TIME` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '資料最後異動時間',
  `UPDATE_BY` varchar(50) NOT NULL COMMENT '資料最後異動人'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表新增前先清除舊資料 `script_type`
--

TRUNCATE TABLE `script_type`;
--
-- 資料表的匯出資料 `script_type`
--

INSERT INTO `script_type` (`SCRIPT_TYPE_ID`, `SCRIPT_TYPE_CODE`, `SCRIPT_TYPE_NAME`, `DEFAULT_FLAG`, `DELETE_FLAG`, `DELETE_TIME`, `DELETE_BY`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`) VALUES
('b99f0340-909f-11e8-affc-4ccc6a7f', 'SYS_001', '備份', 'Y', 'N', NULL, NULL, '2018-07-26 06:46:00', 'SYS', '2018-07-26 06:46:00', 'SYS'),
('b99f1232-909f-11e8-affc-4ccc6a7f', 'SYS_002', '還原', 'Y', 'N', NULL, NULL, '2018-07-26 06:46:00', 'SYS', '2018-07-26 06:46:00', 'SYS');

-- --------------------------------------------------------

--
-- 資料表結構 `system_log`
--

DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log` (
  `LOG_ID` varchar(100) NOT NULL,
  `ENTRY_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `LOGGER` varchar(100) DEFAULT NULL,
  `LOG_LEVEL` varchar(100) DEFAULT NULL,
  `MESSAGE` text,
  `EXCEPTION` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 資料表結構 `sys_config_setting`
--

DROP TABLE IF EXISTS `sys_config_setting`;
CREATE TABLE `sys_config_setting` (
  `SETTING_ID` varchar(32) NOT NULL,
  `SETTING_NAME` varchar(100) NOT NULL,
  `SETTING_VALUE` varchar(300) NOT NULL,
  `SETTING_REMARK` varchar(100) DEFAULT NULL,
  `DELETE_FLAG` varchar(1) NOT NULL,
  `DELETE_TIME` timestamp NULL DEFAULT NULL,
  `DELETE_BY` varchar(50) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `CREATE_BY` varchar(50) NOT NULL,
  `UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `UPDATE_BY` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 資料表新增前先清除舊資料 `sys_config_setting`
--

TRUNCATE TABLE `sys_config_setting`;
--
-- 資料表的匯出資料 `sys_config_setting`
--

INSERT INTO `sys_config_setting` (`SETTING_ID`, `SETTING_NAME`, `SETTING_VALUE`, `SETTING_REMARK`, `DELETE_FLAG`, `DELETE_TIME`, `DELETE_BY`, `CREATE_TIME`, `CREATE_BY`, `UPDATE_TIME`, `UPDATE_BY`) VALUES
('02265d4d-9ad7-11e8-b986-4ccc6a7f', 'DECODE_FIELDS', 'DEFAULT_DEVICE_ENABLE_PASSWORD', '定義編碼欄位', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('0706145f-9a22-11e8-b986-4ccc6a7f', 'ADMIN_ROLE_USERNAME', 'prtgadmin', 'ADMIN腳色username ', 'N', NULL, NULL, '2018-08-07 09:12:37', 'SYS', '2018-08-09 04:03:08', 'admin'),
('07978eae-8e43-11e8-bca4-4ccc6a7f', 'DEFAULT_FTP_DIR_GROUP_NAME', 'GID_[gid]', 'Config落地檔預設群組(GROUP)層資料夾名稱\r\n([gid]為固定字眼for系統替換，不可修改)', 'N', NULL, NULL, '2018-07-23 06:38:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('07979c32-8e43-11e8-bca4-4ccc6a7f', 'DEFAULT_FTP_DIR_DEVICE_NAME', 'DID_[did]', 'Config落地檔預設設備(DEVICE)層資料夾名稱\r\n([did]為固定字眼for系統替換，不可修改)', 'N', NULL, NULL, '2018-07-23 06:38:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('094d95d1-99e7-11e8-8fdc-4ccc6a7f', 'ADMIN_USERNAME', 'YWRtaW4=', '', 'N', NULL, NULL, '2018-08-07 02:10:08', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('094d9c41-99e7-11e8-8fdc-4ccc6a7f', 'ADMIN_PASSWORD', 'AC9689E2272427085E35B9D3E3E8BED88CB3434828B43B86FC0596CAD4C6E270', '', 'N', NULL, NULL, '2018-08-07 02:10:08', 'SYS', '2018-08-09 06:06:20', 'prtgadmin'),
('0ce2e92a-96fc-11e8-8fdc-4ccc6a7f', 'MENU_CODE_OF_CONFIG_TYPE', 'CONFIG_TYPE', 'CONFIG_TYPE選單代碼 >> MenuItem.menuCode', 'N', NULL, NULL, '2018-08-03 02:37:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('13926355-96c3-11e8-8fdc-4ccc6a7f', 'DEFAULT_BACKUP_SCRIPT_CODE', 'SYS_002', '預設備份腳本代碼', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-09 08:14:29', 'SYS'),
('15624e13-866e-11e8-a90a-4ccc6a7f', 'FTP_LOGIN_ACCOUNT', 'ZnRwdXNlcg==', NULL, 'N', NULL, NULL, '2018-07-13 07:26:39', 'SYS', '2018-08-13 08:21:40', 'SYS'),
('1562569b-866e-11e8-a90a-4ccc6a7f', 'FTP_LOGIN_PASSWORD', 'aHdhY29tMTIz', NULL, 'N', NULL, NULL, '2018-07-13 07:26:39', 'SYS', '2018-08-13 08:21:40', 'SYS'),
('15835fbe-96e8-11e8-8fdc-4ccc6a7f', 'FILE_TRANSFER_MODE', 'TFTP', '設定組態檔傳輸模式 FOR 備份、預覽、比對', 'N', NULL, NULL, '2018-08-03 06:40:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('1794eb85-9f9d-11e8-b986-4ccc6a7f', 'TFTP_SERVER_AT_LOCAL', 'false', 'TFTP Server是否與系統架設在同一台主機上', 'N', NULL, NULL, '2018-08-14 08:35:45', 'SYS', '2018-08-15 03:00:58', 'prtgadmin'),
('19eee7e9-a1e6-11e8-9f3d-4ccc6a7f', 'USER_IP_JOB', 'localhost', '排程執行時預設的IP', 'N', NULL, NULL, '2018-08-17 06:23:43', 'SYS', '2018-08-17 06:23:43', 'SYS'),
('1a2ca2e8-9923-11e8-8fdc-4ccc6a7f', 'MENU_CODE_OF_SCHED_TYPE', 'SCHED_TYPE', 'SCHED_TYPE選單代碼 >> MenuItem.menuCode', 'N', NULL, NULL, '2018-08-03 02:37:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('206bd2a6-96c0-11e8-8fdc-4ccc6a7f', 'DEFAULT_DEVICE_LOGIN_ACCOUNT', 'Y2lzY28=', '預設裝置連線登入帳號', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('22ddd5bd-96c6-11e8-8fdc-4ccc6a7f', 'SIGN_TFTP_IP', '[TFTP_IP]', '命令腳本內容替換字眼 >> TFTP IP\r\n(**修改時須同步調整腳本TABLE設定**)', 'N', NULL, NULL, '2018-08-03 02:37:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('22dde044-96c6-11e8-8fdc-4ccc6a7f', 'SIGN_TFTP_OUTPUT_FILE_PATH', '[TFTP_OUTPUT_FILE_PATH]', '命令腳本內容替換字眼 >> \r\n(**修改時須同步調整腳本TABLE設定**)', 'N', NULL, NULL, '2018-08-03 02:37:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('25203137-9923-11e8-8fdc-4ccc6a7f', 'MENU_CODE_OF_MIS_FIRE_POLICY', 'MIS_FIRE_POLICY', 'MIS_FIRE_POLICY選單代碼 >> MenuItem.menuCode', 'N', NULL, NULL, '2018-08-03 02:37:15', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('2a2920e0-9c3d-11e8-b986-4ccc6a7f', 'RETRY_TIMES', '3', '腳本執行錯誤retry次數', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('2a869924-96c0-11e8-8fdc-4ccc6a7f', 'DEFAULT_DEVICE_LOGIN_PASSWORD', 'Y2lzY28=', '預設裝置連線登入密碼', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('2b771b27-9f9c-11e8-b986-4ccc6a7f', 'TFTP_LOCAL_ROOT_DIR_PATH', 'D:/TFTP', '本機TFTP Server檔案上傳根目錄', 'N', NULL, NULL, '2018-08-14 08:29:09', 'SYS', '2018-08-14 08:29:09', 'SYS'),
('2f21eb92-866e-11e8-a90a-4ccc6a7f', 'FTP_BASE_DIR_PATH', '/', '', 'N', NULL, NULL, '2018-07-13 07:27:23', 'SYS', '2018-08-16 04:07:35', 'prtgadmin'),
('2f21f4e3-866e-11e8-a90a-4ccc6a7f', 'FTP_DEFAULT_TIME_OUT', '1500', '', 'N', NULL, NULL, '2018-07-13 07:27:23', 'SYS', '2018-08-14 03:03:19', 'prtgadmin'),
('311ec5a1-96c3-11e8-8fdc-4ccc6a7f', 'DEFAULT_RESTORE_SCRIPT_CODE', 'SYS_003', '預設還原腳本代碼', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('37e221f0-866e-11e8-a90a-4ccc6a7f', 'FTP_CONNECT_TIME_OUT', '1500', '', 'N', NULL, NULL, '2018-07-13 07:27:37', 'SYS', '2018-08-14 03:04:14', 'prtgadmin'),
('3ca7cbda-9ed0-11e8-b986-4ccc6a7f', 'FILES_UPLOAD_PER_BATCH_COUNT', '50', '每日例行異地備份，分批上傳，每批數量限制設定', 'N', NULL, NULL, '2018-08-13 08:09:30', 'SYS', '2018-08-13 08:09:30', 'SYS'),
('40f1a241-96c5-11e8-8fdc-4ccc6a7f', 'SSH_CONNECT_TIME_OUT', '1000', 'SSH連線建立timeout時間', 'N', NULL, NULL, '2018-08-03 02:30:56', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('40f1b1bd-96c5-11e8-8fdc-4ccc6a7f', 'SSH_SOCKET_TIME_OUT', '1000', 'SSH socket timeout時間', 'N', NULL, NULL, '2018-08-03 02:30:56', 'SYS', '2018-08-14 03:04:14', 'prtgadmin'),
('4716d954-9f6c-11e8-b986-4ccc6a7f', 'TFTP_DEFAULT_TIME_OUT', '1500', 'TFTP DEFAULT TIMEOUT', 'N', NULL, NULL, '2018-08-13 18:46:23', 'SYS', '2018-08-13 18:47:17', 'prtgadmin'),
('4716e751-9f6c-11e8-b986-4ccc6a7f', 'TFTP_SOCKET_TIME_OUT', '1000', 'TFTP SOCKET TIMEOUT', 'N', NULL, NULL, '2018-08-13 18:46:23', 'SYS', '2018-08-13 18:47:17', 'prtgadmin'),
('4b128e96-9f9d-11e8-b986-4ccc6a7f', 'TFTP_TEMP_DIR_PATH', '/temp', 'TFTP檔案上傳暫存目錄 for 內容比對', 'N', NULL, NULL, '2018-08-14 08:37:11', 'SYS', '2018-08-15 03:10:33', 'prtgadmin'),
('4b6ecf04-96c6-11e8-8fdc-4ccc6a7f', 'QUARTZ_DEFAULT_PRIORITY', '5', '排程預設優先度', 'N', NULL, NULL, '2018-08-03 02:38:23', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('613ae82e-a1e7-11e8-9f3d-4ccc6a7f', 'PROVISION_REASON_OF_JOB', '系統排程例行作業', '供裝紀錄中「供裝原因」for 排程', 'N', NULL, NULL, '2018-08-17 06:32:52', 'SYS', '2018-08-17 06:32:52', 'SYS'),
('618bb2ef-9b99-11e8-b986-4ccc6a7f', 'TELNET_DEFAULT_TIME_OUT', '1500', 'TELNET 請求回應timeout時間', 'N', NULL, NULL, '2018-08-09 05:59:21', 'SYS', '2018-08-15 09:11:00', 'prtgadmin'),
('618be6c2-9b99-11e8-b986-4ccc6a7f', 'TELNET_CONNECT_TIME_OUT', '1500', 'TELNET 連線timeout時間', 'N', NULL, NULL, '2018-08-09 05:59:21', 'SYS', '2018-08-15 09:24:20', 'prtgadmin'),
('7dae9958-96c8-11e8-8fdc-4ccc6a7f', 'SIGN_ENABLE_PWD', '[ENABLE_PWD]', '命令腳本內容替換字眼 >> Enable密碼\r\n(**修改時須同步調整腳本TABLE設定**)', 'N', NULL, NULL, '2018-08-03 02:35:41', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('856add7f-96c5-11e8-8fdc-4ccc6a7f', 'CONFIG_FILE_EXTENSION_NAME', 'cfg', '組態備份落地檔附檔名', 'N', NULL, NULL, '2018-08-03 02:32:51', 'SYS', '2018-08-09 05:53:24', 'prtgadmin'),
('856b222f-96c5-11e8-8fdc-4ccc6a7f', 'COMM_SEPARATE_SYMBOL', '@~', '通用文字串接符號(勿任意修改，除影響系統內使用亦會影響DB內部分欄位設定內容)', 'N', NULL, NULL, '2018-08-03 02:32:51', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('87a3901d-9bb4-11e8-b986-4ccc6a7f', 'MENU_ITEM_COMBINE_SYMBOL', '. ', '當選單設定要組成order和label欄位時，中間的串接符號', 'N', NULL, NULL, '2018-08-07 02:10:08', 'SYS', '2018-08-09 06:06:20', 'prtgadmin'),
('89840fa9-96c0-11e8-8fdc-4ccc6a7f', 'DEFAULT_DEVICE_ENABLE_PASSWORD', 'Y2lzY28=', '預設裝置Enable密碼', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('8ccb97fa-9953-11e8-8fdc-4ccc6a7f', 'PRTG_SERVER_IP', 'https://192.168.26.153/', 'PRTG HOST', 'N', NULL, NULL, '2018-08-06 08:34:26', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('8ccba106-9953-11e8-8fdc-4ccc6a7f', 'PRTG_API_LOGIN', 'api/getpasshash.htm?username={username}&password={password}', 'PRTG Login API', 'N', NULL, NULL, '2018-08-06 08:34:26', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('9d54407e-9953-11e8-8fdc-4ccc6a7f', 'PRTG_API_SENSOR_TREE', 'api/table.xml?content=sensortree&output=xml&username={username}&passhash={passhash}', 'PRTG get sensor tree API', 'N', NULL, NULL, '2018-08-06 08:34:54', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('c23ce760-96bf-11e8-8fdc-4ccc6a7f', 'TFTP_HOST_IP', '192.168.26.153', 'TFTP IP address', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-15 02:45:52', 'prtgadmin'),
('c2fde428-99de-11e8-8fdc-4ccc6a7f', 'LOGIN_AUTH_MODE', 'PRTG', '登入驗證模式 (PRTG / PRIME / DB)', 'N', NULL, NULL, '2018-08-07 01:10:54', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('cfdf197e-a1e5-11e8-9f3d-4ccc6a7f', 'USER_NAME_JOB', 'JOB', '排程執行時寫入TABLE的USER NAME', 'N', NULL, NULL, '2018-08-17 06:21:39', 'SYS', '2018-08-17 06:21:39', 'SYS'),
('dda1e015-9c53-11e8-b986-4ccc6a7f', 'SCHED_TYPE_CLASS_MAPPING', 'backupConfig@~com.cmap.service.impl.jobs.JobBackupConfig', '排程類別對應Class檔設定', 'N', NULL, NULL, '2018-08-10 04:14:12', 'SYS', '2018-08-13 01:50:10', 'prtgadmin'),
('dda1ea71-9c53-11e8-b986-4ccc6a7f', 'SCHED_TYPE_CLASS_MAPPING', 'uploadBackupConfigFile2FTP@~com.cmap.service.impl.jobs.JobUploadBackupConfigFile2FTP', '排程類別對應Class檔設定', 'N', NULL, NULL, '2018-08-10 04:14:12', 'SYS', '2018-08-13 01:50:10', 'prtgadmin'),
('e15dd8a6-9ed4-11e8-b986-4ccc6a7f', 'UPLOAD_NEWEST_BACKUP_FILE_ONLY', 'Y', '每日排程異地備份，是否只上傳每一台設備當天最新的備份檔(Y:指上傳最新；N:全部上傳)', 'N', NULL, NULL, '2018-08-13 08:42:44', 'SYS', '2018-08-14 06:33:37', 'prtgadmin'),
('e245514d-9b99-11e8-b986-4ccc6a7f', 'SSH_DEFAULT_PORT', '22', 'SSH default PORT', 'N', NULL, NULL, '2018-08-03 02:30:56', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('e3f74805-96ee-11e8-8fdc-4ccc6a7f', 'TFTP_HOST_PORT', '69', 'TFTP IP port', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-15 01:33:57', 'prtgadmin'),
('e9fbd251-9ad6-11e8-b986-4ccc6a7f', 'DECODE_FIELDS', 'FTP_LOGIN_PASSWORD', '定義編碼欄位', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('eaee44cb-96c5-11e8-8fdc-4ccc6a7f', 'SIGN_PWD', '[PWD]', '命令腳本內容替換字眼 >> 密碼\r\n(**修改時須同步調整腳本TABLE設定**)', 'N', NULL, NULL, '2018-08-03 02:35:41', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('eaee4f38-96c5-11e8-8fdc-4ccc6a7f', 'SIGN_ACT', '[ACT]', '命令腳本內容替換字眼 >> 帳號\r\n(**修改時須同步調整腳本TABLE設定**)', 'N', NULL, NULL, '2018-08-03 02:35:41', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('ee81989c-96c2-11e8-8fdc-4ccc6a7f', 'DECODE_FIELDS', 'FTP_LOGIN_ACCOUNT', '定義編碼欄位', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('ef4f7db4-96c4-11e8-8fdc-4ccc6a7f', 'HTTP_CONNECTION_TIME_OUT', '1500', 'HTTP連線請求timeout時間', 'N', NULL, NULL, '2018-08-03 02:28:39', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('ef4f860d-96c4-11e8-8fdc-4ccc6a7f', 'HTTP_SOCKET_TIME_OUT', '1500', 'HTTP連線請求timeout時間', 'N', NULL, NULL, '2018-08-03 02:28:39', 'SYS', '2018-08-08 02:00:32', 'SYS'),
('f09e77bb-9ad6-11e8-b986-4ccc6a7f', 'DECODE_FIELDS', 'DEFAULT_DEVICE_LOGIN_ACCOUNT', '定義編碼欄位', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('fa9462ad-9ad6-11e8-b986-4ccc6a7f', 'DECODE_FIELDS', 'DEFAULT_DEVICE_LOGIN_PASSWORD', '定義編碼欄位', 'N', NULL, NULL, '2018-08-03 01:51:00', 'SYS', '2018-08-09 03:16:11', 'prtgadmin'),
('fe3b9df5-866d-11e8-a90a-4ccc6a7f', 'FTP_HOST_IP', '192.168.60.197', 'FTP IP address', 'N', NULL, NULL, '2018-07-13 07:26:00', 'SYS', '2018-08-14 04:31:14', 'prtgadmin'),
('fe3baacb-866d-11e8-a90a-4ccc6a7f', 'FTP_HOST_PORT', '21', 'FTP port', 'N', NULL, NULL, '2018-07-13 07:26:00', 'SYS', '2018-08-08 02:00:32', 'SYS');

-- --------------------------------------------------------

--
-- 資料表結構 `user_operation_log`
--

DROP TABLE IF EXISTS `user_operation_log`;
CREATE TABLE `user_operation_log` (
  `LOG_ID` varchar(32) NOT NULL COMMENT 'UUID PK',
  `USER_NAME` varchar(50) DEFAULT NULL,
  `TABLE_NAME` varchar(50) DEFAULT NULL,
  `TARGET_ID` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `OPERATE_TIME` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 已匯出資料表的索引
--

--
-- 資料表索引 `config_version_info`
--
ALTER TABLE `config_version_info`
  ADD PRIMARY KEY (`VERSION_ID`),
  ADD UNIQUE KEY `UK1` (`GROUP_ID`,`DEVICE_ID`,`CONFIG_VERSION`) USING BTREE;

--
-- 資料表索引 `device_list`
--
ALTER TABLE `device_list`
  ADD PRIMARY KEY (`DEVICE_LIST_ID`),
  ADD UNIQUE KEY `UK1` (`GROUP_ID`,`DEVICE_ID`);

--
-- 資料表索引 `i18n`
--
ALTER TABLE `i18n`
  ADD PRIMARY KEY (`i18n_id`);

--
-- 資料表索引 `menu_item`
--
ALTER TABLE `menu_item`
  ADD PRIMARY KEY (`MENU_ITEM_ID`),
  ADD UNIQUE KEY `UK1` (`MENU_CODE`,`OPTION_LABEL`,`OPTION_VALUE`),
  ADD KEY `MENU_CODE` (`MENU_CODE`);

--
-- 資料表索引 `provision_log_device`
--
ALTER TABLE `provision_log_device`
  ADD PRIMARY KEY (`LOG_DEVICE_ID`);

--
-- 資料表索引 `provision_log_master`
--
ALTER TABLE `provision_log_master`
  ADD PRIMARY KEY (`LOG_MASTER_ID`);

--
-- 資料表索引 `provision_log_step`
--
ALTER TABLE `provision_log_step`
  ADD PRIMARY KEY (`LOG_STEP_ID`);

--
-- 資料表索引 `qrtz_blob_triggers`
--
ALTER TABLE `qrtz_blob_triggers`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `qrtz_calendars`
--
ALTER TABLE `qrtz_calendars`
  ADD PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`);

--
-- 資料表索引 `qrtz_cron_triggers`
--
ALTER TABLE `qrtz_cron_triggers`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `qrtz_fired_triggers`
--
ALTER TABLE `qrtz_fired_triggers`
  ADD PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`);

--
-- 資料表索引 `qrtz_job_details`
--
ALTER TABLE `qrtz_job_details`
  ADD PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`);

--
-- 資料表索引 `qrtz_locks`
--
ALTER TABLE `qrtz_locks`
  ADD PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`);

--
-- 資料表索引 `qrtz_paused_trigger_grps`
--
ALTER TABLE `qrtz_paused_trigger_grps`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `qrtz_scheduler_state`
--
ALTER TABLE `qrtz_scheduler_state`
  ADD PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`);

--
-- 資料表索引 `qrtz_simple_triggers`
--
ALTER TABLE `qrtz_simple_triggers`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `qrtz_simprop_triggers`
--
ALTER TABLE `qrtz_simprop_triggers`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`);

--
-- 資料表索引 `qrtz_triggers`
--
ALTER TABLE `qrtz_triggers`
  ADD PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  ADD KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`);

--
-- 資料表索引 `script_list_default`
--
ALTER TABLE `script_list_default`
  ADD PRIMARY KEY (`SCRIPT_LIST_ID`);

--
-- 資料表索引 `script_type`
--
ALTER TABLE `script_type`
  ADD PRIMARY KEY (`SCRIPT_TYPE_ID`);

--
-- 資料表索引 `system_log`
--
ALTER TABLE `system_log`
  ADD PRIMARY KEY (`LOG_ID`);

--
-- 資料表索引 `sys_config_setting`
--
ALTER TABLE `sys_config_setting`
  ADD PRIMARY KEY (`SETTING_ID`),
  ADD UNIQUE KEY `UK1` (`SETTING_NAME`,`SETTING_VALUE`) USING BTREE;

--
-- 資料表索引 `user_operation_log`
--
ALTER TABLE `user_operation_log`
  ADD PRIMARY KEY (`LOG_ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
