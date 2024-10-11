package com.project_calendar.CronJob;

import com.project_calendar.Service.LimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LimitCronJob {
    @Autowired
    private LimitService limitService;

    @Scheduled(cron = "0 * * * * *")
    public void scheduledLimitCheck() {
        limitService.limitCalculate();
    }
}
