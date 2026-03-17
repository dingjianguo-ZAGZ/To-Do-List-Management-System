package com.todolist.controller;

import com.todolist.dto.ApiResponse;
import com.todolist.dto.UserSettingsDTO;
import com.todolist.service.UserSettingsService;
import com.todolist.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户设置控制器
 */
@RestController
@RequestMapping("/settings")
@CrossOrigin
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ApiResponse<UserSettingsDTO> getUserSettings(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            UserSettingsDTO result = userSettingsService.getUserSettings(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<UserSettingsDTO> updateUserSettings(
            @RequestHeader("Authorization") String token,
            @RequestBody UserSettingsDTO settingsDTO) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            UserSettingsDTO result = userSettingsService.updateUserSettings(userId, settingsDTO);
            return ApiResponse.success("设置更新成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
