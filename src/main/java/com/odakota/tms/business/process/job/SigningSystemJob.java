package com.odakota.tms.business.process.job;

import com.odakota.tms.business.process.service.QuartzScheduleService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haidv
 * @version 1.0
 */
@Configuration
public class SigningSystemJob {

    private final QuartzScheduleService quartzScheduleService;

    @Autowired
    public SigningSystemJob(QuartzScheduleService quartzScheduleService) {
        this.quartzScheduleService = quartzScheduleService;
    }

    @Bean(name = "auto delete token expired job")
    public void clearTokenScheduler() throws SchedulerException {
        quartzScheduleService.creatDeleteTokenExpiredJob();
        quartzScheduleService.creatHappyBirthDayJob();
    }
}
