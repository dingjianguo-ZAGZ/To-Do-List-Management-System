package com.todolist.enums;

/**
 * 提醒方式枚举
 */
public enum ReminderType {
    SYSTEM_NOTIFICATION("系统弹窗"),
    DESKTOP_NOTIFICATION("桌面通知"),
    SMS("短信"),
    EMAIL("邮件");

    private final String description;

    ReminderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
