package com.todolist.controller;

import com.todolist.dto.ApiResponse;
import com.todolist.dto.UserLoginDTO;
import com.todolist.dto.UserRegisterDTO;
import com.todolist.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<String> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        try {
            String token = userService.register(registerDTO);
            return ApiResponse.success("注册成功", token);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        try {
            String token = userService.login(loginDTO);
            return ApiResponse.success("登录成功", token);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
