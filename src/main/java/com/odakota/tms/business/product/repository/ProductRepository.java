package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.resource.ProductResource.ProductCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ProductRepository extends BaseRepository<Product, ProductCondition> {

    Optional<Product> findByCodeAndDeletedFlagFalse(String code);
}
