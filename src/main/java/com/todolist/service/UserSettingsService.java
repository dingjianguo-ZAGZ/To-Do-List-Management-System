package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.dto.UserSettingsDTO;
import com.todolist.entity.UserSettings;
import com.todolist.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户设置服务
 */
@Service
public class UserSettingsService {

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    public UserSettingsDTO getUserSettings(Long userId) {
        UserSettings settings = userSettingsRepository.selectOne(new LambdaQueryWrapper<UserSettings>()
                .eq(UserSettings::getUserId, userId));

        if (settings == null) {
            settings = new UserSettings();
            settings.setUserId(userId);
            userSettingsRepository.insert(settings);
        }

        return convertToDTO(settings);
    }

    @Transactional
    public UserSettingsDTO updateUserSettings(Long userId, UserSettingsDTO settingsDTO) {
        UserSettings settings = userSettingsRepository.selectOne(new LambdaQueryWrapper<UserSettings>()
                .eq(UserSettings::getUserId, userId));

        if (settings == null) {
            throw new RuntimeException("用户设置不存在");
        }

        if (settingsDTO.getTheme() != null) {
            settings.setTheme(settingsDTO.getTheme());
        }
        if (settingsDTO.getFontSize() != null) {
            settings.setFontSize(settingsDTO.getFontSize());
        }
        if (settingsDTO.getShowTitle() != null) {
            settings.setShowTitle(settingsDTO.getShowTitle());
        }
        if (settingsDTO.getShowDueDate() != null) {
            settings.setShowDueDate(settingsDTO.getShowDueDate());
        }
        if (settingsDTO.getShowPriority() != null) {
            settings.setShowPriority(settingsDTO.getShowPriority());
        }
        if (settingsDTO.getShowTags() != null) {
            settings.setShowTags(settingsDTO.getShowTags());
        }
        if (settingsDTO.getReminderWorkHoursOnly() != null) {
            settings.setReminderWorkHoursOnly(settingsDTO.getReminderWorkHoursOnly());
        }
        if (settingsDTO.getReminderWorkDaysOnly() != null) {
            settings.setReminderWorkDaysOnly(settingsDTO.getReminderWorkDaysOnly());
        }

        userSettingsRepository.updateById(settings);
        return convertToDTO(settings);
    }

    private UserSettingsDTO convertToDTO(UserSettings settings) {
        UserSettingsDTO dto = new UserSettingsDTO();
        dto.setTheme(settings.getTheme());
        dto.setFontSize(settings.getFontSize());
        dto.setShowTitle(settings.getShowTitle());
        dto.setShowDueDate(settings.getShowDueDate());
        dto.setShowPriority(settings.getShowPriority());
        dto.setShowTags(settings.getShowTags());
        dto.setReminderWorkHoursOnly(settings.getReminderWorkHoursOnly());
        dto.setReminderWorkDaysOnly(settings.getReminderWorkDaysOnly());
        return dto;
    }
}
