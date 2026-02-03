package com.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * 统计响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponseDTO {

    private Integer todayCompleted;
    private Integer weekCompleted;
    private Integer todayOverdue;
    private Integer totalPending;
    private Map<String, Integer> tagDistribution;
    private Map<LocalDate, Integer> completionTrend;
}
