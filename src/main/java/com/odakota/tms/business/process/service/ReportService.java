package com.odakota.tms.business.process.service;

import com.odakota.tms.business.process.repository.BestsellerReportRepository;
import com.odakota.tms.business.process.repository.InventoryReportRepository;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.process.ReportType;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ReportService {

    private final InventoryReportRepository inventoryReportRepository;

    private final BestsellerReportRepository bestsellerReportRepository;

    private final UserSession userSession;

    @Autowired
    public ReportService(InventoryReportRepository inventoryReportRepository,
                         BestsellerReportRepository bestsellerReportRepository,
                         UserSession userSession) {
        this.inventoryReportRepository = inventoryReportRepository;
        this.userSession = userSession;
        this.bestsellerReportRepository = bestsellerReportRepository;
    }

    public Object getReport(ReportType reportType, String reportDate) {

        switch (reportType) {
            case INVENTORY:
                return inventoryReportRepository.findByBranchId(userSession.getBranchId(),
                                                                Utils.convertToDate(reportDate, Constant.YYYY_MM_DD));
            case BESTSELLERS_OF_MONTH:
                return bestsellerReportRepository
                        .findByBranchIdAndReportDateOrderByOrderNumberAsc(userSession.getBranchId(),
                                                                           reportDate.substring(0, 7));
            case BESTSELLERS_OF_YEAR:
                return bestsellerReportRepository
                        .findByBranchIdAndReportDateOrderByOrderNumberAsc(userSession.getBranchId(), reportDate);
            case REVENUE_BY_STAFF:
            default:
                throw new CustomException(MessageCode.MSG_RESOURCE_NOT_EXIST, HttpStatus.BAD_REQUEST);
        }
    }

    public Object getDashboard() {

        return bestsellerReportRepository
                .findByBranchIdAndReportDateOrderByOrderNumberAsc(userSession.getBranchId(),
                                                                  Utils.convertToStringFormat(Utils.calculationTimeMonth(new Date(), -1),
                                                                                              Constant.YYYY_MM_DD).substring(0, 7));
    }
}
