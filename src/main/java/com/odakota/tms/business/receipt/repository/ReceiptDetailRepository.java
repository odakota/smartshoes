package com.odakota.tms.business.receipt.repository;

import com.odakota.tms.business.receipt.entity.ReceiptDetail;
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
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {

    List<ReceiptDetail> findByDeletedFlagFalseAndReceiptId(Long receiptId);

    @Query("select rd from ReceiptDetail rd join Receipt r on r.id = rd.receiptId where r.branchId = ?1 " +
           "and r.approvedFlag = true and r.approvedDate = ?2 ")
    List<ReceiptDetail> findAllByBranch(Long branchId, Date date);
}
