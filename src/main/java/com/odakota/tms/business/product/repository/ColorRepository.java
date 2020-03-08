package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

    Optional<Color> findByCodeAndDeletedFlagFalse(String code);
}
