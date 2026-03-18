package com.todolist.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签数据访问层
 */
@Mapper
public interface TagRepository extends BaseMapper<Tag> {
}
