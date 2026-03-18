package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.todolist.enums.Priority;
import com.todolist.enums.RepeatCycle;
import com.todolist.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 待办事项实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("todo_items")
public class TodoItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private Priority priority = Priority.MEDIUM;

    private TodoStatus status = TodoStatus.NOT_STARTED;

    private LocalDateTime dueDate;

    private RepeatCycle repeatCycle = RepeatCycle.NONE;

    private LocalDateTime nextRepeatDate;

    private LocalDateTime completedAt;

    private String completionNote;

    private Long userId;

    private Long folderId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Folder folder;

    @TableField(exist = false)
    private Set<Tag> tags = new HashSet<>();

    @TableField(exist = false)
    private Set<Attachment> attachments = new HashSet<>();

    @TableField(exist = false)
    private Set<Reminder> reminders = new HashSet<>();

    /**
     * 标记为完成
     */
    public void complete(String note) {
        this.status = TodoStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.completionNote = note;

        if (this.repeatCycle != RepeatCycle.NONE) {
            calculateNextRepeatDate();
        }
    }

    /**
     * 计算下次重复日期
     */
    private void calculateNextRepeatDate() {
//        LocalDateTime base = this.nextRepeatDate != null ? this.nextRepeatDate : LocalDateTime.now();
//
//        switch (this.repeatCycle) {
//            case DAILY -> this.nextRepeatDate = base.plusDays(1);
//            case WEEKLY -> this.nextRepeatDate = base.plusWeeks(1);
//            case MONTHLY -> this.nextRepeatDate = base.plusMonths(1);
//            case YEARLY -> this.nextRepeatDate = base.plusYears(1);
//            case WORKDAY -> {
//                LocalDateTime next = base.plusDays(1);
//                while (next.getDayOfWeek().getValue() >= 6) {
//                    next = next.plusDays(1);
//                }
//                this.nextRepeatDate = next;
//            }
//        }
    }

    /**
     * 检查是否逾期
     */
    public boolean isOverdue() {
        return this.dueDate != null
            && this.status != TodoStatus.COMPLETED
            && LocalDateTime.now().isAfter(this.dueDate);
    }
}
