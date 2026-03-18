package com.todolist.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UploadAvatarDTO {
    @NotNull
    private Long id;
    @NotNull
    MultipartFile file;

}