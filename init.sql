-- ============================================
-- To-Do List Management System 数据库初始化脚本
-- ============================================
-- 说明：
-- 1. 本脚本用于创建数据库和表结构
-- 2. Spring Boot JPA会自动根据Entity创建表（ddl-auto: update）
-- 3. 本脚本提供完整的表结构定义作为参考和备份
-- 4. 如需手动创建表，请取消注释相应的CREATE TABLE语句
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS todolist
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE todolist;

-- ============================================
-- 表结构定义（由JPA自动创建，这里提供参考）
-- ============================================

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) UNIQUE NOT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `two_factor_enabled` BOOLEAN DEFAULT FALSE COMMENT '是否启用两步验证',
    `two_factor_secret` VARCHAR(255) COMMENT '两步验证密钥',
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '账户是否激活',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 用户设置表
DROP TABLE IF EXISTS `user_settings`;
CREATE TABLE `user_settings` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '设置ID',
    `user_id` BIGINT UNIQUE NOT NULL COMMENT '用户ID',
    `theme` VARCHAR(20) DEFAULT 'LIGHT' COMMENT '主题（LIGHT/DARK/AUTO）',
    `language` VARCHAR(10) DEFAULT 'zh_CN' COMMENT '语言',
    `timezone` VARCHAR(50) DEFAULT 'Asia/Shanghai' COMMENT '时区',
    `email_notifications` BOOLEAN DEFAULT TRUE COMMENT '邮件通知',
    `push_notifications` BOOLEAN DEFAULT TRUE COMMENT '推送通知',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户设置表';

-- 3. 文件夹表
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文件夹ID',
    `name` VARCHAR(100) NOT NULL COMMENT '文件夹名称',
    `color` VARCHAR(20) DEFAULT '#1890ff' COMMENT '颜色',
    `icon` VARCHAR(50) COMMENT '图标',
    `user_id` BIGINT NOT NULL COMMENT '所属用户ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件夹表';

-- 4. 标签表
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `color` VARCHAR(20) DEFAULT '#52c41a' COMMENT '颜色',
    `user_id` BIGINT NOT NULL COMMENT '所属用户ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_user_tag` (`user_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 5. 待办事项表
DROP TABLE IF EXISTS `todo_item`;
CREATE TABLE `todo_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '待办事项ID',
    `title` VARCHAR(200) NOT NULL COMMENT '标题',
    `description` TEXT COMMENT '描述',
    `status` VARCHAR(20) DEFAULT 'TODO' COMMENT '状态（TODO/IN_PROGRESS/COMPLETED/CANCELLED）',
    `priority` VARCHAR(20) DEFAULT 'MEDIUM' COMMENT '优先级（LOW/MEDIUM/HIGH/URGENT）',
    `due_date` DATETIME COMMENT '截止日期',
    `repeat_cycle` VARCHAR(20) COMMENT '重复周期（DAILY/WEEKLY/MONTHLY/YEARLY）',
    `is_pinned` BOOLEAN DEFAULT FALSE COMMENT '是否置顶',
    `completed_at` TIMESTAMP NULL COMMENT '完成时间',
    `user_id` BIGINT NOT NULL COMMENT '所属用户ID',
    `folder_id` BIGINT COMMENT '所属文件夹ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`folder_id`) REFERENCES `folder`(`id`) ON DELETE SET NULL,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_due_date` (`due_date`),
    INDEX `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';

-- 6. 待办事项-标签关联表
DROP TABLE IF EXISTS `todo_item_tags`;
CREATE TABLE `todo_item_tags` (
    `todo_item_id` BIGINT NOT NULL COMMENT '待办事项ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`todo_item_id`, `tag_id`),
    FOREIGN KEY (`todo_item_id`) REFERENCES `todo_item`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `tag`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项-标签关联表';

-- 7. 提醒表
DROP TABLE IF EXISTS `reminder`;
CREATE TABLE `reminder` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '提醒ID',
    `reminder_time` DATETIME NOT NULL COMMENT '提醒时间',
    `reminder_type` VARCHAR(20) DEFAULT 'EMAIL' COMMENT '提醒类型（EMAIL/PUSH/SMS）',
    `is_sent` BOOLEAN DEFAULT FALSE COMMENT '是否已发送',
    `todo_item_id` BIGINT NOT NULL COMMENT '关联的待办事项ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`todo_item_id`) REFERENCES `todo_item`(`id`) ON DELETE CASCADE,
    INDEX `idx_reminder_time` (`reminder_time`),
    INDEX `idx_is_sent` (`is_sent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提醒表';

