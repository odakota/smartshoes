package com.odakota.tms.business.sales.service;

import com.odakota.tms.business.process.service.QuartzScheduleService;
import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.business.sales.repository.CampaignRepository;
import com.odakota.tms.business.sales.resource.CampaignResource;
import com.odakota.tms.business.sales.resource.CampaignResource.CampaignCondition;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.system.service.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class CampaignService extends BaseService<Campaign, CampaignResource, CampaignCondition> {

    private final CampaignRepository campaignRepository;

    private final UserSession userSession;

    private final QuartzScheduleService quartzScheduleService;

    private final WebSocket webSocket;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, UserSession userSession,
                           QuartzScheduleService quartzScheduleService,
                           WebSocket webSocket) {
        super(campaignRepository);
        this.campaignRepository = campaignRepository;
        this.userSession = userSession;
        this.quartzScheduleService = quartzScheduleService;
        this.webSocket = webSocket;
    }

    /**
     * Create a new resource.
     *
     * @param resource resource
     * @return The created resource is returned.
     */
    @Override
    public CampaignResource createResource(CampaignResource resource) {
        if (resource.getBranchId() == null) {
            resource.setBranchId(userSession.getBranchId());
        }
        if (campaignRepository.existsByBranchIdAndNameAndDeletedFlagFalse(resource.getBranchId(), resource.getName())) {
            throw new CustomException(MessageCode.MSG_CAMPAIGN_EXISTED, HttpStatus.CONFLICT);
        }
        CampaignResource campaignResource = super.createResource(resource);
        quartzScheduleService
                .createApplyCampaignItemJob(resource.getBranchId(), campaignResource, resource.getSelectAll(),
                                           resource.getSelectCategory(), resource.getSelectProduct());
        return campaignResource;
    }

    /**
     * Update resources.
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return The updated resource is returned.
     */
    @Override
    protected CampaignResource updateResource(Long id, CampaignResource resource) {
        if (resource.getBranchId() == null) {
            resource.setBranchId(userSession.getBranchId());
        }
        if (campaignRepository.existsByBranchIdAndNameAndDeletedFlagFalseAndIdNot(resource.getBranchId(),
                                                                                  resource.getName(), id)) {
            throw new CustomException(MessageCode.MSG_CAMPAIGN_EXISTED, HttpStatus.CONFLICT);
        }
        CampaignResource campaignResource = super.updateResource(id, resource);
        quartzScheduleService.deleteApplyCampaignItemJob(resource.getBranchId(), resource.getName());
        quartzScheduleService
                .createApplyCampaignItemJob(resource.getBranchId(), campaignResource, resource.getSelectAll(),
                                           resource.getSelectCategory(), resource.getSelectProduct());
        return resource;
    }

    /**
     * Specify a resource identifier and delete the resource.
     *
     * @param id Resource identifier
     */
    @Override
    public void deleteResource(Long id) {
        campaignRepository.findByIdAndDeletedFlagFalse(id).ifPresent(var -> {
            var.setDeletedFlag(true);
            campaignRepository.save(var);
            quartzScheduleService.deleteApplyCampaignItemJob(var.getBranchId(), var.getName());
        });
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected CampaignResource convertToResource(Campaign entity) {
        return mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected Campaign convertToEntity(Long id, CampaignResource resource) {
        resource.setId(id);
        return mapper.convertToEntity(resource);
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected CampaignCondition getCondition(FindCondition condition) {
        CampaignCondition campaignCondition = condition.get(CampaignCondition.class);
        return campaignCondition == null ? new CampaignCondition() : campaignCondition;
    }
}
