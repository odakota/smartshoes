package com.odakota.tms.business.sales.repository;

import com.odakota.tms.business.sales.entity.SalesOrder;
import com.odakota.tms.business.sales.resource.SalesOrderResource.SalesOrderCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface SalesOrderRepository extends BaseRepository<SalesOrder, SalesOrderCondition> {

//    @Query("select e from Campaign e where e.deletedFlag = false " +
//           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.endDate)} is null or " +
//           "e.endDate <=:#{#condition.endDate}) " +
//           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.startDate)} is null or " +
//           "e.startDate >= :#{#condition.startDate}) " +
//           "and (:#{#condition.name} is null or e.name like %:#{#condition.name}%) " +
//           "and ((:#{@userSession.branchId} is null and (:#{#condition.branchId} is null " +
//           "or e.branchId = :#{#condition.branchId})) or e.branchId = :#{@userSession.branchId}) ")
//    Page<Campaign> findByCondition(CampaignCondition condition, Pageable pageable);
}