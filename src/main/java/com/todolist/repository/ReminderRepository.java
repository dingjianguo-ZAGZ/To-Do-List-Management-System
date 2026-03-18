package com.todolist.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Reminder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提醒数据访问层
 */
@Mapper
public interface ReminderRepository extends BaseMapper<Reminder> {
}
