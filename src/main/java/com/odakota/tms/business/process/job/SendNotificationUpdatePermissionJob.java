package com.odakota.tms.business.process.job;

import com.odakota.tms.business.auth.entity.UserRole;
import com.odakota.tms.business.auth.repository.UserRoleRepository;
import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.entity.NotificationUser;
import com.odakota.tms.business.notification.repository.NotificationRepository;
import com.odakota.tms.business.notification.repository.NotificationUserRepository;
import com.odakota.tms.business.process.service.QuartzScheduleService;
import com.odakota.tms.constant.NotificationConstant;
import com.odakota.tms.constant.ProcessConstant;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.enums.notify.Priority;
import com.odakota.tms.enums.notify.SendStatus;
import com.odakota.tms.mapper.ModelMapper;
import com.odakota.tms.system.service.websocket.WebSocket;
import com.odakota.tms.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class SendNotificationUpdatePermissionJob extends QuartzJobBean {

    @Autowired
    private QuartzScheduleService quartzScheduleService;

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private WebSocket webSocket;

    private ModelMapper mapper = Mappers.getMapper(ModelMapper.class);

    /**
     * Execute the actual job. The job data map will already have been applied as bean property values by execute. The
     * contract is exactly the same as for the standard Quartz execute method.
     *
     * @param context {@link JobExecutionContext}
     * @see #execute
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("-------{} start-----", SendNotificationUpdatePermissionJob.class.getSimpleName());

        JobDetail jobDetail = context.getJobDetail();
        try {
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            Long roleId = jobDataMap.getLongFromString(ProcessConstant.MAP_KEY_ROLE_ID);
            List<UserRole> userRoles = userRoleRepository.findByRoleIdAndDeletedFlagFalse(roleId);
            Notification notification = new Notification();
            if (!userRoles.isEmpty()) {
                notification.setPriority(Priority.HIGH);
                notification.setType(MsgType.SYSTEM);
                notification.setTitle(NotificationConstant.TITLE_CHANGE_PERMISSION);
                notification.setContent(NotificationConstant.CONTENT_CHANGE_PERMISSION);
                notification.setSendStatus(SendStatus.PUBLISHED);
                notification.setStartDate(new Date());
                notification.setSendTime(new Date());
                notification = notificationRepository.save(notification);
            }
            for (UserRole userRole : userRoles) {
                NotificationUser notificationUser = new NotificationUser();
                notificationUser.setNotificationId(notification.getId());
                notificationUser.setRead(false);
                notificationUser.setUserId(userRole.getUserId());
                notificationUserRepository.save(notificationUser);
                webSocket.sendOneMessage(userRole.getUserId(), Utils.toJson(mapper.convertToResource(notification)));
            }
        } catch (Exception e) {
            quartzScheduleService.sendMailWhenJobFailed(jobDetail.getKey().getName());
        }
        log.info("-------{} end-----", SendNotificationUpdatePermissionJob.class.getSimpleName());
    }
}
