package com.odakota.tms.business.customer.service;

import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.repository.CustomerRepository;
import com.odakota.tms.business.customer.resource.CustomerResource;
import com.odakota.tms.business.customer.resource.CustomerResource.CustomerCondition;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class CustomerService extends BaseService<Customer, CustomerResource, CustomerCondition> {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        super(customerRepository);
        this.customerRepository = customerRepository;
    }

    /**
     * Create a new resource.
     *
     * @param resource resource
     * @return The created resource is returned.
     */
    @Override
    public CustomerResource createResource(CustomerResource resource) {
        if (resource.getAvatar() == null){
            resource.setAvatar(Constant.CUS_IMAGE_PATH_DEFAULT);
        }
        return super.createResource(resource);
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected CustomerResource convertToResource(Customer entity) {
        return super.mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected Customer convertToEntity(Long id, CustomerResource resource) {
        Customer customer = super.mapper.convertToEntity(resource);
        customer.setId(id);
        return customer;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected CustomerCondition getCondition(FindCondition condition) {
        CustomerCondition customerCondition = condition.get(CustomerCondition.class);
        return customerCondition == null ? new CustomerCondition() : customerCondition;
    }
}
