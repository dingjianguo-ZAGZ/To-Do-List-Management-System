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
 * 待办事项响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemResponseDTO {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private TodoStatus status;
    private LocalDateTime dueDate;
    private RepeatCycle repeatCycle;
    private LocalDateTime nextRepeatDate;
    private LocalDateTime completedAt;
    private String completionNote;
    private Long folderId;
    private String folderName;
    private Set<TagResponseDTO> tags;
    private Set<AttachmentResponseDTO> attachments;
    private Set<ReminderResponseDTO> reminders;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isOverdue;
}
