package com.todolist.controller;

import com.todolist.dto.ApiResponse;
import com.todolist.dto.StatisticsResponseDTO;
import com.todolist.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 统计控制器
 */
@RestController
@RequestMapping("/statistics")
@CrossOrigin
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping
    public ApiResponse<StatisticsResponseDTO> getStatistics(
            @RequestAttribute("userId") Long userId) {
        try {
            StatisticsResponseDTO result = statisticsService.getStatistics(userId);
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
