package com.todolist.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.todolist.dto.StatisticsResponseDTO;
import com.todolist.entity.Statistics;
import com.todolist.entity.TodoItem;
import com.todolist.enums.TodoStatus;
import com.todolist.repository.StatisticsRepository;
import com.todolist.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计服务
 */
@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private TodoItemRepository todoItemRepository;

    public StatisticsResponseDTO getStatistics(Long userId) {
        StatisticsResponseDTO dto = new StatisticsResponseDTO();

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);
        Long todayCompleted = todoItemRepository.selectCount(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getUserId, userId)
                .eq(TodoItem::getStatus, TodoStatus.COMPLETED)
                .between(TodoItem::getCompletedAt, todayStart, todayEnd));
        dto.setTodayCompleted(todayCompleted.intValue());

        LocalDateTime weekStart = LocalDate.now().minusDays(7).atStartOfDay();
        Long weekCompleted = todoItemRepository.selectCount(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getUserId, userId)
                .eq(TodoItem::getStatus, TodoStatus.COMPLETED)
                .ge(TodoItem::getCompletedAt, weekStart));
        dto.setWeekCompleted(weekCompleted.intValue());

        Long overdueCount = todoItemRepository.selectCount(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getUserId, userId)
                .ne(TodoItem::getStatus, TodoStatus.COMPLETED)
                .lt(TodoItem::getDueDate, LocalDateTime.now()));
        dto.setTodayOverdue(overdueCount.intValue());

        Long pendingCount = todoItemRepository.selectCount(new LambdaQueryWrapper<TodoItem>()
                .eq(TodoItem::getUserId, userId)
                .ne(TodoItem::getStatus, TodoStatus.COMPLETED));
        dto.setTotalPending(pendingCount.intValue());

        List<Statistics> last7Days = statisticsRepository.selectList(new LambdaQueryWrapper<Statistics>()
                .eq(Statistics::getUserId, userId)
                .between(Statistics::getStatDate, LocalDate.now().minusDays(7), LocalDate.now())
                .orderByAsc(Statistics::getStatDate));

        Map<LocalDate, Integer> completionTrend = last7Days.stream()
                .collect(Collectors.toMap(
                        Statistics::getStatDate,
                        Statistics::getCompletedCount
                ));
        dto.setCompletionTrend(completionTrend);

        Map<String, Integer> tagDistribution = new HashMap<>();
        dto.setTagDistribution(tagDistribution);

        return dto;
    }

    public void updateDailyStatistics(Long userId, LocalDate date) {
        Statistics statistics = statisticsRepository.selectOne(new LambdaQueryWrapper<Statistics>()
                .eq(Statistics::getUserId, userId)
                .eq(Statistics::getStatDate, date));

        if (statistics == null) {
            statistics = new Statistics();
            statistics.setUserId(userId);
            statistics.setStatDate(date);
            statistics.setCompletedCount(1);
            statisticsRepository.insert(statistics);
        } else {
            statistics.setCompletedCount(statistics.getCompletedCount() + 1);
            statisticsRepository.updateById(statistics);
        }
    }
}
