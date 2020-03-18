package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Size;
import com.odakota.tms.business.product.resource.SizeResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

    Optional<Size> findByCodeAndDeletedFlagFalse(String code);

    @Query("select new com.odakota.tms.business.product.resource.SizeResource(s.id, s.name, s.code, ap.total) from " +
           "Size s join AllocationProduct ap on ap.sizeId = s.id where ap.productId = ?1 and " +
           "ap.colorId = ?2 and ap.total > 0 and s.deletedFlag = false and ap.branchId = :#{@userSession.branchId}")
    List<SizeResource> findByProductIdAndBranchIdAndColorId(Long productId, Long colorId);
}
