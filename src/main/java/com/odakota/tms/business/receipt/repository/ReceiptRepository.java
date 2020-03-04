package com.odakota.tms.business.receipt.repository;

import com.odakota.tms.business.receipt.entity.Receipt;
import com.odakota.tms.business.receipt.resource.ReceiptResource.ReceiptCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ReceiptRepository extends BaseRepository<Receipt, ReceiptCondition> {
}
