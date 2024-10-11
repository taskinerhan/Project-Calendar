package com.project_calendar.Repository;

import com.project_calendar.Entity.Limit;
import com.project_calendar.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit,Long> {
    List<Limit> findByUserId(Long userId);
    boolean existsByUserIdAndMonthAndYear(Long userId, int month, Integer year);
}
