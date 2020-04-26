package com.odakota.tms.business.sales.repository;

import com.odakota.tms.business.sales.entity.SalesOrder;
import com.odakota.tms.business.sales.resource.SalesOrderResource.SalesOrderCondition;
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
public interface SalesOrderRepository extends BaseRepository<SalesOrder, SalesOrderCondition> {

    @Query("select e from SalesOrder e where e.deletedFlag = false " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.endDate)} is null or " +
           "e.createdDate <=:#{#condition.endDate}) " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.startDate)} is null or " +
           "e.createdDate >= :#{#condition.startDate}) " +
           "and ((:#{@userSession.branchId} is null and (:#{#condition.branchId} is null " +
           "or e.branchId = :#{#condition.branchId})) or e.branchId = :#{@userSession.branchId}) ")
    Page<SalesOrder> findByCondition(SalesOrderCondition condition, Pageable pageable);
}
