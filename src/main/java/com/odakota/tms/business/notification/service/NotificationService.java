package com.odakota.tms.business.notification.service;

import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.entity.NotificationUser;
import com.odakota.tms.business.notification.repository.NotificationRepository;
import com.odakota.tms.business.notification.repository.NotificationUserRepository;
import com.odakota.tms.business.notification.resource.NotificationResource;
import com.odakota.tms.business.notification.resource.NotificationResource.NotificationCondition;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class NotificationService extends BaseService<Notification, NotificationResource, NotificationCondition> {

    private final NotificationRepository notificationRepository;

    private final NotificationUserRepository notificationUserRepository;

    private final UserSession userSession;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository,
                               NotificationUserRepository notificationUserRepository,
                               UserSession userSession) {
        super(notificationRepository);
        this.notificationRepository = notificationRepository;
        this.notificationUserRepository = notificationUserRepository;
        this.userSession = userSession;
    }

    /**
     * Get notification
     *
     * @param baseReq {@link BaseParameter}
     * @return Object
     */
    public Object getNotification(BaseParameter baseReq) {
        Map<String, Object> map = new HashMap<>();
        NotificationCondition condition = this.getCondition(baseReq.getFindCondition());
        Pageable pageRequest = BaseParameter.getPageable(baseReq.getSort(), baseReq.getPage(), baseReq.getLimit());
        // get notification system
        condition.setType(MsgType.SYSTEM);
        Page<Notification> notify = notificationRepository.findByCondition(condition, pageRequest);
        map.put("notify", new BaseResponse<>(this.getResources(notify.getContent()), notify));
        // get message
        condition.setType(MsgType.NOTIFICATION_BULLETIN);
        Page<Notification> msg = notificationRepository.findByCondition(condition, pageRequest);
        map.put("msg", new BaseResponse<>(this.getResources(msg.getContent()), msg));
        return map;
    }

    /**
     * Update status read notification of user
     *
     * @param id notification id
     */
    public void readNotification(Long id) {
        NotificationUser notificationUser = notificationUserRepository
                .findByDeletedFlagFalseAndNotificationIdAndUserId(id, userSession.getUserId())
                .orElseThrow(() -> new CustomException(MessageCode.MSG_NOTIFY_NOT_EXISTED, HttpStatus.NOT_FOUND));
        notificationUser.setRead(true);
        notificationUser.setReadTime(new Date());
        notificationUserRepository.save(notificationUser);
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected NotificationResource convertToResource(Notification entity) {
        return super.mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected Notification convertToEntity(Long id, NotificationResource resource) {
        Notification entity = super.mapper.convertToEntity(resource);
        entity.setId(id);
        return entity;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected NotificationCondition getCondition(BaseParameter.FindCondition condition) {
        NotificationCondition notificationCondition = condition.get(NotificationCondition.class);
        return notificationCondition != null ? notificationCondition : new NotificationCondition();
    }
}
