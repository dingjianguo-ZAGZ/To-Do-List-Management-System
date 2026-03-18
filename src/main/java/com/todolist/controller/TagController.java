package com.todolist.controller;

import com.todolist.dto.ApiResponse;
import com.todolist.dto.TagCreateDTO;
import com.todolist.dto.TagResponseDTO;
import com.todolist.service.TagService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 */
@RestController
@RequestMapping("/tags")
@CrossOrigin
public class TagController {

    @Autowired
    private TagService tagService;

    @PostMapping
    public ApiResponse<TagResponseDTO> createTag(
            @RequestAttribute("userId") Long userId,
            @Valid @RequestBody TagCreateDTO createDTO) {
        try {
            TagResponseDTO result = tagService.createTag(userId, createDTO);
            return ApiResponse.success("创建成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping("/{tagId}")
    public ApiResponse<TagResponseDTO> updateTag(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long tagId,
            @Valid @RequestBody TagCreateDTO updateDTO) {
        try {
            TagResponseDTO result = tagService.updateTag(userId, tagId, updateDTO);
            return ApiResponse.success("更新成功", result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{tagId}")
    public ApiResponse<Void> deleteTag(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long tagId) {
        try {
            tagService.deleteTag(userId, tagId);
            return ApiResponse.success("删除成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<TagResponseDTO>> getAllTags(
            @RequestAttribute("userId") Long userId) {
        try {
            List<TagResponseDTO> result = tagService.getAllTags(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
