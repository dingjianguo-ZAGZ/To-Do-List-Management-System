package com.todolist.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Statistics;
import org.apache.ibatis.annotations.Mapper;

/**
 * 统计数据访问层
 */
@Mapper
public interface StatisticsRepository extends BaseMapper<Statistics> {
}
