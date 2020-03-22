package com.odakota.tms.business.process.service;

import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.process.job.ApplyCampaignItemJob;
import com.odakota.tms.business.process.job.DeleteTokenExpiredJob;
import com.odakota.tms.business.process.job.HappyBirthDayJob;
import com.odakota.tms.business.process.job.SendNotificationUpdatePermissionJob;
import com.odakota.tms.business.sales.resource.CampaignResource;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.ProcessConstant;
import com.odakota.tms.enums.file.TemplateName;
import com.odakota.tms.system.service.email.SendMailService;
import com.odakota.tms.system.service.scheduler.JobScheduleCreator;
import com.odakota.tms.utils.Utils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class QuartzScheduleService {

    private final JobScheduleCreator jobScheduleCreator;

    private final ApplicationContext applicationContext;

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final SendMailService sendMailService;

    private final UserRepository userRepository;

    @Autowired
    public QuartzScheduleService(JobScheduleCreator jobScheduleCreator,
                                 ApplicationContext applicationContext,
                                 SchedulerFactoryBean schedulerFactoryBean,
                                 SendMailService sendMailService,
                                 UserRepository userRepository) {
        this.jobScheduleCreator = jobScheduleCreator;
        this.applicationContext = applicationContext;
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.sendMailService = sendMailService;
        this.userRepository = userRepository;
    }

    public void creatApplyCampaignItemJob(Long branchId, CampaignResource campaignResource, Boolean selectAll,
                                          List<Long> selectCategory, List<Long> selectProduct) {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.putAsString(ProcessConstant.MAP_KEY_BRANCH_ID, branchId);
            jobDataMap.putAsString(ProcessConstant.MAP_KEY_CAMPAIGN_ID, campaignResource.getId());
            jobDataMap.putAsString(ProcessConstant.MAP_KEY_SELECT_ALL, selectAll);
            jobDataMap.put(ProcessConstant.MAP_KEY_SELECT_CATEGORY, selectCategory);
            jobDataMap.put(ProcessConstant.MAP_KEY_SELECT_PRODUCT, selectProduct);
            JobDetail jobDetail = jobScheduleCreator.createJob(ApplyCampaignItemJob.class, true,
                                                               applicationContext,
                                                               ProcessConstant.APPLY_CAMPAIGN_JOB_NAME
                                                                       .replace("{0}", branchId.toString())
                                                                       .replace("{1}",
                                                                                campaignResource.getName()),
                                                               ProcessConstant.SYSTEM_GROUP, jobDataMap);
            SimpleTrigger simpleTrigger = jobScheduleCreator.createSimpleTrigger(
                    ProcessConstant.APPLY_CAMPAIGN_TRIGGER_NAME.replace("{0}", branchId.toString())
                                                               .replace("{1}", campaignResource.getName()),
                    Utils.calculationTime(campaignResource.getStartDate(), 0, 0, -30, 0), 0L, 0,
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void deleteApplyCampaignItemJob(Long branchId, String name) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler
                    .deleteJob(JobKey.jobKey(ProcessConstant.APPLY_CAMPAIGN_JOB_NAME.replace("{0}", branchId.toString())
                                                                                    .replace("{1}", name),
                                             ProcessConstant.SYSTEM_GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void creatDeleteTokenExpiredJob() {
        try {
            JobDetail jobDetail = jobScheduleCreator.createJob(DeleteTokenExpiredJob.class, true,
                                                               applicationContext,
                                                               ProcessConstant.DELETE_TOKEN_JOB_NAME,
                                                               ProcessConstant.SYSTEM_GROUP,
                                                               null);
            CronTrigger cronTrigger = jobScheduleCreator
                    .createCronTrigger(ProcessConstant.DELETE_TOKEN_TRIGGER_NAME, ProcessConstant.SYSTEM_GROUP,
                                       new Date(),
                                       ProcessConstant.DELETE_TOKEN_CRON_EXPRESSION,
                                       SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            // check job existed
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, cronTrigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void creatSendNotificationJob(Long roleId) {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.putAsString(ProcessConstant.MAP_KEY_ROLE_ID, roleId);
            JobDetail jobDetail = jobScheduleCreator.createJob(SendNotificationUpdatePermissionJob.class, true,
                                                               applicationContext,
                                                               ProcessConstant.SEND_NOTIFY_UPDATE_PERMISSION_JOB_NAME
                                                                       .replace("{0}", roleId.toString())
                                                                       .replace("{1}", System.currentTimeMillis() + ""),
                                                               ProcessConstant.SYSTEM_GROUP, jobDataMap);
            SimpleTrigger simpleTrigger = jobScheduleCreator.createSimpleTrigger(
                    ProcessConstant.SEND_NOTIFY_UPDATE_PERMISSION_TRIGGER_NAME.replace("{0}", roleId.toString())
                                                                              .replace("{1}",
                                                                                       System.currentTimeMillis() + ""),
                    new Date(), 0L, 0, SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void creatHappyBirthDayJob() {
        try {
            JobDetail jobDetail = jobScheduleCreator.createJob(HappyBirthDayJob.class, true,
                                                               applicationContext,
                                                               ProcessConstant.HAPPY_BIRTHDAY_JOB_NAME,
                                                               ProcessConstant.SYSTEM_GROUP,
                                                               null);
            CronTrigger cronTrigger = jobScheduleCreator
                    .createCronTrigger(ProcessConstant.HAPPY_BIRTHDAY_TRIGGER_NAME, ProcessConstant.SYSTEM_GROUP,
                                       new Date(),
                                       ProcessConstant.HAPPY_BIRTHDAY_CRON_EXPRESSION,
                                       SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            // check job existed
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.scheduleJob(jobDetail, cronTrigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void sendMailWhenJobFailed(String jobName) {
        Map<String, Object> map = new HashMap<>();
        map.put("job", jobName);
        map.put("time", Utils.convertToStringFormat(new Date(), Constant.HH_MM_SS_DD_MM_YYYY));
        userRepository.findByUsernameAndDeletedFlagFalse("root").ifPresent(var -> {
            sendMailService.sendSimpleMail("Warns of failed processes", var.getEmail(),
                                           TemplateName.TEMPLATE_JOB_ERROR, map);
        });
    }
}