-- 8. 附件表
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '附件ID',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
    `file_type` VARCHAR(50) COMMENT '文件类型',
    `todo_item_id` BIGINT NOT NULL COMMENT '关联的待办事项ID',
    `uploaded_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    FOREIGN KEY (`todo_item_id`) REFERENCES `todo_item`(`id`) ON DELETE CASCADE,
    INDEX `idx_todo_item_id` (`todo_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='附件表';

-- 9. 统计表
DROP TABLE IF EXISTS `statistics`;
CREATE TABLE `statistics` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '统计ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `date` DATE NOT NULL COMMENT '统计日期',
    `total_tasks` INT DEFAULT 0 COMMENT '总任务数',
    `completed_tasks` INT DEFAULT 0 COMMENT '已完成任务数',
    `created_tasks` INT DEFAULT 0 COMMENT '新建任务数',
    `completion_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '完成率',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    UNIQUE KEY `uk_user_date` (`user_id`, `date`),
    INDEX `idx_date` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='统计表';

-- ============================================
-- 初始测试数据
-- ============================================

-- 插入测试用户（密码都是：password123）
INSERT INTO `user` (`username`, `email`, `phone`, `password`, `is_active`) VALUES
('admin', 'admin@example.com', '13800138000', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKt.Bdm2', true),
('testuser', 'test@example.com', '13800138001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKt.Bdm2', true);

-- 插入用户设置
INSERT INTO `user_settings` (`user_id`, `theme`, `language`, `timezone`) VALUES
(1, 'LIGHT', 'zh_CN', 'Asia/Shanghai'),
(2, 'DARK', 'zh_CN', 'Asia/Shanghai');

-- 插入测试文件夹
INSERT INTO `folder` (`name`, `color`, `icon`, `user_id`) VALUES
('工作', '#1890ff', 'work', 1),
('个人', '#52c41a', 'user', 1),
('学习', '#faad14', 'book', 1),
('生活', '#f5222d', 'home', 2);

-- 插入测试标签
INSERT INTO `tag` (`name`, `color`, `user_id`) VALUES
('重要', '#f5222d', 1),
('紧急', '#fa8c16', 1),
('会议', '#1890ff', 1),
('编码', '#52c41a', 2),
('阅读', '#722ed1', 2);

-- 插入测试待办事项
INSERT INTO `todo_item` (`title`, `description`, `status`, `priority`, `due_date`, `user_id`, `folder_id`) VALUES
('完成项目文档', '编写项目的README和API文档', 'TODO', 'HIGH', DATE_ADD(NOW(), INTERVAL 2 DAY), 1, 1),
('代码评审', '评审团队提交的代码变更', 'IN_PROGRESS', 'MEDIUM', DATE_ADD(NOW(), INTERVAL 1 DAY), 1, 1),
('学习Spring Boot', '学习Spring Boot新特性', 'TODO', 'MEDIUM', DATE_ADD(NOW(), INTERVAL 7 DAY), 1, 3),
('购买日用品', '去超市购买生活用品', 'TODO', 'LOW', DATE_ADD(NOW(), INTERVAL 3 DAY), 2, 4),
('健身计划', '每周三次健身房锻炼', 'COMPLETED', 'MEDIUM', NOW(), 2, 4);

-- 关联待办事项和标签
INSERT INTO `todo_item_tags` (`todo_item_id`, `tag_id`) VALUES
(1, 1),
(2, 1),
(2, 2),
(3, 5);

-- 插入提醒
INSERT INTO `reminder` (`reminder_time`, `reminder_type`, `todo_item_id`) VALUES
(DATE_ADD(NOW(), INTERVAL 1 DAY), 'EMAIL', 1),
(DATE_ADD(NOW(), INTERVAL 12 HOUR), 'PUSH', 2);

-- ============================================
-- 索引优化建议
-- ============================================
-- 根据实际使用情况，可以添加以下复合索引：
-- ALTER TABLE todo_item ADD INDEX idx_user_status (user_id, status);
-- ALTER TABLE todo_item ADD INDEX idx_user_due_date (user_id, due_date);
-- ALTER TABLE reminder ADD INDEX idx_time_sent (reminder_time, is_sent);

-- ============================================
-- 使用说明
-- ============================================
-- 1. 首次运行：执行本脚本创建数据库和初始数据
-- 2. 默认用户：admin / password123
-- 3. 测试用户：testuser / password123
-- 4. Spring Boot启动后，JPA会自动同步Entity与数据库表结构
-- 5. 生产环境建议将 application.yml 中的 ddl-auto 改为 validate
-- ============================================
