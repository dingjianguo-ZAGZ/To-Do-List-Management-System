package com.todolist.repository;

import com.todolist.entity.Attachment;
import com.todolist.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 附件数据访问层
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByTodoItem(TodoItem todoItem);
}
