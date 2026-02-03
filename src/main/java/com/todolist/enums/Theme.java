package com.todolist.enums;

/**
 * 主题枚举
 */
public enum Theme {
    LIGHT("浅色"),
    DARK("深色");

    private final String description;

    Theme(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
