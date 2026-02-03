# 待办事项管理系统 (To-Do List Management System)

本系统旨在为用户提供一个全面的待办事项管理解决方案，包括创建、分类、提醒、进度跟踪等功能。用户可以方便地创建普通事项、重复事项和截止日期事项，并通过多种视图和通知方式高效管理任务。

一个功能完善的待办事项管理系统后端，使用Spring Boot开发。

## 功能特性

### 1. 用户管理
- 用户注册/登录（支持用户名、邮箱、手机号登录）
- JWT令牌认证
- 两步验证支持
- 个性化用户设置

### 2. 事项管理
- 创建、编辑、删除待办事项
- 事项优先级设置（高、中、低）
- 事项状态管理（未开始、进行中、已完成、已逾期）
- 截止日期设置
- 重复事项支持（每天、每周、每月、每年、工作日）
- 完成事项并添加备注

### 3. 标签管理
- 创建自定义标签
- 标签颜色设置
- 事项标签关联
- 按标签筛选事项

### 4. 文件夹管理
- 创建自定义文件夹
- 事项分类整理
- 文件夹描述

### 5. 提醒功能
- 多种提醒类型（系统通知、桌面通知、短信、邮件）
- 自定义提醒时间
- 工作时段提醒设置

### 6. 统计功能
- 今日/本周完成事项统计
- 逾期事项统计
- 标签分布统计
- 完成趋势图表

### 7. 个性化设置
- 主题切换（浅色/深色）
- 字体大小调整
- 显示字段自定义
- 提醒时段设置

## 技术栈

- **框架**: Spring Boot 3.2.1
- **数据库**: MySQL 8.0
- **缓存**: Redis
- **安全**: Spring Security + JWT
- **ORM**: Spring Data JPA / Hibernate
- **邮件**: Spring Mail
- **定时任务**: Quartz
- **工具**: Lombok, MapStruct
- **文档导出**: Apache POI

## 项目结构

```
src/main/java/com/todolist/
├── config/              # 配置类
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
├── controller/          # 控制器层
│   ├── AuthController.java
│   ├── TodoItemController.java
│   ├── TagController.java
│   ├── FolderController.java
│   ├── StatisticsController.java
│   └── UserSettingsController.java
├── dto/                 # 数据传输对象
├── entity/              # 实体类
│   ├── User.java
│   ├── TodoItem.java
│   ├── Tag.java
│   ├── Folder.java
│   ├── Attachment.java
│   ├── Reminder.java
│   ├── UserSettings.java
│   └── Statistics.java
├── enums/               # 枚举类
│   ├── Priority.java
│   ├── TodoStatus.java
│   ├── RepeatCycle.java
│   ├── ReminderType.java
│   └── Theme.java
├── repository/          # 数据访问层
├── service/             # 服务层
│   ├── UserService.java
│   ├── TodoItemService.java
│   ├── TagService.java
│   ├── FolderService.java
│   ├── StatisticsService.java
│   └── UserSettingsService.java
├── util/                # 工具类
│   └── JwtUtil.java
└── TodoListApplication.java  # 主应用类
```

## 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 配置数据库

1. 创建数据库：
```sql
CREATE DATABASE todolist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改 `application.yml` 配置文件：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/todolist?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 启动项目

```bash
# 克隆项目
cd To-Do-List-Management-System

# 安装依赖
mvn clean install

# 运行项目
mvn spring-boot:run
```

项目启动后，访问 `http://localhost:8080`

## API 接口文档

### 认证接口

#### 用户注册
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "phone": "13800138000",
  "password": "password123"
}
```

#### 用户登录
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

### 待办事项接口

#### 创建事项
```
POST /api/todos
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "完成项目文档",
  "description": "编写API文档和使用说明",
  "priority": "HIGH",
  "dueDate": "2024-12-31T18:00:00",
  "repeatCycle": "NONE",
  "tagIds": [1, 2],
  "folderId": 1
}
```

#### 获取所有事项
```
GET /api/todos
Authorization: Bearer {token}
```

#### 更新事项
```
PUT /api/todos/{todoId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "更新后的标题",
  "status": "IN_PROGRESS"
}
```

#### 标记完成
```
POST /api/todos/{todoId}/complete?note=已完成
Authorization: Bearer {token}
```

#### 删除事项
```
DELETE /api/todos/{todoId}
Authorization: Bearer {token}
```

更多API接口文档请查看 [API_TEST_GUIDE.md](API_TEST_GUIDE.md)

## 数据库设计

### 主要表结构

- **users**: 用户表
- **user_settings**: 用户设置表
- **todo_items**: 待办事项表
- **tags**: 标签表
- **folders**: 文件夹表
- **todo_tags**: 事项标签关联表（多对多）
- **attachments**: 附件表
- **reminders**: 提醒表
- **statistics**: 统计表

## 待实现功能

1. 附件上传功能
2. 邮件提醒服务
3. 短信提醒服务
4. 数据备份与恢复
5. 批量操作接口
6. Excel导出功能
7. WebSocket实时通知

## 开发规范

- 使用RESTful API设计风格
- 统一的异常处理
- 统一的响应格式
- JWT令牌认证
- 事务管理
- 日志记录

## 许可证

MIT License

## 作者

苏宏润
