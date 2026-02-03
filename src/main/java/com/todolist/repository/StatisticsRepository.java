package com.todolist.repository;

import com.todolist.entity.Statistics;
import com.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 统计数据访问层
 */
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    Optional<Statistics> findByUserAndStatDate(User user, LocalDate statDate);

    @Query("SELECT s FROM Statistics s WHERE s.user = :user AND s.statDate BETWEEN :start AND :end ORDER BY s.statDate")
    List<Statistics> findByUserAndDateRange(
        @Param("user") User user,
        @Param("start") LocalDate start,
        @Param("end") LocalDate end
    );
}
