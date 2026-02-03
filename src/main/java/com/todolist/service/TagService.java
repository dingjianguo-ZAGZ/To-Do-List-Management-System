package com.todolist.service;

import com.todolist.dto.TagCreateDTO;
import com.todolist.dto.TagResponseDTO;
import com.todolist.entity.Tag;
import com.todolist.entity.User;
import com.todolist.repository.TagRepository;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签服务
 */
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public TagResponseDTO createTag(Long userId, TagCreateDTO createDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (tagRepository.existsByUserAndName(user, createDTO.getName())) {
            throw new RuntimeException("标签名称已存在");
        }

        Tag tag = new Tag();
        tag.setName(createDTO.getName());
        tag.setColor(createDTO.getColor());
        tag.setUser(user);

        tag = tagRepository.save(tag);
        return convertToResponseDTO(tag);
    }

    @Transactional
    public TagResponseDTO updateTag(Long userId, Long tagId, TagCreateDTO updateDTO) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("标签不存在"));

        if (!tag.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        if (updateDTO.getName() != null) {
            tag.setName(updateDTO.getName());
        }
        if (updateDTO.getColor() != null) {
            tag.setColor(updateDTO.getColor());
        }

        tag = tagRepository.save(tag);
        return convertToResponseDTO(tag);
    }

    @Transactional
    public void deleteTag(Long userId, Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("标签不存在"));

        if (!tag.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        tagRepository.delete(tag);
    }

    public List<TagResponseDTO> getAllTags(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        List<Tag> tags = tagRepository.findByUser(user);
        return tags.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private TagResponseDTO convertToResponseDTO(Tag tag) {
        TagResponseDTO dto = new TagResponseDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setColor(tag.getColor());
        dto.setTodoCount(tag.getTodoItems().size());
        dto.setCreatedAt(tag.getCreatedAt());
        return dto;
    }
}
