package com.todolist.entity;

import com.todolist.enums.Theme;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户设置实体
 */
@Entity
@Table(name = "user_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Theme theme = Theme.LIGHT;

    @Column(name = "font_size")
    private Integer fontSize = 14;

    @Column(name = "show_title")
    private Boolean showTitle = true;

    @Column(name = "show_due_date")
    private Boolean showDueDate = true;

    @Column(name = "show_priority")
    private Boolean showPriority = true;

    @Column(name = "show_tags")
    private Boolean showTags = true;

    @Column(name = "reminder_work_hours_only")
    private Boolean reminderWorkHoursOnly = true;

    @Column(name = "reminder_work_days_only")
    private Boolean reminderWorkDaysOnly = true;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
