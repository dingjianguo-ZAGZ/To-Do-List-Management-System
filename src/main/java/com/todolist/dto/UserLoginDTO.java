package com.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {

    @NotBlank(message = "用户名/邮箱/手机号不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String verificationCode;
}
