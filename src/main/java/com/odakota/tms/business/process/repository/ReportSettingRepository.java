package com.odakota.tms.business.process.repository;

import com.odakota.tms.business.process.entity.ReportSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ReportSettingRepository extends JpaRepository<ReportSetting, Long> {

    List<ReportSetting> findByDeletedFlagFalseAndBranchId(Long branchId);

    @Modifying
    @Transactional
    @Query("update ReportSetting set deletedFlag = true, updatedBy = :#{@userSession.userId}, " +
           "updatedDate = :#{T(java.time.LocalDateTime).now()} where :branchId is null or branchId = :branchId")
    void deleteByBranchId(@Param("branchId") Long branchId);
}
