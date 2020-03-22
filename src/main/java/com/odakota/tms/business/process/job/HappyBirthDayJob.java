package com.odakota.tms.business.process.job;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.entity.NotificationUser;
import com.odakota.tms.business.notification.repository.NotificationRepository;
import com.odakota.tms.business.notification.repository.NotificationUserRepository;
import com.odakota.tms.business.process.service.QuartzScheduleService;
import com.odakota.tms.constant.NotificationConstant;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.enums.notify.Priority;
import com.odakota.tms.enums.notify.SendStatus;
import com.odakota.tms.mapper.ModelMapper;
import com.odakota.tms.system.service.websocket.WebSocket;
import com.odakota.tms.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
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
public class HappyBirthDayJob extends QuartzJobBean {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocket webSocket;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationUserRepository notificationUserRepository;

    @Autowired
    private QuartzScheduleService quartzScheduleService;

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
        log.info("-------{} start-----", HappyBirthDayJob.class.getSimpleName());
        try {
            List<User> users = userRepository.findByBirthDayToDay();
            Notification notification = new Notification();
            if (!users.isEmpty()) {
                notification.setPriority(Priority.HIGH);
                notification.setType(MsgType.SYSTEM);
                notification.setTitle(NotificationConstant.TITLE_HAPPY_BIRTHDAY);
                notification.setContent(NotificationConstant.CONTENT_HAPPY_BIRTHDAY);
                notification.setStartDate(new Date());
                notification.setSendTime(new Date());
                notification.setSendStatus(SendStatus.PUBLISHED);
                notification = notificationRepository.save(notification);
            }
            for (User user : users) {
                NotificationUser notificationUser = new NotificationUser();
                notificationUser.setNotificationId(notification.getId());
                notificationUser.setRead(false);
                notificationUser.setUserId(user.getId());
                notificationUserRepository.save(notificationUser);
                webSocket.sendOneMessage(user.getId(), Utils.toJson(mapper.convertToResource(notification)));
            }
        } catch (Exception e) {
            quartzScheduleService.sendMailWhenJobFailed(context.getJobDetail().getKey().getName());
        }
        log.info("-------{} end-----", HappyBirthDayJob.class.getSimpleName());
    }
}
