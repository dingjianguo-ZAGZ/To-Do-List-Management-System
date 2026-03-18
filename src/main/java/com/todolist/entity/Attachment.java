package com.todolist.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 附件实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("attachments")
public class Attachment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String fileName;

    private String filePath;

    private Long fileSize;

    private String contentType;

    private Long todoId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime uploadedAt;

    @TableField(exist = false)
    private TodoItem todoItem;
}
