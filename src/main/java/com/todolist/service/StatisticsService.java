package com.todolist.service;

import com.todolist.dto.StatisticsResponseDTO;
import com.todolist.entity.Statistics;
import com.todolist.entity.User;
import com.todolist.enums.TodoStatus;
import com.todolist.repository.StatisticsRepository;
import com.todolist.repository.TodoItemRepository;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Autowired
    private UserRepository userRepository;

    public StatisticsResponseDTO getStatistics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        StatisticsResponseDTO dto = new StatisticsResponseDTO();

        Long todayCompleted = todoItemRepository.countTodayCompleted(user);
        dto.setTodayCompleted(todayCompleted.intValue());

        Long weekCompleted = todoItemRepository.countWeekCompleted(user);
        dto.setWeekCompleted(weekCompleted.intValue());

        List<Statistics> last7Days = statisticsRepository.findByUserAndDateRange(
                user,
                LocalDate.now().minusDays(7),
                LocalDate.now()
        );

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Statistics statistics = statisticsRepository
                .findByUserAndStatDate(user, date)
                .orElseGet(() -> {
                    Statistics newStats = new Statistics();
                    newStats.setUser(user);
                    newStats.setStatDate(date);
                    return newStats;
                });

        statistics.setCompletedCount(statistics.getCompletedCount() + 1);
        statisticsRepository.save(statistics);
    }
}
