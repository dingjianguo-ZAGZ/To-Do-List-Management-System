package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.dto.FolderCreateDTO;
import com.todolist.dto.FolderResponseDTO;
import com.todolist.entity.Folder;
import com.todolist.entity.TodoItem;
import com.todolist.repository.FolderRepository;
import com.todolist.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件夹服务
 */
@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Transactional
    public FolderResponseDTO createFolder(Long userId, FolderCreateDTO createDTO) {
        Long count = folderRepository.selectCount(new LambdaQueryWrapper<Folder>()
                .eq(Folder::getUserId, userId)
                .eq(Folder::getName, createDTO.getName()));
        if (count > 0) {
            throw new RuntimeException("文件夹名称已存在");
        }

        Folder folder = new Folder();
        folder.setName(createDTO.getName());
        folder.setDescription(createDTO.getDescription());
        folder.setUserId(userId);

        folderRepository.insert(folder);
        return convertToResponseDTO(folder);
    }

    @Transactional
    public FolderResponseDTO updateFolder(Long userId, Long folderId, FolderCreateDTO updateDTO) {
        Folder folder = folderRepository.selectById(folderId);
        if (folder == null) {
            throw new RuntimeException("文件夹不存在");
        }

        if (!folder.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        if (updateDTO.getName() != null) {
            folder.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            folder.setDescription(updateDTO.getDescription());
        }

        folderRepository.updateById(folder);
        return convertToResponseDTO(folder);
    }

    @Transactional
    public void deleteFolder(Long userId, Long folderId) {
        Folder folder = folderRepository.selectById(folderId);
        if (folder == null) {
            throw new RuntimeException("文件夹不存在");
        }

        if (!folder.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        folderRepository.deleteById(folderId);
    }

    public List<FolderResponseDTO> getAllFolders(Long userId) {
        List<Folder> folders = folderRepository.selectList(new LambdaQueryWrapper<Folder>()
                .eq(Folder::getUserId, userId));
        return folders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private FolderResponseDTO convertToResponseDTO(Folder folder) {
        FolderResponseDTO dto = new FolderResponseDTO();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setDescription(folder.getDescription());

        Long todoCount = todoItemRepository.selectCount(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getFolderId, folder.getId()));
        dto.setTodoCount(todoCount.intValue());

        dto.setCreatedAt(folder.getCreatedAt());
        dto.setUpdatedAt(folder.getUpdatedAt());
        return dto;
    }
}
