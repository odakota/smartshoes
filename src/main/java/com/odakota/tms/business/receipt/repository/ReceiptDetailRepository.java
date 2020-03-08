package com.odakota.tms.business.receipt.repository;

import com.odakota.tms.business.receipt.entity.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, Long> {
}