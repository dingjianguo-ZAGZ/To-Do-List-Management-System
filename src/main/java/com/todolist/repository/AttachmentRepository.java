package com.todolist.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.todolist.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 附件数据访问层
 */
@Mapper
public interface AttachmentRepository extends BaseMapper<Attachment> {
}
