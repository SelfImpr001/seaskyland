/** kn_testpaper表 添加  hasAnalysis 字段 用于判断科目是不是要分析*/
ALTER TABLE `kn_testpaper` ADD COLUMN `hasAnalysis` TINYINT(1) DEFAULT 0 NULL COMMENT '是否分析' AFTER `selOptions`; 