package com.todolist.dto;

import com.todolist.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户设置DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDTO {

    private Theme theme;
    private Integer fontSize;
    private Boolean showTitle;
    private Boolean showDueDate;
    private Boolean showPriority;
    private Boolean showTags;
    private Boolean reminderWorkHoursOnly;
    private Boolean reminderWorkDaysOnly;
}
