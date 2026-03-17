package com.todolist.controller;

import com.todolist.dto.ApiResponse;
import com.todolist.dto.FolderCreateDTO;
import com.todolist.dto.FolderResponseDTO;
import com.todolist.service.FolderService;
import com.todolist.util.JwtUtil;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文件夹控制器
 */
@RestController
@RequestMapping("/folders")
@CrossOrigin
public class FolderController {

    @Autowired
    private FolderService folderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ApiResponse<FolderResponseDTO> createFolder(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody FolderCreateDTO createDTO) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            FolderResponseDTO result = folderService.createFolder(userId, createDTO);
            return ApiResponse.success("创建成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{folderId}")
    public ApiResponse<FolderResponseDTO> updateFolder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long folderId,
            @Valid @RequestBody FolderCreateDTO updateDTO) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            FolderResponseDTO result = folderService.updateFolder(userId, folderId, updateDTO);
            return ApiResponse.success("更新成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{folderId}")
    public ApiResponse<Void> deleteFolder(
            @RequestHeader("Authorization") String token,
            @PathVariable Long folderId) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            folderService.deleteFolder(userId, folderId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<FolderResponseDTO>> getAllFolders(
            @RequestHeader("Authorization") String token) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(token.substring(7));
            List<FolderResponseDTO> result = folderService.getAllFolders(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
