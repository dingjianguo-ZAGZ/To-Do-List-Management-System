package com.todolist.repository;

import com.todolist.entity.Reminder;
import com.todolist.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒数据访问层
 */
@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByTodoItem(TodoItem todoItem);

    @Query("SELECT r FROM Reminder r WHERE r.isSent = false AND r.remindAt <= :now")
    List<Reminder> findPendingReminders(@Param("now") LocalDateTime now);
}
