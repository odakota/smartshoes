package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

    Optional<Size> findByCodeAndDeletedFlagFalse(String code);
}
