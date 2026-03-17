package com.todolist.entity;

import com.todolist.enums.Priority;
import com.todolist.enums.RepeatCycle;
import com.todolist.enums.TodoStatus;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 待办事项实体
 */
@Entity
@Table(name = "todo_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status = TodoStatus.NOT_STARTED;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeat_cycle")
    private RepeatCycle repeatCycle = RepeatCycle.NONE;

    @Column(name = "next_repeat_date")
    private LocalDateTime nextRepeatDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "completion_note", columnDefinition = "TEXT")
    private String completionNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToMany
    @JoinTable(
        name = "todo_tags",
        joinColumns = @JoinColumn(name = "todo_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "todoItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "todoItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reminder> reminders = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 标记为完成
     */
    public void complete(String note) {
        this.status = TodoStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.completionNote = note;

        if (this.repeatCycle != RepeatCycle.NONE) {
            calculateNextRepeatDate();
        }
    }

    /**
     * 计算下次重复日期
     */
    private void calculateNextRepeatDate() {
//        LocalDateTime base = this.nextRepeatDate != null ? this.nextRepeatDate : LocalDateTime.now();
//
//        switch (this.repeatCycle) {
//            case DAILY -> this.nextRepeatDate = base.plusDays(1);
//            case WEEKLY -> this.nextRepeatDate = base.plusWeeks(1);
//            case MONTHLY -> this.nextRepeatDate = base.plusMonths(1);
//            case YEARLY -> this.nextRepeatDate = base.plusYears(1);
//            case WORKDAY -> {
//                LocalDateTime next = base.plusDays(1);
//                while (next.getDayOfWeek().getValue() >= 6) {
//                    next = next.plusDays(1);
//                }
//                this.nextRepeatDate = next;
//            }
//        }
    }

    /**
     * 检查是否逾期
     */
    public boolean isOverdue() {
        return this.dueDate != null
            && this.status != TodoStatus.COMPLETED
            && LocalDateTime.now().isAfter(this.dueDate);
    }
}
