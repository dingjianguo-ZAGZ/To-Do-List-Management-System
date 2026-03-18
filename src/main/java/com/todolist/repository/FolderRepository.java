package com.todolist.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Folder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件夹数据访问层
 */
@Mapper
public interface FolderRepository extends BaseMapper<Folder> {
}
