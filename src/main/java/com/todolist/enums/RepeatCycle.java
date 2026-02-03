package com.todolist.enums;

/**
 * 重复周期枚举
 */
public enum RepeatCycle {
    NONE("不重复"),
    DAILY("每天"),
    WEEKLY("每周"),
    MONTHLY("每月"),
    YEARLY("每年"),
    WORKDAY("工作日");

    private final String description;

    RepeatCycle(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
