package com.todolist.repository;

import com.todolist.entity.TodoItem;
import com.todolist.entity.User;
import com.todolist.enums.Priority;
import com.todolist.enums.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 待办事项数据访问层
 */
@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    List<TodoItem> findByUser(User user);

    List<TodoItem> findByUserAndStatus(User user, TodoStatus status);

    List<TodoItem> findByUserAndPriority(User user, Priority priority);

    @Query("SELECT t FROM TodoItem t WHERE t.user = :user AND :tag MEMBER OF t.tags")
    List<TodoItem> findByUserAndTag(@Param("user") User user, @Param("tag") String tagName);

    @Query("SELECT t FROM TodoItem t WHERE t.user = :user AND t.dueDate BETWEEN :start AND :end")
    List<TodoItem> findByUserAndDueDateBetween(
        @Param("user") User user,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("SELECT t FROM TodoItem t WHERE t.user = :user AND t.status != 'COMPLETED' AND t.dueDate < :now")
    List<TodoItem> findOverdueItems(@Param("user") User user, @Param("now") LocalDateTime now);

    @Query("SELECT t FROM TodoItem t WHERE t.folder.id = :folderId")
    List<TodoItem> findByFolderId(@Param("folderId") Long folderId);

    @Query("SELECT COUNT(t) FROM TodoItem t WHERE t.user = :user AND t.status = 'COMPLETED' AND DATE(t.completedAt) = CURRENT_DATE")
    Long countTodayCompleted(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM TodoItem t WHERE t.user = :user AND t.status = 'COMPLETED' AND WEEK(t.completedAt) = WEEK(CURRENT_DATE)")
    Long countWeekCompleted(@Param("user") User user);
}
