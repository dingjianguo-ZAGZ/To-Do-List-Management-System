package com.todolist.enums;

/**
 * 事项优先级枚举
 */
public enum Priority {
    HIGH("高"),
    MEDIUM("中"),
    LOW("低");

    private final String description;

    Priority(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
