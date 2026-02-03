package com.todolist.controller;

import com.todolist.dto.*;
import com.todolist.enums.TodoStatus;
import com.todolist.service.TodoItemService;
import com.todolist.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待办事项控制器
 */
@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class TodoItemController {

    @Autowired
    private TodoItemService todoItemService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ApiResponse<TodoItemResponseDTO> createTodoItem(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody TodoItemCreateDTO createDTO) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            TodoItemResponseDTO result = todoItemService.createTodoItem(userId, createDTO);
            return ApiResponse.success("创建成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{todoId}")
    public ApiResponse<TodoItemResponseDTO> updateTodoItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long todoId,
            @Valid @RequestBody TodoItemUpdateDTO updateDTO) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            TodoItemResponseDTO result = todoItemService.updateTodoItem(userId, todoId, updateDTO);
            return ApiResponse.success("更新成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{todoId}")
    public ApiResponse<Void> deleteTodoItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long todoId) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            todoItemService.deleteTodoItem(userId, todoId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/{todoId}/complete")
    public ApiResponse<TodoItemResponseDTO> completeTodoItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long todoId,
            @RequestParam(required = false) String note) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            TodoItemResponseDTO result = todoItemService.completeTodoItem(userId, todoId, note);
            return ApiResponse.success("标记完成", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{todoId}")
    public ApiResponse<TodoItemResponseDTO> getTodoItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long todoId) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            TodoItemResponseDTO result = todoItemService.getTodoItem(userId, todoId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<TodoItemResponseDTO>> getAllTodoItems(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            List<TodoItemResponseDTO> result = todoItemService.getAllTodoItems(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<TodoItemResponseDTO>> getTodoItemsByStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable TodoStatus status) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            List<TodoItemResponseDTO> result = todoItemService.getTodoItemsByStatus(userId, status);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/overdue")
    public ApiResponse<List<TodoItemResponseDTO>> getOverdueTodoItems(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            List<TodoItemResponseDTO> result = todoItemService.getOverdueTodoItems(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
