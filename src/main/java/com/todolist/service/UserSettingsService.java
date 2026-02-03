package com.todolist.service;

import com.todolist.dto.UserSettingsDTO;
import com.todolist.entity.User;
import com.todolist.entity.UserSettings;
import com.todolist.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    public UserSettingsDTO getUserSettings(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        UserSettings settings = userSettingsRepository.findByUser(user)
                .orElseGet(() -> {
                    UserSettings newSettings = new UserSettings();
                    newSettings.setUser(user);
                    return userSettingsRepository.save(newSettings);
                });

        return convertToDTO(settings);
    }

    @Transactional
    public UserSettingsDTO updateUserSettings(Long userId, UserSettingsDTO settingsDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        UserSettings settings = userSettingsRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("用户设置不存在"));

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

        settings = userSettingsRepository.save(settings);
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
