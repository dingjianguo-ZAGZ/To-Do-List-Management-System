package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.dto.TagCreateDTO;
import com.todolist.dto.TagResponseDTO;
import com.todolist.entity.Tag;
import com.todolist.repository.TagRepository;
import com.todolist.repository.TodoItemRepository;
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
    private TodoItemRepository todoItemRepository;

    @Transactional
    public TagResponseDTO createTag(Long userId, TagCreateDTO createDTO) {
        Long count = tagRepository.selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getUserId, userId)
                .eq(Tag::getName, createDTO.getName()));
        if (count > 0) {
            throw new RuntimeException("标签名称已存在");
        }

        Tag tag = new Tag();
        tag.setName(createDTO.getName());
        tag.setColor(createDTO.getColor());
        tag.setUserId(userId);

        tagRepository.insert(tag);
        return convertToResponseDTO(tag);
    }

    @Transactional
    public TagResponseDTO updateTag(Long userId, Long tagId, TagCreateDTO updateDTO) {
        Tag tag = tagRepository.selectById(tagId);
        if (tag == null) {
            throw new RuntimeException("标签不存在");
        }

        if (!tag.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        if (updateDTO.getName() != null) {
            tag.setName(updateDTO.getName());
        }
        if (updateDTO.getColor() != null) {
            tag.setColor(updateDTO.getColor());
        }

        tagRepository.updateById(tag);
        return convertToResponseDTO(tag);
    }

    @Transactional
    public void deleteTag(Long userId, Long tagId) {
        Tag tag = tagRepository.selectById(tagId);
        if (tag == null) {
            throw new RuntimeException("标签不存在");
        }

        if (!tag.getUserId().equals(userId)) {
            throw new RuntimeException("无权限操作");
        }

        tagRepository.deleteById(tagId);
    }

    public List<TagResponseDTO> getAllTags(Long userId) {
        List<Tag> tags = tagRepository.selectList(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getUserId, userId));
        return tags.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private TagResponseDTO convertToResponseDTO(Tag tag) {
        TagResponseDTO dto = new TagResponseDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setColor(tag.getColor());
        dto.setTodoCount(0); // TODO: 需要通过中间表查询
        dto.setCreatedAt(tag.getCreatedAt());
        return dto;
    }
}
