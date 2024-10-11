package com.project_calendar.DTO;

import com.project_calendar.Entity.Category;
import com.project_calendar.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseDTO {
    private Long id;
    private String expenseItem;
    private String categoryName;
    private String product;
    private double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private User user;

    private Category category;
    private LimitDTO limitDTO;
}

