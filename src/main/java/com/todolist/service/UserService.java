package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.dto.UserLoginDTO;
import com.todolist.dto.UserPasswordUpdateDTO;
import com.todolist.dto.UserProfileDTO;
import com.todolist.dto.UserProfileUpdateDTO;
import com.todolist.dto.UserRegisterDTO;
import com.todolist.entity.User;
import com.todolist.entity.UserSettings;
import com.todolist.repository.UserRepository;
import com.todolist.repository.UserSettingsRepository;
import com.todolist.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 用户服务
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.upload.avatar-dir:uploads/avatars}")
    private String avatarDir;

    @Value("${app.upload.avatar-url-prefix:/uploads/avatars}")
    private String avatarUrlPrefix;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Transactional
    public String register(UserRegisterDTO registerDTO) {
        Long count = userRepository.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, registerDTO.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }

        count = userRepository.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, registerDTO.getEmail()));
        if (count > 0) {
            throw new RuntimeException("邮箱已被注册");
        }

        if (registerDTO.getPhone() != null) {
            count = userRepository.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, registerDTO.getPhone()));
            if (count > 0) {
                throw new RuntimeException("手机号已被注册");
            }
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setIsActive(true);

        userRepository.insert(user);

        UserSettings settings = new UserSettings();
        settings.setUserId(user.getId());
        userSettingsRepository.insert(settings);

        return jwtUtil.generateToken(user.getUsername(), user.getId());
    }

    public String login(UserLoginDTO loginDTO) {
        User user = userRepository.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("账户已被禁用");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getId());
    }

    public User getUserById(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }

    public UserProfileDTO getProfile(Long userId) {
        User user = getUserById(userId);
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    @Transactional
    public UserProfileDTO updateProfile(Long userId, UserProfileUpdateDTO updateDTO) {
        User user = getUserById(userId);
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail())) {
            Long count = userRepository.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, updateDTO.getEmail()));
            if (count > 0) {
                throw new RuntimeException("邮箱已被使用");
            }
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPhone() != null && !updateDTO.getPhone().equals(user.getPhone())) {
            Long count = userRepository.selectCount(new LambdaQueryWrapper<User>()
                    .eq(User::getPhone, updateDTO.getPhone()));
            if (count > 0) {
                throw new RuntimeException("手机号已被使用");
            }
            user.setPhone(updateDTO.getPhone());
        }
        userRepository.updateById(user);
        return getProfile(user.getId());
    }

    @Transactional
    public String updateAvatar(Long userId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("只支持图片格式");
        }

        Path uploadPath = Paths.get(avatarDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String ext = file.getOriginalFilename() != null
                ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
                : ".jpg";
        String filename = UUID.randomUUID().toString() + ext;
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));

        User user = getUserById(userId);
        user.setAvatarUrl(avatarUrlPrefix + "/" + filename);
        userRepository.updateById(user);
        return user.getAvatarUrl();
    }

    @Transactional
    public void updatePassword(Long userId, UserPasswordUpdateDTO dto) {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("两次密码不一致");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.updateById(user);
    }
}
