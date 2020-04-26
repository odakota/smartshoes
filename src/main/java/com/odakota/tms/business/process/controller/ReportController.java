package com.odakota.tms.business.process.controller;

import com.odakota.tms.business.process.service.ReportService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.process.ReportType;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.config.data.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * API get report setting
     *
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get inventory report setting")
    @RequiredAuthentication//(value = ApiId.R_BRANCH)
    @GetMapping(value = "/report", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getReports(@RequestParam Integer reportType, @RequestParam String reportDate) {
        return ResponseEntity.ok(new ResponseData<>().success(reportService.getReport(ReportType.of(reportType),
                                                                                      reportDate)));
    }

    /**
     * API get report setting
     *
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get inventory report setting")
    @RequiredAuthentication//(value = ApiId.R_BRANCH)
    @GetMapping(value = "/dashboard", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getDashboard(@RequestParam Integer reportType) {
        return ResponseEntity.ok(new ResponseData<>().success(reportService.getDashboard()));
    }
}
