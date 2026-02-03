# 待办事项管理系统 API 接口测试指南

## 测试环境设置

基础URL: `http://localhost:8080`

## 1. 认证测试

### 1.1 用户注册
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "phone": "13800138000",
    "password": "password123"
  }'
```

预期响应:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 1.2 用户登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## 2. 待办事项测试

### 2.1 创建待办事项
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title": "完成毕业设计",
    "description": "完成待办事项管理系统的开发",
    "priority": "HIGH",
    "dueDate": "2024-06-30T18:00:00",
    "repeatCycle": "NONE"
  }'
```

### 2.2 获取所有待办事项
```bash
curl -X GET http://localhost:8080/api/todos \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.3 更新待办事项
```bash
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "title": "更新后的标题",
    "status": "IN_PROGRESS",
    "priority": "MEDIUM"
  }'
```

### 2.4 标记完成
```bash
curl -X POST "http://localhost:8080/api/todos/1/complete?note=已完成所有开发工作" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.5 获取逾期事项
```bash
curl -X GET http://localhost:8080/api/todos/overdue \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2.6 按状态查询
```bash
curl -X GET http://localhost:8080/api/todos/status/IN_PROGRESS \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 3. 标签测试

### 3.1 创建标签
```bash
curl -X POST http://localhost:8080/api/tags \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "工作",
    "color": "#FF5733"
  }'
```

### 3.2 获取所有标签
```bash
curl -X GET http://localhost:8080/api/tags \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 3.3 更新标签
```bash
curl -X PUT http://localhost:8080/api/tags/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "重要工作",
    "color": "#FF0000"
  }'
```

### 3.4 删除标签
```bash
curl -X DELETE http://localhost:8080/api/tags/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 4. 文件夹测试

### 4.1 创建文件夹
```bash
curl -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "项目A任务",
    "description": "与项目A相关的所有任务"
  }'
```

### 4.2 获取所有文件夹
```bash
curl -X GET http://localhost:8080/api/folders \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 5. 统计测试

### 5.1 获取统计数据
```bash
curl -X GET http://localhost:8080/api/statistics \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 6. 用户设置测试

### 6.1 获取用户设置
```bash
curl -X GET http://localhost:8080/api/settings \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 6.2 更新用户设置
```bash
curl -X PUT http://localhost:8080/api/settings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "theme": "DARK",
    "fontSize": 16,
    "showTitle": true,
    "showDueDate": true,
    "showPriority": true,
    "showTags": true,
    "reminderWorkHoursOnly": true,
    "reminderWorkDaysOnly": false
  }'
```

## 7. 完整测试流程

```bash
# 1. 注册用户
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"password123"}' \
  | jq -r '.data')

# 2. 创建标签
TAG_ID=$(curl -s -X POST http://localhost:8080/api/tags \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"工作","color":"#FF5733"}' \
  | jq -r '.data.id')

# 3. 创建文件夹
FOLDER_ID=$(curl -s -X POST http://localhost:8080/api/folders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"项目A","description":"项目A相关任务"}' \
  | jq -r '.data.id')

# 4. 创建待办事项
TODO_ID=$(curl -s -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"title\":\"完成开发\",\"priority\":\"HIGH\",\"tagIds\":[$TAG_ID],\"folderId\":$FOLDER_ID}" \
  | jq -r '.data.id')

# 5. 查看所有待办事项
curl -X GET http://localhost:8080/api/todos \
  -H "Authorization: Bearer $TOKEN"

# 6. 标记完成
curl -X POST "http://localhost:8080/api/todos/$TODO_ID/complete?note=开发完成" \
  -H "Authorization: Bearer $TOKEN"

# 7. 查看统计
curl -X GET http://localhost:8080/api/statistics \
  -H "Authorization: Bearer $TOKEN"
```

## 注意事项

1. 请将 `YOUR_TOKEN` 替换为实际的JWT令牌
2. 所有需要认证的接口都需要在请求头中携带 `Authorization: Bearer {token}`
3. 日期时间格式为 ISO 8601 格式: `yyyy-MM-ddTHH:mm:ss`
4. 优先级枚举值: `HIGH`, `MEDIUM`, `LOW`
5. 状态枚举值: `NOT_STARTED`, `IN_PROGRESS`, `COMPLETED`, `OVERDUE`
6. 重复周期枚举值: `NONE`, `DAILY`, `WEEKLY`, `MONTHLY`, `YEARLY`, `WORKDAY`
