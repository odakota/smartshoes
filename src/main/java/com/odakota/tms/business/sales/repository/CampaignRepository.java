package com.odakota.tms.business.sales.repository;

import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.business.sales.resource.CampaignResource.CampaignCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CampaignRepository extends BaseRepository<Campaign, CampaignCondition> {

    @Query("select e from Campaign e where e.deletedFlag = false " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.endDate)} is null or " +
           "e.endDate <=:#{#condition.endDate}) " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.startDate)} is null or " +
           "e.startDate >= :#{#condition.startDate}) " +
           "and (:#{#condition.name} is null or e.name like %:#{#condition.name}%) " +
           "and ((:#{@userSession.branchId} is null and (:#{#condition.branchId} is null " +
           "or e.branchId = :#{#condition.branchId})) or e.branchId = :#{@userSession.branchId}) ")
    Page<Campaign> findByCondition(CampaignCondition condition, Pageable pageable);

    boolean existsByBranchIdAndNameAndDeletedFlagFalse(Long branchId, String name);

    boolean existsByBranchIdAndNameAndDeletedFlagFalseAndIdNot(Long branchId, String name, Long id);
}
