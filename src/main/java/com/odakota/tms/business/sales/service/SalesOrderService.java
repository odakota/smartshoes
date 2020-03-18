package com.odakota.tms.business.sales.service;

import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.repository.CustomerRepository;
import com.odakota.tms.business.sales.entity.SalesOrder;
import com.odakota.tms.business.sales.repository.SalesOrderRepository;
import com.odakota.tms.business.sales.resource.SalesOrderResource;
import com.odakota.tms.business.sales.resource.SalesOrderResource.SalesOrderCondition;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.sale.OrderStatus;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class SalesOrderService extends BaseService<SalesOrder, SalesOrderResource, SalesOrderCondition> {

    private final SalesOrderRepository salesOrderRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             CustomerRepository customerRepository) {
        super(salesOrderRepository);
        this.salesOrderRepository = salesOrderRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Get a list of resources.
     *
     * @param baseReq request param
     * @return Resource search results are returned.
     */
    @Override
    public BaseResponse<SalesOrderResource> getResources(BaseParameter baseReq) {
        BaseResponse<SalesOrderResource> response = super.getResources(baseReq);
        List<SalesOrderResource> salesOrderResources = response.getData();
        List<Long> customerIds = salesOrderResources.stream().map(SalesOrderResource::getCustomerId)
                                                    .collect(Collectors.toList());
        if (!customerIds.isEmpty()) {
            List<Customer> customers = customerRepository.findByDeletedFlagFalseAndIdIn(customerIds);
            salesOrderResources = response.getData().stream()
                                          .peek(tmp -> customers.stream()
                                                                .filter(var -> tmp.getCustomerId().equals(var.getId()))
                                                                .findFirst().ifPresent(
                                                          customer -> tmp.setCustomerName(customer.getFullName())))
                                          .collect(Collectors.toList());
        }
        return new BaseResponse<>(response.getPagination(), salesOrderResources);
    }

    /**
     * Cancel order
     */
    public void cancelSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findByIdAndDeletedFlagFalse(id)
                                                    .orElseThrow(() -> new CustomException(
                                                            MessageCode.MSG_ORDER_NOT_EXISTED, HttpStatus.NOT_FOUND));
        salesOrder.setStatus(OrderStatus.CANCEL);
        salesOrderRepository.save(salesOrder);

    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected SalesOrderResource convertToResource(SalesOrder entity) {
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
    protected SalesOrder convertToEntity(Long id, SalesOrderResource resource) {
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
    protected SalesOrderCondition getCondition(FindCondition condition) {
        SalesOrderCondition salesOrderCondition = condition.get(SalesOrderCondition.class);
        return salesOrderCondition == null ? new SalesOrderCondition() : salesOrderCondition;
    }
}
