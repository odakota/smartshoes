package com.odakota.tms.business.receipt.service;

import com.odakota.tms.business.receipt.entity.Receipt;
import com.odakota.tms.business.receipt.repository.ReceiptRepository;
import com.odakota.tms.business.receipt.resource.ReceiptResource;
import com.odakota.tms.business.receipt.resource.ReceiptResource.ReceiptCondition;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ReceiptService extends BaseService<Receipt, ReceiptResource, ReceiptCondition> {

    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        super(receiptRepository);
        this.receiptRepository = receiptRepository;
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected ReceiptResource convertToResource(Receipt entity) {
        return mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected Receipt convertToEntity(Long id, ReceiptResource resource) {
        resource.setId(id);
        return mapper.convertToEntity(resource);
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected ReceiptCondition getCondition(BaseParameter.FindCondition condition) {
        return super.getCondition(condition);
    }
}
