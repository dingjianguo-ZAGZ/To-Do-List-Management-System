package com.todolist.service;

import com.todolist.dto.FolderCreateDTO;
import com.todolist.dto.FolderResponseDTO;
import com.todolist.entity.Folder;
import com.todolist.entity.User;
import com.todolist.repository.FolderRepository;
import com.todolist.repository.UserRepository;
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
    private UserRepository userRepository;

    @Transactional
    public FolderResponseDTO createFolder(Long userId, FolderCreateDTO createDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (folderRepository.existsByUserAndName(user, createDTO.getName())) {
            throw new RuntimeException("文件夹名称已存在");
        }

        Folder folder = new Folder();
        folder.setName(createDTO.getName());
        folder.setDescription(createDTO.getDescription());
        folder.setUser(user);

        folder = folderRepository.save(folder);
        return convertToResponseDTO(folder);
    }

    @Transactional
    public FolderResponseDTO updateFolder(Long userId, Long folderId, FolderCreateDTO updateDTO) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("文件夹不存在"));

        if (!folder.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        if (updateDTO.getName() != null) {
            folder.setName(updateDTO.getName());
        }
        if (updateDTO.getDescription() != null) {
            folder.setDescription(updateDTO.getDescription());
        }

        folder = folderRepository.save(folder);
        return convertToResponseDTO(folder);
    }

    @Transactional
    public void deleteFolder(Long userId, Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("文件夹不存在"));

        if (!folder.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        folderRepository.delete(folder);
    }

    public List<FolderResponseDTO> getAllFolders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<Folder> folders = folderRepository.findByUser(user);
        return folders.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private FolderResponseDTO convertToResponseDTO(Folder folder) {
        FolderResponseDTO dto = new FolderResponseDTO();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setDescription(folder.getDescription());
        dto.setTodoCount(folder.getTodoItems().size());
        dto.setCreatedAt(folder.getCreatedAt());
        dto.setUpdatedAt(folder.getUpdatedAt());
        return dto;
    }
}
