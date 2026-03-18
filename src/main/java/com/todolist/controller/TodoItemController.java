package com.todolist.controller;

import com.todolist.dto.*;
import com.todolist.enums.TodoStatus;
import com.todolist.service.TodoItemService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待办事项控制器
 */
@RestController
@RequestMapping("/todos")
@CrossOrigin
public class TodoItemController {

    @Autowired
    private TodoItemService todoItemService;

    @PostMapping
    public ApiResponse<TodoItemResponseDTO> createTodoItem(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody TodoItemCreateDTO createDTO) {
        try {
            TodoItemResponseDTO result = todoItemService.createTodoItem(userId, createDTO);
            return ApiResponse.success("创建成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{todoId}")
    public ApiResponse<TodoItemResponseDTO> updateTodoItem(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long todoId,
            @Valid @RequestBody TodoItemUpdateDTO updateDTO) {
        try {
            TodoItemResponseDTO result = todoItemService.updateTodoItem(userId, todoId, updateDTO);
            return ApiResponse.success("更新成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> deleteTodoItem(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long todoId) {
        try {
            todoItemService.deleteTodoItem(userId, todoId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/{todoId}/complete")
    public ApiResponse<TodoItemResponseDTO> completeTodoItem(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long todoId,
            @RequestParam(required = false) String note) {
        try {
            TodoItemResponseDTO result = todoItemService.completeTodoItem(userId, todoId, note);
            return ApiResponse.success("标记完成", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{todoId}")
    public ApiResponse<TodoItemResponseDTO> getTodoItem(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long todoId) {
        try {
            TodoItemResponseDTO result = todoItemService.getTodoItem(userId, todoId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<TodoItemResponseDTO>> getAllTodoItems(
            @RequestAttribute("userId") Long userId) {
        try {
            List<TodoItemResponseDTO> result = todoItemService.getAllTodoItems(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<TodoItemResponseDTO>> getTodoItemsByStatus(
            @RequestAttribute("userId") Long userId,
            @PathVariable TodoStatus status) {
        try {
            List<TodoItemResponseDTO> result = todoItemService.getTodoItemsByStatus(userId, status);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/overdue")
    public ApiResponse<List<TodoItemResponseDTO>> getOverdueTodoItems(
            @RequestAttribute("userId") Long userId) {
        try {
            List<TodoItemResponseDTO> result = todoItemService.getOverdueTodoItems(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
