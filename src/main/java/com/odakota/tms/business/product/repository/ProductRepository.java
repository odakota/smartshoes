package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.resource.ProductResource;
import com.odakota.tms.business.product.resource.ProductResource.ProductCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface ProductRepository extends BaseRepository<Product, ProductCondition> {

    @Query("select p from Product p where p.deletedFlag = false and (:#{#condition.categoryId} is null or " +
           "p.categoryId = :#{#condition.categoryId}) " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.saleStartAt)} is null or  " +
           "p.saleStartAt <=:#{#condition.saleStartAt})  " +
           "and (:#{T(com.odakota.tms.utils.Utils).convertToString(#condition.saleEndAt)} is null or  " +
           "p.saleEndAt >= :#{#condition.saleEndAt}) ")
    Page<Product> findByCondition(ProductCondition condition, Pageable pageable);

    Optional<Product> findByCodeAndDeletedFlagFalse(String code);

    @Query("select p from Product p join AllocationProduct ap on p.id = ap.productId join Category c on p.categoryId = c.id " +
           "where p.deletedFlag = false and ap.branchId = :#{@userSession.branchId} and c.id in ?1 ")
    List<Product> findAllByCategoryAndBranch(List<Long> categoryId);

    @Query("select p from Product p where p.deletedFlag = false and p.saleStartAt <= current_date " +
           "and (p.saleEndAt is null or p.saleEndAt > current_date) " +
           "and (:categoryId is null or p.categoryId = :categoryId) " +
           "and (:productName is null or p.name like %:productName%)")
    List<Product> findAllProductSale(@Param("categoryId") Long categoryId, @Param("productName") String productName);

    @Query("select p from Product p where p.deletedFlag = false ")
    List<Product> findAllByBranch();

    @Query("select new com.odakota.tms.business.product.resource.ProductResource(p.id, sum (al.total)) from Product p " +
           "join AllocationProduct al on al.productId = p.id " +
           "where p.deletedFlag = false and (:branchId is null or al.branchId = :branchId) group by p.id")
    List<ProductResource> countByBranch(@Param("branchId") Long branchId);

    Boolean existsByCategoryIdAndDeletedFlagFalse(Long category);
}
