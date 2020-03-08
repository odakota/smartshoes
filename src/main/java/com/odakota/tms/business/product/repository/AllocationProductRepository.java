package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.AllocationProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface AllocationProductRepository extends JpaRepository<AllocationProduct, Long> {

    Optional<AllocationProduct> findByDeletedFlagFalseAndProductIdAndColorIdAndSizeIdAndBranchId(Long productId,
                                                                                                 Long colorId,
                                                                                                 Long sizeId,
                                                                                                 Long branchId);
}
