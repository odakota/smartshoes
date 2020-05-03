package com.odakota.tms.business.customer.repository;

import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.resource.CustomerResource.CustomerCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends BaseRepository<Customer, CustomerCondition> {

    @Query("select e from Customer e where e.deletedFlag = false " +
           "and (:#{#condition.customerSegment} is null or " +
           "e.customerSegment = :#{T(com.odakota.tms.enums.customer.Segment).of(#condition.customerSegment)}) " +
           "and (:#{#condition.customerType} is null or " +
           "e.customerType = :#{T(com.odakota.tms.enums.customer.CustomerType).of(#condition.customerType)}) " +
           "and (:#{#condition.name} is null or e.fullName like %:#{#condition.name}%) " )
    Page<Customer> findByCondition(CustomerCondition condition, Pageable pageable);

    List<Customer> findByDeletedFlagFalseAndIdIn(List<Long> ids);
}
