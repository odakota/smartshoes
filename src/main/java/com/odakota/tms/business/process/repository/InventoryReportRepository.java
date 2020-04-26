package com.odakota.tms.business.process.repository;

import com.odakota.tms.business.process.entity.InventoryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface InventoryReportRepository extends JpaRepository<InventoryReport, Long> {

    @Query("select i from InventoryReport i where i.branchId = ?1 and i.reportDate = ?2 ")
    List<InventoryReport> findByBranchId(Long branchId, Date date);
}
