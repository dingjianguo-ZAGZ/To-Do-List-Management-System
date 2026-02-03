package com.todolist.enums;

/**
 * 事项状态枚举
 */
public enum TodoStatus {
    NOT_STARTED("未开始"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    OVERDUE("已逾期");

    private final String description;

    TodoStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
