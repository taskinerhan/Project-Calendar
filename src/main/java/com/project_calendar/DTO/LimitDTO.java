package com.project_calendar.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LimitDTO {
    private Double limitAmount;
    private int month;
    private Integer year;
}
