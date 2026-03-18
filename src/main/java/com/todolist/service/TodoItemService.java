package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    private TagRepository tagRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private TodoTagsMapper todoTagsMapper;

    @Transactional
    public TodoItemResponseDTO createTodoItem(Long userId, TodoItemCreateDTO createDTO) {
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle(createDTO.getTitle());
        todoItem.setDescription(createDTO.getDescription());
        todoItem.setPriority(createDTO.getPriority());
        todoItem.setDueDate(createDTO.getDueDate());
        todoItem.setRepeatCycle(createDTO.getRepeatCycle());
        todoItem.setStatus(TodoStatus.NOT_STARTED);
        todoItem.setUserId(userId);

        if (createDTO.getFolderId() != null) {
            Folder folder = folderRepository.selectById(createDTO.getFolderId());
            if (folder == null) {
                throw new RuntimeException("文件夹不存在");
            }
            todoItem.setFolderId(createDTO.getFolderId());
        }

        todoItemRepository.insert(todoItem);

        if (createDTO.getTagIds() != null && !createDTO.getTagIds().isEmpty()) {
            for (Long tagId : createDTO.getTagIds()) {
                Tag tag = tagRepository.selectById(tagId);
                if (tag == null) {
                    throw new RuntimeException("标签不存在");
                }
                todoTagsMapper.insertTodoTag(todoItem.getId(), tagId);
            }
        }

        if (createDTO.getReminders() != null && !createDTO.getReminders().isEmpty()) {
            for (ReminderCreateDTO reminderDTO : createDTO.getReminders()) {
                Reminder reminder = new Reminder();
                reminder.setType(reminderDTO.getType());
                reminder.setRemindAt(reminderDTO.getRemindAt());
                reminder.setTodoId(todoItem.getId());
                reminderRepository.insert(reminder);
            }
        }

        return convertToResponseDTO(todoItem);
    }

    @Transactional
    public TodoItemResponseDTO updateTodoItem(Long userId, Long todoId, TodoItemUpdateDTO updateDTO) {
        TodoItem todoItem = todoItemRepository.selectById(todoId);
        if (todoItem == null) {
            throw new RuntimeException("事项不存在");
        }

        if (!todoItem.getUserId().equals(userId)) {
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
            Folder folder = folderRepository.selectById(updateDTO.getFolderId());
            if (folder == null) {
                throw new RuntimeException("文件夹不存在");
            }
            todoItem.setFolderId(updateDTO.getFolderId());
        }

        if (updateDTO.getTagIds() != null) {
            todoTagsMapper.deleteByTodoId(todoId);
            for (Long tagId : updateDTO.getTagIds()) {
                Tag tag = tagRepository.selectById(tagId);
                if (tag == null) {
                    throw new RuntimeException("标签不存在");
                }
                todoTagsMapper.insertTodoTag(todoId, tagId);
            }
        }

        todoItemRepository.updateById(todoItem);
        return convertToResponseDTO(todoItem);
    }

    @Transactional
    public void deleteTodoItem(Long userId, Long todoId) {
        TodoItem todoItem = todoItemRepository.selectById(todoId);
        if (todoItem == null) {
            throw new RuntimeException("事项不存在");
        }

        if (!todoItem.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        todoTagsMapper.deleteByTodoId(todoId);
        reminderRepository.delete(new LambdaQueryWrapper<Reminder>()
                .eq(Reminder::getTodoId, todoId));
        todoItemRepository.deleteById(todoId);
    }

    @Transactional
    public TodoItemResponseDTO completeTodoItem(Long userId, Long todoId, String completionNote) {
        TodoItem todoItem = todoItemRepository.selectById(todoId);
        if (todoItem == null) {
            throw new RuntimeException("事项不存在");
        }

        if (!todoItem.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        todoItem.complete(completionNote);
        todoItemRepository.updateById(todoItem);

        return convertToResponseDTO(todoItem);
    }

    public TodoItemResponseDTO getTodoItem(Long userId, Long todoId) {
        TodoItem todoItem = todoItemRepository.selectById(todoId);
        if (todoItem == null) {
            throw new RuntimeException("事项不存在");
        }

        if (!todoItem.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        return convertToResponseDTO(todoItem);
    }

    public List<TodoItemResponseDTO> getAllTodoItems(Long userId) {
        List<TodoItem> todoItems = todoItemRepository.selectList(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getUserId, userId));
        return todoItems.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TodoItemResponseDTO> getTodoItemsByStatus(Long userId, TodoStatus status) {
        List<TodoItem> todoItems = todoItemRepository.selectList(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getUserId, userId)
                .eq(TodoItem::getStatus, status));
        return todoItems.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TodoItemResponseDTO> getOverdueTodoItems(Long userId) {
        List<TodoItem> todoItems = todoItemRepository.selectList(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getUserId, userId)
                .ne(TodoItem::getStatus, TodoStatus.COMPLETED)
                .lt(TodoItem::getDueDate, LocalDateTime.now()));
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

        if (todoItem.getFolderId() != null) {
            Folder folder = folderRepository.selectById(todoItem.getFolderId());
            if (folder != null) {
                dto.setFolderId(folder.getId());
                dto.setFolderName(folder.getName());
            }
        }

        List<Long> tagIds = todoTagsMapper.selectTagIdsByTodoId(todoItem.getId());
        Set<TagResponseDTO> tagDTOs = new HashSet<>();
        for (Long tagId : tagIds) {
            Tag tag = tagRepository.selectById(tagId);
            if (tag != null) {
                TagResponseDTO tagDTO = new TagResponseDTO();
                tagDTO.setId(tag.getId());
                tagDTO.setName(tag.getName());
                tagDTO.setColor(tag.getColor());
                tagDTOs.add(tagDTO);
            }
        }
        dto.setTags(tagDTOs);

        List<Reminder> reminders = reminderRepository.selectList(new LambdaQueryWrapper<Reminder>()
                .eq(Reminder::getTodoId, todoItem.getId()));
        Set<ReminderResponseDTO> reminderDTOs = reminders.stream()
                .map(reminder -> {
                    ReminderResponseDTO reminderDTO = new ReminderResponseDTO();
                    reminderDTO.setId(reminder.getId());
                    reminderDTO.setType(reminder.getType());
                    reminderDTO.setRemindAt(reminder.getRemindAt());
                    reminderDTO.setIsSent(reminder.getIsSent());
                    reminderDTO.setSentAt(reminder.getSentAt());
                    reminderDTO.setCreatedAt(reminder.getCreatedAt());
                    return reminderDTO;
                })
                .collect(Collectors.toSet());
        dto.setReminders(reminderDTOs);

        dto.setAttachments(new HashSet<>());

        return dto;
    }
}
