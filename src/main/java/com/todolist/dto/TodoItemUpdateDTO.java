package com.todolist.dto;

import com.todolist.enums.Priority;
import com.todolist.enums.RepeatCycle;
import com.todolist.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 更新待办事项DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemUpdateDTO {

    private String title;

    private String description;

    private Priority priority;

    private TodoStatus status;

    private LocalDateTime dueDate;

    private RepeatCycle repeatCycle;

    private Long folderId;

    private Set<Long> tagIds;
}
