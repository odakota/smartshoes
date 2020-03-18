package com.odakota.tms.business.customer.repository;

import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.resource.CustomerResource.CustomerCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends BaseRepository<Customer, CustomerCondition> {

    List<Customer> findByDeletedFlagFalseAndIdIn(List<Long> ids);
}
