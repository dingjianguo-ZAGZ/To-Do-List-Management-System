package com.todolist.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.TodoItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 待办事项数据访问层
 */
@Mapper
public interface TodoItemRepository extends BaseMapper<TodoItem> {
}
