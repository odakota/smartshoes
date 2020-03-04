package com.odakota.tms.business.sales.controller;

import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.business.sales.resource.CampaignResource;
import com.odakota.tms.business.sales.service.CampaignService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class CampaignController extends BaseController<Campaign, CampaignResource> {

    private final CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        super(campaignService);
        this.campaignService = campaignService;
    }

    /**
     * API get campaign list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get campaign list")
    @RequiredAuthentication//(value = ApiId.R_CAMPAIGN)
    @GetMapping(value = "/campaigns", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getCampaigns(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get campaign by id
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get campaign by id")
    @RequiredAuthentication//(value = ApiId.R_CAMPAIGN)
    @GetMapping(value = "/campaigns/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getCampaign(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new campaign
     *
     * @param resource {@link CampaignResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API create new campaign")
    @RequiredAuthentication//(value = ApiId.C_CAMPAIGN)
    @PostMapping(value = "/campaigns", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createCampaign(@Validated @RequestBody CampaignResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update campaign
     *
     * @param id       campaign id
     * @param resource {@link CampaignResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API update campaign")
    @RequiredAuthentication//(value = ApiId.U_CAMPAIGN)
    @PutMapping(value = "/campaigns/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateCampaign(@PathVariable Long id, @RequestBody CampaignResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete campaign
     *
     * @param id campaign id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API delete campaign")
    @RequiredAuthentication//(value = ApiId.D_CAMPAIGN)
    @DeleteMapping(value = "/campaigns/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        return super.deleteResource(id);
    }
}
