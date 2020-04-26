package com.odakota.tms.business.process.service;

import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.process.entity.ReportSetting;
import com.odakota.tms.business.process.job.*;
import com.odakota.tms.business.sales.resource.CampaignResource;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.ProcessConstant;
import com.odakota.tms.enums.file.TemplateName;
import com.odakota.tms.enums.process.ProcessStatus;
import com.odakota.tms.enums.process.ReportType;
import com.odakota.tms.system.service.email.SendMailService;
import com.odakota.tms.system.service.scheduler.JobScheduleCreator;
import com.odakota.tms.utils.Utils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.*;

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

    /**
     * Create apply campaign item. This job have run previous 30m when campaign start
     *
     * @param branchId         branch id
     * @param campaignResource {@link CampaignResource}
     * @param selectAll        apply all product in branch
     * @param selectCategory   apply all product of category in branch
     * @param selectProduct    apply to products
     */
    public void createApplyCampaignItemJob(Long branchId, CampaignResource campaignResource, Boolean selectAll,
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
                                                               ProcessConstant.CAMPAIGN_GROUP, jobDataMap);
            SimpleTrigger simpleTrigger = jobScheduleCreator.createSimpleTrigger(
                    ProcessConstant.APPLY_CAMPAIGN_TRIGGER_NAME.replace("{0}", branchId.toString())
                                                               .replace("{1}", campaignResource.getName()),
                    Utils.calculationTime(campaignResource.getStartDate(), -30, 0), 0L, 0,
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail, simpleTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete apply campaign item job
     *
     * @param branchId branch id
     * @param name     campaign name
     */
    public void deleteApplyCampaignItemJob(Long branchId, String name) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler
                    .deleteJob(JobKey.jobKey(ProcessConstant.APPLY_CAMPAIGN_JOB_NAME.replace("{0}", branchId.toString())
                                                                                    .replace("{1}", name),
                                             ProcessConstant.CAMPAIGN_GROUP));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create delete token expired job
     */
    public void createDeleteTokenExpiredJob() {
        try {
            JobDetail jobDetail = jobScheduleCreator.createJob(DeleteTokenExpiredJob.class, true,
                                                               applicationContext,
                                                               ProcessConstant.DELETE_TOKEN_JOB_NAME,
                                                               ProcessConstant.SYSTEM_GROUP);
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

    /**
     * Create send notification job when change role of user
     *
     * @param roleId role id
     */
    public void createSendNotificationJob(Long roleId) {
        try {
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.putAsString(ProcessConstant.MAP_KEY_ROLE_ID, roleId);
            JobDetail jobDetail = jobScheduleCreator.createJob(SendNotificationUpdatePermissionJob.class, true,
                                                               applicationContext,
                                                               ProcessConstant.SEND_NOTIFY_UPDATE_PERMISSION_JOB_NAME
                                                                       .replace("{0}", roleId.toString())
                                                                       .replace("{1}", System.currentTimeMillis() + ""),
                                                               ProcessConstant.NOTIFY_GROUP, jobDataMap);
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

    /**
     * Create happy birthday job
     */
    public void createHappyBirthDayJob() {
        try {
            JobDetail jobDetail = jobScheduleCreator.createJob(HappyBirthDayJob.class, true,
                                                               applicationContext,
                                                               ProcessConstant.HAPPY_BIRTHDAY_JOB_NAME,
                                                               ProcessConstant.SYSTEM_GROUP);
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

    /**
     * Create report job
     *
     * @param reportSettings {@link ReportSetting}
     */
    public void createReportJob(List<ReportSetting> reportSettings) {
        try {
            for (ReportSetting setting : reportSettings) {
                String dailyJobName = null;
                String dailyTriggerName = null;
                String dailyCronExpression = null;
                String monthlyJobName = null;
                String monthlyTriggerName = null;
                String monthlyCronExpression = null;
                String yearlyJobName = null;
                String yearlyTriggerName = null;
                String yearlyCronExpression = null;
                JobDetail jobDetail;
                CronTrigger cronTrigger;
                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.put(ProcessConstant.MAP_KEY_BRANCH_ID, setting.getBranchId());
                if (setting.getType().equals(ReportType.INVENTORY) && setting.getBranchId() != null) {
                    dailyJobName = ProcessConstant.INVENTORY_REPORT_DAILY_JOB_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    dailyTriggerName = ProcessConstant.INVENTORY_REPORT_DAILY_TRIGGER_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    dailyCronExpression = ProcessConstant.REPORT_DAILY_CRON_EXPRESSION
                            .replace("{0}", setting.getDailyTime().substring(3))
                            .replace("{1}", setting.getDailyTime().substring(0, 2));
                    jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.INVENTORY);
                }
                if (setting.getType().equals(ReportType.SALE_SUMMARY)) {
                    if (setting.getBranchId() != null) {
                        dailyJobName = ProcessConstant.SALE_SUMMARY_REPORT_DAILY_JOB_NAME
                                .replace("{0}", setting.getBranchId() + "");
                        dailyTriggerName = ProcessConstant.SALE_SUMMARY_REPORT_DAILY_TRIGGER_NAME
                                .replace("{0}", setting.getBranchId() + "");
                        dailyCronExpression = ProcessConstant.REPORT_DAILY_CRON_EXPRESSION
                                .replace("{0}", setting.getDailyTime().substring(3))
                                .replace("{1}", setting.getDailyTime().substring(0, 2));
                        jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.SALE_SUMMARY_OF_DAY);
                    }
                    monthlyJobName = ProcessConstant.SALE_SUMMARY_REPORT_MONTHLY_JOB_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    monthlyTriggerName = ProcessConstant.SALE_SUMMARY_REPORT_MONTHLY_TRIGGER_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    monthlyCronExpression = ProcessConstant.REPORT_MONTHLY_CRON_EXPRESSION
                            .replace("{0}", setting.getMonthlyTime().substring(3))
                            .replace("{1}", setting.getMonthlyTime().substring(0, 2))
                            .replace("{2}", setting.getMonthlyDay().toString());
                    yearlyJobName = ProcessConstant.SALE_SUMMARY_REPORT_YEARLY_JOB_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    yearlyTriggerName = ProcessConstant.SALE_SUMMARY_REPORT_YEARLY_TRIGGER_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    yearlyCronExpression = ProcessConstant.REPORT_YEARLY_CRON_EXPRESSION
                            .replace("{0}", setting.getYearlyTime().substring(3))
                            .replace("{1}", setting.getYearlyTime().substring(0, 2))
                            .replace("{2}", setting.getYearlyDay().toString())
                            .replace("{3}", setting.getYearlyMonth().toString());
                }
                if (setting.getType().equals(ReportType.REVENUE_BY_STAFF)) {
                    monthlyJobName = ProcessConstant.REVENUE_BY_STAFF_REPORT_MONTHLY_JOB_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    monthlyTriggerName = ProcessConstant.REVENUE_BY_STAFF_REPORT_MONTHLY_TRIGGER_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    monthlyCronExpression = ProcessConstant.REPORT_MONTHLY_CRON_EXPRESSION
                            .replace("{0}", setting.getMonthlyTime().substring(3))
                            .replace("{1}", setting.getMonthlyTime().substring(0, 2))
                            .replace("{2}", setting.getMonthlyDay().toString());
                    yearlyJobName = ProcessConstant.REVENUE_BY_STAFF_REPORT_YEARLY_JOB_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    yearlyTriggerName = ProcessConstant.REVENUE_BY_STAFF_REPORT_YEARLY_TRIGGER_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    yearlyCronExpression = ProcessConstant.REPORT_YEARLY_CRON_EXPRESSION
                            .replace("{0}", setting.getYearlyTime().substring(3))
                            .replace("{1}", setting.getYearlyTime().substring(0, 2))
                            .replace("{2}", setting.getYearlyDay().toString())
                            .replace("{3}", setting.getYearlyMonth().toString());
                }
                if (setting.getType().equals(ReportType.BESTSELLERS)) {
                    monthlyJobName = ProcessConstant.BESTSELLERS_REPORT_MONTHLY_JOB_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    monthlyTriggerName = ProcessConstant.BESTSELLERS_REPORT_MONTHLY_TRIGGER_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    monthlyCronExpression = ProcessConstant.REPORT_MONTHLY_CRON_EXPRESSION
                            .replace("{0}", setting.getMonthlyTime().substring(3))
                            .replace("{1}", setting.getMonthlyTime().substring(0, 2))
                            .replace("{2}", setting.getMonthlyDay().toString());
                    yearlyJobName = ProcessConstant.BESTSELLERS_REPORT_YEARLY_JOB_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    yearlyTriggerName = ProcessConstant.BESTSELLERS_REPORT_YEARLY_TRIGGER_NAME
                            .replace("{0}", setting.getBranchId() + "");
                    yearlyCronExpression = ProcessConstant.REPORT_YEARLY_CRON_EXPRESSION
                            .replace("{0}", setting.getYearlyTime().substring(3))
                            .replace("{1}", setting.getYearlyTime().substring(0, 2))
                            .replace("{2}", setting.getYearlyDay().toString())
                            .replace("{3}", setting.getYearlyMonth().toString());
                    jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.BESTSELLERS);
                }
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                if (dailyJobName != null) {
                    jobDataMap.put(ProcessConstant.MAP_KEY_TIME_TYPE, ProcessConstant.MAP_VALUE_TIME_TYPE_DAILY);
                    jobDetail = jobScheduleCreator.createJob(ReportJob.class, true,
                                                             applicationContext, dailyJobName,
                                                             ProcessConstant.REPORT_GROUP, jobDataMap);
                    cronTrigger = jobScheduleCreator.createCronTrigger(dailyTriggerName,
                                                                       ProcessConstant.REPORT_GROUP,
                                                                       new Date(), dailyCronExpression,
                                                                       SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW,
                                                                       TimeZone.getTimeZone("GMT+7:00"));
                    // check job existed
                    if (scheduler.checkExists(jobDetail.getKey())) {
                        scheduler.deleteJob(jobDetail.getKey());
                    }
                    if (setting.getStatus().equals(ProcessStatus.IN_PROGRESS)) {
                        scheduler.scheduleJob(jobDetail, cronTrigger);
                    }
                }
                if (monthlyJobName != null) {
                    if (setting.getType().equals(ReportType.BESTSELLERS)) {
                        jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.BESTSELLERS_OF_MONTH);
                    }
                    if (setting.getType().equals(ReportType.REVENUE_BY_STAFF)) {
                        jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.REVENUE_BY_STAFF_OF_MONTH);
                    }
                    if (setting.getType().equals(ReportType.SALE_SUMMARY)) {
                        jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.SALE_SUMMARY_OF_MONTH);
                    }
                    jobDataMap.put(ProcessConstant.MAP_KEY_TIME_TYPE, ProcessConstant.MAP_VALUE_TIME_TYPE_MONTHLY);
                    jobDetail = jobScheduleCreator.createJob(ReportJob.class, true,
                                                             applicationContext, monthlyJobName,
                                                             ProcessConstant.REPORT_GROUP, jobDataMap);
                    cronTrigger = jobScheduleCreator.createCronTrigger(monthlyTriggerName,
                                                                       ProcessConstant.REPORT_GROUP,
                                                                       new Date(), monthlyCronExpression,
                                                                       SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW,
                                                                       TimeZone.getTimeZone("GMT+7:00"));
                    // check job existed
                    if (scheduler.checkExists(jobDetail.getKey())) {
                        scheduler.deleteJob(jobDetail.getKey());
                    }
                    if (setting.getStatus().equals(ProcessStatus.IN_PROGRESS)) {
                        scheduler.scheduleJob(jobDetail, cronTrigger);
                    }
                }
                if (yearlyJobName != null) {
                    if (setting.getType().equals(ReportType.BESTSELLERS)) {
                        jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.BESTSELLERS_OF_YEAR);
                    }
                    if (setting.getType().equals(ReportType.REVENUE_BY_STAFF)) {
                        jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.REVENUE_BY_STAFF_OF_YEAR);
                    }
                    if (setting.getType().equals(ReportType.SALE_SUMMARY)) {
                        jobDataMap.put(ProcessConstant.MAP_KEY_REPORT_TYPE, ReportType.SALE_SUMMARY_OF_YEAR);
                    }
                    jobDataMap.put(ProcessConstant.MAP_KEY_TIME_TYPE, ProcessConstant.MAP_VALUE_TIME_TYPE_YEARLY);
                    jobDetail = jobScheduleCreator.createJob(ReportJob.class, true,
                                                             applicationContext, yearlyJobName,
                                                             ProcessConstant.REPORT_GROUP, jobDataMap);
                    cronTrigger = jobScheduleCreator.createCronTrigger(yearlyTriggerName,
                                                                       ProcessConstant.REPORT_GROUP,
                                                                       new Date(), yearlyCronExpression,
                                                                       SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW,
                                                                       TimeZone.getTimeZone("GMT+7:00"));
                    // check job existed
                    if (scheduler.checkExists(jobDetail.getKey())) {
                        scheduler.deleteJob(jobDetail.getKey());
                    }
                    if (setting.getStatus().equals(ProcessStatus.IN_PROGRESS)) {
                        scheduler.scheduleJob(jobDetail, cronTrigger);
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send mail when job run failed
     *
     * @param jobName job name
     */
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
