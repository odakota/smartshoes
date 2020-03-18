package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Color;
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
public interface ColorRepository extends JpaRepository<Color, Long> {

    Optional<Color> findByCodeAndDeletedFlagFalse(String code);

    @Query("select distinct c from Color c join AllocationProduct ap on ap.colorId = c.id where ap.productId = ?1 and " +
           "ap.total > 0 and c.deletedFlag = false and ap.branchId = :#{@userSession.branchId}")
    List<Color> findByProductIdAndBranchId(Long productId);
}
