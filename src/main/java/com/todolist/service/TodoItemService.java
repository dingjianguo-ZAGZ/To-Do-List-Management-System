package com.todolist.service;

import com.todolist.dto.*;
import com.todolist.entity.*;
import com.todolist.enums.TodoStatus;
import com.todolist.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 待办事项服务
 */
@Service
public class TodoItemService {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Transactional
    public TodoItemResponseDTO createTodoItem(Long userId, TodoItemCreateDTO createDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(createDTO.getTitle());
        todoItem.setDescription(createDTO.getDescription());
        todoItem.setPriority(createDTO.getPriority());
        todoItem.setDueDate(createDTO.getDueDate());
        todoItem.setRepeatCycle(createDTO.getRepeatCycle());
        todoItem.setStatus(TodoStatus.NOT_STARTED);
        todoItem.setUser(user);

        if (createDTO.getFolderId() != null) {
            Folder folder = folderRepository.findById(createDTO.getFolderId())
                    .orElseThrow(() -> new RuntimeException("文件夹不存在"));
            todoItem.setFolder(folder);
        }

        if (createDTO.getTagIds() != null && !createDTO.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : createDTO.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("标签不存在"));
                tags.add(tag);
            }
            todoItem.setTags(tags);
        }

        todoItem = todoItemRepository.save(todoItem);

        if (createDTO.getReminders() != null && !createDTO.getReminders().isEmpty()) {
            for (ReminderCreateDTO reminderDTO : createDTO.getReminders()) {
                Reminder reminder = new Reminder();
                reminder.setType(reminderDTO.getType());
                reminder.setRemindAt(reminderDTO.getRemindAt());
                reminder.setTodoItem(todoItem);
                reminderRepository.save(reminder);
            }
        }

        return convertToResponseDTO(todoItem);
    }

    @Transactional
    public TodoItemResponseDTO updateTodoItem(Long userId, Long todoId, TodoItemUpdateDTO updateDTO) {
        TodoItem todoItem = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("事项不存在"));

        if (!todoItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        if (updateDTO.getTitle() != null) {
            todoItem.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            todoItem.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getPriority() != null) {
            todoItem.setPriority(updateDTO.getPriority());
        }
        if (updateDTO.getStatus() != null) {
            todoItem.setStatus(updateDTO.getStatus());
        }
        if (updateDTO.getDueDate() != null) {
            todoItem.setDueDate(updateDTO.getDueDate());
        }
        if (updateDTO.getRepeatCycle() != null) {
            todoItem.setRepeatCycle(updateDTO.getRepeatCycle());
        }

        if (updateDTO.getFolderId() != null) {
            Folder folder = folderRepository.findById(updateDTO.getFolderId())
                    .orElseThrow(() -> new RuntimeException("文件夹不存在"));
            todoItem.setFolder(folder);
        }

        if (updateDTO.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>();
            for (Long tagId : updateDTO.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new RuntimeException("标签不存在"));
                tags.add(tag);
            }
            todoItem.setTags(tags);
        }

        todoItem = todoItemRepository.save(todoItem);
        return convertToResponseDTO(todoItem);
    }

    @Transactional
    public void deleteTodoItem(Long userId, Long todoId) {
        TodoItem todoItem = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("事项不存在"));

        if (!todoItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        todoItemRepository.delete(todoItem);
    }

    @Transactional
    public TodoItemResponseDTO completeTodoItem(Long userId, Long todoId, String completionNote) {
        TodoItem todoItem = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("事项不存在"));

        if (!todoItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        todoItem.complete(completionNote);
        todoItem = todoItemRepository.save(todoItem);

        return convertToResponseDTO(todoItem);
    }

    public TodoItemResponseDTO getTodoItem(Long userId, Long todoId) {
        TodoItem todoItem = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("事项不存在"));

        if (!todoItem.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        return convertToResponseDTO(todoItem);
    }

    public List<TodoItemResponseDTO> getAllTodoItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<TodoItem> todoItems = todoItemRepository.findByUser(user);
        return todoItems.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TodoItemResponseDTO> getTodoItemsByStatus(Long userId, TodoStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<TodoItem> todoItems = todoItemRepository.findByUserAndStatus(user, status);
        return todoItems.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TodoItemResponseDTO> getOverdueTodoItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<TodoItem> todoItems = todoItemRepository.findOverdueItems(user, LocalDateTime.now());
        return todoItems.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private TodoItemResponseDTO convertToResponseDTO(TodoItem todoItem) {
        TodoItemResponseDTO dto = new TodoItemResponseDTO();
        dto.setId(todoItem.getId());
        dto.setTitle(todoItem.getTitle());
        dto.setDescription(todoItem.getDescription());
        dto.setPriority(todoItem.getPriority());
        dto.setStatus(todoItem.getStatus());
        dto.setDueDate(todoItem.getDueDate());
        dto.setRepeatCycle(todoItem.getRepeatCycle());
        dto.setNextRepeatDate(todoItem.getNextRepeatDate());
        dto.setCompletedAt(todoItem.getCompletedAt());
        dto.setCompletionNote(todoItem.getCompletionNote());
        dto.setCreatedAt(todoItem.getCreatedAt());
        dto.setUpdatedAt(todoItem.getUpdatedAt());
        dto.setIsOverdue(todoItem.isOverdue());

        if (todoItem.getFolder() != null) {
            dto.setFolderId(todoItem.getFolder().getId());
            dto.setFolderName(todoItem.getFolder().getName());
        }

        Set<TagResponseDTO> tagDTOs = todoItem.getTags().stream()
                .map(tag -> {
                    TagResponseDTO tagDTO = new TagResponseDTO();
                    tagDTO.setId(tag.getId());
                    tagDTO.setName(tag.getName());
                    tagDTO.setColor(tag.getColor());
                    return tagDTO;
                })
                .collect(Collectors.toSet());
        dto.setTags(tagDTOs);

        return dto;
    }
}
