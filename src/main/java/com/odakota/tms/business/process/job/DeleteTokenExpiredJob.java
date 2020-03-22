package com.odakota.tms.business.process.job;

import com.odakota.tms.business.auth.repository.AccessTokenRepository;
import com.odakota.tms.business.process.service.QuartzScheduleService;
import com.odakota.tms.enums.auth.Client;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class DeleteTokenExpiredJob extends QuartzJobBean {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private QuartzScheduleService quartzScheduleService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("-------{} start-----", DeleteTokenExpiredJob.class.getSimpleName());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -3);
            accessTokenRepository.deleteAccessTokenExpired(Client.ADMIN.name(), calendar.getTime());
            calendar.add(Calendar.DATE, -27);
            accessTokenRepository.deleteAccessTokenExpired(Client.CUSTOMER.name(), calendar.getTime());
        } catch (Exception e) {
            quartzScheduleService.sendMailWhenJobFailed(jobExecutionContext.getJobDetail().getKey().getName());
        }
        log.info("-------{} end-----", DeleteTokenExpiredJob.class.getSimpleName());
    }
}
