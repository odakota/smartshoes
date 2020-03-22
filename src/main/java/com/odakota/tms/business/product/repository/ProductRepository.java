package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.resource.ProductResource.ProductCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ProductRepository extends BaseRepository<Product, ProductCondition> {

    Optional<Product> findByCodeAndDeletedFlagFalse(String code);

    @Query("select p from Product p join AllocationProduct ap on p.id = ap.productId where p.deletedFlag = false " +
           "and ap.branchId = :#{@userSession.branchId}")
    List<Product> findAllByBranch();

    @Query("select p from Product p join AllocationProduct ap on p.id = ap.productId join Category c on p.categoryId = c.id " +
           "where p.deletedFlag = false and ap.branchId = :#{@userSession.branchId} and c.id in ?1 ")
    List<Product> findAllByCategoryAndBranch(List<Long> categoryId);
}
