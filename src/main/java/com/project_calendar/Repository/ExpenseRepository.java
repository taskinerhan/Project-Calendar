package com.project_calendar.Repository;

import com.project_calendar.Entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e WHERE FUNCTION('DATE_PART', 'year', e.date) = :year AND FUNCTION('DATE_PART', 'month', e.date) = :month")
    List<Expense> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT e FROM Expense e WHERE FUNCTION('DATE_PART', 'year', e.date) = :year AND e.user.id=:id")
    List<Expense> findByYearANDUserId(@Param("year") int year, @Param("id") Long id);

    @Query("SELECT e FROM Expense e WHERE FUNCTION('DATE_PART', 'year', e.date) = :year AND FUNCTION('DATE_PART', 'month', e.date) = :month AND FUNCTION('DATE_PART', 'day', e.date) = :day")
    List<Expense> findByDayAndMonthAndYear(@Param("day") int day,@Param("month") int month,@Param("year") int year);

    @Query("SELECT e FROM Expense e WHERE FUNCTION('DATE_PART', 'year', e.date) = :year AND FUNCTION('DATE_PART', 'month', e.date) = :month AND e.user.id = :userId")
    List<Expense> findByYearAndMonthAndUserId(@Param("year") int year, @Param("month") int month, @Param("userId") Long userId);

}
