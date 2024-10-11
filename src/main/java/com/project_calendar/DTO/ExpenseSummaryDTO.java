package com.project_calendar.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExpenseSummaryDTO {
    private List<ExpenseDTO> expenses;
    private double totalSum;

}
