package com.odakota.tms.business.sales.service;

import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.business.sales.repository.CampaignRepository;
import com.odakota.tms.business.sales.resource.CampaignResource;
import com.odakota.tms.business.sales.resource.CampaignResource.CampaignCondition;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class CampaignService extends BaseService<Campaign, CampaignResource, CampaignCondition> {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        super(campaignRepository);
        this.campaignRepository = campaignRepository;
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
