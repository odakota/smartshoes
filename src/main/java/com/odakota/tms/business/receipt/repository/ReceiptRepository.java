package com.odakota.tms.business.receipt.repository;

import com.odakota.tms.business.receipt.entity.Receipt;
import com.odakota.tms.business.receipt.resource.ReceiptResource.ReceiptCondition;
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
public interface ReceiptRepository extends BaseRepository<Receipt, ReceiptCondition> {

    @Query("select e from Receipt e where e.deletedFlag = false " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.endDate)} is null or " +
           "e.receiptDate <=:#{#condition.endDate}) " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.startDate)} is null or " +
           "e.receiptDate >= :#{#condition.startDate}) " +
           "and (:#{#condition.approvedFlag} is null or e.approvedFlag = :#{#condition.approvedFlag}) " +
           "and ((:#{@userSession.branchId} is null and (:#{#condition.branchId} is null " +
           "or e.branchId = :#{#condition.branchId})) or e.branchId = :#{@userSession.branchId}) ")
    Page<Receipt> findByCondition(ReceiptCondition condition, Pageable pageable);
}
