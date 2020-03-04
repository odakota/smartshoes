package com.odakota.tms.business.asserts.repository;

import com.odakota.tms.business.asserts.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
}
