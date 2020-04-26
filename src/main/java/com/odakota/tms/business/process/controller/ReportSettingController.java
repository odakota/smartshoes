package com.odakota.tms.business.process.controller;

import com.odakota.tms.business.auth.resource.BranchResource;
import com.odakota.tms.business.process.resource.ReportSettingResource;
import com.odakota.tms.business.process.service.ReportSettingService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.data.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class ReportSettingController {

    private final ReportSettingService reportSettingService;

    @Autowired
    public ReportSettingController(ReportSettingService reportSettingService) {
        this.reportSettingService = reportSettingService;
    }

    /**
     * API get report setting
     *
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get report setting")
    @RequiredAuthentication//(value = ApiId.R_BRANCH)
    @GetMapping(value = "/report-setting", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getReportSetting() {
        return ResponseEntity.ok(new ResponseData<>().success(reportSettingService.getResource()));
    }

    /**
     * API save report setting
     *
     * @param resource {@link BranchResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API save report setting")
    @RequiredAuthentication//(value = ApiId.C_BRANCH)
    @PostMapping(value = "/report-setting", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> saveReportSetting(@Validated @RequestBody ReportSettingResource resource) {
        return ResponseEntity.ok(new ResponseData<>().success(reportSettingService.saveResource(resource)));
    }
}
