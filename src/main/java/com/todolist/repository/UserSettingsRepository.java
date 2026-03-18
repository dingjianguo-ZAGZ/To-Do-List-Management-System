package com.todolist.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.UserSettings;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户设置数据访问层
 */
@Mapper
public interface UserSettingsRepository extends BaseMapper<UserSettings> {
}
