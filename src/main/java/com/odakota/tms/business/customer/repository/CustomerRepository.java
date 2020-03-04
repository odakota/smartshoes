package com.odakota.tms.business.customer.repository;

import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.resource.CustomerResource.CustomerCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends BaseRepository<Customer, CustomerCondition> {
}
