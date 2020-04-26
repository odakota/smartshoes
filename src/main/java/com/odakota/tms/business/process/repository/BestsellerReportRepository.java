package com.odakota.tms.business.process.repository;

import com.odakota.tms.business.process.entity.BestsellerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface BestsellerReportRepository extends JpaRepository<BestsellerReport, Long> {

    List<BestsellerReport> findByBranchIdAndReportDateOrderByOrderNumberAsc(Long branchId, String reportDate);
}
