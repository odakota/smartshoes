package com.odakota.tms.business.process.service;

import com.odakota.tms.business.process.entity.ReportSetting;
import com.odakota.tms.business.process.repository.ReportSettingRepository;
import com.odakota.tms.business.process.resource.ReportSettingResource;
import com.odakota.tms.mapper.ModelMapper;
import com.odakota.tms.system.config.UserSession;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ReportSettingService {

    private final ReportSettingRepository reportSettingRepository;

    private final UserSession userSession;

    private final QuartzScheduleService quartzScheduleService;

    private final ModelMapper mapper = Mappers.getMapper(ModelMapper.class);

    @Autowired
    public ReportSettingService(ReportSettingRepository reportSettingRepository,
                                UserSession userSession,
                                QuartzScheduleService quartzScheduleService) {
        this.reportSettingRepository = reportSettingRepository;
        this.userSession = userSession;
        this.quartzScheduleService = quartzScheduleService;
    }

    /**
     * Get report setting of branch
     *
     * @return The resource
     */
    public ReportSettingResource getResource() {
        List<ReportSetting> reportSettings = reportSettingRepository
                .findByDeletedFlagFalseAndBranchId(userSession.getBranchId());
        if (!reportSettings.isEmpty()) {
            return mapper.convertToResource(reportSettings);
        }
        return null;
    }

    /**
     * Save report setting of branch
     *
     * @param resource resource
     * @return The created resource is returned.
     */
    public ReportSettingResource saveResource(ReportSettingResource resource) {
        // delete pre setting
        reportSettingRepository.deleteByBranchId(userSession.getBranchId());
        List<ReportSetting> settings = mapper.convertToEntity(resource, userSession.getBranchId());
        // save setting
        reportSettingRepository.saveAll(settings);
        // create job
        quartzScheduleService.createReportJob(settings);
        return resource;
    }
}
